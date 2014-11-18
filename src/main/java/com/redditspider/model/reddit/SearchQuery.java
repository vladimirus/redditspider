package com.redditspider.model.reddit;

/**
 * Query to search.
 *
 */
public class SearchQuery {
    private String query;

    public SearchQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
