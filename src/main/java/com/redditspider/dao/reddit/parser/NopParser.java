package com.redditspider.dao.reddit.parser;

import com.redditspider.model.reddit.SearchResult;

/**
 * Parser that does nothing (NOP).
 */
public class NopParser implements Parser {

    @Override
    public SearchResult parse() {
        return new SearchResult();
    }
}
