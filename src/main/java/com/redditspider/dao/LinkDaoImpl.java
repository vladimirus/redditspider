package com.redditspider.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.redditspider.model.Link;

@Repository
public class LinkDaoImpl implements LinkDao {
	@Autowired
	MongoOperations mongoOperation;

	public void save(Link link) {
		mongoOperation.save(link);
	}
	
	public void save(List<Link> links) {
		mongoOperation.save(links);
	}
}
