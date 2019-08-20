package com.sunny.tinkertest.view;

import android.view.View;

import java.util.Date;

/**
 * Created by Android Studio.
 * Date: 2019/8/20
 * Time: 5:29 PM
 */
public class CourseTableContact {
    public interface Presenter{
        void isHasCourse(String date);
    }
    public interface CourseTableViewHolder{
        View getView();
    }
}
