package com.example.service;

import com.example.model.Event;
import com.example.model.Queue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static com.example.util.TestUtils.cleanUp;
import static com.example.model.EventStatus.INVISIBLE;
import static org.junit.Assert.*;

public class FileQueueServiceTest {
    
    private QueueService queueService;
    private File basePath;
    private Queue queue;
    private ScheduledExecutorService executorService;

    @Before
    public void setUp(){
        basePath = new File(".");
        queue = new Queue("test", 500);
        executorService = new ScheduledThreadPoolExecutor(1);
        queueService = new FileQueueService(basePath, executorService);
    }

    @After
    public void tearDown(){
        // remove filesystem
        cleanUp(new File(basePath + "/" + queue.getName()));
        queueService = null;
    }

    @Test
    public void pull_queue_should_return_last_event_inserted(){
        // GIVEN
        final Event event0 = new Event(UUID.randomUUID(), "message0");
        queueService.push(event0, queue);

        // WHEN
        final Optional<Event> eventPulled = queueService.pull(queue);

        // THEN
        assertTrue(eventPulled.isPresent());
        assertEquals(event0.getUuid(), eventPulled.get().getUuid());
        assertEquals(event0.getValue(), eventPulled.get().getValue());
    }

    @Test
    public void pull_queue_when_empty_should_return_empty(){
        //given

        //when
        Optional<Event> eventPulled = queueService.pull(queue);

        // THEN
        assertFalse(eventPulled.isPresent());
    }

    @Test
    public void pull_2_events_in_queue_should_return_events_inserted_in_FIFO_queue() throws InterruptedException {
        // GIVEN
        final Event event = new Event(UUID.randomUUID(), "message1");
        final Event event2 = new Event(UUID.randomUUID(), "message2");
        queueService.push(event, queue);
        Thread.sleep(1000);
        queueService.push(event2, queue);

        // WHEN
        Optional<Event> eventPulled1 = queueService.pull(queue);
        Optional<Event> eventPulled2 = queueService.pull(queue);

        // THEN
        assertTrue(eventPulled1.isPresent());
        assertEquals(event.getUuid(), eventPulled1.get().getUuid());
        assertEquals(event.getValue(), eventPulled1.get().getValue());
        assertTrue(eventPulled2.isPresent());
        assertEquals(event2.getUuid(), eventPulled2.get().getUuid());
        assertEquals(event2.getValue(), eventPulled2.get().getValue());
    }

    @Test
    public void delete_event_should_delete_specified_event_in_the_queue(){
        //given
        final Event event = new Event(UUID.randomUUID(), "message1");
        event.setStatus(INVISIBLE);
        queueService.push(event, queue);

        //when
        Optional<Event> eventPulled = queueService.pull(queue);
        boolean result = queueService.delete(eventPulled.get(), queue);


        // THEN
        assertTrue(result);
        Optional<Event> eventPulled2 = queueService.pull(queue);
        assertFalse(eventPulled2.isPresent());
    }

}
