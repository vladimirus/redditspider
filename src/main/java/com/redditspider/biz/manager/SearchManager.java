package com.redditspider.biz.manager;

import com.redditspider.model.Link;
import com.redditspider.model.SearchQuery;

import java.util.Collection;

/**
 * Manager to deal with reddit.com output.
 */
public interface SearchManager {
    Collection<Link> findLinks(SearchQuery query);
}
