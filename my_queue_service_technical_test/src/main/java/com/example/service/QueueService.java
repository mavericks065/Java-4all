package com.example.service;

import com.example.model.QueueMessage;

public interface QueueService {

  //
  // Task 1: Define me.
  //
  // This interface should include the following methods.  You should choose appropriate
  // signatures for these methods that prioritise simplicity of implementation for the range of
  // intended implementations (in-memory, file, and SQS).  You may include additional methods if
  // you choose.
  //
  // - push
  //   pushes a message onto a queue.
  // - pull
  //   retrieves a single message from a queue.
  // - delete
  //   deletes a message from the queue that was received by pull().
  //

    /**
     * pushes a queue message onto a queue.
     *
     * @param queueMessage
     */
    void push(QueueMessage queueMessage);

    /**
     * retrieves a single queue message from a queue. This queue message cannot
     * have the status INVISIBLE.
     *
     * @return
     */
    QueueMessage pull();

    /**
     * deletes a queue message that was received
     *
     * @param queueMessage
     */
    void delete(QueueMessage queueMessage);

    // removes and returns the next item in line
    // QueueMessage dequeue();

    /**
     * is true if the queue is empty, false otherwise
     *
     * @return
     */
    boolean isEmpty();
}
