package com.yichang.chuanyin.server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

/**
 * 服务器类，整个服务器端的核心类
 * 
 * @author Administrator
 * 
 */
public class Server implements Runnable {

	InetAddress addr;
	/*
	 * ServerSocket将使Server端的程序处于等待状态，
	 * 程序将一直阻塞直到捕捉到一个来自Client端的请求，
	 * 并返回一个用于与该Client通信的Socket对象Link-Socket。
	 * 此后Server程序只要向这个Socket对象读写数据，就可以实现向远端的Client读写数据。
	 * 结束监听时，关闭ServerSocket对象：
	 */
	ServerSocket se;
	int userId = 1; // 记录用户序号
	// User userNow; //现在选定进行交谈的User
	User user; // 用来传递参数到线程的对象
	ArrayList<User> userList = new ArrayList<User>(); // 用户对象，User对象是包含了个人信息和网络连接信息的对象
	ArrayList<UserInfo> userInfoList = new ArrayList<UserInfo>(); // 用户信息对象，UserInfo对象是包含了个人信息的对象
	private int serverPort = Const.ServerPort;	//服务器端口
	private Gson gson = new Gson();//Gson类，用来To Json转换

	/*
	 * 构造函数
	 * 启动服务器
	 */
	public Server() throws Exception {
		// 开始监听
		addr = InetAddress.getByName("");
		se = new ServerSocket(serverPort);
		System.out.println("服务器启动...");

	}

	/*
	 * 发消息给所有用户
	 */
	public void setToAll(ControlMessage mesg) {
		User user;
		ObjectOutputStream out;
		for (int i = 0; i < userList.size(); i++) {
			user = userList.get(i);
			out = user.getUserConInfo().getOut();
			if (user != null && !user.getUserConInfo().getSo().isClosed()) {
				setMessage(mesg, out);		//调用setMessage 发送消息给用户
			}

		}
	}

	/*
	 * 发送信息给一个用户,根据mesg中的id定位用户，获取输出流
	 */
	private void setMessage(ControlMessage mesg) {
		int userId;
		User user = null;
		userId = mesg.getToUserId();
		ObjectOutputStream out = null;
		for (int i = 0; i < userList.size(); i++) {
			user = userList.get(i);
			if (user.getUserId() == userId) {
				out = user.getUserConInfo().getOut();
				break;
			}
		}
		if (user != null && !user.getUserConInfo().getSo().isClosed()) {
			setMessage(mesg, out);
		}
	}

	/*
	 * 发送信息给一个已经获取到输出流out的用户
	 */
	private void setMessage(ControlMessage mesg, ObjectOutputStream out) {
		String jsonStr = gson.toJson(mesg);//将mesg转换为Json
		try {
			// 将对象转化为json字符串发送出去
			if (out == null) {
				System.out.println("out对象为空" + userId);
			} else {
				System.out.println("发送数据");
				out.writeUTF(jsonStr); 
				out.flush();
			}
		} catch (Exception ex) {
			Logger.getLogger(Server.class.getName())
					.log(Level.SEVERE, null, ex);
		}

	}

	/*
	 * 与客户端进行通讯
	 */
	public void connetUser() {
		Thread t; //定义线程
		Socket so;//socket
		User user;//用户
		String username;
		int userId = this.userId;// 获取id并将id加1
		this.userId++;
		try {
			// 阻塞函数，等待客户端连接
			so = se.accept();//有客户端连接，获取socket对象
			//创建新用户
			// username = "User" + userId;
			user = new User(userId, "", so);// 新建user对象

			System.out.println("有客户端连接...,对方ip为"
					+ so.getInetAddress().getHostAddress());
			this.user = user; // 以全局变量传递参数到线程

			// 新建一个线程接收信息
			t = new Thread(this);
			t.start();
		} catch (Exception ex) {
			Logger.getLogger(Server.class.getName())
					.log(Level.SEVERE, null, ex);
		}
	}

	public void run() {
		String username;
		String mesgVal;

		User user = this.user;  //获得user
		username = user.getUserInfo().getUserName();  //活动username
		int userId = user.getUserId();//获得userid

		ControlMessage mesg;
		ObjectInputStream in = user.getUserConInfo().getIn(); // 获取跟此用户通讯的流对象
		ObjectOutputStream out = user.getUserConInfo().getOut();

		String jsonStr;
		System.out.println("开始客户端接收消息");

		try {
			while (true && in!=null) {
				Thread.sleep(20);//线程睡眠20ms
				String mesgStr = in.readUTF();
				// in.read

				mesg = gson.fromJson(mesgStr, ControlMessage.class);//使用gson把json解析
				mesgVal = mesg.getMesgVaL();//获得mesgval

				System.out.println("接收到数据" + mesg.getType());

				int type = mesg.getType();

				// 文字消息 或 对话请求  或   接受对话请求   或    拒绝用户对话请求的消息
				if (	   type == Const.textMessage 
						|| type == Const.requestTalk
						|| type == Const.promiseTalk
						|| type == Const.refuseTalk 
						|| type == Const.stopTalk) {
					System.out.println(mesg.getFromUserId() + "to"
							+ mesg.getToUserId() + "  :" + type
							+ mesg.getMesgVaL());
					setMessage(mesg);
				} else if (mesg.getType() == Const.soundMessage) {

					// System.out.println("test接收到声音信息");
					this.setMessage(mesg);

				} else if (mesg.getType() == Const.Login) {

					// 获取用户发送过来的密码和用户名，然后校验，校验通过则回复信息，不通过则回复密码和用户名有错
					UserInfo userInfo_client = gson.fromJson(mesgVal,
							UserInfo.class);
					UserInfo userInfo_server = user.getUserInfo();

					// 校验用户名和密码，如果为true则为通过校验--------bug
					boolean bool = checkPassword(userInfo_client.getUserName(),
							userInfo_client.getPassword());

					if (bool) {
						// 更新服务器端上的用户信息
						userInfo_server.setUserName(userInfo_client.getUserName());
						userInfo_server.setPassword(userInfo_client.getPassword());

						// 发送申请链接的用户自己的信息，包括用户名，id等，服务器发出的消息，fromUserID直接用0
						jsonStr = gson.toJson(userInfo_server);
						mesg = new ControlMessage(Const.userInfo, jsonStr, 0,
								userId);
						this.setMessage(mesg, out);

						// 发送目前在线的用户信息列表
						jsonStr = gson.toJson(userInfoList);
						mesg = new ControlMessage(Const.userInfoList, jsonStr,
								0, userId);
						this.setMessage(mesg, out);

						// 发送新用户的UDP信息给其他用户
						jsonStr = gson.toJson(userInfo_server);
						mesg = new ControlMessage(Const.addUser, jsonStr, 0, 0);
						setToAll(mesg);

						addUser(user);// 添加user到列表中
					} else {
						// 登录失败
						jsonStr = gson.toJson(userInfo_server);
						mesg = new ControlMessage(Const.Login_Fail, jsonStr, 0,
								userId);
						this.setMessage(mesg, out);

					}
				} else if (mesg.getType() == Const.Logout) {
					removeUser(user);

					colseConect(user);
					in = null;
					out = null;

					break;
				}
			}

		} catch (Exception ex) {

		 if (!user.getUserConInfo().getSo().isClosed()) {
			ex.printStackTrace();
			System.out.print(ex.getMessage());
			System.out.print("出错了，有异常");
			
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }else{
			 System.out.print("连接已关闭");
		 }
		}
	}

	/**
	 * 中断跟用户的tcp连接
	 * 
	 * @param user
	 */
	private void colseConect(User user) {
		try {
			user.getUserConInfo().getSo().close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean checkPassword(String userName, String password) {
		boolean bool = true;

		return bool;
	}

	/**
	 * 将用户对象增加到列表中
	 */
	private void addUser(User user) {
		// 添加user到列表中
		userInfoList.add(user.getUserInfo());
		userList.add(user);
		System.out.println("增加用户，用户名为" + user.getUserInfo().getUserName());
	}

	/**
	 * 有用户下线，通知其他用户
	 * 
	 * @param user
	 */
	public void removeUser(User user) {
		// 删除user对象，并同时删除用户信息列表中的对象
		userInfoList.remove(user.getUserInfo());
		userList.remove(user);

		String jsonStr;
		jsonStr = gson.toJson(user.getUserInfo());
		ControlMessage mes = new ControlMessage(Const.removeUser, jsonStr, 0, 0);
		setToAll(mes);//通知所有人
	}

}
