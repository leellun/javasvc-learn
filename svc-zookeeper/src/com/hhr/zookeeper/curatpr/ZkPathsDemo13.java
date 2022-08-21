package com.hhr.zookeeper.curatpr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.utils.ZKPaths.PathAndNode;
import org.apache.zookeeper.ZooKeeper;

public class ZkPathsDemo13 {
	static String path = "/curator_zkpath";
	
	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client=CuratorFrameworkFactory.builder().connectString("192.168.10.106:2181,192.168.10.106:2182,192.168.10.106:2183").connectionTimeoutMs(10000).sessionTimeoutMs(5000).retryPolicy(retryPolicy).build();
		client.start();
		
		ZooKeeper zooKeeper=client.getZookeeperClient().getZooKeeper();
		System.out.println(ZKPaths.fixForNamespace(path, "/sub"));
		System.out.println(ZKPaths.makePath(path, "/sub"));
	
		System.out.println(ZKPaths.getNodeFromPath(path+"/sub1"));
	
		PathAndNode pn=ZKPaths.getPathAndNode(path+"/sub1");
		System.out.println(pn.getPath());
		System.out.println(pn.getNode());
		
		String dir1=path+"/child1";
		String dir2=path+"/child2";
		ZKPaths.mkdirs(zooKeeper, dir1);
		ZKPaths.mkdirs(zooKeeper, dir2);
		
		System.out.println(ZKPaths.getSortedChildren(zooKeeper, path));
		
		ZKPaths.deleteChildren(zooKeeper, path, true);
		
		Thread.sleep(Integer.MAX_VALUE);
	}
}
