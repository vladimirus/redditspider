package com.redditspider.dao.reddit.parser;

import com.redditspider.model.reddit.SearchResult;
import org.openqa.selenium.WebDriver;

/**
 * Parser that does nothing (NOP).
 */
public class NopParser implements Parser {
    @Override
    public boolean isApplicable(WebDriver driver) {
        return false;
    }

    @Override
    public SearchResult parse(WebDriver driver) {
        return new SearchResult();
    }
}
