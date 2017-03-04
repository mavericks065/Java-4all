package com.example.service;

import com.example.model.Event;
import com.example.model.Queue;

import java.util.Optional;

public interface QueueService<E extends Event> {

    /**
     * pushes a queue message onto a queue.
     *
     * @param event to store
     * @param queue Queue in which the event has to be stored
     */
    void push(E event, Queue queue);

    /**
     * retrieves a single event message from a queue. This queue message cannot
     * have the status INVISIBLE.
     *
     * @return
     */
    Optional<E> pull(Queue queue);

    /**
     * deletes an event message that was received in the specific queue passed in
     * parameter.
     *
     * @param event to be deleted
     * @param queue where the event is from
     * @return
     */
    boolean delete(E event, Queue queue);

}
