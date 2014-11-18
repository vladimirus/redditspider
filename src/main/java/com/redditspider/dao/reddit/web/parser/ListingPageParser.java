package com.redditspider.dao.reddit.web.parser;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Parsing subreddits.
 */
public class ListingPageParser extends AbstractListingPageParser implements Parser {
    private static final transient Logger LOG = Logger.getLogger(ListingPageParser.class);
    private String subreddit;

    public ListingPageParser(WebDriver driver) {
        super(driver);
    }

    @Override
    protected String getSubredditUrl(WebElement rawEntry) {
        if (subreddit == null) {
            subreddit = parseSubreddit();
        }
        return subreddit;
    }

    private String parseSubreddit() {
        String subreddit = null;
        try {
            WebElement rawSubreddit = driver.findElement(By.cssSelector("div#header-bottom-left span.hover.pagename.redditname a"));
            subreddit = rawSubreddit.getAttribute("href");
        } catch (Exception ignore) {
            LOG.warn("Can't parse subreddit, ignoring: " + ignore);
        }

        return subreddit;
    }
}
