package com.redditspider.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.redditspider.biz.manager.LinkManager;

@Controller
@RequestMapping("/index")
public class IndexController {
	@Autowired
	LinkManager linkManager;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody String index() {
		linkManager.startIndexThread();
		return "started";
	}
}
