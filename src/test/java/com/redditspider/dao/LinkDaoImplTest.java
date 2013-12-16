package com.redditspider.dao;

import static org.mockito.BDDMockito.given;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;

import com.redditspider.model.Link;

public class LinkDaoImplTest {
	private LinkDaoImpl dao;
	
	@Mock
	private MongoOperations mongoOperation;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
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
