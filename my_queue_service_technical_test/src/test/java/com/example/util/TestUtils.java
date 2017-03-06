package com.example.util;

import java.io.File;

public class TestUtils {

    public static void cleanUp(File folder) {
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
