package com.example.model;

/**
 * Created by Nicolas Guignard on 15/06/2016.
 *
 * Idendifies a queue to handle a message.
 */
public interface Queue {

    /**
     * The friendly name of the queue. Persisted with the queue message for identification purposes
     *
     * @return
     */
    String getQueueName();

    /**
     * The visibility of the messages of the queue.
     *
     * @return
     */
    long getVisibilityTimeout();

    static Queue create(String queueName) {
        return new QueueImpl(queueName);
    }
}
