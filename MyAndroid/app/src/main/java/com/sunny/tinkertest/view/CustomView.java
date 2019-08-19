package com.sunny.tinkertest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.sunny.tinkertest.R;

/**
 * Created by Android Studio.
 * Date: 2019/8/13
 * Time: 8:10 PM
 */
public class CustomView extends RelativeLayout {
    private View rootView;
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.test, this);

    }
}
