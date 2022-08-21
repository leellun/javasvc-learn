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

public class ZookeeperExistsDemo implements Watcher{
	private static CountDownLatch connectedSemaphore=new CountDownLatch(1);
	private static ZooKeeper zk;
	public static void main(String[] args) throws Exception{
		String path="/zk-book";
		zk=new ZooKeeper("192.168.10.106:2181", 5000, new ZookeeperExistsDemo());
		connectedSemaphore.await();
		
		zk.exists(path, true);
		zk.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
		zk.setData(path, "1233".getBytes(), -1);
		zk.exists(path+"/c1", true);
		zk.create(path+"/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		zk.delete(path+"/c1", -1);
		zk.delete(path, -1);
		
		Thread.sleep(Integer.MAX_VALUE);
		
	}
	@Override
	public void process(WatchedEvent event) {
		try {
			if(KeeperState.SyncConnected==event.getState()){
				if(EventType.None==event.getType()&&event.getPath()==null){
					connectedSemaphore.countDown();
				}else if(EventType.NodeCreated==event.getType()){
					System.out.println("Node("+event.getPath()+") created");
					zk.exists(event.getPath(), true);
				}else if(EventType.NodeDeleted==event.getType()){
					System.out.println("Node("+event.getPath()+") deleted");
					zk.exists(event.getPath(), true);
				}else if(EventType.NodeDataChanged==event.getType()){
					System.out.println("Node("+event.getPath()+") datachanged");
				}
			}
		} catch (Exception e) {
		}
	}

}
