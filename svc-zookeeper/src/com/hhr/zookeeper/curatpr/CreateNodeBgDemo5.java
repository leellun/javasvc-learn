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
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class CreateNodeBgDemo5 {
	static String path = "/zk-book/c1";
	static CountDownLatch semaphre = new CountDownLatch(2);
	static ExecutorService tp = Executors.newFixedThreadPool(2);

	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.106:2181", 5000, 10000, retryPolicy);
		client.start();
		System.out.println("Main Thread :" + Thread.currentThread().getName());

		client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL)
				.inBackground(new BackgroundCallback() {

					@Override
					public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
						System.out.println(
								"excutor event[code:" + event.getResultCode() + ",type:" + event.getType().name() + "]");
						System.out.println("Excutor Thread of processResult:" + Thread.currentThread().getName());
						semaphre.countDown();
					}
				}, tp).forPath(path, "init".getBytes());

		client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL)
				.inBackground(new BackgroundCallback() {

					@Override
					public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
						System.out.println(
								"event[code:" + event.getResultCode() + ",type:" + event.getType().name() + "]");
						System.out.println("Thread of processResult:" + Thread.currentThread().getName());
						semaphre.countDown();
					}
				}).forPath(path, "init".getBytes());

		semaphre.await();
		tp.shutdown();

	}
}
