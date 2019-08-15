package com.sunny.tinkertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Android Studio.
 * Date: 2019/8/8
 * Time: 1:55 PM
 */

public class RoundImageView2 extends AppCompatImageView {

    float width, height;
    int radius;
    Path path;
    private DrawFilter filter;
    private Paint paint;
    private Bitmap bitmap;

    public RoundImageView2(Context context) {
        this(context, null);
    }

    public RoundImageView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView2(Context context, AttributeSet attrs, int defStyleAttr) {
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
        radius = 100;
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        paint.setAntiAlias(true);
        filter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (width >= radius && height > radius) {
            //试试二阶的
            path.moveTo(0, 0);
            path.lineTo(width, 0);
            path.lineTo(width, height - radius);
            //path.quadTo(width, height, width - radius, height);
            path.cubicTo(width, height - radius / 2, width - radius / 2, height, width - radius, height);
            path.lineTo(radius, height);
            path.cubicTo(radius / 2, height, 0, height - radius / 2, 0, height - radius);
            path.lineTo(0, 0);
            canvas.setDrawFilter(filter);
            canvas.clipPath(path);
            super.onDraw(canvas);
        }
    }

    public void drawTopLeft() {

    }
}
