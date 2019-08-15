package com.sunny.tinkertest

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator


/**
 * Created by Android Studio.
 * Date: 2019/8/7
 * Time: 5:14 PM
 */
class BezierView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    var color  = Color.RED
    set(value) {
        paint.color = value
        invalidate()
    }
    private var mPointPath : Path = Path()
    private val bezierPath = Path()
    private val mStartPoint = Point()
    private val mControlPoint = Point()
    private val mEndPoint = Point()
    private val drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val itemWidth = 600
    private val halfItemWidth = 300
    private var offset : Int = 0
    private val valueAnimator : ValueAnimator by lazy{
        ValueAnimator.ofInt(0, itemWidth)
    }
    init {
        paint.color = Color.RED
        paint.strokeWidth = 5F
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        //起点
        mStartPoint.set(0, 300)
        valueAnimator.addUpdateListener {
            offset = it.animatedValue as Int
            invalidate()
        }
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.repeatCount = -1
        valueAnimator.start()
    }



    override fun onDraw(canvas: Canvas?) {
        //起点
        bezierPath.reset()
        bezierPath.moveTo(mStartPoint.x.toFloat(), mStartPoint.y.toFloat())

        bezierPath.moveTo(-itemWidth + offset.toFloat(), 300.toFloat())

        for (i in -300..width + 300 step itemWidth) {
            bezierPath.rQuadTo((halfItemWidth / 2).toFloat(), (-100).toFloat(), halfItemWidth.toFloat(), 0F)
            bezierPath.rQuadTo((halfItemWidth / 2).toFloat(), 100F, halfItemWidth.toFloat(), 0F)
        }
        bezierPath.lineTo(width.toFloat(), height.toFloat())
        bezierPath.lineTo(0F, height.toFloat())
        bezierPath.close()
        canvas?.drawPath(bezierPath, paint)

    }
}