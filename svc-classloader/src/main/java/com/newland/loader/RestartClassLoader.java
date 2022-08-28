package com.newland.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Author: leell
 * Date: 2022/8/27 20:21:11
 */
public class RestartClassLoader extends ClassLoader {

    private String fileName;

    public RestartClassLoader(String fileName) {
        this.fileName = fileName;
    }

    public RestartClassLoader() {
        this.fileName = System.getProperty("java.class.path");
    }

    protected Class<?> findClass(Class<?> clazz) {
        return findClass(clazz.getName());
    }

    @Override
    protected Class<?> findClass(String className) {
        Class<?> clazz = null;
        try {
            String classFile = getClassFile(className);
            FileInputStream fis = new FileInputStream(classFile);
            FileChannel fileC = fis.getChannel();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            WritableByteChannel outC = Channels.newChannel(baos);
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            while (true) {
                int i = fileC.read(buffer);
                if (i == 0 || i == -1) {
                    break;
                }
                buffer.flip();
                outC.write(buffer);
                buffer.clear();
            }
            fis.close();
            byte[] bytes = baos.toByteArray();
            clazz = defineClass(className, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clazz;
    }

    private String getClassFile(String className) {
        return fileName + File.separator + className.replace(".", "/") + ".class";
    }
}