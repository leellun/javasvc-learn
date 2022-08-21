package com.hhr.zookeeper.curatpr;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class RecipesBarrierDemo12 {
	static String barrier_path = "/curator_recipes_barrier_path";
	static List<String> lock_path = new ArrayList<>();
	static String leaderPath="/curator_recipes_master_path";
	
	public static void main(String[] args) throws Exception {
		
		for(int i=0;i<5;i++){
			new Thread(new Runnable() {
				public void run(){
					try{
						RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
						CuratorFramework client=CuratorFrameworkFactory.builder().connectString("192.168.10.106:2181").connectionTimeoutMs(10000).sessionTimeoutMs(5000).retryPolicy(retryPolicy).build();
						client.start();
						DistributedDoubleBarrier barrier=new DistributedDoubleBarrier(client, barrier_path,5);
						System.out.println(Thread.currentThread().getName()+"号barrier设置");
						barrier.enter();
						System.out.println("启动了..."+Thread.currentThread().getName());
						Thread.sleep(Math.round(Math.random()*3000));
						barrier.leave();
						System.out.println("退出了..."+Thread.currentThread().getName());
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
			}).start();
		}
	}
}
