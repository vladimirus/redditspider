package com.redditspider.biz.manager.impl;

import static com.codahale.metrics.MetricRegistry.name;
import static com.google.common.base.Splitter.on;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.DigestUtils.md5DigestAsHex;
import static org.springframework.util.StringUtils.hasText;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.redditspider.biz.manager.LinkManager;
import com.redditspider.biz.manager.SearchManager;
import com.redditspider.biz.manager.task.ParallelTask;
import com.redditspider.dao.LinkDao;
import com.redditspider.dao.LinkExtendedDao;
import com.redditspider.model.EntryLink;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    @Value("${entryLink.initial}")
    String initialEntryLink;
    boolean firstRun = true;
    @Autowired
    MetricRegistry metricRegistry;

    @Override
    public List<Link> findAll() {
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
    @Scheduled(cron = "0 */1 * * * ?")
    public void index() {
        SearchQuery query = new SearchQuery(mongoDao.nextEntryLink().getUri());
        save(recordMetric(redditManager.findLinks(query), query.getSearchUri()));
    }

    @Override
    public void save(List<Link> links) {
        if (!isEmpty(links)) {
            Set<EntryLink> entryLinks = newHashSet();

            for (Link link : links) {
                link.setId(generateId(link.getCommentsUri()));

                if (hasText(link.getGroupUri())) {
                    entryLinks.add(createEntryLink(link.getGroupUri()));
                }

            }
            mongoDao.save(links);
            addFirstRunEntryLink(entryLinks);
            saveEntryLinks(entryLinks);
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
    @Scheduled(cron = "0 */1 * * * ?")
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

    Collection<EntryLink> saveEntryLinks(Iterable<EntryLink> entryLinks) {
        return from(entryLinks).filter(new Predicate<EntryLink>() {
            @Override
            public boolean apply(EntryLink input) {
                return (mongoDao.findEntryLinkById(input.getId()) == null);
            }
        }).transform(new Function<EntryLink, EntryLink>() {
            @Override
            public EntryLink apply(EntryLink input) {
                mongoDao.insertEntryLink(input);
                return input;
            }
        }).toList();
    }

    List<Link> recordMetric(List<Link> links, String uri) {
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

    private void addFirstRunEntryLink(Set<EntryLink> entryLinks) {
        if (firstRun && hasText(initialEntryLink)) {
            entryLinks.add(createEntryLink(initialEntryLink));
            firstRun = false;
        }
    }

    private EntryLink createEntryLink(String uri) {
        EntryLink entryLink = new EntryLink(generateId(uri), uri);
        entryLink.setUpdated(new Date());
        return entryLink;
    }

    private List<Link> getLinksToBroadcast() {
        return mongoDao.findToBroadcast();
    }

    private String generateId(String uri) {
        return md5DigestAsHex(uri.getBytes());
    }
}
