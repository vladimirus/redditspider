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
    private String groupUri;

    public ListingPageParser(WebDriver driver) {
        super(driver);
    }

    @Override
    protected String getGroupUri(WebElement rawEntry) {
        if (groupUri == null) {
            groupUri = parseGroupUri();
        }
        return groupUri;
    }

    private String parseGroupUri() {
        String groupUri = null;
        try {
            WebElement rawSubreddit = driver.findElement(By.cssSelector("div#header-bottom-left span.hover.pagename.redditname a"));
            groupUri = rawSubreddit.getAttribute("href");
        } catch (Exception ignore) {
            LOG.warn("Can't parse groupUri, ignoring: " + ignore);
        }

        return groupUri;
    }
}
