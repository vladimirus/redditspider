package com.redditspider.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.redditspider.biz.manager.LinkManager;

/**
 * Controller for admin stuff like indexing, broadcasting.
 *
 */
@Controller
@RequestMapping("/_admin")
public class AdminController {
    @Autowired
    LinkManager linkManager;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    @ResponseBody
    public String index() {
        linkManager.startIndexThread();
        return "started";
    }
}
