package com.tsinova.httprequest;

import android.util.Log;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;


public class NetworkHttpRequest {


    public static void executeHttpGet(CoreNetRequest request, NetworkCallback httpCallback) {
        CommonUtils.log("---------------->executeHttpGet");
        long startTime = System.currentTimeMillis();

        String data = "";
        HttpGet httpget = null;
        try {
//			String url = request.isLogin() ? request.getNormalURLParams() : request.getURLParams();
            String url = request.getNormalURLParams();
//			if(!TextUtils.isEmpty(AppParams.getInstance().getToken())){
//				url = url + "&token=" +  AppParams.getInstance().getToken();
//			}
            httpget = new HttpGet(url);
            CommonUtils.log("*****************请求信息：\n" + url + "\n");
            httpget.addHeader("Accept-Encoding", "gzip,deflate");
            HttpResponse response = HttpClientManager.execute(httpget);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity responseEntity = response.getEntity();
                InputStream is = responseEntity.getContent();
                org.apache.http.Header contentEncoding = responseEntity.getContentEncoding();
                if (contentEncoding != null && contentEncoding.getValue() != null && contentEncoding.getValue().contains("gzip")) {
                    is = new GZIPInputStream(is);
                }
                data = transferInputStreamToString(is);
                is.close();
                responseEntity.consumeContent();
            } else {
                CommonUtils.log("响应异常，响应吗为：\n" + (response != null && response.getStatusLine() != null ? response.getStatusLine().getStatusCode() : -9999), Log.ERROR);
                data = "{\"success\":false, \"errorCode\":-999}";
            }
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
            data = "{\"success\":false, \"errorCode\":-999}";
        } finally {
            if (httpget != null) {
                httpget.abort();
            }
        }
        long endTime = System.currentTimeMillis();
        CommonUtils.log("*****************响应信息：" + "响应时间:" + (endTime - startTime) / 1000.0 + "秒 \ndata:\n" + data + "\n");
        httpCallback.onResult(request, data);
    }

    public static void executeHttpPost(CoreNetRequest request, NetworkCallback httpCallback) {
        long startTime = System.currentTimeMillis();
        CommonUtils.log("*****************post请求信息start*****************");
        String data = "";
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(request.getUrl());
            httpPost.addHeader("Accept-Encoding", "gzip,deflate");
            List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
//			if(request.isGsonDate() && !TextUtils.isEmpty(request.getGsonString())){
//				CommonUtils.log("\n?data=" + request.getGsonString() + "");
//				parameters.add(new BasicNameValuePair("data", request.getGsonString()));
//			}else if(!TextUtils.isEmpty( request.getParamsByJsonString())){
//				CommonUtils.log("\n?data=" + request.getParamsByJsonString() + "");
//				parameters.add(new BasicNameValuePair("data", request.getParamsByJsonString()));
//			}
//			parameters.add(new BasicNameValuePair("token", AppParams.getInstance().getToken()));

            parameters = request.getParameters();
            CommonUtils.log(request.getUrl() + "?" + request.getParamsByJsonString() + "");
            CommonUtils.log("*****************post请求信息end*****************");
            HttpEntity requestEntity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
            httpPost.setEntity(requestEntity);
            HttpResponse response = HttpClientManager.execute(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity responseEntity = response.getEntity();
                InputStream is = responseEntity.getContent();
                org.apache.http.Header contentEncoding = responseEntity.getContentEncoding();
                if (contentEncoding != null && contentEncoding.getValue() != null && contentEncoding.getValue().contains("gzip")) {
                    is = new GZIPInputStream(is);
                }
                data = transferInputStreamToString(is);
                is.close();
                responseEntity.consumeContent();
            } else {
                CommonUtils.log("响应异常，响应吗为：" + (response != null &&
                        response.getStatusLine() != null ? response.getStatusLine().getStatusCode() : -9999), Log.ERROR);
                data = "{\"success\":false, \"errorCode\":-999}";
            }
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
            data = "{\"success\":false, \"errorCode\":-999}"; // 网络连接失败,请稍后重试
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
        }
        long endTime = System.currentTimeMillis();
        CommonUtils.log("*****************响应信息start*****************" + "\n响应时间:" + (endTime - startTime) / 1000.0 + "秒 \n data:\n" + data + "\n*****************响应信息end*****************");
        httpCallback.onResult(request, data);
    }

    public static void executeHttpDelete(CoreNetRequest request, NetworkCallback httpCallback) {
        long startTime = System.currentTimeMillis();
        CommonUtils.log("*****************post请求信息start*****************");
        String data = "";
        HttpDelete httpDelete = null;
        try {
            httpDelete = new HttpDelete(request.getUrl());

            HttpResponse response = HttpClientManager.execute(httpDelete);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity responseEntity = response.getEntity();
                InputStream is = responseEntity.getContent();
                org.apache.http.Header contentEncoding = responseEntity.getContentEncoding();
                if (contentEncoding != null && contentEncoding.getValue() != null && contentEncoding.getValue().contains("gzip")) {
                    is = new GZIPInputStream(is);
                }
                data = transferInputStreamToString(is);
                is.close();
                responseEntity.consumeContent();
            } else {
                CommonUtils.log("响应异常，响应吗为：" + (response != null &&
                        response.getStatusLine() != null ? response.getStatusLine().getStatusCode() : -9999), Log.ERROR);
                data = "{\"success\":false, \"errorCode\":-999}";
            }
        } catch (Exception e) {
            CommonUtils.printStackTrace(e);
            data = "{\"success\":false, \"errorCode\":-999}"; // 网络连接失败,请稍后重试
        } finally {
            if (httpDelete != null) {
                httpDelete.abort();
            }
        }
        long endTime = System.currentTimeMillis();
        CommonUtils.log("*****************响应信息start*****************" + "\n响应时间:" + (endTime - startTime) / 1000.0 + "秒 \n data:\n" + data + "\n*****************响应信息end*****************");
        httpCallback.onResult(request, data);
    }

    private static String transferInputStreamToString(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count = -1;
        while ((count = in.read(data, 0, 4096)) != -1) {
            outStream.write(data, 0, count);
        }
        data = null;
        return new String(outStream.toByteArray(), "UTF-8");
    }

}
