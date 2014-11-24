package com.redditspider.dao;

import com.redditspider.model.Subreddit;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

import java.util.Collection;

/**
 * Interface to connect to reddit.com.
 *
 */
public interface SearchDao {

    SearchResult search(SearchQuery query);

    Collection<Subreddit> discoverSubreddits();

}
