package com.tsinova.httprequest;

import java.io.Serializable;

public final class BaseResponse implements Serializable {
	private boolean success;
	private String message = "";
	private Object data;
	private String result;//服务器返回的原始json字符串
	private Integer errorCode = -1;
	private int total;//用于分页列表返回总数
	
	private static final long serialVersionUID = 1L;
	
	public BaseResponse(){}
	
	public BaseResponse(boolean success, String message, Object data) {
		this.success = success;
		this.message = message;
		this.data = data;
		
	}
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	
	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
	
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
		
	}

	@Override
	public String toString() {
		return "BaseResponse [success=" + success + ", message=" + message
				+ ", data=" + data + ", result=" + result + ", errorCode="
				+ errorCode + ", total=" + total + "]";
	}
	
	
	
}
