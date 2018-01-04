package com.tsinova.httprequest;

import android.os.Process;

import com.tsinova.bike.util.CommonUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class NetworkRequestBlockQueue implements Runnable, Singleton {
	private static NetworkRequestBlockQueue sInstance = null;
	private final BlockingQueue<HttpBlockQueueRequest> mRequests = new LinkedBlockingQueue<HttpBlockQueueRequest>();
	private final Thread mThread;
	
	protected NetworkRequestBlockQueue() {
		mThread = new Thread(this);
		mThread.start();
	}
	
	public synchronized static NetworkRequestBlockQueue getInstance() {
		if(sInstance == null) {
			sInstance = new NetworkRequestBlockQueue();
		}
		return sInstance;
	}
	
	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		while(true) {
			HttpBlockQueueRequest request;
			try {
				CommonUtils.log("-----RequestController mRequests.take()----------");
				request = mRequests.take();
			}catch(InterruptedException e) {
				continue;
			}
			request.runnable.run();
		}
	}
	
	public void addRequestToQueue(final CoreNetRequest request, final NetworkCallback callback) {
		CommonUtils.log("-----RequestController addRequestToQueue----------");
		put(request, new Runnable() {
			@Override
			public void run() {
				if("get".equals(request.getMothed())) {
					NetworkHttpRequest.executeHttpGet(request, callback);
				}else if("post".equals(request.getMothed())) {
					NetworkHttpRequest.executeHttpPost(request, callback);
				}else {
					throw new RuntimeException("请求方法异常");//TODO 硬处理
				}
			}
		});
	}
	
	private void put(CoreNetRequest request, Runnable runnable) {
		try {
			HttpBlockQueueRequest queueRequest = new HttpBlockQueueRequest();
			queueRequest.runnable = runnable;
			queueRequest.request = request;
			mRequests.add(queueRequest);
		}catch(IllegalStateException ie) {
			throw new Error(ie);
		}
	}
	
	class HttpBlockQueueRequest {
		public CoreNetRequest request;
		public Runnable runnable;
	}
	
	@Override
	public void init() {
		if(mRequests != null) {
			mRequests.clear();
		}
	}
	
	@Override
	public void release() {
	}
}
