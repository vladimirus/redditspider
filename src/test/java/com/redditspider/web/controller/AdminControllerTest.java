package com.redditspider.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.redditspider.biz.manager.LinkManager;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {
	private AdminController controller;
	@Mock
	private LinkManager linkManager;

	@Before
	public void before() {
		this.controller = new AdminController();
		this.controller.linkManager = linkManager;
	}

	@Test
	public void index() {
		
		// when
		String actual = controller.index();
		
		// then
		verify(linkManager).startIndexThread();
		assertEquals("started", actual);
	}
}
