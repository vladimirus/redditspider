package com.redditspider.biz.manager;

import java.util.List;

import com.redditspider.web.model.Link;

/**
 * Manager to deal with links.
 */
public interface LinkManager {

	public List<Link> findAll();

}
