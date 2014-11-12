package com.redditspider.dao.reddit.api;

import com.github.jreddit.entity.User;
import com.github.jreddit.utils.restclient.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Reddit's api to connect to user account.
 */
@Component
public class RedditApiUserManager {
    @Value("${rs.reddit.name}")
    String username;
    @Value("${rs.reddit.pass}")
    String password;
    @Autowired
    RestClient restClient;

    //TODO: implement caching
    User getUser() throws Exception {
        User user = new User(restClient, username, password);
        user.connect();
        return user;
    }
}
