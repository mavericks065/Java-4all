package com.example.callable;

import com.example.model.Event;
import com.example.model.EventStatus;
import com.example.model.Queue;
import org.junit.After;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InMemoryCallableTest {

    private InMemoryCallable<Event> callable;

    @After
    public void tearDown() throws Exception {
        callable = null;
    }

    @Test
    public void should_set_event_reinsert_it_at_the_head_of_the_queue() throws Exception {
        // GIVEN
        final ConcurrentHashMap<Queue, ConcurrentLinkedQueue<Event>> queues = new ConcurrentHashMap<>();
        final Queue topic = new Queue("pullQueue", 300);
        final Event event = new Event(UUID.randomUUID(), "message1");
        event.setStatus(EventStatus.INVISIBLE);
        final Event event2 = new Event(UUID.randomUUID(), "message2");
        final Event event3 = new Event(UUID.randomUUID(), "message3");
        final Event event4 = new Event(UUID.randomUUID(), "message4");

        ConcurrentLinkedQueue<Event> linkQueue = new ConcurrentLinkedQueue<>();
        linkQueue.add(event);
        linkQueue.add(event2);
        linkQueue.add(event3);
        linkQueue.add(event4);
        queues.put(topic, linkQueue);

        callable = new InMemoryCallable<>(queues, topic, event);

        // WHEN
        ConcurrentMap<Queue, ConcurrentLinkedQueue<Event>> result = callable.call();

        // THEN
        assertEquals(4, result.get(topic).size());
        Event event0 = result.get(topic).poll();
        assertEquals(event.getUuid(), event0.getUuid());
        assertEquals(event.getStatus(), event0.getStatus());
        assertEquals(event2.getUuid(), result.get(topic).poll().getUuid());
        assertEquals(event3.getUuid(), result.get(topic).poll().getUuid());

    }

    @Test
    public void should_not_do_anything() throws Exception {
        // GIVEN
        final ConcurrentHashMap<Queue, ConcurrentLinkedQueue<Event>> queues = new ConcurrentHashMap<>();
        final Queue topic = new Queue("pullQueue", 300);
        final Event event = new Event(UUID.randomUUID(), "message1");
        event.setStatus(EventStatus.INVISIBLE);
        final Event event2 = new Event(UUID.randomUUID(), "message2");

        ConcurrentLinkedQueue<Event> linkQueue = new ConcurrentLinkedQueue<>();
        linkQueue.add(event);
        linkQueue.add(event2);
        queues.put(topic, linkQueue);

        callable = new InMemoryCallable<>(queues, topic, new Event(UUID.randomUUID(), "test"));

        // WHEN
        ConcurrentMap<Queue, ConcurrentLinkedQueue<Event>> result = callable.call();

        // THEN
        assertNull(result);
    }
}
