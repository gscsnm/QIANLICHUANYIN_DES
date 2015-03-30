package com.yichang.qianlichuanyin.main;

import com.yichang.qianlichuanyin.net.UserInfo;
import com.yichang.qianlichuanyin.view.ImageTextButton;
import android.os.Bundle;
import android.os.Message;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 个人信息页面
 *
 */
public class UserInfoActivity extends BaseActivity implements MainActivity{
	private ImageTextButton mImgTxtBtnMy;
	private ImageTextButton mImgTxtBtnTalk;
	private TextView userNameText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		mImgTxtBtnMy = (ImageTextButton) findViewById(R.id.image_text_btn_myselef);
		mImgTxtBtnTalk = (ImageTextButton) findViewById(R.id.image_text_btn_talk);
		userNameText=(TextView) findViewById(R.id.user_name_textview);

		// 初始化界面
		initView();

		//设置目前的activity ，这样客户端通信对象的发送消息后，handler可以调用到本activity的handleMessage方法
		setRunningActivity(this);
		
		mImgTxtBtnTalk.setOnClickListener(new MyButtonListener());

	}

	private void initView() {
		UserInfo myself=getMyself();
		mImgTxtBtnMy.setBackgroundResource(R.drawable.select);
		//显示用户名
		userNameText.setText(myself.getUserName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user_info, menu);
		return true;
	}
	
    class MyButtonListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent intent=new Intent(UserInfoActivity.this,YuyinActivity.class);
    		startActivity(intent);
    		finish();
    	}
    }

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		
	}


}
