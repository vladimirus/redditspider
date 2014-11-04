package com.redditspider.dao.reddit.parser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Parsing subreddits.
 */
public class ListingPageParser extends AbstractListingPageParser implements Parser {
    private String groupUri;

    public ListingPageParser(WebDriver driver) {
        super(driver);
    }

    @Override
    protected String getGroupUri(WebElement rawEntry) {
        if (groupUri == null) {
            groupUri = parseGroupUri(rawEntry);
        }
        return groupUri;
    }

    private String parseGroupUri(WebElement rawEntry) {
        String groupUri;
        try {
            WebElement rawSubreddit = driver.findElement(By.cssSelector("div#header-bottom-left span.hover.pagename.redditname a"));
            groupUri = rawSubreddit.getAttribute("href");
        } catch (Exception ignore) {
            groupUri = null;
        }

        return groupUri;
    }
}
