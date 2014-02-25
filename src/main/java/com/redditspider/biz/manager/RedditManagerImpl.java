package com.redditspider.biz.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.redditspider.dao.SearchDao;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

/**
 * Manager which connects to reddit and calls a service to save links etc.
 *
 */
@Service
public class RedditManagerImpl implements SearchManager {
    @Autowired
    LinkManager linkManager;
    @Autowired
    SearchDao searchDao;

    public void findNewLinks() {
        SearchQuery query = new SearchQuery("http://www.reddit.com/");
        findNewLinks(query);
    }

    public void findNewLinks(SearchQuery q) {
        SearchQuery query = q;
        SearchResult result = retrieveSearchResult(query);
        processSearchResult(result);
        boolean checkAgain = true;

        while (checkAgain) {
            if (result != null && StringUtils.hasText(result.getNextPage())) {
                query = new SearchQuery(result.getNextPage());
                result = retrieveSearchResult(query);
                processSearchResult(result);
            } else {
                checkAgain = false;
            }
        }
    }

    void processSearchResult(SearchResult result) {
        if (result != null && !CollectionUtils.isEmpty(result.getLinks())) {
            linkManager.save(result.getLinks());
        }
    }

    SearchResult retrieveSearchResult(SearchQuery query) {
        return searchDao.search(query);
    }
}
