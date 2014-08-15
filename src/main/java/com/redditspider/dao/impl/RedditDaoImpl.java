package com.redditspider.dao.impl;

import com.redditspider.dao.SearchDao;
import com.redditspider.dao.browser.WebBrowser;
import com.redditspider.dao.browser.WebBrowserPool;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * This class actually connects to reddit and parses its response.
 *
 */
@Repository
public class RedditDaoImpl implements SearchDao {
    private static final transient Logger LOG = Logger.getLogger(RedditDaoImpl.class);
    @Autowired
    WebBrowserPool webBrowserPool;

    public SearchResult search(SearchQuery query) {
        SearchResult searchResult = new SearchResult();
        if (query != null && StringUtils.hasText(query.getSearchUri())) {
            WebBrowser browser = webBrowserPool.get();
            if (browser != null) {
                doSearch(query, searchResult, browser);
            }
            webBrowserPool.release(browser);
        }
        return searchResult;
    }

    void doSearch(SearchQuery query, SearchResult searchResult, WebBrowser browser) {
        try {
            WebDriver driver = browser.getDriver();
            driver.get(query.getSearchUri());
            new RedditParser(driver, searchResult).parse(); //this should come from a factory... maybe later..
        } catch (Exception ignore) {
            LOG.error(ignore);
        }
    }
}
