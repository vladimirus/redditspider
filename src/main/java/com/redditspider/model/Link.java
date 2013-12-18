package com.redditspider.model;

import java.util.Date;


public class Link {
	private String id;
	private String uri;
	private Date created = new Date();
	
	public Link() {
	}
	
	public Link(String uri) {
		this.uri = uri;
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
}
