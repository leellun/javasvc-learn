package com.hhr.zookeeper.curatpr;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class DelDataDemo3 {
	static String path = "/zk-book/c1";

	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.106:2181", 5000, 10000, retryPolicy);
//		CuratorFrameworkFactory.builder().connectString("").sessionTimeoutMs(3000).retryPolicy(retryPolicy)
//				.namespace("base").build();
		client.start();
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());
		Stat stat=new Stat();
		client.getData().storingStatIn(stat).forPath(path);
		client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
		
		
		Thread.sleep(Integer.MAX_VALUE);
	}
}
