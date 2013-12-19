package com.redditspider.biz.manager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RedditManagerImplTest {
	private RedditManagerImpl manager;
	
	@Mock
	private LinkManager linkManager;
	
	@Before
	public void before() {
		this.manager = new RedditManagerImpl();
		this.manager.linkManager = linkManager;
	}
	
	@Test
	public void findNewLinks() {
		
		//when
		manager.findNewLinks();
	}
}
