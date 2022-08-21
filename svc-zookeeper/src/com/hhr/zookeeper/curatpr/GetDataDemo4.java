package com.hhr.zookeeper.curatpr;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class GetDataDemo4 {
	static String path = "/zk-book/c1";

	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.106:2181", 5000, 10000, retryPolicy);
		client.start();
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());
		Stat stat=new Stat();
		String data=new String(client.getData().storingStatIn(stat).forPath(path));
		client.setData().forPath(path,"hello".getBytes()).setVersion(stat.getVersion());
		System.out.println(data);
		
		data=new String(client.getData().storingStatIn(stat).forPath(path));
		client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
		
		System.out.println(data);
		Thread.sleep(Integer.MAX_VALUE);
	}
}
