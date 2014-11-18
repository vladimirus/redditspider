package com.redditspider.dao.elasticsearch;

import com.redditspider.model.Link;
import org.springframework.stereotype.Component;

/**
 * Converts objects to ElasticSearch and back.
 */
@Component
public class ElasticsearchConverter {

    public ElasticLink convert(Link link) {
        ElasticLink elasticLink = new ElasticLink();
        elasticLink.setId(link.getId());
        elasticLink.setRating(link.getUp() - link.getDown());
        elasticLink.setUri(link.getUri());
        elasticLink.setText(link.getTitle());
        elasticLink.setCreated(link.getCreated());
        elasticLink.setCommentsUri(link.getCommentsUri());
        return elasticLink;
    }
}
