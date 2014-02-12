package com.redditspider.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import com.redditspider.dao.browser.WebBrowser;
import com.redditspider.dao.browser.WebBrowserPool;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

/**
 * This class accually connects to reddit and parses its response.
 *
 */
@Repository
public class RedditDaoImpl implements RedditDao {
    @Autowired
    WebBrowserPool webBrowserPool;
    private final transient Logger log = Logger.getLogger(this.getClass());

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
            WebElement siteTable = driver.findElement(By.id("siteTable"));
            processLinks(searchResult, siteTable.findElements(By.className("link")));
            processPaginationUris(searchResult, siteTable.findElements(By.cssSelector("span.nextprev a")));
        } catch (Exception ignore) { // in case browser is closed while searching
            log.error(ignore);
        }
    }

    private void processLinks(SearchResult searchResult, List<WebElement> links) {
        if (links != null && links.size() > 0) {
            for (WebElement rawLink : links) {
                Link link = null;
                try {
                    if (rawLink.isDisplayed()) {
                        link = processLink(rawLink);
                    }
                } catch (Exception ignore) {
                    log.debug("Can't parse link, ignoring...", ignore);
                }

                if (link != null && StringUtils.hasText(link.getUri())) {
                    searchResult.getLinks().add(link);
                }
            }
        }
    }

    private Link processLink(WebElement rawLink) {
        Link link = new Link();
        WebElement rawRank = rawLink.findElement(By.className("rank"));
        String rank = rawRank.getText();

        if (StringUtils.hasText(rank)) {
            Integer down = NumberUtils.parseNumber(rawLink.getAttribute("data-downs"), Integer.class);
            Integer up = NumberUtils.parseNumber(rawLink.getAttribute("data-ups"), Integer.class);
            WebElement rawEntry = rawLink.findElement(By.className("entry"));
            WebElement rawTitle = rawEntry.findElement(By.cssSelector("a.title"));
            String uri = rawTitle.getAttribute("href");
            String text = rawTitle.getText();

            if (StringUtils.hasText(uri) && StringUtils.hasText(text)) {
                link.setDown(down);
                link.setUp(up);
                link.setUri(uri);
                link.setText(text);
            }
        }
        return link;
    }

    private void processPaginationUris(SearchResult searchResult, List<WebElement> uris) {
        if (uris != null) {
            for (WebElement uri : uris) {
                String rel = uri.getAttribute("rel");
                if (StringUtils.hasText(rel)) {
                    if (rel.contains("next")) {
                        searchResult.setNextPage(processPaginationUri(uri));
                    } else if (rel.contains("prev")) {
                        searchResult.setPrevPage(processPaginationUri(uri));
                    }
                }
            }
        }
    }

    private String processPaginationUri(WebElement rawUri) {
        String uri = null;
        if (rawUri != null) {
            uri = rawUri.getAttribute("href");
            if (!StringUtils.hasText(uri)) {
                uri = null; //reset if empty
            }
        }
        return uri;
    }
}
