package com.redditspider.dao.reddit.parser;

import com.redditspider.model.reddit.SearchResult;
import org.openqa.selenium.WebDriver;

/**
 * Parser interface for reddit pages.
 */
public interface Parser {

    /**
     * A method to decided if this parser can be used parsing given url.
     * @param driver - page to parse
     * @return true if this parser can be used, false otherwise
     */
    boolean isApplicable(WebDriver driver);

    /**
     * Parses a reddit's page.
     *
     * @return Search result.
     */
    SearchResult parse(WebDriver driver);
}
