package com.example.callable;

import com.example.model.Event;
import com.example.model.EventStatus;
import com.example.model.Queue;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class InMemoryCallable<E extends Event> implements Callable {

    private final ConcurrentMap<Queue, ConcurrentLinkedQueue<E>> queues;
    private final Queue queue;
    private final E tempEvent;

    public InMemoryCallable(final ConcurrentMap<Queue, ConcurrentLinkedQueue<E>> queues,
                            final Queue queue,
                            final E event) {
        this.queues = queues;
        this.queue = queue;
        this.tempEvent = event;
    }

    @Override
    public ConcurrentMap<Queue, ConcurrentLinkedQueue<E>> call() throws Exception {
        final ConcurrentLinkedQueue<E> existingQueue = queues.get(queue);

        if (existingQueue.contains(tempEvent)) {
            // remove invisible event
            existingQueue.remove(tempEvent);

            // make event visible and on top of the queue
            makeEventVisible(existingQueue);
            return queues;
        }
        return null;
    }

    private void makeEventVisible(ConcurrentLinkedQueue<E> existingQueue) {
        tempEvent.setStatus(EventStatus.NEW);

        final ConcurrentLinkedQueue<E> reOrderedQueue = new ConcurrentLinkedQueue<>();
        reOrderedQueue.add(tempEvent);
        reOrderedQueue.addAll(existingQueue);

        // replace old queue with new ordered one
        queues.put(queue, reOrderedQueue);
    }
}
