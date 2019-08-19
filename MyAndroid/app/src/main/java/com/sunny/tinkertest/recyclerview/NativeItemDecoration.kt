package com.sunny.tinkertest.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.sunny.tinkertest.bean.CityBean
import java.util.*

/**
 * Created by sunshuo
 * Date: 2019/8/15
 * Time: 7:45 PM
 * 完全用Paint绘制Header
 */
class NativeItemDecoration(val context: Context, val datas : ArrayList<CityBean>) : RecyclerView.ItemDecoration() {
    var headerheight = 0
    private val paint = Paint()
    private val bound = Rect()
    //画悬浮头部，就是画第一个显示的item的tag
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //头部
        val firstPosition = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val tag = datas[firstPosition].tag
        //第一个正在显示的item
        val child = parent.findViewHolderForAdapterPosition(firstPosition)?.itemView

        //实现往上顶的效果
        if (firstPosition + 1 < parent.adapter?.itemCount!!) {
            val nextChild = parent.findViewHolderForAdapterPosition(firstPosition + 1)?.itemView
            //如果下一个item有一个新的header
            if (datas[firstPosition].tag != datas[firstPosition + 1].tag) {
                //拿到当前item的header的top
                val top = nextChild?.top!! - headerheight
                //当前的header要遮盖上一个header的时候，将上一个header往上平移
                if (top <= headerheight) {
                    paint.color = Color.CYAN
                    c.drawRect(parent.paddingLeft.toFloat(), 0F, (parent.right - parent.paddingRight).toFloat(), top.toFloat(), paint)
                    paint.getTextBounds(tag,0, tag.length, bound)
                    paint.color = Color.RED
                    c.drawText(tag, nextChild.paddingLeft.toFloat(), (top - (headerheight - bound.height()) / 2).toFloat(), paint)
                    return
                }
            }
        }


        paint.color = Color.CYAN
        paint.textSize = 40F
        c.drawRect(parent.paddingLeft.toFloat(),parent.paddingTop.toFloat(), (parent.right - parent.paddingRight).toFloat(), (headerheight + parent.paddingTop).toFloat(), paint)
        paint.getTextBounds(tag,0, tag.length, bound)
        paint.color = Color.RED
        c.drawText(tag, child?.paddingLeft?.toFloat()!!,
                (parent.paddingTop + headerheight - (headerheight - bound.height()) / 2).toFloat(), paint)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //当前item的 position
        val position =  parent.getChildLayoutPosition(view)
        if (position > -1) {
            when {
                //如果是第一个位置绘制头部
                position == 0 -> outRect.set(parent.paddingLeft, headerheight,0,0)
                datas[position].tag != datas[position-1].tag -> //这个tag和上一个tag不一样，就绘制
                    outRect.set(parent.paddingLeft, headerheight,0,0)
                else -> outRect.set(0,0,0,0)
            }
        }

    }

    /**
     * 绘制在itemView的下层
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            //Log.d("child", child.toString())
            //拿到child
            val params = child.layoutParams as RecyclerView.LayoutParams
            //拿到child在数据中的位置
            val position = parent.getChildAdapterPosition(child)
            //开始绘制title
            if (position > -1) {
                when {
                    position == 0 -> drawTitle(c, left, right, child, params, position, parent)
                    datas[position].tag != datas[position - 1].tag ->
                        drawTitle(c, left, right, child, params, position, parent.parent as ViewGroup)
                }
            }
        }
    }

    private fun drawTitle(c: Canvas, left : Int, right : Int, child : View, params : RecyclerView.LayoutParams, position : Int, parent: ViewGroup) {

        //直接通过paint画上去

        //画背景
         paint.color = Color.CYAN
         c.drawRect(left.toFloat(), (child.top - headerheight - params.topMargin).toFloat(), right.toFloat(), (child.top - params.topMargin).toFloat(), paint)
         paint.color = Color.RED
         paint.textSize = 40F
         paint.getTextBounds(datas[position].tag, 0, datas[position].tag.length, bound)
         c.drawText(datas[position].tag, child.paddingLeft.toFloat(),
                 (child.top - params.topMargin - (headerheight - bound.height()) / 2).toFloat(), paint)
    }
}