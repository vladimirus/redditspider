package com.redditspider.dao.reddit.parser;

import org.openqa.selenium.WebDriver;

/**
 * Parser interface for reddit pages.
 */
public interface ParserCreator {

    /**
     * A method to decided if this parser can be used parsing given url.
     *
     * @param driver - page to parse
     * @return true if this parser can be used, false otherwise
     */
    boolean isApplicable(WebDriver driver);

    /**
     * Returns instance of a parser.
     *
     * @param driver
     * @return
     */
    Parser getInstance(WebDriver driver);
}
