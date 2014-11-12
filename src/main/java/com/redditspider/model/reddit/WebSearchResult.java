package com.redditspider.model.reddit;

/**
 * Produced result.
 * TODO create the builder for this (ie. immutable)
 */
public class WebSearchResult extends SearchResult {
    private String nextPage;
    private String prevPage;

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(String prevPage) {
        this.prevPage = prevPage;
    }
}
