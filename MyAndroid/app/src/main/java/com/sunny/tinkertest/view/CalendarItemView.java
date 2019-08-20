package com.sunny.tinkertest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Android Studio.
 * Date: 2019/8/20
 * Time: 5:09 PM
 */
public class CalendarItemView extends LinearLayout {
    private TextView content;
    private View redPoint;


    public CalendarItemView(Context context) {
        this(context, null);
    }

    public CalendarItemView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CalendarItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
    private void init(Context context) {
        content = new TextView(context);
        redPoint = new View(context);
    }

    public void update(String text, boolean isRed) {

    }

    //被选中就将背景改为红色，字体改为白色
    public void setSelected() {

    }

}
