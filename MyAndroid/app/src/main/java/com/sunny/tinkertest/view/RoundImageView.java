package com.sunny.tinkertest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.sunny.tinkertest.R;

/**
 * Created by Android Studio.
 * Date: 2019/8/8
 * Time: 1:55 PM
 */

public class RoundImageView extends AppCompatImageView {

    //左上角
    private static final int TOP_LEFT = 0;
    //右上角
    private static final int TOP_RIGHT = 1;
    //右下角
    private static final int BOTTOM_RIGHT = 2;
    //左下角
    private static final int BOTTOM_LEFT = 3;
    //上两角
    private static final int TOP = 4;
    //右两角
    private static final int RIGHT = 5;
    //下两角
    private static final int BOTTOM = 6;
    //左两角
    private static final int LEFT = 7;
    //所有角
    private static final int ALL = 8;
    float width, height;
    private int radius;
    //默认全角
    private int roundType = 8;
    Path path;
    private DrawFilter filter;
    private Paint paint;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            roundType = array.getInteger(R.styleable.RoundImageView_round_type, 8);
            radius = array.getDimensionPixelSize(R.styleable.RoundImageView_round_radius, 0);
            array.recycle();
        }
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        filter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width > radius && height > radius) {
            switch (roundType) {
                case TOP_LEFT:
                    drawTopLeft();
                    break;
                case TOP_RIGHT:
                    drawTopRight();
                    break;
                case BOTTOM_RIGHT:
                    drawBottomRight();
                    break;
                case BOTTOM_LEFT:
                    drawBottomLeft();
                    break;
                case TOP:
                    drawTop();
                    break;
                case RIGHT:
                    drawRight();
                    break;
                case BOTTOM:
                    drawBottom();
                    break;
                case LEFT:
                    drawLeft();
                    break;
                case ALL:
                    drawAll();
                    break;
            }
            canvas.setDrawFilter(filter);
            canvas.clipPath(path);
        }
        super.onDraw(canvas);

    }


    private void drawTopLeft() {
        path.moveTo(0, radius);
        path.cubicTo(0, radius / 2, radius / 2, 0, radius, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
    }

    private void drawTopRight() {
        path.moveTo(0, 0);
        path.lineTo(width - radius, 0);
        path.cubicTo(width - radius / 2, 0, width, radius / 2, width, radius);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
    }

    private void drawBottomRight() {
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height - radius);
        path.cubicTo(width, height - radius / 2, width - radius / 2, height, width - radius, height);
        path.lineTo(0, height);
        path.close();
    }

    private void drawBottomLeft() {
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height);
        path.lineTo(radius, height);
        path.cubicTo(radius / 2, height, 0, height - radius / 2, 0, height - radius);
        path.close();
    }
    private void drawTop() {
        path.moveTo(0, radius);
        path.cubicTo(0, radius / 2, radius / 2, 0, radius, 0);
        path.lineTo(width - radius, 0);
        path.cubicTo(width - radius / 2, 0, width, radius / 2, width, radius);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
    }
    private void drawRight() {
        path.moveTo(0, 0);
        path.lineTo(width - radius, 0);
        path.cubicTo(width - radius / 2, 0, width, radius / 2, width, radius);
        path.lineTo(width, height - radius);
        path.cubicTo(width, height - radius / 2, width - radius / 2, height, width - radius, height);
        path.lineTo(0, height);
        path.close();
    }
    private void drawBottom() {
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height - radius);
        path.cubicTo(width, height - radius / 2, width - radius / 2, height, width - radius, height);
        path.lineTo(radius, height);
        path.cubicTo(radius / 2, height, 0, height - radius / 2, 0, height - radius);
        path.close();
    }

    private void drawLeft() {
        path.moveTo(width, 0);
        path.lineTo(width, height);
        path.lineTo(radius, height);
        path.cubicTo(radius / 2, height, 0, height - radius / 2, 0, height - radius);
        path.lineTo(0, radius);
        path.cubicTo(0, radius / 2, radius / 2, 0, radius, 0);
        path.close();
    }

    private void drawAll() {

        path.moveTo(width - radius, 0);
        path.cubicTo(width - radius / 2, 0, width, radius / 2, width, radius);
        path.lineTo(width, height - radius);
        path.cubicTo(width, height - radius / 2, width - radius / 2, height, width - radius, height);
        path.lineTo(radius, height);
        path.cubicTo(radius / 2, height, 0, height - radius / 2, 0, height - radius);
        path.lineTo(0, radius);
        path.cubicTo(0, radius / 2, radius / 2, 0, radius, 0);
        path.close();
    }
}
