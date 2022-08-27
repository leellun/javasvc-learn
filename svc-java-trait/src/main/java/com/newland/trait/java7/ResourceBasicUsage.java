package com.newland.trait.java7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * try-with-resource
 * 不需要使用finally来保证打开的流被正确关闭
 * 这是自动完成的。
 * 自动回收资源
 */
public class ResourceBasicUsage {
    public String readFile(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(String.format("%n"));
            }
            return builder.toString();
        }
    }

    /**
     * 自动回收资源的原理其实是通过AutoCloseable实现的
     */
    public static class CustomResource  implements AutoCloseable {
        public void close() throws Exception {
            System.out.println("进行资源释放。");
        }
        public void useCustomResource() throws Exception {
            try (CustomResource resource = new CustomResource())  {
                System.out.println("使用资源。");
            }
        }
        public static void main(String[] args) {
            try {
                new CustomResource().useCustomResource();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
