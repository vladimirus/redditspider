package com.redditspider.biz.manager.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

import com.redditspider.biz.manager.LinkManager;
import com.redditspider.biz.manager.SearchManager;
import com.redditspider.biz.manager.task.ParallelTask;
import com.redditspider.dao.LinkDao;
import com.redditspider.dao.LinkExtendedDao;
import com.redditspider.model.EntryLink;
import com.redditspider.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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
        save(redditManager.findLinks());
    }

    @Override
    public void save(List<Link> links) {
        if (!isEmpty(links)) {
            Set<EntryLink> entryLinks = newHashSet();
            for (Link link : links) {
                link.setId(generateId(link.getCommentsUri()));

                if (hasText(link.getGroupUri())) {
                    entryLinks.add(createEntryLink(link.getCommentsUri()));
                }

            }
            mongoDao.save(links);
            // TODO save entryLinks
        }
    }

    private EntryLink createEntryLink(String uri) {
        EntryLink entryLink = new EntryLink(generateId(uri), uri);
        entryLink.setUpdated(new Date());
        return entryLink;
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

    private List<Link> getLinksToBroadcast() {
        return mongoDao.findToBroadcast();
    }

    private String generateId(String uri) {
        return DigestUtils.md5DigestAsHex(uri.getBytes());
    }
}
