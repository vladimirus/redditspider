package com.redditspider.model.reddit;

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Objects;
import com.redditspider.model.Link;

import java.util.List;

/**
 * Produced result.
 * TODO create the builder for this (ie. immutable)
 *
 */
public class SearchResult {
    private String nextPage;
    private String prevPage;
    private List<Link> links = newArrayList();

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(String prevPage) {
        this.prevPage = prevPage;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this.getClass()).add("nextPage", nextPage)
                .add("prevPage", prevPage)
                .add("numberOfLinks", links.size())
                .toString();
    }
}
