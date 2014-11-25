package com.redditspider.dao.reddit.api;

import static java.util.concurrent.TimeUnit.MINUTES;

import com.github.jreddit.entity.User;
import com.github.jreddit.utils.restclient.RestClient;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

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

    Cache<String, User> cache;

    public RedditApiUserManager() {
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(5)
                .expireAfterWrite(10, MINUTES)
                .build();
    }

    User getUser() throws Exception {
        return cache.get("user", new Callable<User>() {
            @Override
            public User call() throws Exception {
                return getUserFromApi();
            }
        });

    }

    private User getUserFromApi() throws Exception {
        User user = new User(restClient, username, password);
        user.connect();
        return user;
    }
}
