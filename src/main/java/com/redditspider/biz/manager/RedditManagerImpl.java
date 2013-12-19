package com.redditspider.biz.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redditspider.dao.RedditDao;

@Service
public class RedditManagerImpl implements RedditManager {
	@Autowired
	LinkManager linkManager;
	@Autowired
	RedditDao redditDao;

	public void findNewLinks() {
		// TODO Auto-generated method stub
	}
}
