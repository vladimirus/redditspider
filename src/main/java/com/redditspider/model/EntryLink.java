package com.redditspider.model;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * Entry point to reddit world.
 */
public class EntryLink {
    private String id;
    private String uri;
    private Date updated;

    public EntryLink() {
    }

    public EntryLink(String id, String uri) {
        this.id = id;
        this.uri = uri;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EntryLink entryLink = (EntryLink) o;

        if (!id.equals(entryLink.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
