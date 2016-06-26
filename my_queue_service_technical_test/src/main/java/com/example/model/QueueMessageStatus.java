package com.example.model;

/**
 * Created by Nicolas Guignard on 15/06/2016.
 *
 * Defines the status of the message
 */
public enum QueueMessageStatus {

    // New Unprocessed message
    NEW,

    // a Message that has been picked up for processing. Inflight.
    // It is still in the queue but cannot be picked up
    INVISIBLE
}
