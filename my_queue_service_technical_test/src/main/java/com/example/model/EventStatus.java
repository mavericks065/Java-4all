package com.example.model;

/**
 * Defines the status of the message
 */
public enum EventStatus {

    // New Unprocessed message
    NEW,

    // a Message that has been picked up for processing. Inflight.
    // It is still in the queue but cannot be picked up
    INVISIBLE
}
