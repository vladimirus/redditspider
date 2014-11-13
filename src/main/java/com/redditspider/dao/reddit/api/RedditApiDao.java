package com.redditspider.dao.reddit.api;

import static com.github.jreddit.retrieval.params.SubmissionSort.TOP;

import com.github.jreddit.retrieval.Submissions;
import com.github.jreddit.utils.restclient.RestClient;
import com.google.common.base.Throwables;
import com.redditspider.dao.SearchDao;
import com.redditspider.model.reddit.SearchQuery;
import com.redditspider.model.reddit.SearchResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class RedditApiDao implements SearchDao {
    private static final transient Logger LOG = Logger.getLogger(RedditApiDao.class);
    @Autowired
    RestClient restClient;
    @Autowired
    RedditApiUserManager userManager;

    @Override
    public SearchResult search(SearchQuery query) {

        try {
            Submissions submissions = new Submissions(restClient, userManager.getUser());
            submissions.ofSubreddit("flowers", TOP, -1, 100, null, null, true);
        } catch (Exception e) {
            LOG.error("Cannot search using reddit's api", e);
            Throwables.propagate(e);
        }

        return null;
    }


}
