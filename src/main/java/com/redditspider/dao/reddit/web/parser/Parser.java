package com.redditspider.dao.reddit.web.parser;

import com.redditspider.model.reddit.WebSearchResult;

/**
 * Parser interface for reddit pages.
 */
public interface Parser {
    /**
     * Parses a reddit's page.
     *
     * @return Search result.
     */
    WebSearchResult parse();
}
