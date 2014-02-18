package com.redditspider.model;

import java.util.Date;

import com.redditspider.dao.elasticsearch.ElasticLink;

/**
 * It is responsible to instantiate default domain class instances.
 */
public final class DomainFactory {

    private DomainFactory() {
        // Don't instantiate
    }

    public static Link aLink() {
        Link link = new Link("http://example.com");
        link.setCreated(new Date());
        link.setUp(10);
        link.setDown(4);
        link.setText("Some text");
        link.setId("this-is-id");
        return link;
    }

    public static ElasticLink anElasticLink() {
        ElasticLink link = new ElasticLink();
        link.setUri("http://example.com");
        link.setRating(10);
        link.setText("Some text");
        link.setId("this-is-id");
        return link;
    }
}
