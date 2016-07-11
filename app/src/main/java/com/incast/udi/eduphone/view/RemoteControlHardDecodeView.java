package com.incast.udi.eduphone.view;

import java.nio.ByteBuffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cmm.incast.client;
import com.incast.udi.eduphone.dialog.RunningDialog;
import com.incast.udi.eduphone.utils.ToastUtils;
import com.incast.udi.eduphone.utils.ValidateUtils;
import com.incast.udi.eduphone.vo.DeviceInfo;
import com.incast.udi.eduphone.R;
import com.incast.udi.eduphone.utils.L;

public class RemoteControlHardDecodeView extends SurfaceView implements SurfaceHolder.Callback {

	private Context mContext ;
	public RunningDialog dialog = null;
	
	int widthScreen;
	int heightScreen;
	int u_width = 0;
	int u_height = 0;
	public int m = 1; //横屏 ,m=3 竖屏
	
	private MediaCodec decoder = null;
	private SurfaceHolder mHolder;
	
	public Surface getSurface() {
		if(mHolder != null) {
			return mHolder.getSurface();
		}
		return null;
	}
	
	private DeviceInfo deviceInfo = new DeviceInfo();
	
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public void ShowDialog(Context context) {
		if (dialog == null) {
			dialog = new RunningDialog(context, R.layout.layout_running_dialog, R.style.RunningDialogTheme);
			dialog.show();
		}
	}
	
	public void CloseDialog() {
		if (dialog != null) {
			dialog.cancel();
			dialog = null;
		}
	}

	public RemoteControlHardDecodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		mContext = context;
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay() .getMetrics(dm);
		widthScreen = dm.widthPixels;
		heightScreen = dm.heightPixels;

		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	// 触摸事件
	private float v_widthScreen = 1920.0f; // 默认横屏下的宽高
	private float v_heightScreen = 1080.0f;
	
	int KEY_BACK  = 158;
	int BTN_TOUCH = 0x14a;
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (widthScreen == 0 || heightScreen == 0 || u_width == 0 || u_height == 0) {
			return true;
		}

		float multX = 0;
		float multY = 0;
		if(m == 1) { //横屏
			multX = (v_widthScreen / (float)widthScreen);
			multY = (v_heightScreen / (float)heightScreen);
		} else if(m == 3) { // 竖屏
			float wh = (v_widthScreen / v_heightScreen);
			multX = wh * (v_heightScreen / (float)widthScreen) ;
			multY = wh * (v_widthScreen / (float)heightScreen); 
		} else {
			multX = 1;
			multY = 1;
		}
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			L.i("debug","ACTION_DOWN");
			for (int i = 0 ; i < event.getPointerCount() ; i++) {
				
				float e_x = event.getX(i);
				float e_y = event.getY(i);
				
				if(e_x != 0 && e_y != 0) {
					client.FingerInfo(i,1,(int)(e_x * multX),(int)(e_y * multY));
				}
			}
			
			break;
		case MotionEvent.ACTION_POINTER_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:
			L.i("debug", "ACTION_MOVE");
			for (int i = 0 ; i < event.getPointerCount() ; i++) {
				
				float e_x = event.getX(i);
				float e_y = event.getY(i);
				
				if(e_x != 0 && e_y != 0) {
					client.FingerInfo(i,1,(int)(e_x * multX),(int)(e_y * multY));
				}
			}
			client.Keyboard(BTN_TOUCH, 1);
			
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			L.i("debug", "ACTION_UP");
			for (int i = 0 ; i < event.getPointerCount() ; i++) {
				
				float e_x = event.getX(i);
				float e_y = event.getY(i);
				
				if(e_x != 0 && e_y != 0) {
					client.FingerInfo(i,0,(int)(e_x * multX),(int)(e_y * multY));
				}
			}
			client.Keyboard(BTN_TOUCH, 0);

			break;
			
		default:

			break;
		}
		return true;
	}
	
	private InitDeviceCallback_H mInitDeviceCallback = null;
	public void setInitDeviceCallback_H(InitDeviceCallback_H initDeviceCallback) {
		this.mInitDeviceCallback = initDeviceCallback;
	}
	public interface InitDeviceCallback_H {
		
		void onInitDevice();
	}

	@SuppressLint("NewApi") class VideoThread extends Thread {
		// 通知停止线程的标记
		private boolean stopFlag = false;
		private DeviceInfo mInfo = null;
		Surface surfaceholder = null;
		ByteBuffer[] inputBuffers  = null;
		ByteBuffer[] outputBuffers = null;

		public VideoThread(DeviceInfo info,Surface surface) {
			this.mInfo = info;
			this.surfaceholder = surface;
		}
		
		@Override
		public void run() {
			
			int r,c,w,h;
			int[] size = new int[1];
			byte[] outData = new byte[1024*1024*4]; 
			
			
			int initRet = client.InitDevice(mInfo.getSerial(), "123456");
			L.i("length", "InitDevice--->" + initRet + " " + mInfo.getSerial() + " " + mInfo.getIp());
			if(initRet == 0) {
				if(mInitDeviceCallback != null) {
					mInitDeviceCallback.onInitDevice();
				}
			}
			
			int reqRet = client.RequestVideo(0x0D,2);
			L.i("length", "RequestVideo--->" + reqRet);
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_DISPLAY);
			
			while (!stopFlag && !Thread.currentThread().isInterrupted()) {
				// input 	
				size[0] = 0;
				c = client.RecvVideoStream(outData,size);
				r = (byte)(c >> 24);
				
				if( r < 0  ) {	
	        		client.RequestVideo(0x0D, 2);
					continue;
				}
				
				w = (c >> 12)&0x0FFF;
				h = (c)&0x0FFF; 
				
				if(u_width != w || u_height != h) {
					u_width = w;
					u_height = h;
				}
				
				if( size[0] <= 0 ) {
	        		client.RequestVideo(0x0D, 2);
					continue;					
				}
				try {
					if( decoder == null ) {
						
						L.i("length", "----decoder == null ----");
						MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", 1920,1080);	
						mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 1920*1080);
						mediaFormat.setString(MediaFormat.KEY_MIME, "video/avc");
						String mime = mediaFormat.getString(MediaFormat.KEY_MIME); 
						decoder = MediaCodec.createDecoderByType(mime);
						if (decoder == null) {
							L.e("length", "createDecoderByType fail!");
							continue;
						}
						decoder.configure(mediaFormat,( (u_width>0) ? surfaceholder : null ), null, 0);//surfaceholder
						decoder.start();	 
						inputBuffers = decoder.getInputBuffers();	
						outputBuffers = decoder.getOutputBuffers();
					}
					int inputBufferIndex= decoder.dequeueInputBuffer(-1);
					if (inputBufferIndex >= 0) {
						ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];  
						inputBuffer.clear();  
						inputBuffer.put(outData, 0, size[0]);	
						decoder.queueInputBuffer(inputBufferIndex, 0,size[0], 0, 0);
					}
					BufferInfo bufferInfo = new BufferInfo();
					int outIndex = decoder.dequeueOutputBuffer(bufferInfo,0);
					if( outIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED ) {
						outputBuffers = decoder.getOutputBuffers(); 
						L.e("length", "INFO_OUTPUT_BUFFERS_CHANGED: " + MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED );
					} else if( outIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED ) {
						MediaFormat mediaFormat = decoder.getOutputFormat();
						u_width=mediaFormat.getInteger(MediaFormat.KEY_WIDTH);
						u_height=mediaFormat.getInteger(MediaFormat.KEY_HEIGHT);
						L.e("length", "---------------------w: " + u_width +  ",h: " + u_height);	
						if( u_width <= 0 ) {
							releaseDecoder();
							continue;	
						}							
					} else if( outIndex == MediaCodec.INFO_TRY_AGAIN_LATER ) {
	
					} else if(outIndex >= 0) {  
						if( outputBuffers != null ) {
							ByteBuffer outBuffer = outputBuffers[outIndex];   
							if(outBuffer != null) {
								outBuffer.get(outData,0,bufferInfo.size); 						
								outBuffer.clear();
							}
							CloseDialog();
						}					
						decoder.releaseOutputBuffer(outIndex, true);  
					} 		
				} catch (Exception e) {
					e.printStackTrace();
					releaseDecoder();
					continue; 
				}
			}
			releaseDecoder();
			int stopRet = client.StopVideo(0x0D);
			L.i("length", "StopVideo--->" + stopRet);
		}

		public void releaseDecoder() {
			try {
				if (decoder != null) {
					decoder.stop();
					decoder.release();
					decoder = null;			
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					decoder.release();
				} catch (Exception e2) {
					e2.printStackTrace();
					decoder = null;
				}
				decoder = null;
			}
			
			inputBuffers  = null;
			outputBuffers = null;
		}
		
		/**
		 * 通知线程需要停止
		 */
		public void stopThread() {
			stopFlag = true;
		}
	}
	
	public VideoThread _thread = null;

	public void StartThread(DeviceInfo info,Surface surface) {
		if (_thread == null && info != null && ValidateUtils.isSupportMediaCodecHardDecoder()) {
			_thread = new VideoThread(info,surface);
			_thread.start();
			
			ShowDialog(mContext);
		} else {
			ToastUtils.showToastCenter(mContext, "本设备不支持硬解码");
		}
	}

	public void StopThread() {
		if (_thread != null) {
			VideoThread moribund = _thread;
			_thread = null;
			moribund.stopThread();
			try {
				moribund.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
		L.i("length", "+++++++++++surfaceChanged++++++++++");
		StartThread(deviceInfo,holder.getSurface());
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		L.i("length", "+++++++++++surfaceCreated++++++++++");
		holder.setSizeFromLayout();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		L.i("length", "+++++++++++surfaceDestroyed++++++++++");
		StopThread();
	}
}
