package com.tsinova.httprequest;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsinova.bike.network.CoreNetRequest.Priority;
import com.tsinova.bike.util.CommonUtils;
import com.tsinova.bike.util.StringUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;

/**
 * RequestDispatcher
 */
public class NetworkManager implements Singleton {
    private static NetworkManager instance;
    private NetworkThreadPoolExecutor executor;
    private Handler handler;

    private NetworkManager() {
        executor = NetworkThreadPoolExecutor.getInstance();
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void init() {
        if (executor == null) {
            executor = NetworkThreadPoolExecutor.getInstance();
        }
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
    }

    public static synchronized NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    /**
     * 发送核心网络请求
     *
     * @param request ：核心请求，不能为空
     */
    public void sendRequest(CoreNetRequest request, final Type type) {

        if (request == null || request.isIllegal()) {
            throw new RuntimeException("请求非法");
        }
        NetworkCallback callback = new NetworkCallback() {
            @Override
            public void onResult(final CoreNetRequest request, final String data) {
                BaseResponse response = null;
//				if(request.isNewRequset()){
//					response = parseJsonToNewResponse(data, type);
//				}else{
                response = parseJsonToResponse(data, type);
//				}

                final Session session = new Session(request, response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (request!=null){
                            if (!request.isCancel()) {//如果没有取消的话，将结果传回Activity
                                request.getListener().onResult(session);
                            }
                            request.setCancel(false);
                        }
                    }
                });
            }
        };

        if (request != null) { // 所有网络请求都加入语言类型
            if (request.isSetLocaleParame())
                if (StringUtils.getLocalLanguage().equals("ja")) {
                    request.put("locale", "jp");
                } else {
                    request.put("locale", StringUtils.getLocalLanguage());
                }
        }

        if (Priority.HIGH.equals(request.getPriority())) {//优先级高，直接开线程处理
            executeHighPriorityTask(request, callback);
        } else {//优先级低，加入请求队列，逐一处理
            NetworkRequestBlockQueue.getInstance().addRequestToQueue(request, callback);
        }
    }

    private void executeHighPriorityTask(final CoreNetRequest request, final NetworkCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if ("get".equals(request.getMothed())) {
                    NetworkHttpRequest.executeHttpGet(request, callback);
                } else if ("post".equals(request.getMothed())) {
                    NetworkHttpRequest.executeHttpPost(request, callback);
                } else if ("delete".equals(request.getMothed())) {
                    NetworkHttpRequest.executeHttpDelete(request, callback);
                } else {
                    throw new RuntimeException("请求方法异常");
                }
            }
        };
        executor.execute(runnable);
    }


    public BaseResponse parseJsonToResponse(String data, Type type) {
        BaseResponse response = new BaseResponse();
        response.setResult(data);
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter())
                    .setDateFormat("yyyy-MM-dd HH:mm:ss").create();//GSON 解析
            JSONObject object = new JSONObject(data);
            boolean success = false;
            String message = null;
            CommonUtils.log("----------parseJsonToResponse------------");
            if (object.has("result") && !object.isNull("result")) {
                String result = object.get("result").toString();
                if (result.equals("0")) {
                    success = true;
                }
            } else if (object.has("ret") && !object.isNull("ret")) {
                String result = object.get("ret").toString();
                if (result.equals("0")) {
                    success = true;
                }
            } else if (object.has("code") && !object.isNull("code")) { // https请求
                String result = object.get("code").toString();
                if (result.equals("1")) {
                    success = true;
                }
            }
            CommonUtils.log("success :" + success);
            response.setSuccess(success);

            if (object.has("msg") && !object.isNull("msg")) {
                message = object.get("msg").toString();
                response.setMessage(message);
            }
            CommonUtils.log("message :" + message);
            Object dataModel = null;
            if (object.has("data") && type != null) {
                String dataString = object.get("data").toString();
                if (type.toString().equals("class java.lang.String")) {
                    response.setData(dataString);
                } else {
                    dataModel = gson.fromJson(dataString, type);
                    response.setData(dataModel);
                }
            } else if (type != null) {
                response.setResult(data);
                dataModel = gson.fromJson(data, type);
                response.setData(dataModel);
            } else {
                CommonUtils.log("parseJsonToResponse is false !!!!!");
            }

            if (object.has("app") && type != null) {
                String dataString = object.get("app").toString();
                if (type.toString().equals("class java.lang.String")) {
                    response.setData(dataString);
                } else {
                    dataModel = gson.fromJson(dataString, type);
                    response.setData(dataModel);
                }
            } else if (type != null) {
                response.setResult(data);
                dataModel = gson.fromJson(data, type);
                response.setData(dataModel);
            } else {
                CommonUtils.log("parseJsonToResponse is false !!!!!");
            }

//			if(object.has("origin") && type != null) {
//				JSONArray origin = (JSONArray) object.get("origin");
//				response.setData(origin);
//			} else if (type != null){
//				response.setResult(data);
//				dataModel = gson.fromJson(data, type);
//				response.setData(dataModel);
//			} else {
//				CommonUtils.log("parseJsonToResponse is false !!!!!");
//			}false

            if (object.has("errorCode") && !object.isNull("errorCode")) {
                int errorCode = object.getInt("errorCode");
                response.setErrorCode(errorCode);
            }

            if (object.has("total") && !object.isNull("total")) {
                if (object.getInt("total") != 0) {
                    response.setTotal(object.getInt("total"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//			response = null;
            CommonUtils.log("parseJsonToResponse ---> \n :" + e.toString());
        }
        return response;
    }


//	public BaseResponse parseJsonToNewResponse(String data, Type type) {
//		BaseResponse response;
//		try {
//			Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();//GSON 解析
//			JSONObject object = new JSONObject(data);
//			boolean success = true;
//			String message = "";
//			
//			CommonUtils.log("----------parseJsonToNewResponse------------");
//			
//			Object dataModel = null;
//			if(object.has("data") && type != null) {
//				String dataString = object.get("data").toString();
//				dataModel = gson.fromJson(dataString, type);
//			}
//			
//			response = new BaseResponse(success, message, dataModel);
//			
//			response.setResult(data);
//		}catch(Exception e) {
//			e.printStackTrace();
//			response = null;
//		}
//		return response;
//	}

    @Override
    public void release() {
    }
}
