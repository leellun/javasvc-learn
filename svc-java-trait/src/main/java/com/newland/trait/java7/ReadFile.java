package com.newland.trait.java7;

import com.newland.trait.java8.Lambda;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 记录异常，不被淹没
 * addSuppressed
 */
public class ReadFile {
    public void read(String filename) throws RuntimeException, IOException {
        FileInputStream input = null;
        IOException readException = null;
        try {
            input = new FileInputStream(filename);
        } catch (IOException ex) {
            readException = ex;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    if (readException == null) {
                        readException = ex;
                    } else {
                        //使用java7的
                        readException.addSuppressed(ex);
                    }
                }
            }
            if (readException != null) {
                throw new RuntimeException(readException);
            }
        }
    }

    /**
     * 捕获多个异常
     * @param filename
     */
    public void handle(String filename) {
        try {
            read(filename);
        } catch (IOException | RuntimeException ab) {
            System.out.println(ab.getClass());
        }
    }

    public static void main(String[] args) throws IOException {
        ReadFile readFile = new ReadFile();
        readFile.read("sdfsdfsdfsdfsdf");
    }
}
