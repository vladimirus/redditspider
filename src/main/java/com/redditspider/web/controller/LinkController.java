package com.redditspider.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

import com.redditspider.biz.manager.LinkManager;
import com.redditspider.model.Link;

/**
 * Controller to deal with links.
 */
@Controller
@RequestMapping("/links")
public class LinkController {
    @Autowired
    LinkManager linkManager;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Link> list() {
        return linkManager.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = { "application/json" })
    @ResponseStatus(HttpStatus.CREATED)
    public HttpEntity<?> create(@RequestBody Link l, @Value("#{request.requestURL}") StringBuffer parentUri) {
        Link link = linkManager.save(l);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(childLocation(parentUri, link.getId()));
        return new HttpEntity<Object>(headers);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Link find(@PathVariable("id") String id) {
        Link link = this.linkManager.findById(id);
        if (link == null) {
            throw new LinkNotFoundException(id);
        }
        return link;
    }

    private URI childLocation(StringBuffer parentUri, Object childId) {
        UriTemplate uri = new UriTemplate(parentUri.append("/{childId}")
                .toString());
        return uri.expand(childId);
    }

    /**
     * Exception raised if links is not found.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class LinkNotFoundException extends RuntimeException {
        public LinkNotFoundException(String id) {
            super("Link '" + id + "' not found.");
        }
    }
}
