package com.example.service;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import com.example.model.Queue;
import com.example.model.QueueImpl;
import com.example.model.QueueMessage;
import com.example.model.QueueMessageStatus;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Nicolas Guignard on 16/06/2016.
 */
public class SqsQueueServiceTest {

    private SqsQueueService queueService;

    private AmazonSQSClient sqsClient;
    private ReceiveMessageRequest receiveMessageRequest;

    private Queue queue;

    @Before
    public void setUp() throws Exception {

        queue = new QueueImpl("queueNameTest", 300);

        sqsClient = mock(AmazonSQSClient.class);
        receiveMessageRequest = mock(ReceiveMessageRequest.class);

        queueService = new SqsQueueService(sqsClient, queue);
    }

    @Test
    public void should_push_messages_to_aws_sqs() throws Exception {
        // GIVEN
        GetQueueUrlResult getQueueUrlResult = mock(GetQueueUrlResult.class);
        final String payload = "This is a test for the push functionality : ";
        QueueMessage queueMessage = new QueueMessage(queue, payload);

        // WHEN
        when(sqsClient.getQueueUrl(anyString())).thenReturn(getQueueUrlResult);
        when(getQueueUrlResult.getQueueUrl()).thenReturn("test/queue/url");
        queueService.push(queueMessage);

        // THEN
        verify(sqsClient, times(1))
                .sendMessage(eq(new SendMessageRequest("test/queue/url", queueMessage.toString())));

    }

    @Test
    public void should_pull_messages_from_aws_sqs() throws Exception {
        // GIVEN
        QueueMessage queueMessage = new QueueMessage(queue, "payload object test",
                Instant.now(), QueueMessageStatus.NEW);
        queueMessage.setId("test");

        Message sqsMessage = mock(Message.class);
        ReceiveMessageResult receiveMessageResult = mock(ReceiveMessageResult.class);

        // WHEN

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult);
        when(receiveMessageResult.getMessages()).thenReturn(Lists.newArrayList(sqsMessage));
        when(sqsMessage.getBody()).thenReturn(queueMessage.toString());

        QueueMessage result = queueService.pull();

        // THEN
        verify(sqsClient, times(1)).receiveMessage(any(ReceiveMessageRequest.class));
        Assert.assertEquals(queueMessage, result);
    }

    @Test
    public void should_check_if_the_queue_is_empty() throws Exception {
        // GIVEN
        ReceiveMessageResult receiveMessageResult = mock(ReceiveMessageResult.class);

        // WHEN
        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult);
        when(receiveMessageResult.getMessages()).thenReturn(new ArrayList<>());
        boolean result = queueService.isEmpty();

        // THEN
        Assert.assertTrue(result);
    }

    @Test
    public void should_delete_messages_from_aws_queue() throws Exception {
        // GIVEN
        GetQueueUrlResult getQueueUrlResult = mock(GetQueueUrlResult.class);
        final String payload = "This is a test for the push functionality : ";
        QueueMessage queueMessage = new QueueMessage(queue, payload);
        Message sqsMessage = mock(Message.class);
        ReceiveMessageResult receiveMessageResult = mock(ReceiveMessageResult.class);


        // WHEN
        when(sqsClient.getQueueUrl(anyString())).thenReturn(getQueueUrlResult);
        when(getQueueUrlResult.getQueueUrl()).thenReturn("test/queue/url");
        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult);
        when(receiveMessageResult.getMessages()).thenReturn(Lists.newArrayList(sqsMessage));
        when(sqsMessage.getReceiptHandle()).thenReturn("receiptHandlerTest");
        when(sqsMessage.getBody()).thenReturn(queueMessage.toString());

        queueService.delete(queueMessage);

        // THEN
        verify(sqsClient).deleteMessage(new DeleteMessageRequest("test/queue/url", "receiptHandlerTest"));
    }
}