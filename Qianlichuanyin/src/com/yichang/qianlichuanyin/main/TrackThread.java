package com.yichang.qianlichuanyin.main;

import java.util.LinkedList;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import com.google.gson.Gson;
import com.yichang.qianlichuanyin.net.Client;

/**
 * 播放声音
 *
 */
public class TrackThread implements Runnable {
	private boolean stopTrack = true;
	private AudioTrack audioTrack;
	private Client client;
	private Gson gson = new Gson();
	private LinkedList<byte[]> trackingSoundList; 
	// 保存即将要播放声音包的lingkedList，线程播放时播放这里的全部声音包

	public TrackThread() {
		client = FirstActivity.control.getClient();
	}

	//建立线程进行播放收到的声音信息
	@Override
	public void run() {
		byte[] buffer;
		int bufferSize = Const.TrackBufferSize;
		int realSize;
		trackingSoundList = client.getTrackingSoundList();
		stopTrack = true;

		// 为空才初始化
		if (audioTrack == null) {
			initAudioTrack();
		}

		// Log.v("test", "语音包数量"+trackingSoundList.size());

		try {
			//播放声音，自动播放audioTrack.write写入的声音
			audioTrack.play();
		} catch (Exception e) {
			Log.v("test", "启动播放失败");
			e.printStackTrace();
		}

		while (stopTrack && trackingSoundList.size() != 0) {

			buffer = trackingSoundList.getFirst(); // short类型对应16bit音频数据格式，byte类型对应于8bit
			bufferSize = buffer.length;
			//把要播放的声音流写入到audioTrack中，调用audioTrack.play()进行播放
			realSize = audioTrack.write(buffer, 0, bufferSize); // 返回值是个int类型的数据长度值
			trackingSoundList.remove(0);

			// Log.v("test", "语音包收到后十个包的内容为" + buffer[0] + buffer[150]
			// + buffer[800] + buffer[900]);

			try {
				Thread.sleep(Const.TrackTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//更改标志为false
		changTrackState();
	}

	/*
	 * 更改标志为false
	 */
	private void changTrackState() {
		client.setIfTracking(false);
	}

	/**
	 * 初始化AudioTrack
	 */
	public void initAudioTrack() {
		// 获取缓冲区最小的字节数，以免设定的缓冲区太小
		int minSize = AudioTrack.getMinBufferSize(Const.RateInHz,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
		Log.v("test", "播放minSize" + minSize);

		/*
		 * 初始化录音对象 streamType： 音频流
		 * ,sampleRateInHz：默认的采样频率,channelConfig：描述音频通道设置 audioFormat：音频数据支持格式
		 * 单/双通道, bufferSizeInBytes： 缓冲区的总数（字节），最后一个参数应该是解码模式
		 */
		try {
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
					Const.TrackHz, AudioFormat.CHANNEL_OUT_MONO,
					AudioFormat.ENCODING_PCM_16BIT, Const.TrackBufferSize,
					AudioTrack.MODE_STREAM);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 停下整个录音线程
	 */
	public void stopTrack() {
		stopTrack = false;
		stopAudioTrack();
	}

	/**
	 * 暂停录音对象的录音，并释放录音资源
	 */
	public void stopAudioTrack() {
		if (audioTrack != null) {
			audioTrack.stop();
			audioTrack.release();
			audioTrack = null;
		}
	}

}
