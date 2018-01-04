package com.tsinova.httprequest;

public interface Singleton {
	/**
	 * 单例对象初始化
	 */
	void init();
	
	/**
	 * 单例对象释放资源
	 */
	void release();
}
