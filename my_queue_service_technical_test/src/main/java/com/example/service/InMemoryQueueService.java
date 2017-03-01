package com.example.service;

import com.example.model.Event;
import com.example.model.EventStatus;
import com.example.model.Queue;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * An in-memory queue, suitable for same-JVM producers and consumers
 */
public class InMemoryQueueService<E extends Event> implements QueueService<E> {

    private static final Logger LOGGER = Logger.getLogger(InMemoryQueueService.class.getName());

    private final ConcurrentHashMap<Queue, ConcurrentLinkedQueue<E>> queues;

    public InMemoryQueueService(final ConcurrentHashMap<Queue, ConcurrentLinkedQueue<E>> queues) {
        this.queues = checkNotNull(queues);
    }

    @Override
    public void push(final E event, final Queue topic) {
        checkNotNull(event);
        checkNotNull(topic);

        // get topic
        ConcurrentLinkedQueue<E> queue = queues.get(topic);
        // create it if does not exist
        if (queue == null) {
            queue = new ConcurrentLinkedQueue();
            queues.put(topic, queue);
        }

        queue.add(event);
    }

    @Override
    public Optional<E> pull(Queue queue) {
        checkNotNull(queue);

        Optional<E> result = Optional.empty();
        final ConcurrentLinkedQueue<E> existingQueue = queues.get(queue);

        if (existingQueue == null) {
            LOGGER.severe(String.format("Queue %s does not exist", existingQueue));
            return result;
        }

        for (E event : existingQueue) {
            if (EventStatus.NEW.equals(event.getStatus())) {
                event.setStatus(EventStatus.INVISIBLE);

                // re-inject in the list of queues the queue with a modified event
                queues.put(queue, existingQueue);
                
                result = Optional.of(event);
                break;
            }
        }

        if (result.isPresent()) {
            // after the visibility timeout, if an INVISIBLE message has not been removed it is set back to NEW
            E tempEvent = result.get();

            final Timer timer = new Timer();
            timer.schedule (new TimerTask() {
                public void run() {
                    final ConcurrentLinkedQueue<E> existingQueue2 = queues.get(queue);
                    if (existingQueue2.contains(tempEvent)) {
                        existingQueue2.remove(tempEvent);
                        tempEvent.setStatus(EventStatus.NEW);
                        existingQueue2.add(tempEvent);
                        queues.put(queue, existingQueue2);
                    }
                }
            }, queue.getVisibilityTimeout());
        }

        return result;
    }

    @Override
    public boolean delete(E event, Queue topic) {
        checkNotNull(event);
        checkNotNull(topic);

        final ConcurrentLinkedQueue<E> queue = queues.get(topic);
        if (queue == null) {
            LOGGER.severe(String.format("Queue %s does not exist", queue));
            return false;
        }

        if (!queue.contains(event)) {
            LOGGER.severe(String.format("Queue %s does not contain the event %s", queue, event.getUuid()));
            return false;
        }

        queue.remove(event);

        return true;
    }
}

