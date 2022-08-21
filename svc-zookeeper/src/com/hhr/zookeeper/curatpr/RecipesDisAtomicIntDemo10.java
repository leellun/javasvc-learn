package com.hhr.zookeeper.curatpr;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class RecipesDisAtomicIntDemo10 {
	static String path = "/zk-book";
	static List<String> lock_path = new ArrayList<>();
	static String leaderPath="/curator_recipes_master_path";
	
	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.106:2181", 5000, 10000, retryPolicy);
		client.start();
		lock_path.add("/curator_recipes_lock_path");
		DistributedAtomicInteger atomicInteger=new DistributedAtomicInteger(client, path, retryPolicy);
		
		AtomicValue<Integer> rc=atomicInteger.add(8);
		System.out.println("result:"+rc.succeeded());
	}
}
