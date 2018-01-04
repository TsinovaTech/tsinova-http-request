package com.tsinova.httprequest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	@SuppressLint("NewApi")
	public static boolean isEmpty(String str) {
		return (str == null) || (str.equals(""));
	}

	/**
	 * 验证码是否有效(6位数字)
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isValideVerify(String s) {
		return s.matches("^\\d{6}$");
	}

	/**
	 * 验证用户名是否有效(4-20位字符)
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isValideAccount(String s) {
		return s.matches("^\\w{4,20}$");
	}
	/**
	 * 验证密码是否有效(8-16位字符)
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isValidePwd(String s) {
		return s.matches("^\\w{8,10000}$");
	}
	/**
	 * 验证是否位车辆条形码
	 */
	public static boolean isCarBluetoothNumber(String s) {
		return s.matches("^\\w{14}$");
	}

	/**
	 * 判断手机号是否合法 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 　　 *
	 * 联通：130、131、132、152、155、156、185、186 　　 * 电信：133、153、180、189、（1349卫通）
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isMobileLawful(String phoneNumber) {
		if (TextUtils.isEmpty(phoneNumber)) {
			return false;
		}
		String phone = phoneNumber.trim();
		if (TextUtils.isDigitsOnly(phone) && phone.length() == 11) {
			// String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
			// Pattern p = Pattern.compile(regExp);
			// Matcher m = p.matcher(phone);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isEmail(String email) {
		String str = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 使用java正则表达式去掉多余的.与0
	 * 
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}
	
	/**
	 * Double类型保留2位小数，返回String类型（千分符）
	 */
	public static String formatThousandDoubleToString(Double d) {

		if (d == null) {
			return "￥ 0.00";
		}
//		NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
//		return numberFormat.format(d);
		DecimalFormat df = new DecimalFormat(",###,##0.00");
		return "￥ " + df.format(d);
	}
	/**
	 * Double类型保留2位小数，返回String类型（千分符）
	 */
	public static String formatMoneyWithoutRMB(Double d) {
		
		if (d == null) {
			return "0.00";
		}
		DecimalFormat df = new DecimalFormat(",###,##0.00");
		return df.format(d);
	}

	/**
	 * Double类型保留一位小数，返回String类型（注意四舍五入的影响）
	 */
	public static String formatDoubleToString(Double d) {
		if (d == null) {
			return "0.0";
		}
		DecimalFormat df = new DecimalFormat("#0.0");
		return df.format(d);
	}

	/**
	 * Double类型保留一位小数，返回double类型（四舍五入）
	 */
	public static double formatDouble(double d) {
		return formatDouble(d, 1);
	}

	/**
	 * Double类型保留指定位数的小数，返回double类型（四舍五入） newScale 为指定的位数
	 */
	public static double formatDouble(double d, int newScale) {
		BigDecimal bd = new BigDecimal(d);
		return bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static String formatString(String d1, int newScale) {
		DecimalFormat f = null;
		double d = Double.valueOf(d1);
		switch (newScale) {
		case 0:
			f = new DecimalFormat("###0");
			break;
		case 1:
			d = formatDouble(d, 1);
			f = new DecimalFormat("###0.0");
			break;
		case 2:
			d = formatDouble(d, 2);
			f = new DecimalFormat("###0.00");
			break;
		default:
			break;
		}
		return (f.format(d));
	}

	/**
	 * 保留小数（注意四舍五入的影响）
	 * 
	 * @param d
	 *            , decimal
	 * @return
	 */
	public static String getDecimal(double d, int decimal) {
		DecimalFormat f = null;
		switch (decimal) {
		case 0:
			f = new DecimalFormat("###0");
			break;
		case 1:
			d = formatDouble(d, 1);
			f = new DecimalFormat("###0.0");
			break;
		case 2:
			d = formatDouble(d, 2);
			f = new DecimalFormat("###0.00");
			break;
		default:
			break;
		}
		return (f.format(d));
	}

	/**
	 * 从URL中获取指定参数的值
	 * 
	 * @param url
	 * @param paramName
	 * @return
	 */
	public static String getParamFromURL(String url, String paramName) {
		String value = "";
		if (url != null && !"".equals(url.trim()) && url.contains(paramName)) {
			int start = url.indexOf("?");
			String t = url.substring(start + 1);
			String[] s = t.split("&");
			if (s != null && s.length > 0) {
				for (int i = 0; i < s.length; i++) {
					if (s[i].contains(paramName) && s[i].contains("=")) {
						value = s[i].substring(s[i].indexOf("=") + 1);
						break;
					}
				}
			}
		}
		return value.trim();
	}

	/**
	 * 生成URL
	 * 
	 * @param maps
	 * @return
	 */
	public static String getUrlParamsFromHashMap(HashMap<String, Object> maps) {
		StringBuilder urlParams = new StringBuilder();
		if (maps != null && maps.size() > 0) {
			urlParams.append("?");
			for (java.util.Map.Entry<String, Object> entry : maps.entrySet()) {
				urlParams.append(entry.getKey() + "=" + entry.getValue() + "&");
			}
			if (urlParams.toString().endsWith("&")) {
				urlParams.deleteCharAt(urlParams.length() - 1);
			}
		}
		return urlParams.toString();
	}

	/**
	 * 获取AppKey
	 */
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	public static String replaceString(String s) {
		String temp = "";
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= 0x4e00) && (c <= 0x9fbb)) {
				temp += c;
			}
		}
		return temp;
	}
	
	@SuppressLint("SimpleDateFormat") 
	public static String dateToString(Date date,String type) {
        String str = null;  
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        if (type.equals("SHORT")) {  
            // 07-1-18  
            format = DateFormat.getDateInstance(DateFormat.SHORT);  
            str = format.format(date);  
        } else if (type.equals("MEDIUM")) {  
            // 2007-1-18  
            format = DateFormat.getDateInstance(DateFormat.MEDIUM);  
            str = format.format(date);  
        } else if (type.equals("FULL")) {  
            // 2007年1月18日 星期四  
            format = DateFormat.getDateInstance(DateFormat.FULL);  
            str = format.format(date);  
        }  
        return str; 
	}
	
//	public static boolean isJson(String json){
//		if(TextUtils.isEmpty(json)){
//			return false;
//		}
//		try{
//		    new JSONObject(json);
//		    return true;
//		}catch(Exception e){
//		  return false;
//		}
//	}
	
	
	private static final String KEY = "04030915";
	/**
	 * 获取蓝牙信息的加密key
	 * @param btName
	 * @return
	 */
	public static String getBikeKey(String btName) {
		if (TextUtils.isEmpty(btName)) {
			return "";
		}
		String btKey = btName.substring(btName.length() - 8, btName.length());
		return btKey + KEY;
	}
	
	/**
	 * 去掉解密后多余的字符
	 */
	public static String fromateJson(String json){
		String str;
		int last = json.lastIndexOf("}");
		if(last > 0){
			str = json.substring(0, last + 1);
//			if(StringUtils.isJson(str)){
				return str;
//			}
		}

		return null;
	}

	/**
	 * 判断是否为完整的json类型
	 * (简单判断--通过花括号个数判断)
	 */
	public static boolean isJson(String json) {
		int left = -1;
		if (!TextUtils.isEmpty(json) && json.startsWith("{")) {
			char[] strs = json.toCharArray();
			left = 0;
			for (char str: strs) {
				if(String.valueOf(str).equals("{")) {
					left++;
				} else if (String.valueOf(str).equals("}")) {
					left--;
				}
			}
		}
		return left == 0;
	}


	/**
	 * 获取本地语言标识
	 */
	public static String getLocalLanguage() {
		String str = Locale.getDefault().getLanguage();
		if (str.equals("zh")) {
			str = "cn";
		}else if (str.equals("es")) {
			str = "es";
		}else if (str.equals("ja")) {
			str = "ja";
		}else if (str.equals("ko")) {
			str = "ko";
		}else {
			str = "en";
		}

		CommonUtils.log("getLocalLanguage :" + str);
		return str;
	}

	/**
	 * 将秒转换成小时
	 * @param second 秒
	 * @return hour 小时（结果保留一位小数）
	 */
	public static String getHour(float second) {
		double hour = second / 60d / 60d;
		hour = formatDouble(hour);
		return String.valueOf(hour);
	}
}
