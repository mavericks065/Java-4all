package com.example.service;

import com.amazonaws.util.json.JSONArray;
import com.example.model.Queue;
import com.example.model.QueueImpl;
import com.example.model.QueueMessage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileQueueTest {
  //
  // Implement me if you have time.
  //

    private Queue queue;

    private FileQueueService fileQueueService;

    private String messagesFilePath;

    @Before
    public void setUp() throws Exception {

        queue = new QueueImpl("queueNameTest", 300);
        String messagesRootFilePath = System.getProperty("user.dir") +  "/queueChallenge/test";
        fileQueueService = new FileQueueService(messagesRootFilePath, queue);
        messagesFilePath = String.join("/", messagesRootFilePath, queue.getQueueName(), "messagesFile.json");

    }

    @After
    public void tearDown() throws Exception {
        Files.delete(Paths.get(messagesFilePath));
    }

    @Test
    public void should_push_queue_messages_to_the_messages_file() throws Exception {
        // GIVEN
        QueueMessage queueMessage = new QueueMessage(queue, "Test push first message");
        QueueMessage queueMessage1 = new QueueMessage(queue, "Test push second message");

        // WHEN
        fileQueueService.push(queueMessage);
        fileQueueService.push(queueMessage1);

        // THEN
        final String fileString = new String(Files.readAllBytes(Paths.get(messagesFilePath)), StandardCharsets.UTF_8);
        final JSONArray result = new JSONArray(fileString);
        Assert.assertEquals(2, result.length());

    }

    @Test
    public void should_pull_a_queue_message_from_the_messages_file() throws Exception {
        // GIVEN
        QueueMessage queueMessage = new QueueMessage(queue, "Test push first message");
        QueueMessage queueMessage1 = new QueueMessage(queue, "Test push second message");
        fileQueueService.push(queueMessage);
        fileQueueService.push(queueMessage1);

        // WHEN
        QueueMessage result = fileQueueService.pull();
        QueueMessage result1 = fileQueueService.pull();

        // THEN
        Assert.assertEquals(queueMessage.getId(), result.getId());
        Assert.assertEquals(queueMessage1.getId(), result1.getId());
    }

    @Test
    public void should_delete_a_queue_message_from_the_messages_file() throws Exception {
        // GIVEN
        QueueMessage queueMessage = new QueueMessage(queue, "Test push first message");
        QueueMessage queueMessage1 = new QueueMessage(queue, "Test push second message");
        QueueMessage queueMessage2 = new QueueMessage(queue, "Test push third message");
        fileQueueService.push(queueMessage);
        fileQueueService.push(queueMessage1);
        fileQueueService.push(queueMessage2);

        // WHEN
        fileQueueService.delete(queueMessage1);

        // THEN
        final String fileString = new String(Files.readAllBytes(Paths.get(messagesFilePath)), StandardCharsets.UTF_8);
        final JSONArray result = new JSONArray(fileString);
        Assert.assertEquals(2, result.length());
    }

    @Test
    public void should_test_if_the_queue_is_empty() throws Exception {
        // GIVEN
        QueueMessage queueMessage = new QueueMessage(queue, "Test push first message");
        QueueMessage queueMessage1 = new QueueMessage(queue, "Test push second message");
        QueueMessage queueMessage2 = new QueueMessage(queue, "Test push third message");
        fileQueueService.push(queueMessage);
        fileQueueService.push(queueMessage1);
        fileQueueService.push(queueMessage2);

        // WHEN
        boolean result = fileQueueService.isEmpty();

        // THEN

        Assert.assertFalse(result);
    }
}
