package com.redditspider.dao;

import java.util.List;

import com.redditspider.model.Link;

public interface LinkDao {
	public void save(Link link);
	public void save(List<Link> links);
	public List<Link> findAll();
	public Link findById(String id);
}
