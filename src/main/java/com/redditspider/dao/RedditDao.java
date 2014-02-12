package com.redditspider.dao;

import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

/**
 * Interface to connect to reddit.com.
 *
 */
public interface RedditDao {

    SearchResult search(SearchQuery query);

}
