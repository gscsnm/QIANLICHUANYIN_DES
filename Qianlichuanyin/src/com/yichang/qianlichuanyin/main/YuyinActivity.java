package com.yichang.qianlichuanyin.main;

import java.util.ArrayList;
import java.util.HashMap;
import com.yichang.qianlichuanyin.main.R;
import com.yichang.qianlichuanyin.net.ControlMessage;
import com.yichang.qianlichuanyin.net.UserInfo;
import com.yichang.qianlichuanyin.view.ImageTextButton;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 用户列表页面
 *
 */
@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class YuyinActivity extends BaseActivity implements MainActivity {

	ImageTextButton mImgTxtBtnMy;
	ImageTextButton mImgTxtBtnTalk;
	// private ClientControl control; //控制客户端保持对服务器连接的对象，可以从中获取client
	// private Client client;//负责传送消息到客户端以及接受消息的对象
	ListView mUserListView;
	// 创建ArrayList对象 并添加数据
	ArrayList<HashMap<String, String>> userNameMapList = new ArrayList<HashMap<String, String>>();

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("test", "建立YuyinActivity");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yuyin);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());

		// 初始化界面
		initView();

		// 设置目前的activity ，这样客户端通信对象的发送消息后，handler可以调用到本activity的handleMessage方法
		setRunningActivity(this);
	}

	/**
	 * 初始化界面的方法
	 */
	private void initView() {
		// 初始化listview
		mUserListView = (ListView) findViewById(R.id.userListView);

		// 生成SimpleAdapter适配器对象
		SimpleAdapter mySimpleAdapter = new SimpleAdapter(this,
				userNameMapList,// 数据源
				R.layout.user_listview,// ListView内部数据展示形式的布局文件listitem.xml
				new String[] { "userName" },// HashMap中的两个key值
											// itemTitle和itemContent
				new int[] { R.id.useritem });/*
											 * 布局文件listitem.xml中组件的id
											 * 布局文件的各组件分别映射到HashMap的各元素上，完成适配
											 */

		mUserListView.setAdapter(mySimpleAdapter);

		// 设置点击事件
		mUserListView.setOnItemClickListener(new MyItemListener());

		mImgTxtBtnMy = (ImageTextButton) findViewById(R.id.image_text_btn_myselef);
		mImgTxtBtnTalk = (ImageTextButton) findViewById(R.id.image_text_btn_talk);
		mImgTxtBtnTalk.setBackgroundResource(R.drawable.select);
		mImgTxtBtnMy.setOnClickListener(new MyButtonListener());

		// 初始化用户列表
		handleUserChange();
	}

	/**
	 * 处理子线程的消息
	 */
	@Override
	public void handleMessage(Message msg) {
		Log.v("test", "已经调用到消息处理方法");
		Bundle data;
		UserInfo userInfo;
		// 根据支线程传回的消息类型进行相应处理
		if (msg.what == Const.handlerAddUser) {
			handleUserChange();

		} else if (msg.what == Const.handlerReduceUser) {
			handleUserChange();
		} else if (msg.what == Const.handlerRequstTall) {
			// 根据id拿到请求对话的用户信息
			data = msg.getData();
			userInfo = getUserInfoById(data.getInt("toUserId"));

			// 提示用户名
			Toast.makeText(getApplicationContext(),
					"用户" + userInfo.getUserName() + "请求通话", Toast.LENGTH_SHORT)
					.show();

			Intent intent = new Intent(YuyinActivity.this, CallActivity.class);

			intent.putExtra("toUserId", userInfo.getUserId());
			intent.putExtra("toUserName", userInfo.getUserName());
			startActivity(intent);
		}

	}

	private void handleUserChange() {
		// ArrayList<String> userNameList=client.getUserNameList();
		ArrayList<UserInfo> userInfoList = getUserInfoList();

		// 此处可以用修改而不是全部重置，以下处理只是为了方便
		userNameMapList.clear();
		HashMap<String, String> map = new HashMap<String, String>();

		for (UserInfo userInfo : userInfoList) {
			map = new HashMap<String, String>();
			map.put("userName", userInfo.getUserName());
			map.put("userId", Integer.toString(userInfo.getUserId()));
			userNameMapList.add(map);
		}

		// 调用适配器方法更新界面
		SimpleAdapter mySimpleAdapter = (SimpleAdapter) mUserListView
				.getAdapter();
		mySimpleAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_yuyin, menu);
		return true;
	}

	class MyButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(YuyinActivity.this,
					UserInfoActivity.class);
			startActivity(intent);
		}
	}

	class MyItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// 获得选中项的HashMap对象 ,进而获取UserInfo对象
			ArrayList<UserInfo> userInfoList = getUserInfoList();
			// UserInfo userInfo=client.get
			HashMap<String, String> map = (HashMap<String, String>) mUserListView
					.getItemAtPosition(arg2);
			int toUserId = Integer.parseInt(map.get("userId"));
			UserInfo toUserInfo = null;

			UserInfo userMyself = getMyself();

			for (UserInfo userInfoTemp : userInfoList) {
				if (userInfoTemp.getUserId() == toUserId) {
					toUserInfo = userInfoTemp;
					break;
				}
			}

			// 当id是自己是救直接打开对话窗口，这是为了方便测试
			if (userMyself.getUserId() == toUserInfo.getUserId()) {

				Intent intent = new Intent(YuyinActivity.this,
						TalkActivity.class);

				intent.putExtra("TALK_MODEL", TalkActivity.TALK_MODEL_NOW);
				intent.putExtra("toUserId", toUserInfo.getUserId());
				intent.putExtra("userName", toUserInfo.getUserName());
				startActivity(intent);
			} else {
				// 当不是自己测试时就要发送消息请求对话
				ControlMessage conMsg = new ControlMessage(Const.requestTalk,
						"", userMyself.getUserId(), toUserId);
				sendMessageAddThread(conMsg);
				Log.v("test", "发送对话请求");

				Intent intent = new Intent(YuyinActivity.this,
						TalkActivity.class);

				// 由于要等对方确认，所以启动模式是等待
				intent.putExtra("TALK_MODEL", TalkActivity.TALK_MODEL_WAIT);
				intent.putExtra("toUserId", toUserInfo.getUserId());
				intent.putExtra("toUserName", toUserInfo.getUserName());
				startActivity(intent);
			}
		}
	}

}
