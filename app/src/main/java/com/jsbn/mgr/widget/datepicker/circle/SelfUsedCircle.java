package com.jsbn.mgr.widget.datepicker.circle;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;

import com.jsbn.mgr.R;

/**
 * 自己占用样式
 * Created by 13510 on 2015/9/11.
 */
public class SelfUsedCircle {

    private float x, y;
    private int radius;

    private ShapeDrawable shape;

    public static SelfUsedCircle createCircle(int x, int y, int circleRadius, Context context){
        OvalShape ovalShape = new OvalShape();
        ovalShape.resize(0, 0);
        ShapeDrawable drawable = new ShapeDrawable(ovalShape);
        SelfUsedCircle circle = new SelfUsedCircle(drawable);
        circle.setX(x);
        circle.setY(y);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            circle.setRadius(circleRadius);
        }
        drawable.getPaint().setColor(context.getResources().getColor(R.color.calendar_used));
        return circle;
    }

    public SelfUsedCircle(ShapeDrawable shape) {
        this.shape = shape;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public ShapeDrawable getShape() {
        return shape;
    }

    public void setShape(ShapeDrawable shape) {
        this.shape = shape;
    }
}
