package com.example.service;

import com.example.model.Event;
import com.example.model.EventStatus;
import com.example.model.Queue;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.junit.Assert.*;


public class InMemoryQueueServiceTest {

    private InMemoryQueueService<Event> queueService;

    private ConcurrentHashMap<Queue, ConcurrentLinkedQueue<Event>> queues;
    private ScheduledExecutorService executorService;

    @Before
    public void setUp() throws Exception {
        queues = new ConcurrentHashMap<>();
        executorService = new ScheduledThreadPoolExecutor(1);

        queueService = new InMemoryQueueService(queues, executorService);
    }

    @After
    public void tearDown() throws Exception {
        queueService = null;
        queues = null;
        executorService = null;
    }

    @Test
    public void should_push_messages_to_the_queue_and_order_it_as_a_queue_FIFO() throws Exception {
        // GIVEN
        Queue topic = new Queue("pushQueue", 1000);

        // WHEN
        for(int i = 0; i < 5; i++) {
            Event event = new Event(UUID.randomUUID(), "test" + i);
            queueService.push(event, topic);
        }

        // THEN
        // to test that all the messages are pushed
        Assert.assertEquals(5, queueService.getQueue(topic).size());
        // to test that the order is as it should be
        Assert.assertEquals("test0", queueService.getQueue(topic).poll().getValue());
        Assert.assertEquals("test1", queueService.getQueue(topic).poll().getValue());

    }

    @Test(expected = NullPointerException.class)
    public void should_not_push_messages_to_the_queue_and_throw_null_pointer_exception_because_of_null_event() throws Exception {
        // GIVEN
        Queue topic = new Queue("pushQueue", 1000);

        // WHEN
        queueService.push(null, topic);
    }

    @Test(expected = NullPointerException.class)
    public void should_not_push_messages_to_the_queue_and_throw_null_pointer_exception_because_of_null_topic() throws Exception {
        // WHEN
        queueService.push(new Event(UUID.randomUUID(), "test"), null);
    }

    @Test
    public void pull_queue_when_empty_should_return_empty(){
        // GIVEN
        Queue topic = new Queue("pullQueue", 1000);

        // WHEN
        Optional<Event> eventPulled = queueService.pull(topic);

        // THEN
        assertFalse(eventPulled.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void pull_queue_when_null_should_throw_exception(){
        // WHEN
        queueService.pull(null);
    }

    @Test
    public void pull_2_events_in_queue_should_return_events_inserted_FIFO(){
        // GIVEN
        final Event event1 = new Event(UUID.randomUUID(), "message1");
        final Event event2 = new Event(UUID.randomUUID(), "message2");
        final Queue topic = new Queue("pullQueue", 1000);
        queueService.push(event1, topic);
        queueService.push(event2, topic);

        // WHEN
        Optional<Event> eventPulled1 = queueService.pull(topic);
        Optional<Event> eventPulled2 = queueService.pull(topic);

        // THEN
        assertTrue(eventPulled1.isPresent());
        assertEquals(event1.getUuid(), eventPulled1.get().getUuid());
        assertEquals(event1.getValue(), eventPulled1.get().getValue());
        assertEquals(EventStatus.INVISIBLE, eventPulled1.get().getStatus());

        assertTrue(eventPulled2.isPresent());
        assertEquals(event2.getUuid(), eventPulled2.get().getUuid());
        assertEquals(event2.getValue(), eventPulled2.get().getValue());
        assertEquals(EventStatus.INVISIBLE, eventPulled2.get().getStatus());

    }

    @Test
    public void delete_method_should_not_delete_event_if_no_queue(){
        // GIVEN
        final Queue topic = new Queue("deleteQueue", 1000);
        final Event event0 = new Event(UUID.randomUUID(), "message1");

        // WHEN
        queueService.push(event0, topic);
        boolean result = queueService.delete(event0, new Queue("deleteQueue1", 1000));

        // THEN
        assertFalse(result);
    }

    @Test
    public void delete_method_should_not_delete_event_if_no_event_in_queue(){
        // GIVEN
        final Queue topic = new Queue("deleteQueue", 1000);
        final Queue topic1 = new Queue("deleteQueue1", 1000);
        final Event event0 = new Event(UUID.randomUUID(), "message1");

        // WHEN
        queueService.push(event0, topic);
        boolean result = queueService.delete(event0, topic1);


        // THEN
        assertFalse(result);
    }

    @Test
    public void delete_method_should_delete_event(){
        // GIVEN
        final Queue topic = new Queue("deleteQueue", 1000);
        final Event event0 = new Event(UUID.randomUUID(), "message1");
        final Event event1 = new Event(UUID.randomUUID(), "message2");
        event1.setStatus(EventStatus.INVISIBLE);
        final Event event2 = new Event(UUID.randomUUID(), "message3");

        // WHEN
        queueService.push(event0, topic);
        queueService.push(event1, topic);
        queueService.push(event2, topic);
        queueService.delete(event1, topic);


        // THEN
        assertEquals(2, queueService.getQueue(topic).size());
        assertEquals(event0.getUuid(), queueService.pull(topic).get().getUuid());
    }

    @Test(expected = NullPointerException.class)
    public void delete_method_should_throw_exception_if_event_null(){
        // GIVEN
        final Queue topic = new Queue("deleteQueue", 1000);
        final Event event0 = new Event(UUID.randomUUID(), "message1");
        final Event event1 = new Event(UUID.randomUUID(), "message2");

        // WHEN
        queueService.push(event0, topic);
        queueService.push(event1, topic);
        queueService.delete(null, topic);
    }

    @Test(expected = NullPointerException.class)
    public void delete_method_should_throw_exception_if_queue_null(){
        // GIVEN
        final Queue topic = null;
        final Event event0 = new Event(UUID.randomUUID(), "message1");
        final Event event1 = new Event(UUID.randomUUID(), "message2");

        // WHEN
        queueService.push(event0, topic);
        queueService.push(event1, topic);
        queueService.delete(event1, topic);
    }

}
