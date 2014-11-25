package com.redditspider.dao;

import com.redditspider.model.SearchQuery;
import com.redditspider.model.SearchResult;
import com.redditspider.model.Subreddit;

import java.util.Collection;

/**
 * Interface to connect to reddit.com.
 */
public interface SearchDao {

    SearchResult search(SearchQuery query);

    Collection<Subreddit> discoverSubreddits();

}
