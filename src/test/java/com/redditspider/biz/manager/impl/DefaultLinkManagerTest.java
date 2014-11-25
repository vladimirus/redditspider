package com.redditspider.biz.manager.impl;

import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static com.redditspider.model.DomainFactory.aLink;
import static com.redditspider.model.DomainFactory.aSubredditWithId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.redditspider.biz.manager.SearchManager;
import com.redditspider.dao.LinkDao;
import com.redditspider.dao.LinkExtendedDao;
import com.redditspider.model.Link;
import com.redditspider.model.SearchQuery;
import com.redditspider.model.Subreddit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collection;

/**
 * Test for LinkManager.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultLinkManagerTest {
    private DefaultLinkManager manager;
    @Mock
    private LinkExtendedDao mongoDao;
    @Mock
    private ThreadPoolTaskExecutor taskExecutor;
    @Mock
    private SearchManager redditManager;
    @Mock
    private LinkDao elasticsearchDao;
    @Mock
    private MetricRegistry metricRegistry;
    @Mock
    private Meter meter;

    @Before
    public void before() {
        this.manager = new DefaultLinkManager();
        this.manager.mongoDao = mongoDao;
        this.manager.taskExecutor = taskExecutor;
        this.manager.redditManager = redditManager;
        this.manager.elasticsearchDao = elasticsearchDao;
        this.manager.metricRegistry = metricRegistry;
    }

    @Test
    public void findAll() {
        // given
        given(mongoDao.findAll()).willReturn(newArrayList(aLink(), aLink()));

        // when
        Collection<Link> actual = manager.findAll();

        // then
        assertThat(actual, hasSize(2));
    }

    @Test
    public void save() {
        // given
        Link link = new Link();
        link.setPermalink("test");

        // when
        Link actual = manager.save(link);

        // then
        verify(mongoDao).save(anyCollectionOf(Link.class));
        assertThat(actual.getId(), equalTo("098f6bcd4621d373cade4e832627b4f6"));
    }

    @Test
    public void saveNull() {

        // when
        manager.save((Link) null);

        // then
        verify(mongoDao, never()).save((Link) null);
    }

    @Test
    public void startIndex() {

        // when
        manager.startIndexThread();

        // then
        verify(taskExecutor).execute(isA(Runnable.class));
    }

    @Test
    public void saveMany() {
        // given
        Collection<Link> links = newArrayList(aLink(), aLink());

        // when
        manager.save(links);

        // then
        verify(mongoDao).save(links);
    }

    @Test
    public void saveNone() {
        // given
        Collection<Link> links = newArrayList();

        // when
        manager.save(links);

        // then
        verify(mongoDao, never()).save(links);
    }

    @Test
    public void saveNullLinks() {

        // when
        manager.save((Collection<Link>) null);

        // then
        verify(mongoDao, never()).save((Collection<Link>) null);
    }

    @Test
    public void index() {
        // given
        Collection<Link> links = newArrayList(aLink(), aLink());
        given(redditManager.findLinks(isA(SearchQuery.class))).willReturn(links);
        given(mongoDao.next()).willReturn(new Subreddit("test", "test"));
        given(metricRegistry.meter(isA(String.class))).willReturn(meter);

        // when
        manager.index();

        // then
        verify(redditManager).findLinks(isA(SearchQuery.class));
        verify(mongoDao).next();
        verify(mongoDao).save(links);
        verify(metricRegistry, times(1)).meter("link.stored.test");
    }

    @Test
    public void findById() {
        // given
        given(mongoDao.findById("1")).willReturn(aLink());

        // when
        Link link = manager.findById("1");

        // then
        verify(mongoDao).findById("1");
        assertThat(link, is(notNullValue()));
    }

    @Test
    public void startBroadcast() {

        // when
        manager.startBroadcastThread();

        // then
        verify(taskExecutor).execute(isA(Runnable.class));
    }

    @Test
    public void broadcast() {
        // given
        given(mongoDao.findToBroadcast()).willReturn(newArrayList(aLink(), aLink()));

        // when
        manager.broadcast();

        // then
        verify(mongoDao).findToBroadcast();
        verify(elasticsearchDao, times(2)).save(isA(Link.class));
        verify(mongoDao, times(2)).delete(isA(Link.class));
    }

    @Test
    public void deleteAll() {

        // when
        manager.deleteAll();

        // then
        verify(mongoDao).deleteAll();
        verify(elasticsearchDao).deleteAll();
    }

    @Test
    public void saveSubreddits() {
        // given
        Subreddit subreddit1 = aSubredditWithId("SubredditId1"); // new entry
        Subreddit subreddit2 = aSubredditWithId("SubredditId2"); // assume this is already exists
        given(mongoDao.findSubredditById(isA(String.class))).willReturn(null, subreddit2);

        // when
        Collection<Subreddit> actual = manager.saveSubreddits(newHashSet(subreddit1, subreddit2));

        // then
        assertThat(actual, hasSize(1));
        verify(mongoDao).insert(subreddit1);
        verify(mongoDao, never()).insert(subreddit2);
        assertThat(get(actual, 0).getId(), is("SubredditId1"));
    }

    @Test
    public void recordMetric() {
        // given
        Collection<Link> links = newArrayList(aLink(), aLink());
        given(metricRegistry.meter(isA(String.class))).willReturn(meter);

        // when
        Collection<Link> outLinks = manager.recordMetric(links, "http://reddit.com/r/subreddit");

        assertThat(outLinks, is(equalTo(links)));
        verify(metricRegistry, times(1)).meter("link.stored.subreddit");
        verify(meter, times(1)).mark(2);
    }

    @Test
    public void recordZeroMetric() {
        // given
        Collection<Link> links = newArrayList();
        given(metricRegistry.meter(isA(String.class))).willReturn(meter);

        // when
        Collection<Link> outLinks = manager.recordMetric(links, "http://reddit.com/r/subreddit");

        assertThat(outLinks, is(equalTo(links)));
        verify(metricRegistry, times(1)).meter("link.stored.subreddit");
        verify(meter, times(1)).mark(0);
    }
}
