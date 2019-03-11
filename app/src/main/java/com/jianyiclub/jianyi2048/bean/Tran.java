package com.jianyiclub.jianyi2048.bean;

import android.graphics.Point;

/**
 * Created by wl624 on 2018/2/21.
 */

public class Tran {
    public Point fromPoint;
    public Point toPoint;
    public MyCard myCard;
    public int fromNum;
    public int toNum;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean flag;

    public int getFinalNum() {
        return finalNum;
    }

    public void setFinalNum(int finalNum) {
        this.finalNum = finalNum;
    }

    public int finalNum;

    public int getFromNum() {
        return fromNum;
    }

    public void setFromNum(int fromNum) {
        this.fromNum = fromNum;
    }

    public int getToNum() {
        return toNum;
    }

    public void setToNum(int toNum) {
        this.toNum = toNum;
    }

    public MyCard getMyCard() {
        return myCard;
    }

    public void setMyCard(MyCard myCard) {
        this.myCard = myCard;
    }

    public Point getFromPoint() {
        return fromPoint;
    }

    public void setFromPoint(Point fromPoint) {
        this.fromPoint = fromPoint;
    }

    public Point getToPoint() {
        return toPoint;
    }

    public void setToPoint(Point toPoint) {
        this.toPoint = toPoint;
    }
}
