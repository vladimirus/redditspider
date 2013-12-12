package com.redditspider.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.redditspider.biz.manager.LinkManager;
import com.redditspider.web.model.Link;

@Controller
@RequestMapping("/links")
public class LinkController {
	@Autowired
	LinkManager linkManager;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<Link> list() {
		return linkManager.findAll();
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	public class LinkNotFoundException extends RuntimeException {
		private static final long serialVersionUID = -2199783491694157773L;

		public LinkNotFoundException(Integer id) {
			super("Link '" + id + "' not found.");
		}
	}
}
