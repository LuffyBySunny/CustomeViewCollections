package com.sunny.tinkertest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Android Studio.
 * Date: 2019/8/20
 * Time: 4:35 PM
 */
public class WeekBar extends LinearLayout {
    private Context context;

    private String[] weeks = {"一","二","三","四","五","六","日"};
    public WeekBar(Context context) {
        this(context, null);
    }

    public WeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        for (int i = 0; i < 7; i++) {
            TextView textView = new TextView(context);
            textView.setText(weeks[i]);
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1F);
            params.gravity = Gravity.CENTER;
            addView(textView, params);
        }
    }
}
