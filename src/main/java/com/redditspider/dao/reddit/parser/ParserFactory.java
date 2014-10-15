package com.redditspider.dao.reddit.parser;

/**
 * Interface for creating parsers.
 */
public interface ParserFactory {

    Parser createParser(String url);
}
