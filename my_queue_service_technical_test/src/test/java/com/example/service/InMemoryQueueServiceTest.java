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

        // THEN
    }

    @Test(expected = NullPointerException.class)
    public void should_not_push_messages_to_the_queue_and_throw_null_pointer_exception_because_of_null_topic() throws Exception {
        // WHEN
        queueService.push(new Event(UUID.randomUUID(), "test"), null);

        // THEN
    }

    @Test
    public void pull_queue_should_return_last_event_inserted() {
        // GIVEN
        Queue topic = new Queue("pullQueue", 1000);
        final Event event = new Event(UUID.randomUUID(), "message1");
        queueService.push(event, topic);

        // WHEN
        Optional<Event> eventPulled = queueService.pull(topic);

        // THEN
        assertTrue(eventPulled.isPresent());
        assertEquals(event.getUuid(), eventPulled.get().getUuid());
        assertEquals(event.getValue(), eventPulled.get().getValue());
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

        // THEN
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
    public void should_pull_messages_without_any_call_to_delete() throws Exception {
        // GIVEN
        final Event event1 = new Event(UUID.randomUUID(), "message1");
        final Event event2 = new Event(UUID.randomUUID(), "message2");
        final Event event3 = new Event(UUID.randomUUID(), "message3");
        final Event event4 = new Event(UUID.randomUUID(), "message4");
        final Event event5 = new Event(UUID.randomUUID(), "message5");
        final Queue topic = new Queue("pullQueue", 300);
        queueService.push(event1, topic);
        queueService.push(event2, topic);
        queueService.push(event3, topic);
        queueService.push(event4, topic);
        queueService.push(event5, topic);

        // WHEN
        final Optional<Event> resultEvent1 = queueService.pull(topic);
        final Optional<Event> resultEvent2 = queueService.pull(topic);
        queueService.delete(event1, topic);

        // THEN

        // test it gets the two first messages of the queue
        Assert.assertEquals(event1.getValue(), resultEvent1.get().getValue());
        Assert.assertEquals(event2.getValue(), resultEvent2.get().getValue());

        // wait for the visibility timeout
        Thread.sleep(400);

        // should get the first message because it has not been deleted
        final Optional<Event> resultEvent2_bis = queueService.pull(topic);
        Assert.assertEquals(event2.getValue(), resultEvent2_bis.get().getValue());

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

        // WHEN
        queueService.push(event0, topic);
        queueService.push(event1, topic);
        queueService.delete(event1, topic);


        // THEN
        assertEquals(1, queueService.getQueue(topic).size());
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

/*
    @Test
    public void should_pull_messages_without_any_call_to_delete() throws Exception {
        // GIVEN
        final Queue queue = new QueueImpl("pulQueue", 300);
        pushQueueMessagesToInMemoryQueue(queue, 5);

        // WHEN
        final QueueMessage queueMessage0 = queueService.pull();
        final QueueMessage queueMessage1 = queueService.pull();

        // THEN

        // test it gets the two first messages of the queue
        Assert.assertEquals("This is a test for the push functionality : 0", queueMessage0.getPayload());
        Assert.assertEquals("This is a test for the push functionality : 1", queueMessage1.getPayload());

        // wait for the visibility timeout
        Thread.sleep(300);

        // should get the first message because it has not been deleted
        final QueueMessage queueMessage2 = queueService.pull();
        Assert.assertEquals("This is a test for the push functionality : 0", queueMessage2.getPayload());

    }

    @Test
    public void should_pull_messages() throws Exception {
        // GIVEN
        final Queue queue = Queue.create("pullQueue");
        pushQueueMessagesToInMemoryQueue(queue, 5);

        // WHEN
        final QueueMessage queueMessage0 = queueService.pull();
        queueService.delete(queueMessage0);
        // wait for the visibility timeout
        Thread.sleep(300);
        final QueueMessage queueMessage1 = queueService.pull();

        // THEN

        // test that it gets the second message after that the first has been pulled and deleted
        Assert.assertEquals("This is a test for the push functionality : 0", queueMessage0.getPayload());
        Assert.assertEquals("This is a test for the push functionality : 1", queueMessage1.getPayload());

    }

    @Test
    public void should_not_pull_messages_and_not_throw_an_exception() throws Exception {
        // WHEN
        final QueueMessage queueMessage0 = queueService.pull();

        // THEN
        Assert.assertNull(queueMessage0);

    }

    @Test
    public void should_delete_queue_messages() throws Exception {
        // GIVEN
        final Queue queue = Queue.create("deleteQueue");
        pushQueueMessagesToInMemoryQueue(queue, 5);

        final QueueMessage messageToDelete = queueService.getQueue().getFirst();
        final QueueMessage messageToDelete1 = queueService.getQueue().getLast();

        // WHEN
        queueService.delete(messageToDelete);
        queueService.delete(messageToDelete1);

        // THEN
        Assert.assertEquals(3, queueService.getQueue().size());
    }

    @Test
    public void should_test_the_emptyness_of_queue() throws Exception {

        // WHEN
        boolean isEmpty = queueService.isEmpty();

        // THEN
        Assert.assertTrue(isEmpty);
    }

    // helper methods

    private void pushQueueMessagesToInMemoryQueue(Queue queue, int numberOfQueueMessages) {
        for(int i = 0; i < numberOfQueueMessages; i++) {
            final String payload = "This is a test for the push functionality : " + i;
            QueueMessage message = new QueueMessage(queue, payload);
            queueService.push(message);
        }
    }
    */
}
