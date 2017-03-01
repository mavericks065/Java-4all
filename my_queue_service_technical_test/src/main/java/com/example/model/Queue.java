package com.example.model;

public class Queue {

    private final String name;
    private final long visibilityTimeout;

    public Queue(final String name, final long visibilityTimeout) {
        this.name = name;
        this.visibilityTimeout = visibilityTimeout;
    }

    /**
     * The friendly name of the queue. Persisted with the queue message for identification purposes
     *
     * @return
     *      queue name
     */
    public String getName() {
        return name;
    }

    public long getVisibilityTimeout() {
        return visibilityTimeout;
    }
}
