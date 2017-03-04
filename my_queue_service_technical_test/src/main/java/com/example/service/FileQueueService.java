package com.example.service;

import com.example.model.Event;
import com.example.model.EventStatus;
import com.example.model.Queue;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A file-based queue, suitable for same-host producers and consumers, but potentially different JVMs.
 */
public class FileQueueService<E extends Event> implements QueueService<E> {

    private static final Logger LOGGER = Logger.getLogger(FileQueueService.class.getName());

    private static final String LOCK = ".lock";
    private static final String EVENT_EXTENSION = ".event";
    private static final int LOCK_RETRY = 50;

    private final File basePath;

    public FileQueueService(final File basePath) {
        this.basePath = checkNotNull(basePath);
    }

    @Override
    public void push(E event, Queue queue) {
        checkNotNull(queue);
        checkNotNull(event);
        checkNotNull(event.getUuid());
        checkNotNull(event.getValue());

        final File topicPath = getOrCreateTopicPath(queue);

        final File lock = getOrCreateLockFile(topicPath);
        waitForLock(lock);

        final File messagePath = new File(String.format("%s/%s/%s-%s%s", basePath, queue.getName(),
                event.getUuid().toString(), EventStatus.NEW, EVENT_EXTENSION));
        if (messagePath.isFile()) {
            LOGGER.severe(String.format("This event with id %s has already been stored", event.getUuid().toString()));
            return;
        }

        try {
            messagePath.createNewFile();

            final FileWriter writer = new FileWriter(messagePath);
            writer.write(event.getValue());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOGGER.severe("Error at creating new file");
        } finally {
            unlock(lock);
        }

    }

    @Override
    public Optional pull(Queue queue) {
        checkNotNull(queue);

        final File topicPath = getTopic(queue);
        if (topicPath == null) {
            LOGGER.severe(String.format("Queue %s does not exist", queue));
            return Optional.empty();
        }

        final File lockFile = getOrCreateLockFile(topicPath);
        waitForLock(lockFile);

        final Optional<File> firstFile = getFirstLastModifiedFile(topicPath);

        if (firstFile.isPresent()) {
            final UUID id = UUID.fromString(firstFile.get().getName()
                    .replace(EventStatus.NEW.toString(), "").replace(EVENT_EXTENSION, ""));

            File invisibleFile = new File(String.format("%s/%s/%s-%s%s", basePath, queue.getName(),
                    id.toString(), EventStatus.INVISIBLE, EVENT_EXTENSION));
            firstFile.get().renameTo(invisibleFile);

            try {
                final String content = new String(Files.readAllBytes(Paths.get(String.valueOf(invisibleFile))), StandardCharsets.UTF_8);
                final Event event = new Event(id, content);
                event.setStatus(EventStatus.INVISIBLE);

//                delete((E) event, queue);

                return Optional.of((E) event);
            } catch (IOException e) {
                LOGGER.severe("Error at deleting file during at pull operation");
            } finally {
                unlock(lockFile);
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean delete(E event, Queue queue) {
        checkNotNull(event);
        checkNotNull(queue);

        final File topicPath = getTopic(queue);
        if (topicPath == null) {
            LOGGER.severe(String.format("Queue %s does not exist", queue));
            return false;
        }

        final File messagePath = new File(String.format("%s/%s/%s-%s%s", basePath, queue.getName(),
                event.getUuid().toString(), event.getStatus(), EVENT_EXTENSION));
        return messagePath.delete();

    }

    private File getOrCreateTopicPath(final Queue queue) {
        File topicFile = getTopic(queue);

        //create queue
        if (topicFile == null) {
            topicFile = new File(String.format("%s/%s", basePath, queue.getName()));
            topicFile.mkdir();
        }

        return topicFile;
    }

    private File getTopic(final Queue queue) {
        final File f = new File(String.format("%s/%s", basePath, queue.getName()));

        if (f.exists()) {
            if (!f.isDirectory()) {
                LOGGER.severe(String.format("Queue needs to be a directory %s", queue));
            }
            return f;
        }

        return null;
    }

    private void waitForLock(final File lock) {
        try {
            lock(lock);
        } catch (InterruptedException e) {
            LOGGER.severe(String.format("Error while waiting lock %s. You might have lost data...", lock));
            return;
        }
    }

    private File getOrCreateLockFile(File topic) {
        final File lockFile = new File(String.format("%s/%s/%s", basePath, topic.getName(), LOCK));

        return lockFile;
    }

    private void unlock(File lock) {
        lock.delete();
    }

    private void lock(File lock) throws InterruptedException {
        while (!lock.mkdir()) {
            Thread.sleep(LOCK_RETRY);
        }
    }

    private Optional<File> getFirstLastModifiedFile(File dir) {
        final File[] files = dir.listFiles((FileFilter) FileFileFilter.FILE);

        if (files.length > 0) {
            return Arrays.stream(files)
                    .filter(f -> f.getAbsolutePath().contains(EventStatus.NEW.toString()))
                    .sorted(LastModifiedFileComparator.LASTMODIFIED_COMPARATOR)
                    .findFirst();
        }

        return Optional.empty();
    }


}
