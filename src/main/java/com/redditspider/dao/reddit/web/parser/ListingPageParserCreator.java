package com.redditspider.dao.reddit.web.parser;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * Parsing reddit.
 */
@Component
public class ListingPageParserCreator implements ParserCreator {

    @Override
    public boolean isApplicable(WebDriver driver) {
        boolean result = false;
        try {
            URI uri = new URI(driver.getCurrentUrl());

            if (uri.getPath().matches("^/r/[^/]*/$")) {
                result = true;
            }

        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    @Override
    public Parser getInstance(WebDriver driver) {
        return new ListingPageParser(driver);
    }
}
