package com.redditspider.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.redditspider.biz.manager.LinkManagerImpl;
import com.redditspider.web.model.Link;

public class LinkControllerTest {
	private LinkController controller;

	@Before
	public void before() {
		this.controller = new LinkController();
		this.controller.linkManager = new LinkManagerImpl();
	}

	@Test
	public void list() {
		List<Link> links = controller.list();
		assertNotNull(links);
		assertEquals(2, links.size());
	}
}
