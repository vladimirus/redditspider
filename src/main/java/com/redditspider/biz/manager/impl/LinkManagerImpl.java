package com.redditspider.biz.manager.impl;

import static com.codahale.metrics.MetricRegistry.name;
import static com.google.common.base.Splitter.on;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.redditspider.biz.manager.LinkManager;
import com.redditspider.biz.manager.SearchManager;
import com.redditspider.biz.manager.task.ParallelTask;
import com.redditspider.dao.LinkDao;
import com.redditspider.dao.LinkExtendedDao;
import com.redditspider.model.Link;
import com.redditspider.model.Subreddit;
import com.redditspider.model.reddit.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Manager for link manipulation. addition/removal etc.
 */
@Service
public class LinkManagerImpl implements LinkManager {
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    SearchManager redditManager;
    @Autowired
    @Qualifier("mongoDaoImpl")
    LinkExtendedDao mongoDao;
    @Autowired
    @Qualifier("elasticsearchDaoImpl")
    LinkDao elasticsearchDao;
    @Autowired
    MetricRegistry metricRegistry;

    @Override
    public Collection<Link> findAll() {
        return mongoDao.findAll();
    }

    @Override
    public Link save(Link link) {
        if (link != null) {
            save(newArrayList(link));
        }
        return link;
    }

    @Override
    public void startIndexThread() {
        ParallelTask linkIndexer = new ParallelTask(this, "index");
        taskExecutor.execute(linkIndexer);
    }

    @Override
    @Scheduled(initialDelay = 120000, fixedRate = 60000)
    public void index() {
        Subreddit subreddit = mongoDao.next();
        if (subreddit != null) {
            SearchQuery query = new SearchQuery(subreddit.getName());
            save(recordMetric(redditManager.findLinks(query), query.getQuery()));
        }
    }

    @Override
    public void save(Collection<Link> links) {
        if (!isEmpty(links)) {
            mongoDao.save(from(links).transform(new Function<Link, Link>() {
                @Override
                public Link apply(Link input) {
                    input.setId(generateId(input.getPermalink()));
                    return input;
                }
            }).toList());
        }
    }

    @Override
    public Link findById(String id) {
        return mongoDao.findById(id);
    }

    @Override
    public void startBroadcastThread() {
        ParallelTask linkBroadcaster = new ParallelTask(this, "broadcast");
        taskExecutor.execute(linkBroadcaster);
    }

    @Override
    @Scheduled(initialDelay = 120000, fixedRate = 60000)
    public void broadcast() {
        List<Link> links = getLinksToBroadcast();
        for (Link link : links) {
            elasticsearchDao.save(link);
            mongoDao.delete(link);
        }
    }

    @Override
    public void deleteAll() {
        mongoDao.deleteAll();
        elasticsearchDao.deleteAll();
    }

    Collection<Subreddit> saveSubreddits(Iterable<Subreddit> subreddits) {
        return from(subreddits).filter(new Predicate<Subreddit>() {
            @Override
            public boolean apply(Subreddit input) {
                return mongoDao.findSubredditById(input.getId()) == null;
            }
        }).transform(new Function<Subreddit, Subreddit>() {
            @Override
            public Subreddit apply(Subreddit input) {
                mongoDao.insert(input);
                return input;
            }
        }).toList();
    }

    Collection<Link> recordMetric(Collection<Link> links, String uri) {
        final Meter meter = metricRegistry.meter(metricName(uri));
        if (!links.isEmpty()) {
            meter.mark(links.size());
        } else {
            meter.mark(0);
        }
        return links;
    }

    private String metricName(String uri) {
        return name("link.stored", getLast(on('/').trimResults().omitEmptyStrings().split(uri)));
    }

    private List<Link> getLinksToBroadcast() {
        return mongoDao.findToBroadcast();
    }

    private String generateId(String uri) {
        return md5DigestAsHex(uri.getBytes());
    }
}
