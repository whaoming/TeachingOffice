package com.wxxiaomi.teachingoffice2.bean.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class NetReceiverData {

	private int stateCode;
	private String location;
	private byte[]  content;
	private String fromUrl;
	private Map<String,String> headers = new HashMap<String,String>();
	
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public String getFromUrl() {
		return fromUrl;
	}
	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}
	public int getStateCode() {
		return stateCode;
	}
	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	public String getContent2String(){
		try {
			return new String(content,"gb2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(content);
	}
	
}
