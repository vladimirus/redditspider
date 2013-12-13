package com.redditspider.biz.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.redditspider.model.Link;

public class LinkManagerImplTest {
	private LinkManager manager;

	@Before
	public void before() {
		this.manager = new LinkManagerImpl();
	}

	@Test
	public void findAll() {
		// when
		List<Link> links = manager.findAll();
		
		// then
		assertNotNull(links);
		assertEquals(2, links.size());
	}
}
