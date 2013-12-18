package com.redditspider.biz.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.redditspider.dao.LinkDao;
import com.redditspider.model.Link;

@RunWith(MockitoJUnitRunner.class)
public class LinkManagerImplTest {
	private LinkManagerImpl manager;
	@Mock
	private LinkDao linkDao;
	@Mock
	private ThreadPoolTaskExecutor taskExecutor;

	@Before
	public void before() {
		this.manager = new LinkManagerImpl();
		this.manager.linkDao = linkDao;
		this.manager.taskExecutor = taskExecutor;
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
		verify(linkDao).save(link);
		assertEquals("098f6bcd4621d373cade4e832627b4f6", actual.getId());
	}
	
	@Test
	public void startIndex() {
		
		// when
		manager.startIndexThread();
		
		// then
		verify(taskExecutor).execute(Mockito.isA(Runnable.class));
	}
}
