package com.yichang.chuanyin.server;

import java.net.Socket;

/**
 * 用户对象，包含了个人信息和网络连接信息
 * @author Administrator
 *
 */
public class User {
    private int userId;//用户id
    private UserInfo userInfo;//用户信息，记录用户的姓名等基本信息
	private UserConInfo userConInfo;//用户控制信息，记录用户连接端口等信息。
 
	/*
	 * 构造函数
	 */
	public User(int userId,String userName,Socket socket){
		this.userId=userId;
		this.userInfo=new UserInfo(userId,userName);
		this.userConInfo=new UserConInfo(userId,socket);;
	}
	
    
    public int getUserId() {
		return userId;
	}
	public void setUserId(int userID) {
		this.userId = userId;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public UserConInfo getUserConInfo() {
		return userConInfo;
	}
	public void setUserConInfo(UserConInfo userSocket) {
		this.userConInfo = userSocket;
	}
	
	public String getUserName() {
		return userInfo.getUserName();
	}
	public void setUserName(String userName) {
		this.userInfo.setUserName(userName);
	}
}
