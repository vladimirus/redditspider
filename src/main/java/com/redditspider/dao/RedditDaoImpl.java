package com.redditspider.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.redditspider.dao.browser.WebBrowserPool;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

@Repository
public class RedditDaoImpl implements RedditDao {
	@Autowired
	private WebBrowserPool webBrowserPool;
	
	public SearchResult search(SearchQuery query) {
		SearchResult searchResult = new SearchResult();
		if (query != null && StringUtils.hasText(query.getSearchUri())) {
			//todo
		}
		return searchResult;
	}
}
