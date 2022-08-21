package com.hhr.zookeeper.curatpr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.CancelLeadershipException;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class RecipesLockDemo9 {
	static String path = "/zk-book";
	static List<String> lock_path = new ArrayList<>();
	static String leaderPath="/curator_recipes_master_path";
	
	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.106:2181", 5000, 10000, retryPolicy);
		client.start();
		lock_path.add("/curator_recipes_lock_path");
		final InterProcessMultiLock lock=new InterProcessMultiLock(client, lock_path);
		final CountDownLatch downLatch=new CountDownLatch(1);
		for(int i=0;i<30;i++){
			new Thread(new Runnable() {
				public void run() {
					try {
						downLatch.await();
						lock.acquire();
					} catch (Exception e) {
						e.printStackTrace();
					}
					SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss|SSS");
					String orderNo=sdf.format(new Date());
					System.out.println("生成的订单是:"+orderNo);
					try{
						lock.release();
					}catch (Exception e) {
					}
				}
			}).start();
		}
		downLatch.countDown();
	}
}
