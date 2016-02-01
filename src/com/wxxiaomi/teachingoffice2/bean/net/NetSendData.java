package com.wxxiaomi.teachingoffice2.bean.net;

import java.util.HashMap;
import java.util.Map;

public class NetSendData {

	private String url;
	private String content;
	private Map<String,String> headers = new HashMap<String,String>();
	private Map<String,String> parmars = new HashMap<String,String>();
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public Map<String, String> getParmars() {
		return parmars;
	}
	public void setParmars(Map<String, String> parmars) {
		this.parmars = parmars;
	}
	
	
}
