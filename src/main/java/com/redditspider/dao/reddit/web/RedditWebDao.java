package com.redditspider.dao.reddit.web;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.util.StringUtils.hasText;

import com.redditspider.dao.SearchDao;
import com.redditspider.dao.browser.WebBrowser;
import com.redditspider.dao.browser.WebBrowserPool;
import com.redditspider.dao.reddit.web.parser.ParserFactory;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import com.redditspider.model.reddit.WebSearchResult;
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
        List<Link> links = newArrayList();

        if (query != null && hasText(query.getSearchUri())) {
            WebBrowser browser = webBrowserPool.get();
            if (browser != null) {
                links = findNewLinks(query.getSearchUri(), links, browser.getDriver());
                webBrowserPool.release(browser);
            }
        }

        SearchResult searchResult = new SearchResult();
        searchResult.setLinks(links);
        return searchResult;
    }

    private List<Link> findNewLinks(String query, List<Link> links, WebDriver driver) {
        WebSearchResult result = doSearch(query, driver);
        links.addAll(result.getLinks());

        if (hasText(result.getNextPage())) {
            findNewLinks(result.getNextPage(), links, driver);
        }
        return links;
    }

    WebSearchResult doSearch(String query, WebDriver driver) {
        WebSearchResult result;
        try {
            driver.get(query);
            loginIfNeeded(driver);
            result = parserFactory.getParser(driver).parse();
        } catch (Exception ignore) {
            LOG.error(ignore);
            result = new WebSearchResult();
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
