package com.example.service;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.example.converter.QueueMessageConverter;
import com.example.model.Queue;
import com.example.model.QueueMessage;
import com.example.model.QueueMessageStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * A file-based queue, suitable for same-host producers and consumers, but potentially different JVMs.
 */
public class FileQueueService implements QueueService {
  //
  // Task 3: Implement me if you have time.
  //

    private final File messagesFile;
    private final File bucketFile;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private static final String LOCK_FILE = ".lock";

    public FileQueueService(final String rootBucketPath, final Queue queue) throws IOException {

        // get from the config file the folder root path where to
        // place the queueMessages : bucketPath, the goal is to have one directory by queue like is recommended here:
        // https://engineering.canva.com/2015/03/25/hermeticity/
        String bucketPath = String.join("/", rootBucketPath, queue.getQueueName());
        bucketFile = new File(bucketPath);
        if (!bucketFile.exists())  bucketFile.mkdirs();

        // instantiates messagesFile if no  and make it final
        String fileName = String.join("/", bucketPath, "messagesFile.json");
        messagesFile = new File(fileName);
        if (!messagesFile.exists())  messagesFile.createNewFile();
    }

    @Override
    public final void push(QueueMessage queueMessage) {

        File lock = getLockFile();

        try {

            lock(lock);

            final JSONArray messagesArray = getMessagesFileContent(queueMessage);

            writeNewMessagesContent(messagesArray);

        } catch (IOException e) {
            logger.severe(e.getMessage());
        } catch (InterruptedException ie) {
            logger.severe(ie.getMessage());
        } catch (JSONException e) {
            logger.severe(e.getMessage());
        } finally {
            unlock(lock);
        }
    }


    @Override
    public final QueueMessage pull() {
        // get the lock
        File lock = getLockFile();

        try {

            lock(lock);

            // read messages

            // take first one if not INVISIBLE
            String fileString = new String(Files.readAllBytes(Paths.get(messagesFile.getAbsolutePath())), StandardCharsets.UTF_8);
            JSONArray jsonMessages = new JSONArray(fileString);

            for(int i = 0; i < jsonMessages.length(); i++) {
                JSONObject message = jsonMessages.getJSONObject(i);
                if (message.getString("queueMessageStatus").equals(QueueMessageStatus.NEW.toString())) {

                    QueueMessage queueMessage =  QueueMessageConverter.jsonToQueueMessage(message);
                    queueMessage.setQueueMessageStatus(QueueMessageStatus.INVISIBLE);

                    jsonMessages.getJSONObject(i).put("queueMessageStatus", QueueMessageStatus.INVISIBLE);

                    writeNewMessagesContent(jsonMessages);

                    return queueMessage;
                }
            }
        } catch (IOException ioe) {
            logger.severe(ioe.getMessage());
        } catch (JSONException je) {
            logger.severe(je.getMessage());
        } catch (InterruptedException ie) {
            logger.severe(ie.getMessage());
        } finally {
            unlock(lock);
        }

        return null;
    }

    @Override
    public final void delete(QueueMessage queueMessage) {

        File lock = getLockFile();

        try {

            lock(lock);

            String fileString = com.google.common.io.Files.toString(messagesFile, StandardCharsets.UTF_8);
            JSONArray jsonMessages = new JSONArray(fileString);

            for(int i = 0; i < jsonMessages.length(); i++) {
                JSONObject message = jsonMessages.getJSONObject(i);
                if (message.getString("id").equals(queueMessage.getId())) {
                    jsonMessages.remove(i);
                    writeNewMessagesContent(jsonMessages);
                }
            }
        } catch (IOException ioe) {
            logger.severe(ioe.getMessage());
        } catch (JSONException je) {
            logger.severe(je.getMessage());
        } catch (InterruptedException ie) {
            logger.severe(ie.getMessage());
        } finally {
            unlock(lock);
        }

    }

    @Override
    public final boolean isEmpty() {

        boolean isEmpty = true;

        File lock = getLockFile();
        try {

            lock(lock);

            String fileString = com.google.common.io.Files.toString(messagesFile, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(fileString);

            isEmpty = jsonArray.length() == 0;

        } catch (IOException e) {
            logger.severe(e.getMessage());
        } catch (JSONException e) {
            logger.severe(e.getMessage());
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        } finally {
            unlock(lock);
        }
        return isEmpty;
    }

    private void writeNewMessagesContent(JSONArray messagesArray) throws IOException {
        final PrintWriter pw = new PrintWriter(new FileWriter(messagesFile.getAbsoluteFile()));

        pw.write("");
        pw.write(messagesArray.toString());
        pw.flush();
        pw.close();
    }

    private JSONArray getMessagesFileContent(QueueMessage queueMessage) throws IOException, JSONException {
        final String messagesFileContent = com.google.common.io.Files.toString(messagesFile, StandardCharsets.UTF_8);
        final JSONArray messagesArray;
        if (messagesFileContent.isEmpty()) {
            messagesArray = new JSONArray();
        } else {
            messagesArray = new JSONArray(messagesFileContent);
        }
        messagesArray.put(new JSONObject(queueMessage.toString()));
        return messagesArray;
    }

    // helper methods (lock / unlock) taken from https://engineering.canva.com/2015/03/25/hermeticity/

    private void lock(File lock) throws InterruptedException {
        while (lock !=null && !lock.mkdir()) {
            Thread.sleep(50);
        }
    }

    private void unlock(File lock) {
        if (lock != null) lock.delete();
    }

    private File getLockFile() {
        File lockFile = null;
        try {

            Path lockFilePath = Files.walk(Paths.get(bucketFile.getAbsolutePath())).filter(f -> f.getFileName().toString().contains(LOCK_FILE))
                    .findFirst().orElse(null);

            lockFile = lockFilePath != null ? lockFilePath.toFile() : null;

        } catch (IOException e) {
            // This SHOULD be improved with an exception mechanism to handle errors
            logger.severe(e.getMessage());
        }
        return lockFile;
    }
}
