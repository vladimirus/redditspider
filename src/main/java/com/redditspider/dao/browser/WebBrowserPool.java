package com.redditspider.dao.browser;


/**
 * Pool of webdrivers.
 */
public interface WebBrowserPool {

    /**
     * Gets browser from a pool.
     * @return WebBrowser
     */
    WebBrowser get();

    /**
     * Release browser back to the pool.
     * @param browser - browser to release
     */
    void release(WebBrowser browser);

    /**
     * Closes the WebBrowser and removes it from the pool.
     * @param browser - browser to close
     */
    void close(WebBrowser browser);
    
    /**
     * Cleans the pool by closing all the browsers.
     */
    void closeAll();
}
