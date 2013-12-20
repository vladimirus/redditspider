package com.redditspider.biz.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
		SearchResult result = retrieveSearchResult(query);
		processSearchResult(result);
		boolean checkAgain = true;

		while (checkAgain) {
			if (StringUtils.hasText(result.getNextPage())) {
				result = retrieveSearchResult(query);
				processSearchResult(result);
			} else {
				checkAgain = false;
			}
		}
	}
	
	void processSearchResult(SearchResult result) {
		// TODO Auto-generated method stub
		
	}

	SearchResult retrieveSearchResult(SearchQuery query ) {
		return redditDao.search(query);
	}
}
