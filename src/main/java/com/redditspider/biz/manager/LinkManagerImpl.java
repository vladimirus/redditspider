package com.redditspider.biz.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
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

	public List<Link> findAll() {
		return createDummyLinks();
	}
	
	private List<Link> createDummyLinks() {
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("http://dummy1"));
		links.add(new Link("http://dummy1"));
		return links;
	}

	public Link save(Link link) {
		String id = generateId(link.getUri());
		link.setId(id);
		linkDao.save(link);
		return link;
	}

	public void startIndexThread() {
		LinkIndexer linkIndexer = new LinkIndexer(this);
		taskExecutor.execute(linkIndexer);
	}
	
	public void index() {
		// TODO Auto-generated method stub
		
	}
	
	private String generateId(String uri) {
		return DigestUtils.md5DigestAsHex(uri.getBytes());
	}
}
