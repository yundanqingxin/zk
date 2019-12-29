package com.yjc.zk.shangxiaxian;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class DistributeServer {

	
	private String connectString="129.211.12.96:2181";
	private int sessionTimeout=2000;
	private ZooKeeper zk = null;

	/**
	 * 获取客户端连接
	 * @throws Exception
	 */
	public void getConnect() throws Exception {
		zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				//收到事件通知后的回调函数（用户业务逻辑）
				System.out.println(event.getType() + "--" + event.getPath());
			}
		});
	}
	
	String parentNode ="/servers";
	public void registServer(String hostName) throws Exception {
		if (zk.exists(parentNode, false)==null) {
			zk.create(parentNode,parentNode.getBytes(), Ids.OPEN_ACL_UNSAFE, 
					CreateMode.PERSISTENT);
		}
		
		String create = zk.create(parentNode+"/server", hostName.getBytes(), Ids.OPEN_ACL_UNSAFE, 
				CreateMode.EPHEMERAL_SEQUENTIAL);
		
		System.out.println(hostName+"---"+create);
		
	}
	
	
	/**
	 * 
	 * @param hostname
	 * @throws Exception
	 * 具体业务功能
	 */
	public void business(String hostname) throws Exception {
		System.out.println(hostname+" is working ...");
		Thread.sleep(Long.MAX_VALUE);
	}
	
	public static void main(String[] args) throws Exception {
		String hostname="127.0.0.1:80812";
		//1.获取zk链接
		DistributeServer distributeServer = new DistributeServer();
		distributeServer.getConnect();
		//2.利用zk链接注册服务器信息
		distributeServer.registServer(hostname);
		//3.具体的业务
		distributeServer.business(hostname);
		
	}
	
}
