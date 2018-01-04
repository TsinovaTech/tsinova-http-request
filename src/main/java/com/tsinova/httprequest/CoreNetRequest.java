package com.tsinova.httprequest;

import android.text.TextUtils;


import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 核心网络请求
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings({"rawtypes", "serial"})
public class CoreNetRequest implements Serializable {
	public static final String JSON_ARRAY_KEY = "json_arrays";
	private int id;
	private String url;
	private String mothed = "post";//"get"  和 "post"
	private boolean isCancel = false;//是否取消本次请求
	private boolean isLogin = false;
	private int timeout = 60000;
	private JSONObject jsonObject;
	private OnRequestListener listener;//请求监听
	private HashMap<String, Object> params;
	private Priority priority = Priority.HIGH;//优先级
	private boolean isSetLocaleParame = true; // 网络请求中是否加入locale参数
	
	private boolean isGsonDate;
	private String gsonString;

//	private boolean isNewRequset;
	
	private List<BasicNameValuePair> parameters;
	
//	public boolean isNewRequset() {
//		return isNewRequset;
//	}
//
//	public void setNewRequset(boolean isNewRequset) {
//		this.isNewRequset = isNewRequset;
//	}

	public String getGsonString() {
		return gsonString;
	}

	public void setGsonString(String gsonString) {
		setGsonDate(true);
		this.gsonString = gsonString;
	}


	public boolean isSetLocaleParame() {
		return isSetLocaleParame;
	}

	public void setLocaleParame(boolean isSetLocaleParame){
		this.isSetLocaleParame = isSetLocaleParame;
	}


	public boolean isGsonDate() {
		return isGsonDate;
	}

	public void setGsonDate(boolean isGsonDate) {
		this.isGsonDate = isGsonDate;
	}

	/**
	 * 
	 * @param url
	 * @param listener 请求完成时的回调
	 */
	public CoreNetRequest(String url, OnRequestListener listener) {
		super();
		this.url = url;
		this.listener = listener;
		initURL();
	}
	
	public CoreNetRequest() {
		initURL();
	}
	
	private void initURL() {
		timeout = 60000;
		params = new HashMap<String, Object>();
		jsonObject = new JSONObject();
		parameters = new ArrayList<BasicNameValuePair>();
	}
	
	/**
	 * network请求参数是否非法<br>
	 * url, responseClass ,listener都不能为空
	 * 
	 * @return
	 */
	public boolean isIllegal() {
		return TextUtils.isEmpty(url) || listener == null;
	}
	
	public Object get(String key) {
		return params.get(key);
	}
	
	public void put(String key, String value) {
		parameters.add(new BasicNameValuePair(key, value));
		params.put(key, value);
		try {
			jsonObject.put(key, value);
		}catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void put(String key, boolean value) {
		parameters.add(new BasicNameValuePair(key, String.valueOf(value)));
		params.put(key, value);
		try {
			jsonObject.put(key, value);
		}catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void put(String key, long value) {
		parameters.add(new BasicNameValuePair(key, String.valueOf(value)));
		params.put(key, value);
		try {
			jsonObject.put(key, value);
		}catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void put(String key, double value) {
		parameters.add(new BasicNameValuePair(key, String.valueOf(value)));
		params.put(key, value);
		try {
			jsonObject.put(key, value);
		}catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void put(String key, Object value) {
		parameters.add(new BasicNameValuePair(key, String.valueOf(value)));
		params.put(key, value);
		try {
			jsonObject.put(key, value);
		}catch(JSONException e) {
			e.printStackTrace();
		}
	}
	public void put(String key, byte[] value) {
		parameters.add(new BasicNameValuePair(key, value.toString()));
		params.put(key, value);
		try {
			jsonObject.put(key, value);
		}catch(JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the isLogin
	 */
	public boolean isLogin() {
		return isLogin;
	}
	
	/**
	 * @param isLogin the isLogin to set
	 */
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isCancel() {
		return isCancel;
	}
	
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
	
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	
	public HashMap<String, Object> getParams() {
		return params;
	}
	
	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}
	
	public OnRequestListener getListener() {
		return listener;
	}
	
	public void setListener(OnRequestListener listener) {
		this.listener = listener;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setMothed(String mothed) {
		this.mothed = mothed;
	}
	
	public String getMothed() {
		return this.mothed;
	}
	
	public List<BasicNameValuePair> getParameters() {
		return parameters;
	}

	public void setParameters(List<BasicNameValuePair> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * 当为Post请求时：
	 * 获取JSon字符串格式的URL参数
	 * 
	 * @return
	 */
	public String getParamsByJsonString() {
		if(jsonObject.has(JSON_ARRAY_KEY)) {//是JsonArray
			try {
				return jsonObject.getJSONArray(JSON_ARRAY_KEY).toString();
			}catch(JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonObject.toString();
	}
	
	/**
	 * 当为Get请求时：
	 * 获取URL编码的字符串
	 * 
	 * @return
	 */
	public String getNormalURLParams() {
		StringBuilder builder = new StringBuilder(url);
		if(url != null) {
			if(params != null && params.size() > 0) {
				builder.append("?");
				Set<String> keys = params.keySet();
				for(String key : keys) {
					builder.append(key + "=" + (params.get(key) == null ? "" : params.get(key).toString()));
					builder.append("&");
				}
				builder.deleteCharAt(builder.length() - 1);
			}
			return builder.toString();
		}
		return "";
	}
	
//	/**
//	 * 当为登录,注销请求时，特殊处理
//	 *
//	 * @return
//	 */
//	public String getLoginURLParams() {
//		String data = StringUtils.getUrlParamsFromHashMap(params);
//		if(!TextUtils.isEmpty(data)) {
//			data = data + "&token=" + AppParams.getInstance().getToken();
//		}else {
//			data = "?token=" + AppParams.getInstance().getToken();
//		}
//		return url + data;
//	}
//
//	/**
//	 * 当为Get请求时：
//	 * 获取URL编码的字符串
//	 *
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	public String getURLParams() {
//		String data = "?data=" + jsonObject.toString() + "&token=\"token\"";
//		if(jsonObject != null && !TextUtils.isEmpty(jsonObject.toString())) {
//			data = "?data=" + URLEncoder.encode(getParamsByJsonString()) + "&token=" + AppParams.getInstance().getToken();
//		}else {
//			data = "?token=" + AppParams.getInstance().getToken();
//		}
//		return url + data;
//	}
	
	@Override
	public String toString() {
		Iterator iter = params.entrySet().iterator();
		StringBuffer buffer = new StringBuffer();
		while(iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
			buffer.append("key:" + entry.getKey() + ",value:" + entry.getValue()).append("  ");
		}
		return "url:" + url + ",params:" + buffer.toString();
	}
	
	public enum Priority {
		LOW(0), HIGH(1);
		private int priority;
		
		Priority(int priority) {
			this.priority = priority;
		}
		
		public int getPriority() {
			return priority;
		}
	}
	
	/**
//	 * 当为Get请求时：
//	 * 获取URL编码的字符串
//	 *
//	 * @return
//	 */
//	public String getURLParams1() {
//		String data = "?data=" + jsonObject.toString() + "&token=\"token\"";
//		if(jsonObject != null && !TextUtils.isEmpty(jsonObject.toString())) {
//			data = "?data=" + getParamsByJsonString() + "&token=" + AppParams.getInstance().getToken();
//		}else {
//			data = "?token=" + AppParams.getInstance().getToken();
//		}
//		return url + data;
//	}
	 
}
