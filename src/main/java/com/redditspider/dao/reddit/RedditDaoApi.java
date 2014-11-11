package com.redditspider.dao.reddit;

import org.springframework.beans.factory.annotation.Value;

import com.github.jreddit.entity.User;
import com.github.jreddit.utils.restclient.PoliteHttpRestClient;
import com.github.jreddit.utils.restclient.RestClient;
import com.redditspider.dao.SearchDao;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;

public class RedditDaoApi implements SearchDao {
    @Value("${rs.reddit.name}")
    String username;
    @Value("${rs.reddit.pass}")
    String password;

    RestClient restClient;

    public RedditDaoApi() {
        this.restClient = getRestClient();
    }

    @Override
    public SearchResult search(SearchQuery query) {


        return null;
    }

    private RestClient getRestClient() {
        RestClient restClient = new PoliteHttpRestClient();
        restClient.setUserAgent("redditspider 0.0.1");
        return restClient;
    }

    private User getUser() {
        User user = new User(restClient, username, password);
        return user;
    }

}
