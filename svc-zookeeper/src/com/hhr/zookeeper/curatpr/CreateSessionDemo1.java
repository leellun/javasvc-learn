package com.hhr.zookeeper.curatpr;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CreateSessionDemo1 {
	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.106:2181", 5000, 10000, retryPolicy);
		CuratorFrameworkFactory.builder().connectString("").sessionTimeoutMs(3000).retryPolicy(retryPolicy).namespace("base").build();
		client.start();
		Thread.sleep(Integer.MAX_VALUE);
	}
}
