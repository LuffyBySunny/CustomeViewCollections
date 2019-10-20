package com.sunny.tinkertest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Android Studio.
 * Date: 2019/8/20
 * Time: 5:08 PM
 * 日期视图
 */
public class CalendarView extends ViewGroup {

    public void setViewHolder(CourseTableContact.CourseTableViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    private CourseTableContact.CourseTableViewHolder viewHolder;
    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void updateRedPoint(List<Calendar> list) {

    }

}
