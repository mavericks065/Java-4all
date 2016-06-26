package com.example.model;

/**
 * Created by Nicolas Guignard on 15/06/2016.
 *
 * An arbitrary implementation of a Queue.
 */
public class QueueImpl implements Queue {

    private String queueName;

    private long visibilityTimeout = 500;

    public QueueImpl() {
    }

    public QueueImpl(final String queueName) {
        this.queueName = queueName;
    }

    public QueueImpl(final String queueName, final long visibilityTimeout) {
        this.queueName = queueName;
        this.visibilityTimeout = visibilityTimeout;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public long getVisibilityTimeout() {
        return visibilityTimeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueImpl queue = (QueueImpl) o;

        if (visibilityTimeout != queue.visibilityTimeout) return false;
        return queueName != null ? queueName.equals(queue.queueName) : queue.queueName == null;

    }

    @Override
    public int hashCode() {
        int result = queueName != null ? queueName.hashCode() : 0;
        result = 31 * result + (int) (visibilityTimeout ^ (visibilityTimeout >>> 32));
        return result;
    }

    /**
     *
     * return a string containing JSON syntax
     *
     * @return
     */
    @Override
    public String toString() {

        StringBuilder queueSB = new StringBuilder("{ \"queueName\" : \"");

        queueSB.append(queueName.toString());
        queueSB.append("\", \"visibilityTimeout\": ");
        queueSB.append(visibilityTimeout);
        queueSB.append("}");

        return queueSB.toString();
    }

}
