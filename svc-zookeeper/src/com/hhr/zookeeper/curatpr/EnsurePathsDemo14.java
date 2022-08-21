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
import org.apache.curator.utils.EnsurePath;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.utils.ZKPaths.PathAndNode;
import org.apache.zookeeper.ZooKeeper;

public class EnsurePathsDemo14 {
	static String path = "/curator_zkpath/c1";
	
	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client=CuratorFrameworkFactory.builder().connectString("192.168.10.106:2181").connectionTimeoutMs(10000).sessionTimeoutMs(5000).retryPolicy(retryPolicy).build();
		client.start();
		
		client.usingNamespace("curator_zkpath");
		EnsurePath ensurePath=new EnsurePath(path);
		ensurePath.ensure(client.getZookeeperClient());

		EnsurePath ensurePath2=client.newNamespaceAwareEnsurePath("/c1");
		ensurePath2.ensure(client.getZookeeperClient());
	}
}
