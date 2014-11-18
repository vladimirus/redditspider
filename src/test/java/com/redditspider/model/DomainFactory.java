package com.redditspider.model;

import com.redditspider.dao.elasticsearch.ElasticLink;

import java.util.Date;

/**
 * It is responsible to instantiate default domain class instances.
 */
public final class DomainFactory {

    private DomainFactory() {
        // Don't instantiate
    }

    public static Link aLink() {
        return aLinkWithId("this-is-id");
    }

    public static Link aLinkWithId(String id) {
        Link link = new Link("http://example.com");
        link.setCreated(new Date());
        link.setUp(10);
        link.setDown(4);
        link.setTitle("Some text");
        link.setId(id);
        link.setCommentsUri("http://example.com/comments");
        return link;
    }

    public static ElasticLink anElasticLink() {
        ElasticLink link = new ElasticLink();
        link.setUri("http://example.com");
        link.setRating(10);
        link.setText("Some text");
        link.setId("this-is-id");
        link.setCommentsUri("http://example.com/comments");
        return link;
    }

    public static Subreddit anEntryLink() {
        return anEntryLinkWithId("this-is-id");
    }

    public static Subreddit anEntryLinkWithId(String id) {
        Subreddit subreddit = new Subreddit(id, "url");
        subreddit.setUpdated(new Date());
        return subreddit;
    }
}
