package com.example.service;

import com.example.model.Event;
import com.example.model.Queue;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A file-based queue, suitable for same-host producers and consumers, but potentially different JVMs.
 */
public class FileQueueService implements QueueService {

    private static final Logger LOGGER = Logger.getLogger(FileQueueService.class.getName());


    public FileQueueService(final String rootBucketPath, final Queue queue) throws IOException {
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
