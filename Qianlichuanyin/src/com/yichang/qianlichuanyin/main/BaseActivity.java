package com.yichang.qianlichuanyin.main;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.gson.Gson;
import com.yichang.qianlichuanyin.net.Client;
import com.yichang.qianlichuanyin.net.ClientControl;
import com.yichang.qianlichuanyin.net.ControlMessage;
import com.yichang.qianlichuanyin.net.UserInfo;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

/**
 * 基础的页面类
 *
 */
public class BaseActivity extends Activity implements RunBeforeExit {

	private static ClientControl control; // 控制客户端保持对服务器连接的对象，可以从中获取client
	private Client client;// 负责传送消息到客户端以及接受消息的对象
	private Gson gson = new Gson();
	private ExitBroadcastReceiver exitBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("test", "基础Activity初始化数据前");

		initData();
		
		// 设置广播接收器以便统一处理退出事件。
		exitBroadcastReceiver = new ExitBroadcastReceiver();
		exitBroadcastReceiver.setRunBeforeExit(BaseActivity.this);
		
        // 注册广播接收
		Log.v("test", "注册广播");
//		exitBroadcastReceiver = new ExitBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Const.APPLICATIONEXIT);  
        this.registerReceiver(exitBroadcastReceiver, filter);
	}

	@Override
	// 转屏时不重启activity，而是调用onConfigurationChanged(Configuration newConfig)
	public void onConfigurationChanged(Configuration newConfig) {

	}

	public void initData() {
		control = FirstActivity.control;
		if (control == null) {
			Log.v("test", "control为空");
		}
		client = control.getClient();

	}

	/**
	 * 设置目前Activity为此对象，以便通过消息机制通知前台
	 * 
	 * @param activity
	 */
	public void setRunningActivity(MainActivity activity) {
		client.setRunningActivity(activity);
	}

	public ArrayList<UserInfo> getUserInfoList() {
		return client.getUserInfoList();
	}

	/**
	 * 登陆方法
	 * 
	 * @param userName
	 *            登陆界面传来的用户名
	 * @param password
	 *            登陆界面传来的密码
	 */
	public void login(String userName, String password) {
		UserInfo userInfo = new UserInfo(0, userName, password);
		String jsonStr;
		jsonStr = gson.toJson(userInfo);
		ControlMessage contorlMes = new ControlMessage(Const.Login, jsonStr, 0,
				0);
		sendMessageAddThread(contorlMes);
	}

	/**
	 * 发送消息给服务器端
	 */
	public void sendMessage(ControlMessage message) {
		client.sendMessage(message);
	}

	/**
	 * 发送消息给服务器端,主线程发送消息的方法，需要增开一个支线程
	 */
	public void sendMessageAddThread(ControlMessage message) {
		Log.v("test", "发送方法");
		SendMessageThread messageThread = new SendMessageThread(client, message);
		Thread t = new Thread(messageThread);
		t.start();
		// client.sendMessage(message);
	}

	/**
	 * 根据userId获取UserInfo对象的方法
	 * 
	 * @param userId
	 * @return
	 */
	public UserInfo getUserInfoById(int userId) {

		return client.getUserInfoById(userId);
	}

	/**
	 * Activity销毁前可调用此方法保存数据
	 * 
	 * @return
	 */
	public boolean save() {
		boolean bool = true;

		return bool;
	}

	// 获取用户自身的信息
	public UserInfo getMyself() {
		return client.getMyself();
	}

	/**
	 * 程序启动前可调用此方法获取数据
	 * 
	 * @return
	 */
	public boolean load() {
		boolean bool = true;

		return bool;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return true;
		}
		return true;

	}

	/**
	 * 弹出一个询问是否退出的窗口
	 */
	protected void dialog() {
		Builder builder = new Builder(this);
		builder.setMessage("确定离开程序么?");
		builder.setTitle("提示");
		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 退出前的处理
						beforeLogout();
						// 注销登录
						logout();

						// 睡眠以便退出消息的发送
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// 此处应该用广播实现完全退出程序,
						Intent intent = new Intent(); // Itent就是我们要发送的内容
//						intent.putExtra("data", "this is data from broadcast "
//								+ Calendar.getInstance().get(Calendar.SECOND));
						
						//只有和这个action一样的接受者才能接受者才能接收广播
						intent.setAction(Const.APPLICATIONEXIT); 
						Log.v("test", "发送广播");
						sendBroadcast(intent); // 发送广播
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/**
	 * 退出方法
	 */
	public void logout() {
		// 发送到服务器，注销登录
		if (client == null) {
			Log.v("test", "client为空");
		}
		Log.v("test", "注销登录");
		ControlMessage controlMsg = new ControlMessage(Const.Logout, "", 0, 0);
		this.sendMessageAddThread(controlMsg);

	}

	/**
	 * 退出前的处理方法，有些窗口如对话窗口需要对这个方法进行重写
	 * 
	 * @return
	 */
	public boolean beforeLogout() {
		boolean bool = true;

		Log.v("test", "beforeLogout");

		return bool;
	}

	// 利用广播在退出事件之前关闭Activity
	@Override
	public void beforeExit() {
		// TODO Auto-generated method stub
		Log.v("test", "接收到广播");
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//统一在Activity销毁是注销广播的注册
		unregisterReceiver(exitBroadcastReceiver);
		super.onDestroy();
	}

}
