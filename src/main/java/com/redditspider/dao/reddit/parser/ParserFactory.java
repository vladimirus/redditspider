package com.redditspider.dao.reddit.parser;

import org.openqa.selenium.WebDriver;

/**
 * Interface for creating parsers.
 */
public interface ParserFactory {

    Parser getParser(WebDriver driver);
}
