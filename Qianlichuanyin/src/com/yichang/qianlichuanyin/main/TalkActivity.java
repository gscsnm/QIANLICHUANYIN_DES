package com.yichang.qianlichuanyin.main;

import com.yichang.qianlichuanyin.net.ControlMessage;
import android.media.AudioRecord;
import android.os.Bundle;
import android.os.Message;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 录音和播放的配置可以参考 http://blog.csdn.net/hellofeiya/article/details/8968534
 * 
 * @author Administrator
 * 通话页面 * 
 * 
 */
public class TalkActivity extends BaseActivity implements MainActivity {

	// 启动的两种模式，一种是立刻启动线程录音进行通话，一种是等待对方接受通话
	public static int TALK_MODEL_NOW = 1;
	public static int TALK_MODEL_WAIT = 2;

	private ImageView stopTalk;
	private int talkModel;
	//private int fromUserId;
	private int toUserId;
	private String toUserName;
	// private Client client;
	//private AudioRecord record = null;
	// 录音的线程对象，以及启动线程的对象
	private RecordThread recordThread;

	private TrackThread trackThread;

	/* 
	 * 初始化
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_talk);

		// 获取用户名和用户id
		Intent intent = getIntent();

		// 启动模式，如果是TALK_MODEL_WAIT就要等对方接受通话才能开始录音
		talkModel = intent.getIntExtra("TALK_MODEL", 0);
		toUserId = intent.getIntExtra("toUserId", 0);
		toUserName = intent.getStringExtra("toUserName");

		Log.v("test", "用户名和id" + toUserId + toUserName);

		// 设置目前的activity ，这样客户端通信对象的发送消息后，handler可以调用到本activity的handleMessage方法
		setRunningActivity(this);

		// 初始化录音线程
		recordThread = new RecordThread(toUserId);

		// 初始化播音线程
		trackThread = new TrackThread();

		// 设置按钮事件
		stopTalk = (ImageView) findViewById(R.id.stop_talk);
		stopTalk.setOnClickListener(new StopTalkListener());
	}

	/**
	 * 启动播放声音的线程
	 */
	public void startTrack() {
		// 启动一个线程播放声音
		Thread thread = new Thread(trackThread);
		thread.start();
	}

	/**
	 * 启动录音的线程
	 */
	public void startRecord() {
		// 启动一个线程录音并发送
		Thread thread = new Thread(recordThread);
		thread.start();
	}

	/**
	 * 在此方法中启动录音
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		if (talkModel == this.TALK_MODEL_NOW) {
			// 开始录音
			startRecord();
		}else{
			//运行过一次onResume后就不用等待接受通话了，所以要转变模式
			talkModel = this.TALK_MODEL_NOW;
		}
		super.onResume();
	}

	@Override
	protected void onStop() {

		// TODO Auto-generated method stub
		// 停下播放
		trackThread.stopTrack();

		// 停下录音线程的运行
		recordThread.stopRecord();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_talk, menu);
		return true;
	}

	//根据不同情况进行界面的调整
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		// 已经存储了足够的语音包，可以播放了
		if (msg.what == Const.handlerTrack) {
			startTrack();
		} else if (msg.what == Const.handlerRefuseTall) {
			// 拒绝通话则销毁此界面
			Toast.makeText(getApplicationContext(), "对方拒绝了通话",
					Toast.LENGTH_SHORT).show();
			finish();
		} else if (msg.what == Const.handlerStopTall) {
			// 终止通话
			Toast.makeText(getApplicationContext(), "对方已中断通话",
					Toast.LENGTH_SHORT).show();
			finish();
		} else if (msg.what == Const.handlerPromiseTall) {
			Log.v("test", "接收到同意通话的请求");
			Toast.makeText(getApplicationContext(), "对方已经接受通话，可以开始对话了",
					Toast.LENGTH_SHORT).show();

			// 开始录音
			startRecord();
		}
	}

	class StopTalkListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			stopTalk();
			// 停下播放
//			trackThread.stopTrack();
//
//			// 停下录音线程的运行
//			recordThread.stopRecord();
			// 销毁Activity，在onStop方法会终止录音和播放线程

			finish();
		}
	}
	
	private void stopTalk(){
		// 终止通话
		ControlMessage conMsg = new ControlMessage(Const.stopTalk, "", 0,
				toUserId);
		sendMessageAddThread(conMsg);
		
		Log.v("test", "发送停止通话要求");
	}

	@Override
	public boolean beforeLogout() {
		Log.v("test", "TalkActivity--beforeLogout");
		boolean bool;
		//发送到服务器，注销登录
		stopTalk();
		
		return super.beforeLogout();
	}
	
	

}
