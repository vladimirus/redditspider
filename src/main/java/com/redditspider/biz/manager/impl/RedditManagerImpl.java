package com.redditspider.biz.manager.impl;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.util.StringUtils.hasText;

import com.redditspider.biz.manager.SearchManager;
import com.redditspider.dao.SearchDao;
import com.redditspider.model.Link;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Manager which connects to reddit and calls a service to save links etc.
 */
@Service
public class RedditManagerImpl implements SearchManager {
    @Autowired
    SearchDao searchDao;

    public List<Link> findLinks(SearchQuery query) {
        List<Link> links = newArrayList();
        return findNewLinks(query, links);
    }

    private List<Link> findNewLinks(SearchQuery query, List<Link> links) {
        SearchResult result = retrieveSearchResult(query);
        links.addAll(processSearchResult(result));

        if (hasText(result.getNextPage())) {
            SearchQuery q = new SearchQuery(result.getNextPage());
            findNewLinks(q, links);
        }
        return links;
    }

    List<Link> processSearchResult(SearchResult result) {
        return result.getLinks();
    }

    SearchResult retrieveSearchResult(SearchQuery query) {
        return searchDao.search(query);
    }
}
