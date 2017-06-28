package com.example.service;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import com.example.model.Event;
import com.example.model.Queue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This services haas been created with the help of the AWS documentation :
 * http://aws.amazon.com/fr/sdk-for-java/
 * https://github.com/aws/aws-sdk-java/tree/master/src/samples
 */
public class SqsQueueService<E extends Event> implements QueueService<E> {

    private final AmazonSQSClient sqsClient;
    private final ReceiveMessageRequest receiveMessageRequest;

    /**
     * constructor that takes SQS client to initialise our service
     *
     * @param sqsClient
     */
    public SqsQueueService(final AmazonSQSClient sqsClient, final Queue queue) {
        this.sqsClient = checkNotNull(sqsClient);

        this.receiveMessageRequest = new ReceiveMessageRequest(queue.getName());

        // set visibility timeout of the queue on AWS
        final ChangeMessageVisibilityRequest changeMessageVisibilityRequest = new ChangeMessageVisibilityRequest();
        changeMessageVisibilityRequest.setVisibilityTimeout(Long.valueOf(queue.getVisibilityTimeout()).intValue());

        this.sqsClient.changeMessageVisibility(changeMessageVisibilityRequest);
    }

    /**
     * visibility timeout is taking care of by AWS
     * http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/AboutVT.html
     * <p>
     * Call sqs client send message with a message containing the content of our event message
     *
     * @param event to store
     * @param topic Queue in which the event has to be stored
     */
    @Override
    public void push(final E event, final Queue topic) {

        checkNotNull(event);
        checkNotNull(topic);

        final String queueURL = sqsClient.getQueueUrl(topic.getName()).getQueueUrl();
        final SendMessageRequest messageRequest = buildMessageRequest(event, queueURL);

        sqsClient.sendMessage(messageRequest);
    }


    @Override
    public Optional<E> pull(final Queue topic) {

        checkNotNull(topic);

        final Message sqsMessage = sqsClient.receiveMessage(receiveMessageRequest)
                .getMessages()
                .get(0);

        return Optional.ofNullable(
                (E) new Event(UUID.fromString(sqsMessage.getAttributes().get("uuid")), sqsMessage.getBody()));
    }

    @Override
    public boolean delete(E event, final Queue topic) {

        checkNotNull(event);
        checkNotNull(topic);

        final String queueURL = sqsClient.getQueueUrl(topic.getName()).getQueueUrl();
        final List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();

        final Message messageToDelete = messages.stream()
                .filter(m -> (m.getBody().equals(event.getValue()) && m.getAttributes().get("uuid").equals(event.getUuid().toString())))
                .findFirst().orElse(null);

        if (messageToDelete != null) {
            DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueURL, messageToDelete.getReceiptHandle());
            sqsClient.deleteMessage(deleteMessageRequest);
            return true;
        }
        return false;
    }

    private SendMessageRequest buildMessageRequest(final E event, final String queueURL) {
        final SendMessageRequest messageRequest = new SendMessageRequest(queueURL, event.getValue());
        final MessageAttributeValue attributeValue = new MessageAttributeValue();
        attributeValue.setStringValue(event.getUuid().toString());

        messageRequest.addMessageAttributesEntry("uuid", attributeValue);
        return messageRequest;
    }
}
