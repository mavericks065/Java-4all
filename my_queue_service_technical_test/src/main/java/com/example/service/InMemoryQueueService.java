package com.example.service;

import com.example.callable.InMemoryCallable;
import com.example.model.Event;
import com.example.model.EventStatus;
import com.example.model.Queue;
import com.google.common.annotations.VisibleForTesting;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * An in-memory queue, suitable for same-JVM producers and consumers
 */
public class InMemoryQueueService<E extends Event> implements QueueService<E> {

    private static final Logger LOGGER = Logger.getLogger(InMemoryQueueService.class.getName());

    private final ConcurrentMap<Queue, ConcurrentLinkedQueue<E>> queues;
    private final ScheduledExecutorService executorService;

    public InMemoryQueueService(final ConcurrentMap<Queue, ConcurrentLinkedQueue<E>> queues,
                                final ScheduledExecutorService executorService) {
        this.queues = checkNotNull(queues);
        this.executorService = checkNotNull(executorService);
    }

    @Override
    public void push(final E event, final Queue topic) {
        checkNotNull(event);
        checkNotNull(topic);

        final ConcurrentLinkedQueue<E> queue = getOrCreateQueue(topic);

        queue.add(event);
    }

    @Override
    public Optional<E> pull(Queue topic) {
        checkNotNull(topic);

        final ConcurrentLinkedQueue<E> existingQueue = queues.get(topic);

        if (existingQueue == null) {
            LOGGER.severe(String.format("Queue %s does not exist", existingQueue));
            return Optional.empty();
        }

        final Optional<E> result = getFirstNewEvent(topic, existingQueue);

        result.ifPresent(e -> myConsumer.accept(topic, e));

        return result;
    }

    @Override
    public boolean delete(final E event, final Queue topic) {
        checkNotNull(event);
        checkNotNull(topic);

        final Optional<ConcurrentLinkedQueue<E>> queue = Optional.ofNullable(queues.get(topic));

        if (!queue.isPresent() || !queue.get().contains(event)) {
            LOGGER.severe(String.format("Queue %s either does not exist or does not contain the event %s", queue, event.getUuid()));
            return false;
        }

        queue.get().remove(event);

        return true;
    }

    private BiConsumer<Queue, E> myConsumer = new BiConsumer<Queue, E>() {
        @Override
        public void accept(Queue topic, E e) {
            executorService.schedule(new InMemoryCallable(queues, topic, e),
                    topic.getVisibilityTimeout(),
                    TimeUnit.MILLISECONDS);
        }
    };

    private ConcurrentLinkedQueue<E> getOrCreateQueue(Queue topic) {
        // get topic
        ConcurrentLinkedQueue<E> queue = queues.get(topic);
        // create it if does not exist
        if (queue == null) {
            queue = new ConcurrentLinkedQueue<>();
            queues.put(topic, queue);
        }
        return queue;
    }

    private Optional<E> getFirstNewEvent(final Queue queue,
                                         final ConcurrentLinkedQueue<E> existingQueue) {
        for (E event : existingQueue) {
            if (EventStatus.NEW.equals(event.getStatus())) {
                event.setStatus(EventStatus.INVISIBLE);

                // re-inject in the list of queues the queue with a modified event
                queues.put(queue, existingQueue);

                return Optional.of(event);
            }
        }
        return Optional.empty();
    }

    @VisibleForTesting
    protected ConcurrentLinkedQueue<E> getQueue(Queue queue) {
        return queues.get(queue);
    }

}

