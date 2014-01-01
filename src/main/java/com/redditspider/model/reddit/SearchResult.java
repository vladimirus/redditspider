package com.redditspider.model.reddit;

import java.util.ArrayList;
import java.util.List;

import com.redditspider.model.Link;

public class SearchResult {
	private String nextPage;
	private List<Link> links = new ArrayList<Link>();

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
