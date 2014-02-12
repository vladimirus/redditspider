package com.redditspider.model.reddit;

/**
 * Query to search.
 *
 */
public class SearchQuery {
    private String searchUri;

    public SearchQuery(String searchUri) {
        this.searchUri = searchUri;
    }

    public String getSearchUri() {
        return searchUri;
    }
}
