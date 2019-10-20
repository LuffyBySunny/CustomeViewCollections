package com.sunny.tinkertest.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.provider.CalendarContract
import android.util.AttributeSet
import android.view.View

/**
 * Created by SunShuo.
 * Date: 2019-10-08
 * Time: 16:54
 */
class RotateView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val rectF = RectF(100.0F, 100.0F, 200.0F, 200.0F)

    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        ValueAnimator.ofInt()
        canvas?.drawArc(rectF, 0F, Math.PI.toFloat(),true,paint)
    }




}