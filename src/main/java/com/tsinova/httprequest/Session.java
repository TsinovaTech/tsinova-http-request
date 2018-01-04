package com.tsinova.httprequest;

import java.io.Serializable;


public class Session implements Serializable {
	private static final long serialVersionUID = 1L;
	private CoreNetRequest coreRequest;
	private BaseResponse response;
	
	public Session(CoreNetRequest coreRequest, BaseResponse response) {
		super();
		this.coreRequest = coreRequest;
		this.response = response;
	}
	
	public CoreNetRequest getCoreRequest() {
		return coreRequest;
	}
	
	public void setCoreRequest(CoreNetRequest coreRequest) {
		this.coreRequest = coreRequest;
	}
	
	public BaseResponse getResponse() {
		return response;
	}
	
	public void setResponse(BaseResponse response) {
		this.response = response;
	}
	
}
