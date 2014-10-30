package com.redditspider.model;

import java.util.Date;

/**
 * Main Link object.
 */
public class Link {
    private String id;
    private String uri;
    private String commentsUri;
    private Integer up;
    private Integer down;
    private String text;
    private Date created;
    private Date updated;
    private String groupUri;

    public Link() {
    }

    public Link(String uri) {
        this.uri = uri;
    }

    public String getGroupUri() {
        return groupUri;
    }

    public void setGroupUri(String groupUri) {
        this.groupUri = groupUri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public Integer getUp() {
        return up;
    }

    public void setUp(Integer up) {
        this.up = up;
    }

    public Integer getDown() {
        return down;
    }

    public void setDown(Integer down) {
        this.down = down;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCommentsUri() {
        return commentsUri;
    }

    public void setCommentsUri(String commentsUri) {
        this.commentsUri = commentsUri;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
