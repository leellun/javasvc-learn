package com.hhr.zookeeper.zkclient;

import com.github.zkclient.ZkClient;
import com.github.zkclient.ZkConnection;

public class ZkClientDemo1 {
	public static void main(String[] args) {
		ZkClient zkClient=new ZkClient("192.168.10.106:2181", 15000);
		System.out.println("Zookeeper session established");
	}
}
