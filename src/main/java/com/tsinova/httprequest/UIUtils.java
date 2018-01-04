package com.tsinova.httprequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.InputStream;

public class UIUtils {

    /**
     * 设置光标位置
     *
     * @param et
     */
    public static void localEditCursor(EditText et) {
        et.requestFocus();
        if (!TextUtils.isEmpty(et.getText().toString())) {
            et.setSelection(et.getText().toString().length());// 将光标移至文字末尾
        } else {
            et.setSelection(0);
        }
    }

    /**
     * 带图片的Toast
     *
     * @param context
     * @param resId   图片
     * @param content 内容
     */
    private static void showPicToast(Context context, int resId, String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(context);
        imageCodeProject.setImageResource(resId);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }

    /**
     * 显示长消息
     *
     * @param context
     * @param str
     */
    public static void showCommonToast(Context context, String str) {
        if (!TextUtils.isEmpty(str) && context != null) {
            showToastMessage(context, str, true);
        }
    }

    /**
     * 显示长消息
     *
     * @param context
     * @param str
     * @param resId   没有的话，传-1
     */
    public static void showCommonToast(Context context, String str, int resId) {
        if (!TextUtils.isEmpty(str)) {
            if (resId == -1)
                showToastMessage(context, str, true);
            else
                showPicToast(context, resId, str);
        }
    }

    /**
     * 显示长消息
     *
     * @param context
     * @param str
     */
    public static void showQRErrorToast(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    /**
     * 显示短消息
     *
     * @param context
     * @param str
     */
    public static void showCommonShortToast(Context context, String str) {
        showToastMessage(context, str, false);
    }

    /**
     * @param context
     * @param str
     * @param isLong  显示时间长短 true--->Toast.LENGTH_LONG false--->Toast.LENGTH_SHORT
     */
    public static void showToastMessage(Context context, String str,
                                        boolean isLong) {
        if (str != null && !str.equals("")) {
            Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
            // toast.setGravity(Gravity.CENTER, 0, 0);
            if (isLong)
                toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }

    }

    /**
     * 标题对话框
     *
     * @param context
     * @param title   标题
     * @param ok      确认
     * @param cancel  取消
     * @param lOk     确认listener
     * @param lCancel 取消listener
     * @return
     */
    public static AlertDialog showAlert(final Context context,
                                        final String title, final String ok, final String cancel,
                                        final DialogInterface.OnClickListener lOk,
                                        final DialogInterface.OnClickListener lCancel, String content) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        // builder.setView(view);
        builder.setPositiveButton(ok, lOk);
        builder.setNegativeButton(cancel, lCancel);
        // builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    /**
     * 控制软键盘的显隐
     *
     * @param context
     * @param view    EditText
     * @param isShow  true,显示；false,隐藏
     */
    public static void hideSoftInputMethod(Context context, View view,
                                           boolean isShow) {
        InputMethodManager mInputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow)
            mInputMethodManager.showSoftInput(view, 0);
        else
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                    0);
    }


    public static void cancelNotification(Context context, int id) {
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(id);
    }

    /**
     * 为指定的View设置焦点
     *
     * @param view
     */
    public static void setFocusForView(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    /**
     * 根据内容显示或隐藏清除图标
     *
     * @param value
     * @param view
     */
    public static void showOrHideClearButton(String value, View view) {
        if (!TextUtils.isEmpty(value) && view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(value) && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置控件是否可点击
     *
     * @param context
     * @param isEnable
     * @param view
     * @param backgroundResourceId
     */
    @SuppressWarnings("deprecation")
    public static void setViewEnable(Context context, boolean isEnable,
                                     TextView view, int backgroundResourceId) {
        if (isEnable) {
            view.setEnabled(true);
            // view.setTextColor(context.getResources().getColor(R.color.common_white_color));
        } else {
            view.setEnabled(false);
            // view.setTextColor(context.getResources().getColor(R.color.common_white_color));
        }
        view.setBackgroundDrawable(context.getResources().getDrawable(
                backgroundResourceId));
    }

    /**
     * 设置商品列表图片
     */
    public static void setImage(Context context, ImageView view, String imageUrl) {
        try {
            if (imageUrl != null) {
                AssetManager asm = context.getAssets();
                InputStream is = asm.open(imageUrl);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                view.setImageBitmap(bitmap);
                is.close();
            } else {
                // view.setBackgroundResource(R.drawable.nopic);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 根据屏幕宽度和比例获取图片宽度
     *
     * @return
     */
    public static int getImageWidthPixels(Context context, int num) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels - ((int) (num * dm.density + 0.5f));
        return width;
    }

    public static int getImageheightPixels(int widthPixels, float proportion) {
        return (int) (widthPixels * proportion);
    }



//	/**
//	 * 自定义Toast
//	 */
//	public static void successToast(Context context, String message) {
//		Toast toast = new Toast(context);
//		View view = LayoutInflater.from(context).inflate(R.layout.register_success_toast, null);
//		TextView contant = (TextView) view.findViewById(R.id.toast_contant);
//		contant.setText(message);
//		toast.setView(view);
//		toast.setDuration(Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();
//	}

    public static void toastSuccess(Context context, int messageID) {
        String str = context.getResources().getString(messageID);
        toastFalse(context, str);
    }

    /**
     * 自定义Toast
     */
    public static void toastSuccess(Context context, String message) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.httpsdk_toast_success, null);
        TextView contant = (TextView) view.findViewById(R.id.toast_contant);
        contant.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    /**
     * 自定义Toast
     */
    public static void toastSuccess(Context context, String message,int duration) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.httpsdk_toast_success, null);
        TextView contant = (TextView) view.findViewById(R.id.toast_contant);
        contant.setText(message);
        toast.setView(view);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void toastFalse(Context context, int messageID) {
        try {
            String str = context.getResources().getString(messageID);
            toastFalse(context, str);
        } catch (Exception e) {
            CommonUtils.log("find bug ---------", e.getLocalizedMessage());
        }
    }


    private static Toast toast = null;

    /**
     * 自定义Toast
     */
    public static void toastFalse(Context context, String message) {
        if (toast == null) {
            toast = new Toast(context);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.httpsdk_toast_false, null);
        TextView contant = (TextView) view.findViewById(R.id.toast_contant);
        if (message.equals("")) {
            contant.setText(R.string.httpsdk_network_connect_fail);
        } else {
            contant.setText(message);
        }
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 自定义Toast
     */
    public static void toastFalse(Context context, String message,int duration) {
        if (toast == null) {
            toast = new Toast(context);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.httpsdk_toast_false, null);
        TextView contant = (TextView) view.findViewById(R.id.toast_contant);
        if (message.equals("")) {
            contant.setText(R.string.httpsdk_network_connect_fail);
        } else {
            contant.setText(message);
        }
        toast.setView(view);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showActionBar(ActionBar actionBar, String titleStr) {
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setIcon(drawable.logo_top);
//		if (titleStr != null) {
//			actionBar.setTitle(titleStr);
//		}

    }

    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    private static long lastClickTime;
    private static int mViewId;

    public synchronized static boolean isFastClick(View view) {
        long time = System.currentTimeMillis();
        if (view.getId() != mViewId) {
            mViewId = view.getId();
            lastClickTime = 0;
        }
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;

    }

}
