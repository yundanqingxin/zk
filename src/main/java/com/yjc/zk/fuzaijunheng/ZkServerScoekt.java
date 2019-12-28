package com.yjc.zk.fuzaijunheng;

import java.net.ServerSocket;
import java.net.Socket;

import org.I0Itec.zkclient.ZkClient;

public class ZkServerScoekt implements Runnable{
	private int port = 18081;
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			regServe();
			System.out.println("Server start port:" + port);
			Socket socket = null;
			while (true) {
				socket = serverSocket.accept();
				new Thread(new ServerHandler(socket)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (Exception e2) {

			}
		}
		
	}
	
	/**
	 *  向ZooKeeper注册当前服务器
	 */
	public void regServe() {
		ZkClient client = new ZkClient("129.211.12.96:2181", 5000, 5000);
		String fuPath = "/number";
		if (!client.exists(fuPath)) {
			client.createPersistent(fuPath);
		}
		String path = "/number/service"+port;
		if (client.exists(path)) {
			client.delete(path);
		}
		client.createEphemeral(path, "127.0.0.1:" + port);
		
		
	}
	
	public ZkServerScoekt(int port) {
		this.port = port;
	}
	
	public static void main(String[] args) {
		int port = 18081;
		ZkServerScoekt server = new ZkServerScoekt(port);
		Thread thread = new Thread(server);
		thread.start();
	}
	

}
