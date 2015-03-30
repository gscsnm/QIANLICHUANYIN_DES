package com.yichang.qianlichuanyin.main;

import java.io.FileOutputStream;
import java.util.Arrays;

import com.google.gson.Gson;
import com.yichang.qianlichuanyin.net.Client;
import com.yichang.qianlichuanyin.net.ControlMessage;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

/**
 * @author ly
 * 麦克风录音及发送
 *
 *
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class RecordThread implements Runnable {
	private boolean stopRecord = true;
	private AudioRecord audioRecord = null;
	private Client client;
	private Gson gson = new Gson();
	private int toUserId;
	private int minSize; 

	/*
	 * 个数组大小和AudioRecord和AudioTrack能
	 * 正常实例化所需的最小Buffer大小（即上面实例化
	 * 时的m_in_buf_size和m_out_buf_size参数）相等
	 * 且服务器方进行缓存数据的数组尺寸是上述数值的2倍时
	 * 语音质量最好。由于录音和放音的速度不一致，受到北理工大牛的启发，
	 * 在录音方面，将存放录音数据的数组放到LinkedList中,当LinkedList中数组个数达到2（
	 * 这个也是经过试验验证话音质量最好时的数据）时，
	 * 将先录好的数组中数据传送出去。经过上述反复试验和修改，最终使双方通话质量较好，且延时较短（大概有2秒钟）。
	 * AudioRecord一次会读640个数据，
	 */
	
	public RecordThread(int toUserId) {
		try{
		// this.audioRecord=audioRecord;
		this.toUserId = toUserId;
		this.client = FirstActivity.control.getClient();
		minSize = 640;
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		ControlMessage mesg;
		//String jsonStr;
		String jsonStr1;
		byte[] buffer;
		byte[] realBuffer;
		int bufferSize = minSize;//Const.RecordBufferSize;
		int realSize;
		stopRecord = true;

		// 为空才初始化
		if (audioRecord == null) {
			initAudioRecord();
		}

		//开始录音
		audioRecord.startRecording();
		while (stopRecord) {
			try {
				Thread.sleep(Const.RecordTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			buffer = new byte[bufferSize]; // short类型对应16bit音频数据格式，byte类型对应于8bit
			try {
				realSize = audioRecord.read(buffer, 0, bufferSize); // 返回值是个int类型的数据长度值

				// 发送消息给服务器,只取有效数组即可
				realBuffer = Arrays.copyOfRange(buffer, 0, realSize);
				//加密录音流
				byte[] realBuffer1 = Des.encrypt(realBuffer,"qmwnebrv");
				//把加密后的录音流 转成 json 用于网络传输
				jsonStr1 = gson.toJson(realBuffer1);
				//jsonStr = gson.toJson(realBuffer);
				//把录音json保存到消息中
				mesg = new ControlMessage(Const.soundMessage, jsonStr1, 0,
						toUserId);
				//发送
				client.sendMessage(mesg);
			} catch (Exception e) {
				//处理异常
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 初始化
	 */
	public void initAudioRecord() {
		// 获取缓冲区最小的字节数，以免设定的缓冲区太小， 设置的值比getMinBufferSize()还小则会导致初始化失败
		 minSize = AudioRecord.getMinBufferSize(Const.RateInHz,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		Log.v("test", "录音minSize" + minSize);

		/*
		 * 初始化录音对象 audioSource： 录音源
		 * ,sampleRateInHz：默认的采样频率,channelConfig：描述音频通道设置 audioFormat：音频数据支持格式
		 * 单/双通道, bufferSizeInBytes： 缓冲区的总数（字节）
		 */
		try {
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
					Const.RateInHz, AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT,minSize);// Const.RecordBufferSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 停下整个录音线程
	 */
	public void stopRecord() {
		try{
			stopRecord = false;
			stopAudioRecord();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * 暂停录音对象的录音，并释放录音资源
	 */
	public void stopAudioRecord() {
		if (audioRecord != null) {
			audioRecord.stop();
			audioRecord.release();
			audioRecord = null;
		}
	}

}
