package com.redditspider.dao;

import com.redditspider.model.EntryLink;
import com.redditspider.model.Link;

import java.util.List;

/**
 * Interface to save links.
 */
public interface LinkExtendedDao extends LinkDao {
    void save(Iterable<Link> links);
    List<Link> findAll();
    Link findById(String id);
    List<Link> findToBroadcast();
    void delete(Link link);

    EntryLink nextEntryLink();

    void insertEntryLink(EntryLink entryLink);

    EntryLink findEntryLinkById(String id);

    void deleteEntryLink(EntryLink link);
}
