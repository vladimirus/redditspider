package com.redditspider.dao.reddit;

import com.github.jreddit.entity.User;
import com.github.jreddit.utils.restclient.RestClient;
import com.redditspider.dao.SearchDao;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class RedditApiDao implements SearchDao {
    @Value("${rs.reddit.name}")
    String username;
    @Value("${rs.reddit.pass}")
    String password;
    @Autowired
    RestClient restClient;

    @Override
    public SearchResult search(SearchQuery query) {


        return null;
    }


    //TODO: implement caching
    private User getUser() throws Exception {
        User user = new User(restClient, username, password);
        user.connect();
        return user;
    }

}
