package com.yichang.qianlichuanyin.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 退出发送广播
 *
 */
public class ExitBroadcastReceiver extends BroadcastReceiver{

	private RunBeforeExit runBeforeExit;
	
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
//		context.unregisterReceiver(this);
		runBeforeExit.beforeExit();
	}
	
	public RunBeforeExit getRunBeforeExit() {
		return runBeforeExit;
	}

	public void setRunBeforeExit(RunBeforeExit runBeforeExit) {
		this.runBeforeExit = runBeforeExit;
	}

}
