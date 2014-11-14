package com.redditspider.biz.manager.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redditspider.biz.manager.SearchManager;
import com.redditspider.dao.SearchDao;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;

/**
 * Manager which connects to reddit and calls a service to save links etc.
 */
@Service
public class RedditManagerImpl implements SearchManager {
//    private static final transient Logger LOG = Logger.getLogger(RedditManagerImpl.class);
    @Autowired
    SearchDao searchDao;

    public Collection<Link> findLinks(SearchQuery query) {
        return searchDao.search(query).getLinks();
    }
}
