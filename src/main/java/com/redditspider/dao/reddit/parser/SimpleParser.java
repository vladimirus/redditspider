package com.redditspider.dao.reddit.parser;

import com.redditspider.model.reddit.SearchResult;
import org.openqa.selenium.WebDriver;

/**
 * Simple dummy parser.
 */
public class SimpleParser implements Parser {
    @Override
    public boolean isApplicable(String url) {
        return false;
    }

    @Override
    public SearchResult parse(WebDriver driver) {
        return new SearchResult();
    }
}
