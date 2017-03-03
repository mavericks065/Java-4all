package com.example.service;

import com.example.model.Event;
import com.example.model.Queue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

public class FileQueueServiceTest {
    
    private QueueService queueService;
    private File basePath;
    private Queue queue;

    @Before
    public void setUp(){
        basePath = new File(".");
        queue = new Queue("test", 500);

        queueService = new FileQueueService(basePath);
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
        final Event event = new Event(UUID.randomUUID(), "message1");

        // WHEN
        queueService.push(event, queue);

        // THEN
        Optional<Event> eventPulled = queueService.pull(queue);
        assertTrue(eventPulled.isPresent());
        assertEquals(event.getUuid(), eventPulled.get().getUuid());
        assertEquals(event.getValue(), eventPulled.get().getValue());
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

        // WHEN
        queueService.push(event, queue);
        Thread.sleep(1000);
        queueService.push(event2, queue);

        // THEN
        Optional<Event> eventPulled1 = queueService.pull(queue);
        assertTrue(eventPulled1.isPresent());
        assertEquals(event.getUuid(), eventPulled1.get().getUuid());
        assertEquals(event.getValue(), eventPulled1.get().getValue());
        Optional<Event> eventPulled2 = queueService.pull(queue);
        assertTrue(eventPulled2.isPresent());
        assertEquals(event2.getUuid(), eventPulled2.get().getUuid());
        assertEquals(event2.getValue(), eventPulled2.get().getValue());
    }

    @Test
    public void delete_queue_should_delete_event(){
        //given
        final Event event = new Event(UUID.randomUUID(), "message1");

        //when
        queueService.push(event, queue);
        queueService.delete(event, queue);


        // THEN
        Optional<Event> eventPulled = queueService.pull(queue);
        assertFalse(eventPulled.isPresent());
    }

    private void cleanUp(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    cleanUp(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}
