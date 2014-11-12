package com.redditspider.dao.reddit;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.util.StringUtils.hasText;

import com.redditspider.dao.SearchDao;
import com.redditspider.dao.browser.WebBrowser;
import com.redditspider.dao.browser.WebBrowserPool;
import com.redditspider.dao.reddit.parser.ParserFactory;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class actually connects to reddit and parses its response.
 */
@Repository
public class RedditWebDao implements SearchDao {
    private static final transient Logger LOG = Logger.getLogger(RedditWebDao.class);
    @Autowired
    WebBrowserPool webBrowserPool;
    @Autowired
    RedditWebAuthenticator authenticator;
    @Autowired
    ParserFactory parserFactory;

    public SearchResult search(SearchQuery query) {
        SearchResult result = new SearchResult();
        List<Link> links = newArrayList();
        result.setLinks(findNewLinks(query, links));
        return result;
    }

    private List<Link> findNewLinks(SearchQuery query, List<Link> links) {
        SearchResult result = searchInWebBrowser(query);
        links.addAll(result.getLinks());

        if (hasText(result.getNextPage())) {
            SearchQuery q = new SearchQuery(result.getNextPage());
            findNewLinks(q, links);
        }
        return links;
    }

    private SearchResult searchInWebBrowser(SearchQuery query) {
        SearchResult searchResult = null;
        if (query != null && hasText(query.getSearchUri())) {
            WebBrowser browser = webBrowserPool.get();
            if (browser != null) {
                searchResult = doSearch(query.getSearchUri(), browser.getDriver());
                webBrowserPool.release(browser);
            }
        }

        if (searchResult == null) {
            searchResult = new SearchResult();
        }

        return searchResult;
    }

    SearchResult doSearch(String query, WebDriver driver) {
        SearchResult result;
        try {
            driver.get(query);
            loginIfNeeded(driver);
            result = parserFactory.getParser(driver).parse();
        } catch (Exception ignore) {
            LOG.error(ignore);
            result = new SearchResult();
        }
        return result;
    }

    private void loginIfNeeded(WebDriver driver) {
        String currentUrl = driver.getCurrentUrl();
        if (!authenticator.isLoggedIn(driver)) {
            authenticator.login(driver);
            driver.get(currentUrl);
        }
    }
}
