package com.yjc.zk.shangxiaxian;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class DistributeClient {

	private String connectString="129.211.12.96:2181";
	private int sessionTimeout=2000;
	private ZooKeeper zk = null;
	String parentNode ="/servers";

	/**
	 * 获取客户端连接
	 * @throws Exception
	 */
	public void getConnect() throws Exception {
		zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				try {
					getServerList();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public void getServerList() throws Exception {
		// 获取服务器子节点信息，并且对父节点进行监听
		List<String> children = zk.getChildren(parentNode, true);
		List<String> servers  = new ArrayList<>();
		
		for (String child : children) {
			byte[] data = zk.getData(parentNode+"/"+child, false, null);
			servers.add(new String(data));
		}
		
		System.out.println(servers);
	
	}
	
	// 业务功能
	public void business() throws Exception {
		System.out.println("client is working ...");
		Thread.sleep(Long.MAX_VALUE);
	}
	
	public static void main(String[] args) throws Exception {
		//获取zk链接
		DistributeClient distributeClient = new DistributeClient();
		distributeClient.getConnect();
		//获取servers的子节点信息，从中获取服务器信息列表
		distributeClient.getServerList();
		// 业务进程启动
		distributeClient.business();
		
	}
	
	
}
