package com.example.converter;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.example.model.Queue;
import com.example.model.QueueImpl;
import com.example.model.QueueMessage;
import com.example.model.QueueMessageStatus;

import java.time.Instant;

/**
 * Created by Nicolas Guignard on 16/06/2016.
 */
public class QueueMessageConverter {

    public static QueueMessage sqsMessageToQueueMessage(Message message) throws JSONException {

        final JSONObject jsonQueueMessage = new JSONObject(message.getBody());

        return jsonToQueueMessage(jsonQueueMessage);
    }

    public static QueueMessage jsonToQueueMessage(JSONObject jsonQueueMessage) throws JSONException {

        final JSONObject jsonQueue = jsonQueueMessage.getJSONObject("queue");

        Object payload = jsonQueueMessage.get("payload");
        String id = jsonQueueMessage.getString("id");
        Instant messageQueueTime = Instant.parse(jsonQueueMessage.getString("messageQueueTime"));
        QueueMessageStatus queueMessageStatus = QueueMessageStatus.valueOf(jsonQueueMessage.getString("queueMessageStatus"));

        final Queue queue = new QueueImpl(jsonQueue.getString("queueName"), jsonQueue.getLong("visibilityTimeout"));

        QueueMessage queueMessage = new QueueMessage(queue, payload, messageQueueTime, queueMessageStatus);
        queueMessage.setId(id);

        return queueMessage;
    }
}
