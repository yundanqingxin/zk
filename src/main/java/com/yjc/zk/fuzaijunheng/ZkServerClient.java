package com.yjc.zk.fuzaijunheng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * @author junchao
 * zk实现负载均衡
 * 原理：
 *  服务端将启动的服务注册到zk注册中心中，采用临时结点。客户端从zk结点
 *  上获取最新服务结点信息。
 *  下面使用轮训算法进行分配服务
 * 
 * 
 *
 */
public class ZkServerClient {
	public static List<String> listServer = new ArrayList<String>();

	public static void main(String[] args) {
		initServer();
		ZkServerClient 	client= new ZkServerClient();
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String name;
			try {
				name = console.readLine();
				if ("exit".equals(name)) {
					System.exit(0);
				}
				client.send(name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 注册所有server
	public static void initServer() {
		String path ="/number";
		listServer.clear();
		
		ZkClient client = new ZkClient("129.211.12.96:2181", 5000, 5000);
		List<String> children = client.getChildren("/number");
		for (String p : children) {
			listServer.add((String) client.readData(path + "/" + p));
		}
		System.out.println("####handleChildChange()####listServer:" + listServer.toString());
		//订阅结点变化事件
		client.subscribeChildChanges(path, new IZkChildListener() {
			
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				listServer.clear();
				for (String p : children) {
					listServer.add((String) client.readData(path + "/" + p));
				}
				System.out.println("####handleChildChange()####listServer:" + listServer.toString());
			}
		});
		
		/*listServer.clear();
		listServer.add("127.0.0.1:18080");*/
	}

	//请求次数
	private static int count = 1;
	
	// 获取当前server信息
	public static String getServer() {
		int serveCount = listServer.size();
		++count;
		return listServer.get(count%serveCount);
	}
	
	public void send(String name) {

		String server = ZkServerClient.getServer();
		String[] cfg = server.split(":");

		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			socket = new Socket(cfg[0], Integer.parseInt(cfg[1]));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			out.println(name);
			while (true) {
				String resp = in.readLine();
				if (resp == null)
					break;
				else if (resp.length() > 0) {
					System.out.println("Receive : " + resp);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
