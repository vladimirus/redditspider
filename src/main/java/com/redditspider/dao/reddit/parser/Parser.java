package com.redditspider.dao.reddit.parser;

import com.redditspider.model.reddit.SearchResult;

/**
 * Parser interface for reddit pages.
 */
public interface Parser {

    /**
     * Parses a reddit's page.
     *
     * @return Search result.
     */
    SearchResult parse();
}
