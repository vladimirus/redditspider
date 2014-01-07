package com.redditspider.biz.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import com.redditspider.biz.manager.task.LinkIndexer;
import com.redditspider.dao.LinkDao;
import com.redditspider.model.Link;

@Service
public class LinkManagerImpl implements LinkManager {
	@Autowired
	LinkDao linkDao;
	@Autowired
	ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	RedditManager redditManager;
	
	public List<Link> findAll() {
		return linkDao.findAll();
	}

	public Link save(Link link) {
		if (link != null) {
			link.setId(generateId(link.getUri()));
			linkDao.save(link);
		}
		return link;
	}

	public void startIndexThread() {
		LinkIndexer linkIndexer = new LinkIndexer(this);
		taskExecutor.execute(linkIndexer);
	}
	
	public void index() {
		redditManager.findNewLinks();
	}

	public void save(List<Link> links) {
		if (!CollectionUtils.isEmpty(links)) {
			for (Link link : links) {
				link.setId(generateId(link.getUri()));
			}
			linkDao.save(links);
		}
	}
	
	private String generateId(String uri) {
		return DigestUtils.md5DigestAsHex(uri.getBytes());
	}
}
