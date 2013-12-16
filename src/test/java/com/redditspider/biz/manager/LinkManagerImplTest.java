package com.redditspider.biz.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.redditspider.dao.LinkDao;
import com.redditspider.model.Link;

public class LinkManagerImplTest {
	private LinkManagerImpl manager;
	@Mock
	private LinkDao linkDao;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		this.manager = new LinkManagerImpl();
		this.manager.linkDao = linkDao;
	}

	@Test
	public void findAll() {
		// when
		List<Link> links = manager.findAll();
		
		// then
		assertNotNull(links);
		assertEquals(2, links.size());
	}
	
	@Test
	public void save() {
		// given
		Link link = new Link("test");
		
		// when
		Link actual = manager.save(link);
		
		// then
		assertEquals("098f6bcd4621d373cade4e832627b4f6", actual.getId());
	}
}
