package com.incast.udi.eduphone.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {
    /**
     * 长时间的Toast提示
     * @param context
     * @param msg
     */
    public static void showToast(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间的Toast提示
     * @param context
     * @param msgId
     */
    public static void showToast(Context context,int msgId){
        Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间的Toast提示
     * @param context
     * @param msgId
     */
    public static void showToastShort(Context context,int msgId){
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }


    /**
     * 短时间的Toast提示
     * @param context
     * @param msgId
     */
    public static void showToastShort(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 提示居中的Toast
     * @param mContext
     * @param str
     */
    public static void showToastCenter(Context mContext, String str) {
        Toast toast = new Toast(mContext);
        toast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);//设置居中
        toast.show();
    }
}
