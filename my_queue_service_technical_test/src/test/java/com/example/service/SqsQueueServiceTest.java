package com.example.service;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import com.example.model.Event;
import com.example.model.Queue;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SqsQueueServiceTest {

    private SqsQueueService queueService;

    private AmazonSQSClient sqsClient;
    private ReceiveMessageRequest receiveMessageRequest;

    private Queue queue;

    @Before
    public void setUp() throws Exception {

        queue = new Queue("queueNameTest", 300);

        sqsClient = mock(AmazonSQSClient.class);
        receiveMessageRequest = mock(ReceiveMessageRequest.class);

        queueService = new SqsQueueService(sqsClient, queue);
    }

    @After
    public void tearDown() {
        queue = null;
        sqsClient = null;
        receiveMessageRequest = null;
        queueService = null;
    }

    @Test
    public void should_push_messages_to_aws_sqs() throws Exception {
        // GIVEN
        GetQueueUrlResult getQueueUrlResult = mock(GetQueueUrlResult.class);
        final String payload = "This is a test for the push functionality : ";
        Event event = new Event(UUID.randomUUID(), payload);

        SendMessageRequest messageRequest = new SendMessageRequest("test/queue/url", event.getValue());
        MessageAttributeValue attr = new MessageAttributeValue();
        attr.setStringValue(event.getUuid().toString());

        messageRequest.addMessageAttributesEntry("uuid", attr);

        when(sqsClient.getQueueUrl(anyString())).thenReturn(getQueueUrlResult);
        when(getQueueUrlResult.getQueueUrl()).thenReturn("test/queue/url");

        // WHEN
        queueService.push(event, queue);

        // THEN
        verify(sqsClient, times(1)).sendMessage(eq(messageRequest));
    }

    @Test
    public void should_pull_messages_from_aws_sqs() throws Exception {
        // GIVEN
        Event event = new Event(UUID.randomUUID(), "message 1");

        Message sqsMessage = mock(Message.class);
        ReceiveMessageResult receiveMessageResult = mock(ReceiveMessageResult.class);

        Map<String, String> attributeValueMap = new HashMap<>();
        attributeValueMap.put("uuid", event.getUuid().toString());

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult);
        when(receiveMessageResult.getMessages()).thenReturn(Lists.newArrayList(sqsMessage));
        when(sqsMessage.getBody()).thenReturn(event.getValue());
        when(sqsMessage.getAttributes()).thenReturn(attributeValueMap);

        // WHEN
        Optional<Event> result = queueService.pull(queue);

        // THEN
        verify(sqsClient, times(1)).receiveMessage(any(ReceiveMessageRequest.class));
        assertEquals(event.getUuid(), result.get().getUuid());
    }

    @Test
    public void should_delete_messages_from_aws_queue() throws Exception {
        // GIVEN
        GetQueueUrlResult getQueueUrlResult = mock(GetQueueUrlResult.class);
        final String value = "This is a test for the delete functionality : ";
        Event queueMessage = new Event(UUID.randomUUID(), value);
        Message sqsMessage = mock(Message.class);
        ReceiveMessageResult receiveMessageResult = mock(ReceiveMessageResult.class);

        Map<String, String> attributeValueMap = new HashMap<>();
        attributeValueMap.put("uuid", queueMessage.getUuid().toString());

        when(sqsClient.getQueueUrl(anyString())).thenReturn(getQueueUrlResult);
        when(getQueueUrlResult.getQueueUrl()).thenReturn("test/queue/url");
        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(receiveMessageResult);
        when(receiveMessageResult.getMessages()).thenReturn(Lists.newArrayList(sqsMessage));
        when(sqsMessage.getReceiptHandle()).thenReturn("receiptHandlerTest");
        when(sqsMessage.getBody()).thenReturn(queueMessage.getValue());
        when(sqsMessage.getAttributes()).thenReturn(attributeValueMap);

        // WHEN

        queueService.delete(queueMessage, queue);

        // THEN
        verify(sqsClient).deleteMessage(new DeleteMessageRequest("test/queue/url", "receiptHandlerTest"));
    }

}
