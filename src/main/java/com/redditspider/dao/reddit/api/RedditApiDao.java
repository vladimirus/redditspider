package com.redditspider.dao.reddit.api;

import com.github.jreddit.utils.restclient.RestClient;
import com.redditspider.dao.SearchDao;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;

public class RedditApiDao implements SearchDao {
    @Autowired
    RestClient restClient;
    @Autowired
    RedditApiUserManager userManager;

    @Override
    public SearchResult search(SearchQuery query) {


        return null;
    }


}
