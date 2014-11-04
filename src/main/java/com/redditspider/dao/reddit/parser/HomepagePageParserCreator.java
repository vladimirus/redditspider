package com.redditspider.dao.reddit.parser;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * Parsing reddit.
 */
@Component
public class HomepagePageParserCreator implements ParserCreator {

    @Override
    public boolean isApplicable(WebDriver driver) {
        boolean result = false;
        try {
            URI uri = new URI(driver.getCurrentUrl());

            if ("/".equals(uri.getPath())) {
                result = true;
            }

        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    @Override
    public Parser getInstance(WebDriver driver) {
        return new HomePageParser(driver);
    }
}
