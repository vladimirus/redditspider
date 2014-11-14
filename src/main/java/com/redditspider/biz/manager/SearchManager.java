package com.redditspider.biz.manager;

import java.util.Collection;

import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;

/**
 * Manager to deal with reddit.com output.
 */
public interface SearchManager {
    Collection<Link> findLinks(SearchQuery query);
}
