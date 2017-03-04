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

    public InMemoryCallable(ConcurrentMap<Queue, ConcurrentLinkedQueue<E>> queues, Queue queue, E event) {
        this.queues = queues;
        this.queue = queue;
        this.tempEvent = event;
    }

    @Override
    public ConcurrentMap<Queue, ConcurrentLinkedQueue<E>> call() throws Exception {
        final ConcurrentLinkedQueue<E> existingQueue = queues.get(queue);

        if (existingQueue.contains(tempEvent)) {

            existingQueue.remove(tempEvent);
            tempEvent.setStatus(EventStatus.NEW);

            final ConcurrentLinkedQueue<E> reOrderedQueue = new ConcurrentLinkedQueue<>();
            reOrderedQueue.add(tempEvent);
            reOrderedQueue.addAll(existingQueue);

            queues.put(queue, reOrderedQueue);
            return queues;
        }
        return null;
    }
}
