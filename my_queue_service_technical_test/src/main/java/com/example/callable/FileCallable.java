package com.example.callable;

import com.example.model.EventStatus;
import com.example.model.Queue;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Callable;

public class FileCallable implements Callable {

    private static final String EVENT_EXTENSION = ".event";

    private final File basePath;
    private final Queue topic;
    private final File eventFile;

    public FileCallable(final File basePath, final Queue topic,
                        final File eventFile) {
        this.basePath = basePath;
        this.topic = topic;
        this.eventFile = eventFile;
    }

    @Override
    public File call() throws Exception {

        if (eventFile.exists()) {
            final UUID id = UUID.fromString(eventFile.getName()
                    .replace(EventStatus.INVISIBLE.toString(), "").replace(EVENT_EXTENSION, ""));

            File visibleFile = new File(String.format("%s/%s/%s-%s%s", basePath, topic.getName(),
                    id.toString(), EventStatus.NEW, EVENT_EXTENSION));

            eventFile.renameTo(visibleFile);

            return visibleFile;
        }
        return null;
    }
}
