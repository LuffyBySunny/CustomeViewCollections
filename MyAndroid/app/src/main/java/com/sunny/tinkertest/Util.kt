package com.sunny.tinkertest

import android.graphics.Paint

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