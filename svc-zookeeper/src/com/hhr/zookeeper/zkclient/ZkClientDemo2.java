package com.hhr.zookeeper.zkclient;

import java.util.List;

import com.github.zkclient.IZkChildListener;
import com.github.zkclient.ZkClient;

public class ZkClientDemo2 {
	public static void main(String[] args) throws Exception {
		String path ="/zk-book";
		ZkClient zkClient=new ZkClient("192.168.10.106:2181", 15000);
		System.out.println("Zookeeper session established");
		zkClient.subscribeChildChanges(path, new IZkChildListener() {
			
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				System.out.println(parentPath+" 's child changed,currentChilds:"+currentChilds);
			}
		});
		zkClient.createPersistent(path);
		Thread.sleep(1000);
		zkClient.createPersistent(path+"/c1");
		Thread.sleep(1000);
		zkClient.delete(path+"/c1");
		Thread.sleep(1000);
		zkClient.delete(path);
		Thread.sleep(Integer.MAX_VALUE);
	}
}
