package com.hhr.zookeeper.zkclient;

import java.util.List;

import com.github.zkclient.IZkChildListener;
import com.github.zkclient.IZkDataListener;
import com.github.zkclient.ZkClient;
import com.github.zkclient.ZkConnection;

public class GetDataDemo3 {
	public static void main(String[] args) throws Exception {
		String path ="/zk-book";
		ZkClient zkClient=new ZkClient("192.168.10.106:2181", 15000);
		zkClient.subscribeDataChanges(path, new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("Node "+dataPath+" deleted.");
			}
			
			@Override
			public void handleDataChange(String dataPath, byte[] data) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("Node "+dataPath+" changed,new data:"+new String(data));;
			}
		});
		zkClient.createEphemeral(path,"132".getBytes());
		System.out.println(new String(zkClient.readData(path)));
		zkClient.writeData(path, "456".getBytes());
		Thread.sleep(1000);
		zkClient.delete(path);
		Thread.sleep(Integer.MAX_VALUE);
		
	}
}
