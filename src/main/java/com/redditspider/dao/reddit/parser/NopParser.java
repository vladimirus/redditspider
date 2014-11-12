package com.redditspider.dao.reddit.parser;

import com.redditspider.model.reddit.WebSearchResult;

/**
 * Parser that does nothing (NOP).
 */
public class NopParser implements Parser {

    @Override
    public WebSearchResult parse() {
        return new WebSearchResult();
    }
}
