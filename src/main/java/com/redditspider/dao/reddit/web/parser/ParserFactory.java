package com.redditspider.dao.reddit.web.parser;

import org.openqa.selenium.WebDriver;

/**
 * Interface for creating parsers.
 */
public interface ParserFactory {

    Parser getParser(WebDriver driver);
}
