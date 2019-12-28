package com.yjc.zk.election;

/**
 * @author junchao
 * zk实现选举策略
 * ：谁先在zookeeper上创建临时节点，谁就为主节点，
 * 如果主节点挂了，那么就从剩下的从节点中（事件监听到主挂了）开始比赛，
 * 谁先创建临时节点，谁就为主，（就算第一个主的活了，也只能为从，
 * 服从新主结点的指挥）
 * 
 * 
 * 
 *
 */
public class Election {

}
