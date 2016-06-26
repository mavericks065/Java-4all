package com.example.service;

import com.example.model.QueueMessage;
import com.example.model.QueueMessageStatus;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An in-memory queue, suitable for same-JVM producers and consumers
 */
public class InMemoryQueueService implements QueueService {
  //
  // Task 2: Implement me.
  //
    private final LinkedList<QueueMessage> queue;

    public InMemoryQueueService() {
        queue = new LinkedList<>();
    }

    @Override
    public final void push(QueueMessage queueMessage) {
        // this will ADD a message to the Linked list, it does not PUSH ONTO the stack
        queue.add(queueMessage);
    }

    @Override
    public final QueueMessage pull() {

        if (isEmpty()) {
            return null;
        }

        // pull just NEW messages
        final QueueMessage result;
        if (queue.getFirst().getQueueMessageStatus() != QueueMessageStatus.INVISIBLE) {
            result = queue.getFirst();
        } else {
            result = queue.stream().filter(q -> q.getQueueMessageStatus() != QueueMessageStatus.INVISIBLE).findFirst().orElse(null);
        }

        // we set the status to INVISIBLE to not pull again a message already pulled and not deleted
        if (result != null) result.setQueueMessageStatus(QueueMessageStatus.INVISIBLE);

        // after the visibility timeout, if an INVISIBLE message has not been removed it is set back to NEW
        final Timer timer = new Timer();
        timer.schedule (new TimerTask() {
            public void run() {
                if (queue.contains(result)) {
                    result.setQueueMessageStatus(QueueMessageStatus.NEW);
                }
            }
        }, result.getQueue().getVisibilityTimeout());

        return result;
    }

    @Override
    public final void delete(QueueMessage queueMessage) {
        // we remove the first occurrence (FIFO)
        queue.removeFirstOccurrence(queueMessage);
    }

    @Override
    public final boolean isEmpty() {
        return queue.isEmpty();
    }

    public LinkedList<QueueMessage> getQueue() {
        return queue;
    }

}
