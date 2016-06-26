package com.example.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nicolas Guignard on 15/06/2016.
 */
public class QueueImplTest {

    private QueueImpl queueImpl;

    @Test
    public void should_get_the_queue_name() throws Exception {
        // GIVEN
        queueImpl = new QueueImpl("test");

        // WHEN
        final String result = queueImpl.getQueueName();

        // THEN
        Assert.assertEquals("test", result);
    }

    @Test
    public void should_get_the_visibility() throws Exception {
        // GIVEN
        queueImpl = new QueueImpl("test", 1000);

        // WHEN
        final long result = queueImpl.getVisibilityTimeout();

        // THEN
        Assert.assertEquals(1000, result);
    }
}
