package com.sunny.tinkertest;

import android.util.SparseIntArray;


import java.util.Calendar;

/**
 * Created by SunShuo
 * Date: 2019/8/20
 * Time: 6:19 PM
 * 日期计算辅助工具类
 * 在Java中周日，一，二，三，四，五，六，分别对应1，2，3，4，5，6，7
 */
public class CalendarUtil {


    //缓存每个月的数据行数
    private static SparseIntArray array = new SparseIntArray();

    /**
     * 获取某月的天数
     *
     * @param year  年
     * @param month 月
     * @return 某月的天数
     */
    public static int getMonthDaysCount(int year, int month) {
        int count = 0;
        //判断大月份
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12) {
            count = 31;
        }

        //判断小月
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            count = 30;
        }

        //判断平年与闰年
        if (month == 2) {
            if (isLeapYear(year)) {
                count = 29;
            } else {
                count = 28;
            }
        }
        return count;
    }
    /**
     * 是否是闰年
     *
     * @param year year
     * @return 是否是闰年
     */
    public static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }

    /**
     * @param year 年份
     * @param month 月份
     * @return 当月日期的行数
     */
    public static int getMonthViewLineCount(int year, int month, int weekStartWith) {

        //可以做个缓存
        int nextDiff = CalendarUtil.getMonthEndDiff(year, month, weekStartWith);
        int preDiff = CalendarUtil.getMonthViewStartDiff(year, month, weekStartWith);
        int monthDayCount = CalendarUtil.getMonthDaysCount(year, month);

        return (preDiff + monthDayCount + nextDiff) / 7;
    }

    /**
     * @param year 年份
     * @param month 月份
     * @param weekStart 周起始{@link Constant.WeekStartDay}
     * @return 获取日期所在月视图对应的结束偏移量
     */
    private static int getMonthEndDiff(int year, int month, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, getMonthDaysCount(year, month));
        //本月最后一天是周几
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);


        return week == 1 ? 0 : 7 - week + 1;

    }

    /**
     * @param year 年份
     * @param month 月份
     * @param weekStart 周起始{@link Constant.WeekStartDay}
     * @return 获取日期所在月视图对应的起始偏移量
     */
    public static int getMonthViewStartDiff(int year, int month, int weekStart) {
        java.util.Calendar date = java.util.Calendar.getInstance();
        date.set(year, month - 1, 1);
        //本月第一天是周几
        int week = date.get(java.util.Calendar.DAY_OF_WEEK);


        return week == 1 ? 6 : week - weekStart;
    }

    /**
     * 得到某年某月的第一天是周几
     */

    public static int getWeekdayOfFirstDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }




    public static Calendar getCalendarByPosition(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 500);
        return calendar;
    }


}
