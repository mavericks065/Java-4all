package com.example.service;

import com.example.model.Queue;
import com.example.model.QueueImpl;
import com.example.model.QueueMessage;
import com.example.model.QueueMessageStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InMemoryQueueTest {
  //
  // Implement me.
  //

    private InMemoryQueueService queueService;

    @Before
    public void setUp() throws Exception {
        queueService = new InMemoryQueueService();
    }

    @After
    public void tearDown() throws Exception {
        queueService = null;
    }

    @Test
    public void should_push_messages_to_the_queue_and_order_it_as_a_queue_FIFO() throws Exception {
        // GIVEN
        final Queue queue = Queue.create("pushQueue");

        // WHEN
        for(int i = 0; i < 5; i++) {
            final String payload = "This is a test for the push functionality : " + i;
            QueueMessage message = new QueueMessage(queue, payload);
            queueService.push(message);
        }

        // THEN
        // to test that all the messages are pushed
        Assert.assertEquals(5, queueService.getQueue().size());
        // to test that the order is as it should be
        Assert.assertEquals("This is a test for the push functionality : 0", queueService.getQueue().getFirst().getPayload());
        Assert.assertEquals("This is a test for the push functionality : 4", queueService.getQueue().getLast().getPayload());

    }

    @Test
    public void should_pull_messages_and_get_the_good_ones_without_any_call_to_delete_and_without_letting_time_for_the_timer_to_run() throws Exception {
        // GIVEN
        final Queue queue = Queue.create("pullQueue");
        pushQueueMessagesToInMemoryQueue(queue, 5);

        // WHEN
        final QueueMessage queueMessage0 = queueService.pull();
        final QueueMessage queueMessage1 = queueService.pull();

        // THEN
        Assert.assertEquals(QueueMessageStatus.INVISIBLE, queueMessage0.getQueueMessageStatus());
        Assert.assertEquals("This is a test for the push functionality : 0", queueMessage0.getPayload());
        Assert.assertEquals(QueueMessageStatus.INVISIBLE, queueMessage1.getQueueMessageStatus());
        Assert.assertEquals("This is a test for the push functionality : 1", queueMessage1.getPayload());
    }

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
}