package com.redditspider.biz.manager.task;

import com.redditspider.biz.manager.LinkManager;

public class LinkIndexer implements Runnable {
	private LinkManager linkManager;
	
	public LinkIndexer(LinkManager linkManager) {
		this.linkManager = linkManager;
	}
	
	public void run() {
		linkManager.index();
	}
}
