package com.example.callable;

import com.example.model.EventStatus;
import com.example.model.Queue;

import java.io.File;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Function;

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
        return fnMakeFileVisible.compose(fnGetFileUuid).compose(fnIsFileExisting).apply(eventFile).orElse(null);
    }

    private Function<File, Optional<File>> fnIsFileExisting = file -> {
        if (file.exists())
            return Optional.of(file);
        return Optional.empty();
    };

    private Function<Optional<File>, Optional<UUID>> fnGetFileUuid = file -> {
        if (file.isPresent()) {
            final UUID uuid = UUID.fromString(file.get().getName().replace(EventStatus.INVISIBLE.toString(), "").replace(EVENT_EXTENSION, ""));
            return Optional.of(uuid);
        } else
            return Optional.empty();
    };

    private Function<Optional<UUID>, Optional<File>> fnMakeFileVisible = new Function<Optional<UUID>, Optional<File>>() {
        @Override
        public Optional<File> apply(Optional<UUID> uuid) {
            return uuid.isPresent() ?
                    Optional.of(new File(String.format("%s/%s/%s-%s%s", basePath, topic.getName(),
                        uuid.get().toString(), EventStatus.NEW, EVENT_EXTENSION)))
                    : Optional.empty();
        }
    };
}
