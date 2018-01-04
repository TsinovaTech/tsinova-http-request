package com.tsinova.httprequest;

import com.tsinova.bike.util.CommonUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class NetworkThreadPoolExecutor implements Singleton {
	private static NetworkThreadPoolExecutor instance;
	private static final int CORE_POOL_SIZE = 5;
	private static final int MAXIMUM_POOL_SIZE = 20;
	private static final int KEEP_ALIVE = 10;
	private static final BlockingQueue<Runnable> sWorkQueue = new LinkedBlockingQueue<Runnable>(10);
	private static RejectedExecutionHandler rejectHandler = new DiscardOldestPolicy();
	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);
		
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
		}
	};
	
	private static final ThreadPoolExecutor sExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
			sWorkQueue, sThreadFactory, rejectHandler);
	
	private NetworkThreadPoolExecutor() {
		super();
	}
	
	public static synchronized NetworkThreadPoolExecutor getInstance() {
		if(instance == null) {
			instance = new NetworkThreadPoolExecutor();
		}
		return instance;
	}
	
	public void execute(Runnable runnable) {
		try {
			sExecutor.execute(runnable);
		}catch(Exception e) {
			CommonUtils.printStackTrace(e);
		}
	}
	
	@Override
	public void init() {
		if(sWorkQueue != null) {
			sWorkQueue.clear();
		}
	}
	
	@Override
	public void release() {
	}
}
