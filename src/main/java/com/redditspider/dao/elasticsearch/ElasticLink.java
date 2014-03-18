package com.redditspider.dao.elasticsearch;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "link")
public class ElasticLink {
    private String id;
    private String uri;
    private String commentsUri;
    private Integer rating;
    private String text;
    private Date created = new Date();
    private Date updated = new Date();

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getCommentsUri() {
        return commentsUri;
    }

    public void setCommentsUri(String commentsUri) {
        this.commentsUri = commentsUri;
    }
}
