package com.redditspider.biz.manager;

import com.redditspider.model.reddit.SearchQuery;


public interface RedditManager {

	public void findNewLinks();
	public void findNewLinks(SearchQuery query);
}
