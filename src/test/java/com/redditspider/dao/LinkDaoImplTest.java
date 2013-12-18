package com.redditspider.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoOperations;

import com.redditspider.model.Link;

@RunWith(MockitoJUnitRunner.class)
public class LinkDaoImplTest {
	private LinkDaoImpl dao;
	
	@Mock
	private MongoOperations mongoOperation;
	
	@Before
	public void before() {
		this.dao = new LinkDaoImpl();
		this.dao.mongoOperation = mongoOperation;
	}
	
	@Test
	public void save() {
		// given
		Link link = new Link("test");
		link.setId("1");
		
		dao.save(link);
		
	}
}
