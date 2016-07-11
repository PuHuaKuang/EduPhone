package com.incast.udi.eduphone.vo;

import java.io.Serializable;

/**
 * @author  PuHua.Kuang E-mail:puhua.kuang@hotmail.com.cn 
 * @date 创建时间：2016-4-11 上午9:52:27 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return
 * copyrights reserved by PuHua.Kuang 2015-2016  
 */
public class DeviceInfo implements Serializable{

	private String serial;
	private String name;
	private String ip;
	private int icon;
	
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
}
