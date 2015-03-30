package com.yichang.chuanyin.server;
/**
 * 记录用户的姓名等基本信息
 * @author Administrator
 *
 */

public class UserInfo {
    private int userId;
    private String userName;
    private String password;
    
  
    /**
     * @return password
     */
    public String getPassword() {
		return password;
	}
    
   
	/**
	 * @param password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 
	 * @param userId
	 * @param userName
	 */
	public UserInfo(int userId,String userName){
    	this.userId=userId;
    	this.userName=userName;
    }
	
	/**
	 * 
	 * @return userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * 
	 * @param userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	/**
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
