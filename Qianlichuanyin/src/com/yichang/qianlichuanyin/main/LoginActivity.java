package com.yichang.qianlichuanyin.main;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 登录页面
 *
 */
public class LoginActivity extends BaseActivity implements MainActivity {

	private EditText mEditTextName;
	private EditText mEditTextPassword;
	private Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("test", "登录Activity");
		setContentView(R.layout.activity_login);

		mEditTextName = (EditText) findViewById(R.id.login_name);
		mEditTextPassword = (EditText) findViewById(R.id.password);

		// 初始化按钮并绑定事件
		mButton = (Button) findViewById(R.id.login_button);
		mButton.setOnClickListener(new MyButtonListener());

		// 设置目前的activity ，这样客户端通信对象的发送消息后，handler可以调用到本activity的handleMessage方法
		setRunningActivity(this);

		// 获取以前存储的用户名和密码，设置
		SharedPreferences shareedPreferences = getSharedPreferences("Login",
				Activity.MODE_PRIVATE);

		String userName = shareedPreferences.getString("userName", "");
		String password = shareedPreferences.getString("password", "");

		if (userName.length() != 0) {
			mEditTextName.setText(userName);
		}
		
		if (password.length() != 0) {
			mEditTextPassword.setText(password);
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	/**
	 * 
	 */
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == Const.handlerLogin_success) {
			Intent intent = new Intent(LoginActivity.this, YuyinActivity.class);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * 处理登陆事件，向服务器发送消息校验登陆用户名和密码
	 */
	private void login() {
		String userName = mEditTextName.getText().toString();
		String password ="123456";// mEditTextPassword.getText().toString();
		
		//存储用户名和密码
		SharedPreferences shareedPreferences = getSharedPreferences("Login",
				Activity.MODE_PRIVATE);
		Editor editor = shareedPreferences.edit();
		editor.putString("userName", userName);
		editor.putString("password", password);
		editor.commit();
		
		super.login(userName, password);

	}

	class MyButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			login();
		}
	}

}
