package com.example.exception;

public class QueueException extends Exception {

    public QueueException(Exception e) {
        super(e);
    }

    public QueueException(String message, Throwable cause) {
        super(message, cause);
    }
}
