package com.redditspider.biz.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.redditspider.model.Link;

@Service
public class LinkManagerImpl implements LinkManager {

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
		// TODO Auto-generated method stub
		return link;
	}
}
