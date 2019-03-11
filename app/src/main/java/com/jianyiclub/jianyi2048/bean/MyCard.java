package com.jianyiclub.jianyi2048.bean;

import android.graphics.Point;
import android.graphics.RectF;

/**
 * Created by wl624 on 2018/2/21.
 */

public class MyCard {
    public RectF rectF;
    public int num=0;
    public Point point;
    public int flag=0;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }



    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


}
