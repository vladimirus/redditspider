package com.redditspider.dao.reddit.parser;

import org.springframework.stereotype.Service;

/**
 * Default implementation of parser factory.
 */
@Service
public class DefaultParserFactory implements ParserFactory {

    @Override
    public Parser createParser(String url) {
        return new ListingPageParser();
    }
}
