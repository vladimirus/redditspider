package com.redditspider.model.reddit;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import com.redditspider.model.Link;

/**
 * Produced result.
 * TODO create the builder for this (ie. immutable)
 */
public class SearchResult {
    private Collection<Link> links = newArrayList();

    public Collection<Link> getLinks() {
        return links;
    }

    public void setLinks(Collection<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("numberOfLinks", links.size())
                .toString();
    }
}
