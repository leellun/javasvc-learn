package com.hhr.zookeeper.curatpr;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.CancelLeadershipException;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class MasterSelectDemo8 {
	static String path = "/zk-book";
	static String leaderPath="/curator_recipes_master_path";

	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.106:2181", 5000, 10000, retryPolicy);
		client.start();
		LeaderSelector selector=new LeaderSelector(client, leaderPath, new LeaderSelectorListener() {
			
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				if(newState==ConnectionState.SUSPENDED||newState==ConnectionState.LOST){
					throw new CancelLeadershipException();
				}
			}
			
			@Override
			public void takeLeadership(CuratorFramework client) throws Exception {
				System.out.println("成为master角色");
				Thread.sleep(3000);
				System.out.println("完成master操作，释放master权利");
			}
		});
		selector.autoRequeue();
		selector.start();
		Thread.sleep(Integer.MAX_VALUE);
	}
}
