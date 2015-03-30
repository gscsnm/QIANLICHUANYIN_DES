package com.yichang.qianlichuanyin.net;

/**
 * 用来保存控制信息，声音信息不在其内
 * @author Administrator
 * 
 */
public class ControlMessage {
	private int id;
	private int type;//消息的类型
	private String mesgVaL;
    private int fromUserId;//发出包的用户id
    private int toUserId;//目标用户的id
    
	private static int idSeq=1;
	

	public ControlMessage(int type,String mesgVaL,int fromUserId,int toUserId) {
         this.type=type;
         this.mesgVaL=mesgVaL;
         this.fromUserId=fromUserId;
         this.toUserId=toUserId;
	}

	public String getMesgVaL() {
		return mesgVaL;
	}

	public void setMesgVaL(String mesgVaL) {
		this.mesgVaL = mesgVaL;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	public int getToUserId() {
		return toUserId;
	}

	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
