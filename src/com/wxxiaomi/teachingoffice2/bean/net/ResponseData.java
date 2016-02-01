package com.wxxiaomi.teachingoffice2.bean.net;


public class ResponseData<T>{

	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	private boolean success;
	private String error;
	private T obj;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public T getObj() {
		return obj;
	}
	public void setObj(T obj) {
		this.obj = obj;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
