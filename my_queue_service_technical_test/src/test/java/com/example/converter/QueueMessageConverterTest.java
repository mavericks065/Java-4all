package com.example.converter;

import com.example.model.Queue;
import com.example.model.QueueImpl;
import com.example.model.QueueMessage;
import com.example.model.QueueMessageStatus;

import com.amazonaws.services.sqs.model.Message;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

/**
 * Created by Nicolas Guignard on 16/06/2016.
 */
public class QueueMessageConverterTest {

    @Test
    public void should_convert_sqs_message_to_queue_message() throws Exception {
        // GIVEN

        Queue queue = new QueueImpl("testQueue", Integer.valueOf(300));
        QueueMessage expectedResult = new QueueMessage(queue, "payload object test",
                Instant.now(), QueueMessageStatus.NEW);
        expectedResult.setId("test");

        Message sqsMessage = new Message();
        sqsMessage.setBody(expectedResult.toString());

        // WHEN
        QueueMessage result = QueueMessageConverter.sqsMessageToQueueMessage(sqsMessage);

        // THEN
        Assert.assertEquals(expectedResult, result);

    }
}
