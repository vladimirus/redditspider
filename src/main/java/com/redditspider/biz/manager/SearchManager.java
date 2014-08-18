package com.redditspider.biz.manager;

import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;

import java.util.List;

/**
 * Manager to deal with reddit.com output.
 *
 */
public interface SearchManager {
    List<Link> findNewLinks();
    List<Link> findNewLinks(SearchQuery query);
}
