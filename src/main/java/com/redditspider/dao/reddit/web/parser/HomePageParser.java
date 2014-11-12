package com.redditspider.dao.reddit.web.parser;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Parsing reddit's homepage.
 */
public class HomePageParser extends AbstractListingPageParser implements Parser {

    public HomePageParser(WebDriver driver) {
        super(driver);
    }

    @Override
    protected String getGroupUri(WebElement rawEntry) {
        String groupUri;
        try {
            WebElement rawSubreddit = rawEntry.findElement(By.cssSelector("a.subreddit"));
            groupUri = rawSubreddit.getAttribute("href");
        } catch (NoSuchElementException ignore) {
            groupUri = null;
        }

        return groupUri;
    }
}
