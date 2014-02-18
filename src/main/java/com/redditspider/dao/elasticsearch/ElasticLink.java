package com.redditspider.dao.elasticsearch;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "link")
public class ElasticLink {
    private String id;
    private String uri;
    private Integer rating;
    private String text;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
