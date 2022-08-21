package com.hhr.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.chainsaw.Main;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

public class ZookeeperAuthDemo implements Watcher{
	private static String PATH="/zk-book-auth_test";
	private static CountDownLatch countDownLatch=new CountDownLatch(1);
	public static void main(String[] args) throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper("192.168.10.106:2181", 5000, new ZookeeperAuthDemo());
		countDownLatch.await();
		zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
//		zooKeeper.delete(PATH, -1);
		zooKeeper.create(PATH, "init".getBytes(),Ids.CREATOR_ALL_ACL,CreateMode.EPHEMERAL);
		countDownLatch=new CountDownLatch(1);
		ZooKeeper zooKeeper2 = new ZooKeeper("192.168.10.106:2181", 5000, new ZookeeperAuthDemo());
		countDownLatch.await();
		zooKeeper2.addAuthInfo("digest", "foo:true".getBytes());
		System.out.println(new String(zooKeeper2.getData(PATH, false, null)));

		ZooKeeper zooKeeper3 = new ZooKeeper("192.168.10.106:2181", 5000, new ZookeeperAuthDemo());
		countDownLatch=new CountDownLatch(1);
		countDownLatch.await();
//		zooKeeper3.addAuthInfo("digest", "foo:false".getBytes());
		zooKeeper3.getData(PATH, false, null);
		
		
	}
	@Override
	public void process(WatchedEvent event) {
		if(event.getState()==KeeperState.SyncConnected){
			if(event.getPath()==null&&event.getType()==EventType.None){
				countDownLatch.countDown();
			}
		}
	}
}
