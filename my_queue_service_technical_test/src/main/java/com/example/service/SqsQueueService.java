package com.example.service;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.example.model.Event;
import com.example.model.Queue;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * This services haas been created with the help of the AWS documentation :
 * http://aws.amazon.com/fr/sdk-for-java/
 * https://github.com/aws/aws-sdk-java/tree/master/src/samples
 */
public class SqsQueueService implements QueueService {

    private static final Logger LOGGER = Logger.getLogger(SqsQueueService.class.getName());


    /**
     *
     * constructor that takes SQS client to initialise our service
     *
     * @param sqsClient
     */
    public SqsQueueService(final AmazonSQSClient sqsClient, final Queue queue) {

    }

    @Override
    public void push(Event event, Queue queue) {

    }

    @Override
    public Optional pull(Queue queue) {
        return null;
    }

    @Override
    public boolean delete(Event event, Queue queue) {
        return false;
    }

}
