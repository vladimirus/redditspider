package com.redditspider.dao.reddit;

import static org.springframework.util.StringUtils.hasText;

import com.redditspider.dao.SearchDao;
import com.redditspider.dao.browser.WebBrowser;
import com.redditspider.dao.browser.WebBrowserPool;
import com.redditspider.dao.reddit.parser.ParserFactory;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * This class actually connects to reddit and parses its response.
 *
 */
@Repository
public class RedditDaoImpl implements SearchDao {
    private static final transient Logger LOG = Logger.getLogger(RedditDaoImpl.class);
    @Autowired
    WebBrowserPool webBrowserPool;
    @Autowired
    RedditAuthenticator authenticator;
    @Autowired
    ParserFactory parserFactory;

    public SearchResult search(SearchQuery query) {
        SearchResult searchResult = null;
        if (query != null && hasText(query.getSearchUri())) {
            WebBrowser browser = webBrowserPool.get();
            if (browser != null) {
                searchResult = doSearch(query.getSearchUri(), browser.getDriver());
            }
            webBrowserPool.release(browser);
        }

        if (searchResult == null) {
            searchResult = new SearchResult();
        }

        return searchResult;
    }

    SearchResult doSearch(String query, WebDriver driver) {
        SearchResult result = null;
        try {
            driver.get(query);
            loginIfNeeded(driver);
            result = parserFactory.getParser(driver.getCurrentUrl()).parse(driver);
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
