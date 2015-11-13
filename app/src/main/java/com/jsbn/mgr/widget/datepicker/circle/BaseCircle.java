package com.jsbn.mgr.widget.datepicker.circle;

import android.graphics.drawable.ShapeDrawable;

/**
 * Created by 13510 on 2015/9/11.
 */
public class BaseCircle {
    private float x, y;
    private int radius;
    private int colorRes;

    private ShapeDrawable shape;

    public BaseCircle(ShapeDrawable shape) {
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

    public BaseCircle createCircle(int colorRes){

        return null;
    }
}
