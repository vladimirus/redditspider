package com.redditspider.dao;

import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

public interface RedditDao {

	public SearchResult search(SearchQuery query);

}
