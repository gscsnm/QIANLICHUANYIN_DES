package com.yichang.qianlichuanyin.main;

import com.yichang.qianlichuanyin.main.TalkActivity.StopTalkListener;
import com.yichang.qianlichuanyin.net.ControlMessage;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 此类是接收到对话请求后用来给用户选择是否接受对话的界面
 * @author Administrator
 *
 */
public class CallActivity extends BaseActivity implements MainActivity{
	private ImageView refuseTalk;
	private ImageView promiseTalk;
	private int toUserId;
	private String toUserName;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call);
		
		// 获取用户名和用户id
		Intent intent = getIntent();
		// fromUserId = intent.getIntExtra("fromUserId", 0);
		toUserId = intent.getIntExtra("toUserId", 0);
		toUserName = intent.getStringExtra("userName");
		
		refuseTalk=(ImageView)findViewById(R.id.refuse_image);
		refuseTalk.setOnClickListener(new refuseTalkListener());
        
		promiseTalk=(ImageView)findViewById(R.id.promise_image);
		promiseTalk.setOnClickListener(new promiseTalkListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_call, menu);
		return true;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		//
	}
	
	
	/**
	 * 拒绝对话
	 * @author Administrator
	 *
	 */
    class refuseTalkListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		//发送消息拒绝对话请求
			ControlMessage conMsg=new ControlMessage(Const.refuseTalk,"",0,toUserId);
			sendMessageAddThread(conMsg);
    		
    		//销毁Activity
    		finish();
    	}
    }
    
    /**
     * 接受对话
     * @author Administrator
     *
     */
    class promiseTalkListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		
    		//发送消息接受对话请求
			ControlMessage conMsg=new ControlMessage(Const.promiseTalk,"",0,toUserId);
			sendMessageAddThread(conMsg);
    		
    		//销毁Activity，跳转到对话界面
			Intent intent = new Intent(CallActivity.this,
					TalkActivity.class);

			intent.putExtra("TALK_MODEL", TalkActivity.TALK_MODEL_NOW);
			intent.putExtra("toUserId", toUserId);
			intent.putExtra("toUserName", toUserName);
			startActivity(intent);
			finish();
    	}
    }

}
