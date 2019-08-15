package com.sunny.tinkertest

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import java.util.*

/**
 * Created by Android Studio.
 * Date: 2019/8/8
 * Time: 8:16 PM
 * 中间文字
 * 时钟
 * 分钟
 * 秒钟
 */
class CircleClock @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mWidth = 0F
    private var mHeight = 0F
    private val mPaint = createPaint()
    private var valueAnimator : ValueAnimator

    //当前应该走的角度
    private var mHourD = 0F
    private var mMinuteD = 0F
    private var mSecondD = 0F
    //时钟的半径
    private val mHourR : Float by lazy {
        (mWidth * 0.12).toFloat()
    }
    //分钟的半径
    private val mMinuteR : Float by lazy {
        (mWidth * 0.35).toFloat()
    }
    //秒钟的半径
    private val mSecondR : Float by lazy {
        (mWidth * 0.35).toFloat()
    }
    init {
        mPaint.color = Color.RED
        mPaint.isAntiAlias = true
        valueAnimator = ValueAnimator.ofFloat(6F, 0F)
        valueAnimator.duration = 300
        valueAnimator.interpolator = LinearInterpolator()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mWidth = (measuredWidth - paddingLeft - paddingRight).toFloat()
        mHeight = (measuredHeight - paddingTop - paddingBottom).toFloat()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        canvas.drawColor(Color.BLACK)
        canvas.save()
        canvas.translate(mWidth / 2, mHeight / 2)


        drawCenterInfo(canvas)
        drawHour(canvas, mHourD)
        drawMinute(canvas, mMinuteD)
        drawSecond(canvas, mSecondD)
    }

    /**
     * 画秒钟
     */

    private fun drawSecond(canvas: Canvas, degree : Float) {
        canvas.save()
        canvas.rotate(degree)
        mPaint.textSize = mHourR * 0.25F
        for (i in 1..59) {
            canvas.save()
            val deg = 360 / 60F * i
            canvas.rotate(deg)
            mPaint.alpha = if (deg + degree == 0F) 255 else (0.6 * 255).toInt()
            mPaint.textAlign = Paint.Align.LEFT
            canvas.drawText("${(i).toText()}秒", mSecondR, mPaint.getCenterY(), mPaint)
            canvas.restore()
        }
        canvas.restore()
    }

    //画分钟
    private fun drawMinute(canvas: Canvas, degree : Float) {
        canvas.save()
        canvas.rotate(degree)
        mPaint.textSize = mHourR * 0.25F
        for (i in 0..59) {
            canvas.save()
            val deg = 360 / 60F * i
            canvas.rotate(deg)
            mPaint.textAlign = Paint.Align.RIGHT
            mPaint.alpha = if (deg + degree == 0F) 255 else (0.6 * 255).toInt()
            if (i != 0) {
                canvas.drawText("${(i).toText()}分", mMinuteR, mPaint.getCenterY(), mPaint)
            }
            canvas.restore()
        }
        canvas.restore()
    }

    /**
     * 画时钟
     */
    private fun drawHour(canvas: Canvas, degree : Float) {
        mPaint.textSize = mHourR * 0.3F
        canvas.save()
        canvas.rotate(degree)
        for (i in 0..11) {
            canvas.save()
            //每次绘制的度数
            val deg = 360 / 12F * i
            canvas.rotate(deg)
            mPaint.textAlign = Paint.Align.LEFT
            mPaint.alpha = if (deg + degree == 0F) 255 else (0.6 * 255).toInt()
            canvas.drawText("${(i+1).toText()}点", mHourR, mPaint.getCenterY(), mPaint)
            canvas.restore()
        }
        canvas.restore()

    }

    private fun drawCenterInfo(canvas: Canvas) {
        with(Calendar.getInstance()){
            val hour = get(Calendar.HOUR)
            val minute = get(Calendar.MINUTE).let {
                if (it >= 10) it.toString() else "0$it"
            }
            mPaint.textSize = mHourR * 0.5F
            mPaint.color = Color.WHITE
            mPaint.alpha = 255
            mPaint.textAlign = Paint.Align.CENTER
            canvas.drawText("$hour:$minute", 0f, mPaint.getBottomY(), mPaint)
            val month = (get(Calendar.MONTH) + 1).let {
                if (it < 10) "0$it" else "$it"
            }
            val day = get(Calendar.DAY_OF_MONTH).let {
                if (it < 10) "0$it" else "$it"
            }
            val weekday = get(Calendar.DAY_OF_WEEK).let {
                if (it - 1 != 7) (it-1).toText() else "日"
            }
            mPaint.textSize = mHourR * 0.3F
            mPaint.textAlign = Paint.Align.CENTER
            canvas.drawText("$month.$day 星期$weekday", 0F, mPaint.getTopY(), mPaint)
        }
    }

    /**
     * 创建画笔
     */
    private fun createPaint(colorString: String? = null, color: Int = Color.WHITE): Paint {
        return Paint().apply {
            this.color = if (colorString != null) Color.parseColor(colorString) else color
            this.isAntiAlias = true
            this.style = Paint.Style.FILL
        }
    }

    public fun doInvalidate() {
        Calendar.getInstance().run {
            mHourD = - (get(Calendar.HOUR) - 1) * 30F
            mMinuteD = - get(Calendar.MINUTE) * 6F
            mSecondD = - get(Calendar.SECOND) * 6F
            val h = mHourD
            val m = mMinuteD
            val s = mSecondD
            valueAnimator.removeAllUpdateListeners()
            valueAnimator.addUpdateListener {
                val av = it.animatedValue as Float
                if (get(Calendar.MINUTE) == 0 && get(Calendar.SECOND)==0) {
                    mHourD = h + av * 5
                }
                if (get(Calendar.SECOND) == 0) {
                    mMinuteD = m + av
                }
                mSecondD = s + av
                invalidate()
            }
            valueAnimator.start()
        }
    }

}