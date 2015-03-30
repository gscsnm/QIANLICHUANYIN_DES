package com.yichang.qianlichuanyin.net;
/**
 * 记录用户的姓名等基本信息
 * @author Administrator
 *
 */
public class UserInfo {
    private int userId;
    private String userName;
    private String password;
    
	public UserInfo(int userId,String userName){
    	this.userId=userId;
    	this.userName=userName;
    }
	
	//增加密码
	public UserInfo(int userId,String userName,String password){
    	this.userId=userId;
    	this.userName=userName;
    	this.password=password;
    }
	
    public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
