package com.redditspider.dao;

import org.springframework.stereotype.Repository;

import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

@Repository
public class RedditDaoImpl implements RedditDao {

	public SearchResult search(SearchQuery query) {
		return null;
	}
}
