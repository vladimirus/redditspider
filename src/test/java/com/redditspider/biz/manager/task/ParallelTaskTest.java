package com.redditspider.biz.manager.task;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.redditspider.biz.manager.LinkManager;

@RunWith(MockitoJUnitRunner.class)
public class ParallelTaskTest {
    private ParallelTask linkIndexer;

    @Mock
    private LinkManager linkManager;

    @Test
    public void startIndex() {
        // given
        linkIndexer = new ParallelTask(linkManager, "index");

        // when
        linkIndexer.run();

        // then
        verify(linkManager).index();
    }
}
