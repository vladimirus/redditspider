package com.redditspider.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.isA;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;

import com.redditspider.biz.manager.LinkManager;
import com.redditspider.model.Link;

public class LinkControllerTest {
	private LinkController controller;

	@Mock
	private LinkManager linkManager;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		this.controller = new LinkController();
		this.controller.linkManager = this.linkManager;
	}

	@Test
	public void list() {
		// given
		given(linkManager.findAll()).willReturn(Arrays.asList(new Link("test")));

		// when
		List<Link> links = controller.list();

		// then
		assertNotNull(links);
		assertEquals(1, links.size());
	}

	@Test
	public void create() {
		// given
		Link link = new Link("test");
		link.setId("1");
		given(linkManager.save(isA(Link.class))).willReturn(link);
		
		// when
		HttpEntity<?> httpEntity = this.controller.create(new Link("test"), new StringBuffer("/links"));

		// then
		assertNotNull(httpEntity);
		assertEquals("/links/1", httpEntity.getHeaders().getLocation().toASCIIString());
	}
}
