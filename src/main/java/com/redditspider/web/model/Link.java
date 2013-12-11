package com.redditspider.web.model;

public class Link {
	private String uri;
	
	public Link(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
