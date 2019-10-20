package com.sunny.tinkertest.safetoast;

import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

/**
 * Created by SunShuo.
 * Date: 2019-10-16
 * Time: 17:42
 */
public class SafeToastHandler extends Handler {
    //原Handler
    private Handler mImp;
    public SafeToastHandler(Handler handler) {
        mImp = handler;
    }

    @Override
    public void handleMessage(Message msg) {
        try {
            mImp.handleMessage(msg);
        } catch (WindowManager.BadTokenException e) {
            //ignore
        }
    }
}
