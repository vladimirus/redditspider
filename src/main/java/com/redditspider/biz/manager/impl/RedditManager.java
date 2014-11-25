package com.redditspider.biz.manager.impl;

import com.redditspider.biz.manager.SearchManager;
import com.redditspider.dao.SearchDao;
import com.redditspider.model.Link;
import com.redditspider.model.SearchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Manager which connects to reddit and calls a service to save links etc.
 */
@Service
public class RedditManager implements SearchManager {
    @Autowired
    SearchDao searchDao;

    public Collection<Link> findLinks(SearchQuery query) {
        return searchDao.search(query).getLinks();
    }
}
