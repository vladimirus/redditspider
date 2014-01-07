package com.redditspider.dao;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

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
		
		// when
		dao.save(link);
		
		// then
		verify(mongoOperation).save(link);
	}
	
	@Test
	public void saveMany() {
		// given
		List<Link> links = new ArrayList<Link>();
		links.add(new Link("test1"));
		links.add(new Link("test2"));
		
		// when
		dao.save(links);
		
		// then
		verify(mongoOperation).save(links);
	}
	
	@Test
	public void findAll() {
		
		// when
		dao.findAll();
		
		// then
		verify(mongoOperation).findAll(Link.class);
	}
}
