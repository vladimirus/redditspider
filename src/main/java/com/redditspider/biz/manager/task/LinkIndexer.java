package com.redditspider.biz.manager.task;

import com.redditspider.biz.manager.LinkManager;

/**
 * Thread to start indexing in parellel.
 */
public class LinkIndexer implements Runnable {
    private LinkManager linkManager;

    public LinkIndexer(LinkManager linkManager) {
        this.linkManager = linkManager;
    }

    public void run() {
        linkManager.index();
    }
}
