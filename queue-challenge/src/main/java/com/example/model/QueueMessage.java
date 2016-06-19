package com.example.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Created by Nicolas Guignard on 15/06/2016.
 *
 * Defines a message that is passed in a Queue between two processes.
 *
 */
public class QueueMessage {

    // ID to identify the message, useful for File Queue
    private String id = UUID.randomUUID().toString();

    // the queue the message is on
    private Queue queue;

    // the status of the message
    private QueueMessageStatus queueMessageStatus;

    // the time the message was added to the queue for processing
    private Instant messageQueueTime;

    // custom payload
    private Object payload;

    public QueueMessage() {
    }

    /**
     * Construct a new Message, before adding it to the queue
     *
     * @param queue
     * @param payload
     */
    public QueueMessage(Queue queue, Object payload) {
        this.queue = queue;
        this.payload = payload;
        this.messageQueueTime = Instant.now();
        this.queueMessageStatus = QueueMessageStatus.NEW;
    }

    public QueueMessage(Queue queue, Object payload,
                        Instant messageQueueTime, QueueMessageStatus queueMessageStatus) {
        this.queue = queue;
        this.payload = payload;
        this.messageQueueTime = messageQueueTime;
        this.queueMessageStatus = queueMessageStatus;
    }

    public Queue getQueue() {
        return queue;
    }

    public QueueMessageStatus getQueueMessageStatus() {
        return queueMessageStatus;
    }

    public Instant getMessageQueueTime() {
        return messageQueueTime;
    }

    public Object getPayload() {
        return payload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQueueMessageStatus(QueueMessageStatus queueMessageStatus) {
        this.queueMessageStatus = queueMessageStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueMessage that = (QueueMessage) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (queue != null ? !queue.equals(that.queue) : that.queue != null) return false;
        if (queueMessageStatus != that.queueMessageStatus) return false;
        if (messageQueueTime != null ? !messageQueueTime.equals(that.messageQueueTime) : that.messageQueueTime != null)
            return false;
        return payload != null ? payload.equals(that.payload) : that.payload == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (queue != null ? queue.hashCode() : 0);
        result = 31 * result + (queueMessageStatus != null ? queueMessageStatus.hashCode() : 0);
        result = 31 * result + (messageQueueTime != null ? messageQueueTime.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
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

        StringBuilder qmSB = new StringBuilder("{ \"queue\" : ");

        qmSB.append(queue.toString());
        qmSB.append(", \"queueMessageStatus\": \"");
        qmSB.append(queueMessageStatus);
        qmSB.append("\", \"messageQueueTime\": \"");
        qmSB.append(messageQueueTime.toString());
        qmSB.append("\", \"id\": \"");
        qmSB.append(id);
        qmSB.append("\", \"payload\": \"");
        qmSB.append(payload.toString());
        qmSB.append("\"}");

        return qmSB.toString();
    }
}
