package com.redditspider.dao.reddit.parser;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

/**
 * Parsing reddit.
 */
@Component
public class ListingPageParserCreator implements ParserCreator {

    @Override
    public boolean isApplicable(WebDriver driver) {
//TODO:
//        boolean applicable;
//
//        try {
//            WebElement siteTable = driver.findElement(By.id("siteTable"));
//            Collection<Link> links = processLinks(siteTable.findElements(By.className("link")), driver);
//            if (isEmpty(links)) {
//
//            }
//        } catch (Exception ignore) {
//            // not applicable
//        }

        return true;
    }

    @Override
    public Parser getInstance(WebDriver driver) {
        return new ListingPageParser(driver);
    }
}
