package com.redditspider.biz.manager;

import com.redditspider.model.reddit.SearchQuery;

/**
 * Manager to deal with reddit.com output.
 *
 */
public interface RedditManager {

    void findNewLinks();
    void findNewLinks(SearchQuery query);
}
