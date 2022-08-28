package com.newland.demo02.sync;

import java.io.*;

/**

 */
public class SynchronizedTest {
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("java.class.path") + File.separator + "com/newland/demo02/sync/Synchronized.class";
        Process process = Runtime.getRuntime().exec("javap -v " + path);
        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(process.getErrorStream(), "gbk"));
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
        String str;
        while ((str = br.readLine()) != null) {
            System.out.println(str);
        }
        while ((str = stdError.readLine()) != null) {
            System.out.println(str);
        }
    }
}
