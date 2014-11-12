package com.redditspider.model.reddit;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.collect.Lists.newArrayList;

import com.redditspider.model.Link;

import java.util.List;

/**
 * Produced result.
 * TODO create the builder for this (ie. immutable)
 */
public class SearchResult {
    private List<Link> links = newArrayList();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("numberOfLinks", links.size())
                .toString();
    }
}
