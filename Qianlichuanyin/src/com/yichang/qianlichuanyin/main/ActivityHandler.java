package com.yichang.qianlichuanyin.main;

import android.os.Handler;
import android.os.Message;

public class ActivityHandler extends Handler {
	private MainActivity mainActivity;

	public ActivityHandler() {

	}

	@Override
	public void handleMessage(Message msg) {
		mainActivity.handleMessage(msg);
		super.handleMessage(msg);
	}

	public MainActivity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
}
