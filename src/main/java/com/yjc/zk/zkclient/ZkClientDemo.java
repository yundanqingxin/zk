package com.yjc.zk.zkclient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

/**
 * @author junchao
 * Zkclient 的使用
 */
public class ZkClientDemo {
	
	//地址
	private static final String ADDRESS="129.211.12.96:2181";
	//链接超时时间
	private static final int SESSION_OUTTIME=5000;
	
	static ZkClient zkClient = null;
	
	/**
	 * @param address
	 * @param time
	 * 创建链接
	 */
	public static void createContion(String address,int time) {
		zkClient =  new ZkClient(address,time,3000,new SerializableSerializer());
		System.out.println("链接成功");
	}
	
	/**
	 * @param path
	 * @param date
	 * 创建节点，不设置值
	 */
	public static void add(String path) {
		//创建节点，不设置值
		//zkClient.createEphemeral(path); 创建临时节点
		zkClient.createPersistent(path);//创建持久化节点
		System.out.println("创建"+path+"成功");
	}
	
	/**
	 * @param path
	 * @param date
	 * 创建节点，设置值
	 */
	public static void addDate(String path,String date) {
		//创建节点，设置值
		//zkClient.createEphemeral(path,date);创建临时节点
		zkClient.createPersistent(path, date);//创建持久化节点
		System.out.println("创建"+path+"成功,date"+date);
	}
	
	/**
	 * @param path
	 * @param date
	 * 创建子节点
	 */
	public static void addZiNote(String path) {
		//创建节点
		//zkClient.createEphemeral(path,true);
		zkClient.createPersistent(path, true);
		System.out.println(" 创建子节点成功");
	}
	
	/**
	 * @param path
	 * @param date
	 * 写数据，即更新数据，会update,不会append
	 */
	public static void updateNote(String path,String date) {
		//写数据，即更新数据，会update,不会append
		zkClient.writeData(path, date);
		System.out.println("更新数据成功");
	}
	
	/**
	 * @param path
	 * @param date
	 * 写一个对象，要序列化
	 */
	public static void addObject(String path,String date) {
		Map<String, String> map = new HashMap<>();
		map.put("name", date);
		zkClient.create(path, map, CreateMode.PERSISTENT);
	}
	
	/**
	 * @param path
	 * 删除结点
	 */
	public static void delete(String path) {
		zkClient.delete(path);
	}
	
	/**
	 * @param path
	 * 递归删除结点和子节点
	 */
	public static void deleteRecursive(String path) {
		zkClient.deleteRecursive(path);
	}
	
	public static Object getDate(String path) {
		Object readData = zkClient.readData(path);
		System.out.println("获取数据"+readData);
		
		return readData;
	}
	
	/**
	 * @param path
	 * @return
	 * 判断节点是否存在
	 */
	public static boolean isExists(String path) {
		boolean exists = zkClient.exists(path);
		return exists;
	}
	
	
	/**
	 * 获取该结点下的子节点
	 */
	public static void getList(String path) {
		 List<String> l = zkClient.getChildren(path);
         for(String s : l)
         {
             System.out.println(s);
         }
	}
	
	/**
	 * @param path
	 * 监听节点的变化，节点增加，删除，减少
	 */
	public static void listenerNote(String path) {
		zkClient.subscribeChildChanges(path, new IZkChildListener() {
			
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				 System.out.println("parentPath = " + parentPath);
			}
		});
	}
	
	/**
	 * @param path
	 * 监听节点数据的变化，子节点数据变化不会监听到
	 */
	public static void listenerNoteDate(String path) {
		zkClient.subscribeDataChanges(path, new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("节点删除数据");
				
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println("节点改变数据");
				
			}
		});
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*String path ="/yjc";*/
		
		ZkClientDemo.createContion(ADDRESS, SESSION_OUTTIME);
		
		//ZkClientDemo.add(path);
		
		/*String path ="/yj";
		String date ="555";
		ZkClientDemo.addDate(path, date);*/
		
		/*String path ="/yj";
		String date ="555666999";
		ZkClientDemo.updateNote(path, date);*/
		
		/*String path ="/yj/lisi";
		ZkClientDemo.addZiNote(path);*/
		
		String path ="/yddd";
		/*String date ="555666999";
		ZkClientDemo.addObject(path, date);*/
		
		//ZkClientDemo.getDate(path);
		
		ZkClientDemo.getList(path);
		
		ZkClientDemo.listenerNote(path);
		ZkClientDemo.listenerNoteDate(path);
		
		if (zkClient!=null) {
			zkClient.close();
		}
		
		
	}
	
	

}
