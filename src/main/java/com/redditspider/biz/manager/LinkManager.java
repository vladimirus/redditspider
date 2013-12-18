package com.redditspider.biz.manager;

import java.util.List;

import com.redditspider.model.Link;

/**
 * Manager to deal with links.
 */
public interface LinkManager {

	public List<Link> findAll();

	public Link save(Link link);

	/**
	 * Starts a thread and leaves it.
	 */
	public void startIndexThread();

	/**
	 * Does not start a new thread.
	 */
	public void index();
}
