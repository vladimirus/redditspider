package com.redditspider.model;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * Subreddit.
 */
public class Subreddit {
    private String id;
    private String name;
    private Date updated;
    private Date created;
    private Date crawled;
    private long subscribers;

    public Subreddit() {
    }

    public Subreddit(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(long subscribers) {
        this.subscribers = subscribers;
    }

    public Date getCrawled() {
        return crawled;
    }

    public void setCrawled(Date crawled) {
        this.crawled = crawled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Subreddit subreddit = (Subreddit) o;

        if (!id.equals(subreddit.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
