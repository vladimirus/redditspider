package com.redditspider.biz.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import com.redditspider.biz.manager.LinkManager;
import com.redditspider.biz.manager.SearchManager;
import com.redditspider.biz.manager.task.ParallelTask;
import com.redditspider.dao.ElasticsearchDao;
import com.redditspider.dao.LinkDao;
import com.redditspider.model.Link;

/**
 * Manager for link manupulation. addition/removal etc.
 */
@Service
public class LinkManagerImpl implements LinkManager {
    @Autowired
    LinkDao linkDao;
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    SearchManager redditManager;
    @Autowired
    ElasticsearchDao elasticsearchDao;

    @Override
    public List<Link> findAll() {
        return linkDao.findAll();
    }

    @Override
    public Link save(Link link) {
        if (link != null) {
            link.setId(generateId(link.getUri()));
            linkDao.save(link);
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
        redditManager.findNewLinks();
    }

    @Override
    public void save(List<Link> links) {
        if (!CollectionUtils.isEmpty(links)) {
            for (Link link : links) {
                link.setId(generateId(link.getUri()));
            }
            linkDao.save(links);
        }
    }

    @Override
    public Link findById(String id) {
        return linkDao.findById(id);
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
            linkDao.delete(link);
        }
    }

    @Override
    public void deleteAll() {
        linkDao.deleteAll();
        elasticsearchDao.delete();
    }

    private List<Link> getLinksToBroadcast() {
        return linkDao.findToBroadcast();
    }

    private String generateId(String uri) {
        return DigestUtils.md5DigestAsHex(uri.getBytes());
    }
}
