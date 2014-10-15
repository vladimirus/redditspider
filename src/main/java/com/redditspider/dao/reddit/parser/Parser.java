package com.redditspider.dao.reddit.parser;

import com.redditspider.model.reddit.SearchResult;
import org.openqa.selenium.WebDriver;

/**
 * Parser interface for reddit pages.
 */
public interface Parser {

    /**
     * Parses a reddit's page.
     *
     * @return Search result.
     */
    SearchResult parse(WebDriver driver);
}
