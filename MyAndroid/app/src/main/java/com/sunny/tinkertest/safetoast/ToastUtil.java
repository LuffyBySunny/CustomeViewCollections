package com.sunny.tinkertest.safetoast;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Created by SunShuo.
 * Date: 2019-10-16
 * Time: 17:44
 */
public class ToastUtil {

    private static Field sField_mTN;
    private static Field sField_mHandler;
    static {
        try {
            sField_mTN = Toast.class.getDeclaredField("mTN");
            sField_mTN.setAccessible(true);
            sField_mHandler = sField_mTN.getType().getDeclaredField("mHandler");
            sField_mHandler.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context context) {
        Toast toast = Toast.makeText(context, "测试",Toast.LENGTH_LONG);
        //如果是An
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

        }
        hook(toast);
        toast.show();

    }
    private static void hook(Toast toast) {
        try {
            Object mTN = sField_mTN.get(toast);
            //拿到Handler的实例
            //这个mHandler指向的还是TN实际的Handler
            Handler mHandler = (Handler) sField_mHandler.get(mTN);
            Handler newHandler = new SafeToastHandler(mHandler);
            //这里只是将
            sField_mHandler.set(mTN, newHandler);
            if (mHandler == newHandler) {
                Log.d("handler", "the same");
            } else {
                Log.d("handler", "different");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
