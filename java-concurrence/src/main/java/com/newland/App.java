package com.newland;

import com.newland.volilatile.VolatileBarrierExample;
import com.newland.volilatile.VolatileFeaturesExample;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        String classFileStr = VolatileBarrierExample.class.getName().replaceAll("\\.", "/")+".class";
        String path = System.getProperty("java.class.path") + File.separator + classFileStr;
        Process process = Runtime.getRuntime().exec("javap -c " + path);
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
