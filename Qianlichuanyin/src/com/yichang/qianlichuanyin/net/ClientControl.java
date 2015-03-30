package com.yichang.qianlichuanyin.net;

import android.util.Log;

/**
 * 
 *
 */
public class ClientControl implements Runnable {
	private Client client;

	private boolean ifStop=true;

	public ClientControl(Client client) {
		this.client = client;
		ifStop=true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Thread thread = new Thread(client);
		while (ifStop) {
			if (!client.isIfConnecting()) {
				client.connect();
				Log.v("test", "连接完毕，准备开启线程");
				thread.start();
				Log.v("test", "已开启线程");
				break;//为了方便测试，先只连接一次
			} else {
				try {
					thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void stop(){
		setIfStop(false);
	}
	
	public boolean isIfStop() {
		return ifStop;
	}

	public void setIfStop(boolean ifStop) {
		this.ifStop = ifStop;
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
