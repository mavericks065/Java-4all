package com.example.service;

import com.example.callable.FileCallable;
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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
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
    private final ScheduledExecutorService executorService;

    public FileQueueService(final File basePath, ScheduledExecutorService executorService) {
        this.basePath = checkNotNull(basePath);
        this.executorService = checkNotNull(executorService);
    }

    @Override
    public void push(E event, Queue queue) {
        checkNotNull(queue);
        checkNotNull(event);
        checkNotNull(event.getUuid());
        checkNotNull(event.getValue());

        final File lock = fnGetOrCreateTopicPath.andThen(fnGetOrCreateLockFile).apply(queue);
        waitForLock(lock);

        try {
            final File messagePath = createEventFile(event, queue);

            if (messagePath == null) return;

            writeEventFile(event, messagePath);
        } catch (IOException e) {
            LOGGER.severe("Error at creating new file");
        } finally {
            unlock(lock);
        }
    }

    @Override
    public Optional<E> pull(Queue topic) {
        checkNotNull(topic);

        final File topicPath = getTopic(topic);

        if (topicPath == null) {
            LOGGER.severe(String.format("Queue %s does not exist", topic));
            return Optional.empty();
        }

        final File lockFile = fnGetOrCreateLockFile.apply(topicPath);
        waitForLock(lockFile);

        final Optional<File> firstFile = getFirstLastModifiedFile(topicPath);

        if (!firstFile.isPresent()) {
            return Optional.empty();
        }

        Event event = null;
        try {
            final UUID id = getUuid(firstFile);

            final File invisibleEventFile = createInvisibleEventFile(topic, id);
            firstFile.get().renameTo(invisibleEventFile);

            byte[] contentByte = Files.readAllBytes(Paths.get(String.valueOf(invisibleEventFile)));
            final String content = new String(contentByte, StandardCharsets.UTF_8);

            event = new Event(id, content);
            event.setStatus(EventStatus.INVISIBLE);

            // callable that puts back the event in a visible state if not deleted
            myConsumer.accept(topic, invisibleEventFile);

        } catch (IOException e) {
            LOGGER.severe("Error at deleting file during at pull operation");
        } finally {
            unlock(lockFile);
            return Optional.ofNullable((E) event);
        }
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

        final File msgFile = new File(String.format("%s/%s/%s-%s%s", basePath, queue.getName(),
                event.getUuid().toString(), event.getStatus(), EVENT_EXTENSION));

        return msgFile.delete();
    }

    private File createInvisibleEventFile(final Queue topic, final UUID id) {
        return new File(String.format("%s/%s/%s-%s%s", basePath, topic.getName(),
                id.toString(), EventStatus.INVISIBLE, EVENT_EXTENSION));
    }

    private UUID getUuid(final Optional<File> firstFile) {
        return UUID.fromString(firstFile.get().getName()
                .replace(EventStatus.NEW.toString(), "").replace(EVENT_EXTENSION, ""));
    }


    final BiConsumer<Queue, File> myConsumer = new BiConsumer<Queue, File>() {
        @Override
        public void accept(Queue topic, File file) {
            executorService.schedule(new FileCallable(basePath, topic, file),
                    topic.getVisibilityTimeout(),
                    TimeUnit.MILLISECONDS);
        }
    };

    private void writeEventFile(E event, File messagePath) throws IOException {
        final FileWriter writer = new FileWriter(messagePath);
        writer.write(event.getValue());
        writer.flush();
        writer.close();
    }

    private File createEventFile(E event, Queue queue) throws IOException {
        final File messagePath = new File(String.format("%s/%s/%s-%s%s", basePath, queue.getName(),
                event.getUuid().toString(), EventStatus.NEW, EVENT_EXTENSION));

        if (messagePath.isFile()) {
            LOGGER.severe(String.format("This event with id %s has already been stored", event.getUuid().toString()));
            return null;
        }

        messagePath.createNewFile();
        return messagePath;
    }

    private Function<Queue, File> fnGetOrCreateTopicPath = new Function<Queue, File>() {
        @Override
        public File apply(final Queue queue) {
            File topicFile = getTopic(queue);

            //create queue
            if (topicFile == null) {
                topicFile = new File(String.format("%s/%s", basePath, queue.getName()));
                topicFile.mkdir();
            }

            return topicFile;
        }
    };

    private Function<File, File> fnGetOrCreateLockFile = new Function<File, File>() {
        @Override
        public File apply(File file) {
            return new File(String.format("%s/%s/%s", basePath, file.getName(), LOCK));
        }
    };

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
        }
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