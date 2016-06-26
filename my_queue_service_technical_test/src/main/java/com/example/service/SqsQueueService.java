package com.example.service;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import com.amazonaws.util.json.JSONException;
import com.example.converter.QueueMessageConverter;
import com.example.model.Queue;
import com.example.model.QueueMessage;

import java.util.List;
import java.util.logging.Logger;

/**
 * This services haas been created with the help of the AWS documentation :
 * http://aws.amazon.com/fr/sdk-for-java/
 * https://github.com/aws/aws-sdk-java/tree/master/src/samples
 */
public class SqsQueueService implements QueueService {
    //
    // Task 4: Optionally implement parts of me.
    //
    // This file is a placeholder for an AWS-backed implementation of QueueService.  It is included
    // primarily so you can quickly assess your choices for method signatures in QueueService in
    // terms of how well they map to the implementation intended for a production environment.
    //

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final AmazonSQSClient sqsClient;
    private final ReceiveMessageRequest receiveMessageRequest;
    private final Queue queue;

    /**
     *
     * constructor that takes SQS client to initialise our service
     *
     * @param sqsClient
     */
    public SqsQueueService(final AmazonSQSClient sqsClient, final Queue queue) {

        this.sqsClient = sqsClient;
        this.receiveMessageRequest = new ReceiveMessageRequest(queue.getQueueName());
        this.queue = queue;

        // set visibility timeout of the queue on AWS to make sure it has the same than the one we have
        ChangeMessageVisibilityRequest changeMessageVisibilityRequest = new ChangeMessageVisibilityRequest();
        changeMessageVisibilityRequest.setVisibilityTimeout(Long.valueOf(queue.getVisibilityTimeout()).intValue());
        this.sqsClient.changeMessageVisibility(changeMessageVisibilityRequest);
    }

    @Override
    public final void push(final QueueMessage queueMessage) {

        String queueURL = sqsClient.getQueueUrl(queue.getQueueName()).getQueueUrl();
        SendMessageRequest messageRequest = new SendMessageRequest(queueURL, queueMessage.toString());

        sqsClient.sendMessage(messageRequest);
    }

    /**
     * No need to take care of the visibility timeout in this implementation since it is amazon that is doing it.
     * http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/AboutVT.html
     *
     * @return
     */
    @Override
    public final QueueMessage pull() {

        final Message sqsMessage = sqsClient.receiveMessage(receiveMessageRequest)
                                            .getMessages()
                                            .get(0);
        QueueMessage queueMessage = null;

        try {
            queueMessage = QueueMessageConverter.sqsMessageToQueueMessage(sqsMessage);
        } catch (JSONException e) {
            // This SHOULD be improved with an exception mechanism to handle errors
            logger.severe(e.getMessage());
        }

        return queueMessage;
    }

    @Override
    public final void delete(QueueMessage queueMessage) {
        String queueURL = sqsClient.getQueueUrl(queue.getQueueName()).getQueueUrl();
        final List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();

        final Message messageToDelete = messages.stream().filter(m -> m.getBody().equals(queueMessage.toString())).findFirst().orElse(null);

        if (messageToDelete != null) {
            DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueURL, messageToDelete.getReceiptHandle());
            sqsClient.deleteMessage(deleteMessageRequest);
        }
    }

    @Override
    public final boolean isEmpty() {

        return sqsClient.receiveMessage(receiveMessageRequest).getMessages().isEmpty();
    }
}
