package com.yichang.qianlichuanyin.main;

import com.yichang.qianlichuanyin.net.Client;
import com.yichang.qianlichuanyin.net.ControlMessage;

/**
 * 发送消息线程
 *
 */
public class SendMessageThread  implements Runnable{

	private Client client;
	private ControlMessage message;
	
	
	public SendMessageThread(Client client,ControlMessage message){
		this.client=client;
		this.message=message;
	}
	
	//发送消息给client
	@Override
	public void run() {
		client.sendMessage(message);
	}
}
