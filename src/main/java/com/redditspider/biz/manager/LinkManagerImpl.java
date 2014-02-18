package com.redditspider.biz.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import com.redditspider.biz.manager.task.ParallelTask;
import com.redditspider.dao.LinkDao;
import com.redditspider.dao.elasticsearch.ElasticSearchDao;
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
    RedditManager redditManager;
    @Autowired
    ElasticSearchDao elasticSearchDao;

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
    public void broadcast() {
        List<Link> links = getLinksToBroadcast();
        for (Link link : links) {
            elasticSearchDao.save(link);
        }
    }

    private List<Link> getLinksToBroadcast() {
        return linkDao.findToBroadcast();
    }

    private String generateId(String uri) {
        return DigestUtils.md5DigestAsHex(uri.getBytes());
    }
}
