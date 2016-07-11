package com.cmm.incast;

import android.graphics.Bitmap;



public class client {

	static {
        System.loadLibrary("CloudClient"); 
    }
	
	public static native int Init(String IpOrDNS,int port);
	public static native int UnInit();
	
	public static native int GetVersion(byte[] versionInfo,int size);
	//搜索局域网设备
	public static native int GetDevicesCount(int nTimeOut);
	public static native int GetDevicesInfo(int no, byte[] deviceName, int nameSize, byte[] outIP, int ipSize, byte[] outNO, int noSize);
	//搜索外网设备
	public static native int GetRemoteDevicesCount(String DeviceID,String Password,String IPOrDNS,int nTimeOut);
	public static native int GetRemoteDevicesInfo(int no, byte[] deviceName, int nameSize, byte[] forwardIP, int forwardSize, byte[] outIP, int ipSize, byte[] outNO, int noSize);
	//获取外网设备总个数
	public static native int GetRemoteDevicesAllCount();
	
	public static native int InitDevice(String DeviceId,String Password);
		
	public static native int RequestVideo(int type,int nTimeOut);
	public static native int StopVideo(int type);
	public static native int RecvVideo(Bitmap bmp);

	public static native int FingerInfo(int noFinger,int iState,int x,int y);
	public static native int Keyboard(int key,int value);
	
	public static native int SendOneFrame(int type,int width,int height, byte[] inYuv);
	public static native int SendOneFrameStream(int type,byte[] inStream,int size,int framecount);
	
	public static native int SetGpio(int noGpio,int iValue);
	//小于0：数据没有返回或底层获取不到，1灯亮（对外输出高电压），0灯黑（对外输出低电压）
	public static native int GetGpio(int noGpio);
	
	public static native int SetSerialRate(int noSerial,int iRate);
	public static native int WriteSerialData(int noSerial,byte[] inData,int size);
	public static native int ReadSerialData(int noSerial,byte[] outData,int size,int timeout);
	
	public static native int SendData(int type,byte[] InData,int size);
	
	public static native int Command(byte[] inCommand,int size);
	
	public static native int SendFile(int type,String FileName);
	
	public static native int SendAudio(int sampleRate,int numChannels,byte[] inPCM,int size);
	
	public static synchronized int SetGpioEx(int noGpio , int iValue) {
		return SetGpio(noGpio, iValue);
	}
	public static synchronized int GetGpioEx(int noGpio) {
		return GetGpio(noGpio);
	}
	// 接收音频并播放
	public static native int RecvAudio(byte[] outData, int[] outSize);
	//设置获取云端教育、在线浏览、更新地址ip
	public static native int SetConfigIP(String ipBuf,int type);
	public static native int GetConfigIP(byte[] ipBuf,int type);
	//设置获取远程控制人数和超时时间
	public static native int SetConfigSystem(String ipBuf,int type);
	public static native int GetConfigSystem(byte[] ipBuf,int type);
	//设置获取投影仪开关机码
	public static native int SetConfigOpenCloseDevice(String bps,String closeDelayTime,String openCode,String closeCode,String VGACode,String SVEDIOCode,String Manufacturer,String model,String ParityCheck,String sendCodeDelayTime);
	public static native int GetConfigOpenCloseDevice(byte[] bps,byte[] closeDelayTime,byte[] openCode,byte[] closeCode,byte[] vgaCode,byte[] svedioCode,byte[] Manufacturer,byte[] model,byte[] ParityCheck,byte[] sendCodeDelayTime);
	//设置投影仪信号源type =1 vga type = 2hdm
	public static native int SetDeviceVGAorHDMI(int type);
	//设置获取系统开关机
	public static native int SetDeviceOpenCMD();
	public static native int SetDeviceCloseCMD();
	//修改设备名字
	public static native int SetDeviceName(String name);
	public static native int GetDeviceName(byte[] name);
	//局域网设备修改完设备名，调用此函数显示修改后的名
	public static native int setLANdeviceName(String DeviceID,String DeviceName);
	//广域网设备修改完设备名，调用此函数显示修改后的名
	public static native int setWANdeviceName(String DeviceID,String DeviceName);
	//获取系统状态state=1开机，0关机
	public static native int GetDeviceOpenCloseState(byte[] state);
	//设置投影仪开关机状态state=1开机，0关机
	public static native int SetOpenOrCloseState(int state);
	//获取网络状态返回：1局域网2广域网3转发，-1错误
	public static native int GetGommunicationMethod();
	
	public static native int RecvVideoStream(byte[] outData, int[] outSize);
}
