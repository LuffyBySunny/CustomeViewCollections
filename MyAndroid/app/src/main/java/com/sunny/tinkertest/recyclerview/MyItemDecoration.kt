package com.sunny.tinkertest.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sunny.tinkertest.bean.CityBean
import com.sunny.tinkertest.R
import java.util.*

/**
 * Created by sunshuo.
 * Date: 2019/8/15
 * Time: 10:03 AM
 * 在Canvas上绘制layout资源文件
 */
class MyItemDecoration(val context: Context, val datas : ArrayList<CityBean>) : RecyclerView.ItemDecoration() {


    var headerheight = 0
    private var view : View? = null
    private var headerView : View? = null
    //画悬浮头部，就是画第一个显示的item的tag
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //头部
        val firstPosition = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val tag = datas[firstPosition].tag

        //实现往上顶的效果
        if (firstPosition + 1 < parent.adapter?.itemCount!!) {
            val nextChild = parent.findViewHolderForAdapterPosition(firstPosition + 1)?.itemView
            //如果下一个item有一个新的header
            if (datas[firstPosition].tag != datas[firstPosition + 1].tag) {
                //拿到当前item的header的top
                val top = nextChild?.top!! - headerheight
                //当前的header要遮盖上一个header的时候，将上一个header往上平移
                if (top <= headerheight) {
                    c.save()
                    c.clipRect(parent.paddingLeft.toFloat(), 0F, (parent.right - parent.paddingRight).toFloat(), top.toFloat())
                    c.translate(parent.paddingLeft.toFloat(), (top - headerheight).toFloat())
                    headerView?.draw(c)
                    c.restore()
                    return
                }

            }
        }


        c.save()
        c.clipRect(parent.paddingLeft.toFloat(),parent.paddingTop.toFloat(), (parent.right - parent.paddingRight).toFloat(), (headerheight + parent.paddingTop).toFloat())
        c.translate(0F, 0F)
        headerView?.findViewById<TextView>(R.id.groupText)?.text = tag
        headerView?.draw(c)
        c.restore()

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //outRect相当于给view添加Padding
        val position =  parent.getChildAdapterPosition(view)
        view.top
        val params = view.layoutParams as RecyclerView.LayoutParams
        if (position > -1) {
            when {
                //如果是第一个位置绘制头部
                position == 0 -> outRect.set(parent.paddingLeft, headerheight,0,0)
                //这个tag和上一个tag不一样，就绘制
                datas[position].tag != datas[position-1].tag -> {
                    outRect.set(parent.paddingLeft, headerheight, 0, 0)
                }
                //在最后一个预留尾部，拿到最后一个view的高度
                position == parent.adapter?.itemCount!! - 1 -> {
                    outRect.set(parent.paddingLeft, 0,0,parent.bottom - parent.paddingBottom -  headerheight * 2)
                    Log.d("Item", position.toString() +"parent.bottom---" + parent.bottom + "view.bottom--" + view.height)
                }

                else -> outRect.set(0,0,0,0)
            }
        }
        //Measure, layout, draw,三大步骤，才能画到canvas上面
        //先拿到每次的Header，避免卡顿
        this.view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false)?.apply {
            this.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.WRAP_CONTENT)
            val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(parent.width - parent.paddingLeft-parent.paddingRight, View.MeasureSpec.EXACTLY)
            val heightMeasureSpec = if (this.layoutParams.height > 0) {
                View.MeasureSpec.makeMeasureSpec(params.height, View.MeasureSpec.AT_MOST)
            } else {
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            }
            measure(widthMeasureSpec, heightMeasureSpec)
            layout(left, view.top - headerheight - params.topMargin, right, view.top - params.topMargin)

        }
        if (headerView == null) {
            headerView = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false)?.apply {
                this.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.WRAP_CONTENT)
                val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(parent.width - parent.paddingLeft-parent.paddingRight, View.MeasureSpec.EXACTLY)
                val heightMeasureSpec = if (this.layoutParams.height > 0) {
                    View.MeasureSpec.makeMeasureSpec(params.height, View.MeasureSpec.AT_MOST)
                } else {
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                }
                measure(widthMeasureSpec, heightMeasureSpec)
                layout(0, 0, right, headerheight)
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
            //拿到child
            val params = child.layoutParams as RecyclerView.LayoutParams
            val position = params.viewLayoutPosition
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

        view?.findViewById<TextView>(R.id.groupText)?.text = datas[position].tag
        c.save()
        c.translate(left.toFloat(), (child.top - headerheight - params.topMargin).toFloat())
        c.clipRect(0F, 0F, right.toFloat(), (child.top - params.topMargin).toFloat())
        view?.draw(c)
        c.restore()


    }
}