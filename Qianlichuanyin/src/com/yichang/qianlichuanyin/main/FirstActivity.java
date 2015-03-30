package com.yichang.qianlichuanyin.main;

import com.yichang.qianlichuanyin.net.Client;
import com.yichang.qianlichuanyin.net.ClientControl;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;

/**
 * 
 *
 */
public class FirstActivity extends Activity{
	public static String serverIp;//ip是存放在String.xml中的，所以需要定义为静态全局变量
	public static ClientControl control; //控制客户端保持对服务器连接的对象，可以从中获取client
	private Client client;//负责传送消息到客户端以及接受消息的对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ExitBroadcastReceiver exitBroadcastReceiver;
		Log.v("test", "第一个Activity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		initData();
		
		Intent intent=new Intent();
		intent.setClass(FirstActivity.this, LoginActivity.class);
		this.startActivity(intent);
		
		finish();
	}

	/**
	 * 初始化数据，包括获取ip，以及建立连接服务器的client对象
	 */
	private void initData(){
		//初始化参数 服务器ip和端口
		serverIp=Const.ServerIP;
		Log.v("test", "ip"+serverIp);
		
		//启动一个子线程负责连接服务器
		connectServer();
	}
	
	//启动一个子线程负责连接服务器
	private void connectServer(){
	    client=new Client();
	    control=new ClientControl(client);
		Thread thread =new Thread(control);
		thread.start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_first, menu);
		return true;
	}

}
