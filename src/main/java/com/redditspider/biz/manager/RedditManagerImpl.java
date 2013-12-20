package com.redditspider.biz.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redditspider.dao.RedditDao;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

@Service
public class RedditManagerImpl implements RedditManager {
	@Autowired
	LinkManager linkManager;
	@Autowired
	RedditDao redditDao;

	public void findNewLinks() {
		SearchQuery query = new SearchQuery("http://www.reddit.com/");
		SearchResult result = redditDao.search(query);
	}
}
