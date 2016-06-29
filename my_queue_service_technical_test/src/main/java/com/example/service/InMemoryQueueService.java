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
        QueueMessage result = null;

        for (QueueMessage q : queue) {
            if (q.getQueueMessageStatus() != QueueMessageStatus.INVISIBLE) {

                // we set the status to INVISIBLE to not pull again a message already pulled and not deleted
                q.setQueueMessageStatus(QueueMessageStatus.INVISIBLE);
                result = q;
                break;
            }
        }

        if (result != null) {
            // after the visibility timeout, if an INVISIBLE message has not been removed it is set back to NEW
            QueueMessage temp = result;

            final Timer timer = new Timer();
            timer.schedule (new TimerTask() {
                public void run() {
                    if (queue.contains(temp)) {
                        queue.removeFirstOccurrence(temp);
                        temp.setQueueMessageStatus(QueueMessageStatus.NEW);
                        queue.push(temp);
                    }
                }
            }, result.getQueue().getVisibilityTimeout());
        }

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
