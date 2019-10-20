package com.sunny.tinkertest

import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Android Studio.
 * Date: 2019/8/9
 * Time: 8:53 AM
 */

/**
 * 获取文字贴近x轴的上边缘的y坐标
 */


public fun Paint.getBottomY() : Float {
    //获取基线和文字底边缘的距离
    return - this.fontMetrics.bottom
}

public fun Paint.getCenterY() : Float {

    //两行文字的中心
    return this.fontSpacing / 2 - this.fontMetrics.bottom
}
//
fun Paint.getTopY() : Float {
    //基线到文字上边的距离
    return - this.fontMetrics.ascent
}

//将数字转换为中文,两位数之内
fun Int.toText() : String{
    //首先转为Int数组
    var result = ""
    val iArr = "$this".toCharArray().map { it.toString().toInt()}
    if (iArr.size > 1) {
        //在"十"之前将改数字加上
        if (iArr[0] != 1) {
            result += Text.NUMBER_TEXT_LIST[iArr[0]]
        }
        result += "十"
        //如果第二个数=0可直接跳过
        if (iArr[1] > 0) {
            result += Text.NUMBER_TEXT_LIST[iArr[1]]
        }
    } else {
        result = Text.NUMBER_TEXT_LIST[iArr[0]]
    }

    return result
}

object Text{
    val NUMBER_TEXT_LIST = listOf(
            "日",
            "一",
            "二",
            "三",
            "四",
            "五",
            "六",
            "七",
            "八",
            "九",
            "十"
    )
}

private fun getDeviceInfo(): String {
    val brand = Build.BRAND
    if (TextUtils.isEmpty(brand)) return "navigationbar_is_min"

    return if (brand.equals("HUAWEI", ignoreCase = true) || "HONOR" == brand) {
        "navigationbar_is_min"
    } else if (brand.equals("XIAOMI", ignoreCase = true)) {
        "force_fsg_nav_bar"
    } else if (brand.equals("VIVO", ignoreCase = true)) {
        "navigation_gesture_on"
    } else if (brand.equals("OPPO", ignoreCase = true)) {
        "navigation_gesture_on"
    } else if (brand.equals("samsung", ignoreCase = true)) {
        "navigationbar_hide_bar_enabled"
    } else {
        "navigationbar_is_min"
    }
}
/**
 * 判断设备是否存在NavigationBar
 *
 * @return true 存在, false 不存在
 */
fun deviceHasNavigationBar(): Boolean {
    var haveNav = false
    try {
        //1.通过WindowManagerGlobal获取windowManagerService
        // 反射方法：IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
        val windowManagerGlobalClass = Class.forName("android.view.WindowManagerGlobal")
        val getWmServiceMethod = windowManagerGlobalClass.getDeclaredMethod("getWindowManagerService")
        getWmServiceMethod.isAccessible = true
        //getWindowManagerService是静态方法，所以invoke null
        val iWindowManager = getWmServiceMethod.invoke(null)

        //2.获取windowMangerService的hasNavigationBar方法返回值
        // 反射方法：haveNav = windowManagerService.hasNavigationBar();
        val iWindowManagerClass = iWindowManager.javaClass
        val hasNavBarMethod = iWindowManagerClass.getDeclaredMethod("hasNavigationBar")
        hasNavBarMethod.isAccessible = true
        haveNav = hasNavBarMethod.invoke(iWindowManager) as Boolean
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return haveNav
}

private fun navigationGestureEnabled(context: Context): Boolean {
    val `val` = Settings.Global.getInt(context.getContentResolver(), getDeviceInfo(), 0)
    return `val` != 0
}

fun hasNavigationBar(context: Context): Boolean {
    //navigationGestureEnabled()从设置中取不到值的话，返回false，因此也不会影响在其他手机上的判断
    return deviceHasNavigationBar() && !navigationGestureEnabled(context)
}

fun getNavigationBarHeight(context: Context): Int {
    var result = 0
    if (hasNavigationBar(context)) {
        val res = context.resources
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
    }
    return result
}

fun getForceNavigationBarHeight(context: Context): Int {
    val res = context.resources
    var result = 0
    val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = res.getDimensionPixelSize(resourceId)
    }
    return result
}

fun getStatusBarHeight(context: Context?): Int {
    var statusBarHeight = 0
    if (context != null) {
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val o = c.newInstance()
            val field = c.getField("status_bar_height")
            val x = field.get(o) as Int
            statusBarHeight = context.resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    return statusBarHeight
}

