package com.redditspider.biz.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedditManagerImpl implements RedditManager {
	@Autowired
	LinkManager linkManager;

	public void findNewLinks() {
		// TODO Auto-generated method stub
	}
}
