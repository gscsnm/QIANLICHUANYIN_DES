package com.yichang.qianlichuanyin.main;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public interface MainActivity{
//	private Handler handler = new Handler();
	
	//处理从子线程发回来的方法
	public void handleMessage(Message msg);
	
	//初始化数据，包括client对象等
//	public void initData();
}
