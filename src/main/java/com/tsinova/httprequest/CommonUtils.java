package com.tsinova.httprequest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {


	public static final String LOG_TAG = "tsinovaApp";

	public static final boolean isShowLog = true;
	
	/**
	 * 输出日志<br>
	 * 默认的日志级别是Log.Info
	 * 
	 * @param context
	 * @param text
	 */
	public static void log(String text) {
		log(LOG_TAG, text, Log.INFO);
	}

	/**
	 * 输出日志<br>
	 * 
	 * @param text
	 *            日志内容
	 * @param MODE
	 *            输出的模式<br>
	 *            (Log.VERBOSE Log.DEBUG Log.INFO Log.WARN Log.ERROR)
	 */
	public static void log(String text, int MODE) {
		log(LOG_TAG, text, MODE);
	}
	
	
	public static void log(String tag, String text) {
		log(tag, text, Log.INFO);
	}

	/**
	 * 输出日志<br>
	 * 
	 * @param tag
	 *            日志标签
	 * @param text
	 *            日志内容
	 * @param MODE
	 *            输出的模式<br>
	 *            (Log.VERBOSE Log.DEBUG Log.INFO Log.WARN Log.ERROR)
	 */
	public static void log(String tag, String text, int MODE) {
		if (isShowLog) {// 是否显示日志
			switch (MODE) {
			case Log.VERBOSE:
				Log.v(tag, text);
				break;
			case Log.DEBUG:
				Log.d(tag, text);
				break;
			case Log.INFO:
				Log.i(tag, text);
				break;
			case Log.WARN:
				Log.w(tag, text);
				break;
			case Log.ERROR:
				Log.e(tag, text);
				break;
			}
		}
	}

	/**
	 * 服务器返回数据是否成功
	 * 
	 * @param session
	 * @return
	 */
	public static boolean isReturnDataSuccess(Session session) {
		return session != null && session.getResponse() != null
				&& session.getResponse().isSuccess();
	}

	/**
	 * 解析网络数据失败提示
	 * 
	 */
	public static void accessNetWorkFailtureTip(Context context,
			BaseResponse response) {
		if (context != null) {
			if (response != null && !TextUtils.isEmpty(response.getMessage())) {
//				if(StringUtils.getLocalLanguage().equals("ja")){
//					UIUtils.toastFalse(context, R.string.backdata_emailwrong);
//				} else {
//					UIUtils.toastFalse(context, response.getMessage());
//				}
				UIUtils.toastFalse(context, response.getMessage());
			} else {
				UIUtils.toastFalse(context, R.string.httpsdk_network_connect_fail);
			}
		}
	}

	public static void accessNetWorkFailtureTip(Context context,
			Session session){
		accessNetWorkFailtureTip(context, session, false);
	}
	
	/**
	 * 解析网络数据失败提示
	 * 
	 */
	public static void accessNetWorkFailtureTip(Context context,
			Session session, boolean showDilag) {
		if (context != null) {
			if (session != null && session.getResponse() != null) {
				if (!showDilag) {
					accessNetWorkFailtureTip(context, session.getResponse());
				} else {
//					Dialog dialog = UIUtils.createDialog(context, "温馨提示",
//							session.getResponse().getMessage(), "确定",
//							new DialogInterface.OnClickListener() {
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									dialog.dismiss();
//								}
//							}, null, null);
//					dialog.show();
				}
			} else {
//				UIUtils.showCommonShortToast(context, "网络连接失败,请稍后重试");
			}
		}
	}

	/**
	 * 获取错误码
	 */
	public static Integer getNetWorkErrorCode(Context context, Session session) {
		if (context != null) {
			if (session != null && session.getResponse() != null
					&& (session.getResponse().getErrorCode() != null)) {
				return session.getResponse().getErrorCode();
			}
		}
		return -1;
	}

	/**
	 * 控制台输出文本即System.out.println
	 * 
	 * @param message
	 */
	public static void systemout(String message) {
		if (isShowLog) {
			System.out.println(message);
		}
	}

	/**
	 * 输出异常信息 ，即调用CommonUtils.printStackTrace(e);
	 * 
	 * @param e
	 */
	public static void printStackTrace(Exception e) {
		if (isShowLog && e != null) {
			e.printStackTrace();
			CommonUtils.log(e.toString());
		}
	}

	/**
	 * 检测网络连接状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	/**
	 * 获取内部版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null && pi.versionCode >= 0) {
				return pi.versionCode;
			}
		} catch (Exception e) {
			printStackTrace(e);
		}
		return 0;
	}

	/**
	 * 获取内部版本号名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null && !TextUtils.isEmpty(pi.versionName)) {
				return pi.versionName;
			}
		} catch (Exception e) {
			printStackTrace(e);
		}
		return "1.0.0";
	}

	public static String formatDoubleDigit(double digit) {
		if (digit == (int) digit)
			return "" + (int) digit;
		else
			return "" + digit;
	}

	/**
	 * 获取设备的MAC地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 判断是否为会员
	 */
	public static boolean isMember(Integer memberTypeId) {
		if (memberTypeId != null && memberTypeId != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	 public static String getDate(String str){
			return str.substring(0, 10);
		}
	 
	/**
	 * 拨打电话
	 */
	public static void call(Context context, String phoneNum) {
		phoneNum = phoneNum.replace("-", "");
		Pattern p = Pattern.compile("\\d+?");
		Matcher match = p.matcher(phoneNum);
		// 正则验证输入的是否为数字
		if (match.matches()) {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ phoneNum));
			context.startActivity(intent);
		} else {
			Toast.makeText(context, "号码格式不正确", Toast.LENGTH_SHORT).show();
		}
	}
}
