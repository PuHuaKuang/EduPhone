package com.incast.udi.eduphone.application;

import android.app.Application;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.cmm.incast.client;
import com.incast.udi.eduphone.utils.L;

public class EduPhoneApplication extends Application {

	private String IpOrDNS = "120.24.254.54";
	private int Port = 9417;

	@Override
	public void onCreate() {

		new Thread(new Runnable() {
			@Override
			public void run() {
			    client.Init(IpOrDNS,Port);
			}
		}).start();
		startRecvDataThread();
		
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		
		stopRecvDataThread();
		client.UnInit();

		super.onTerminate();
	}
	
	private boolean stopped;
	int[] outSize = new int[1];
	byte[] outData = new byte[1024 * 20];
	
	static final int frequency = 44100;
	static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	
	public void AudioPlay() {

		stopped = false;
		AudioTrack atrack = null;
		int playBufSize = AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding) * 2;
		atrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, channelConfiguration, audioEncoding, playBufSize, AudioTrack.MODE_STREAM);
		atrack.play();

		while (!stopped) {
			if (stopped) break;
			outSize[0] = 0;
			int r = client.RecvAudio(outData, outSize);
			L.i( "outSize = " + outSize[0]);
			if (r < 0) continue;
			
			if (outSize[0] > 0) {
				atrack.write(outData, 0, outSize[0]);
				atrack.flush();
			}
		}
		stopped = true;
		atrack.stop();
		atrack.release();
	}
	
	private void AudioStop(){
		stopped = true;
	}

	// 接收手机端发过来的数据
	private RecvDataThread mRecvDataThread = null;
	private class RecvDataThread extends Thread {
		// 通知停止线程的标记
		private boolean stopFlag = false;

		@Override
		public void run() {
			if (!stopFlag && !Thread.currentThread().isInterrupted()) {
				//接收到音频后播放
				AudioPlay();
			}
		}
		/**
		 * 通知线程需要停止
		 */
		public void stopThread() {
			AudioStop();
			stopFlag = true;
		}
	};
	
	/**
     * 接收数据线程开启
     */
    public void startRecvDataThread() {
    	if (mRecvDataThread == null) {
    		mRecvDataThread = new RecvDataThread();
    		mRecvDataThread.start();
		}
    }
	/** 
     * 接收数据线程停止 
     */  
    public void stopRecvDataThread() {  
        if (mRecvDataThread != null) {
        	RecvDataThread moribund = mRecvDataThread;
        	mRecvDataThread = null;
			moribund.stopThread();
			try {
				moribund.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
}
