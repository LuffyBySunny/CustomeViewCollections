package com.sunny.tinkertest;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SunShuo.
 * Date: 2019-10-15
 * Time: 15:38
 */
public class test {
    //判断URL是否以http或者https开头
    public static boolean isUrl(String url) {
        return  !TextUtils.isEmpty(url) && ("http".equals(url.split(":")[0]) || "https".equals(url.split(":")[0]));
    }

    public static boolean test(String test) {
       String regex = "^[0-9A-Za-z]{8,16}$";
        Pattern r = Pattern.compile(regex);
        Matcher matcher = r.matcher(test);
        return matcher.matches();
    }

    public static String replace(String str) {
        String regex = "^(\\d{3})\\d{3}(\\d{3})";
        return str.replaceAll(regex, "***");
    }
}
