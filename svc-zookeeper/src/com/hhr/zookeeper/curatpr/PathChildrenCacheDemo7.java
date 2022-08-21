package com.hhr.zookeeper.curatpr;

import java.awt.Event;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.google.common.base.CaseFormat;

public class PathChildrenCacheDemo7 {
	static String path = "/zk-book";
	static CountDownLatch semaphre = new CountDownLatch(1);
	static ExecutorService tp = Executors.newFixedThreadPool(2);

	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.106:2181", 5000, 10000, retryPolicy);
		client.start();
		PathChildrenCache cache=new PathChildrenCache(client, path, true);
		cache.start();
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				// TODO Auto-generated method stub
				switch(event.getType()){
				case CHILD_ADDED:
					System.out.println("CHILD_ADDED,"+event.getData().getPath());
					break;
				case CHILD_REMOVED:
					System.out.println("CHILD_REMOVED,"+event.getData().getPath());
					
					break;
				case CHILD_UPDATED:
					System.out.println("CHILD_UPDATED,"+event.getData().getPath());
					break;
				default: break;
				}
			}
		});
		client.create().withMode(CreateMode.PERSISTENT).forPath(path);
		Thread.sleep(1000);
		
		client.create().withMode(CreateMode.EPHEMERAL).forPath(path+"/c1");
		Thread.sleep(1000);
		
		client.delete().forPath(path+"/c1");
		Thread.sleep(1000);
		client.delete().forPath(path);
		semaphre.await();
		tp.shutdown();

	}
}
