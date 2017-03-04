package com.example.callable;

import com.example.model.Event;
import com.example.model.EventStatus;
import com.example.model.Queue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

import static org.junit.Assert.*;

public class FileCallableTest {

    private FileCallable callable;

    private File basePath;
    private Queue topic;

    @Before
    public void setUp(){
        basePath = new File(".");
        topic = new Queue("callableTest", 500);
    }

    @After
    public void tearDown(){
        // remove filesystem
        cleanUp(new File(basePath + "/" + topic.getName()));
    }

    @Test
    public void should_rename_file() throws Exception {
        // GIVEN
        final Event event0 = new Event(UUID.randomUUID(), "message0");

        File topicFile = new File(String.format("%s/%s", basePath, topic.getName()));
        topicFile.mkdir();

        final File messagePath = new File(String.format("%s/%s/%s-%s%s", basePath, topic.getName(),
                event0.getUuid().toString(), EventStatus.INVISIBLE, ".event"));
        messagePath.createNewFile();

        callable = new FileCallable(basePath, topic, messagePath);

        // WHEN
        File result = callable.call();

        // THEN
        assertNotNull(result);
        assertEquals(String.format("%s-%s%s", event0.getUuid().toString(), EventStatus.NEW, ".event"),
                    result.getName());
    }

    @Test
    public void should_not_rename_file() throws Exception {
        // GIVEN
        File eventFile = new File(String.format("%s/%s/%s-%s%s", basePath, topic.getName(),
                "123456", EventStatus.NEW, ".event"));
        callable = new FileCallable(basePath, topic, eventFile);

        // WHEN
        File result = callable.call();

        // THEN
        assertNull(result);
    }

    private void cleanUp(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    cleanUp(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
}
