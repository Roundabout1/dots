package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Point {
    //xAbs и yAbs - это координаты точки в пикселях
    private float xAbs;
    private float yAbs;

    //xRel и yRel - это индексы в матрице точек из Board
    private int xRel;
    private int yRel;

    private Paint paint;
    private float radius;

    private Player player;
    private Figura figura;


    public Point(float xAbs, float yAbs, int xRel, int yRel, float radius) {
        this.xAbs = xAbs;
        this.yAbs = yAbs;
        this.xRel = xRel;
        this.yRel = yRel;
        this.paint = new Paint();
        this.radius = radius/2;
    }


    //x и y - это координаты цента точки
    public void draw(Canvas canvas){
        canvas.drawOval(xAbs - radius, yAbs - radius, xAbs+radius, yAbs+radius, paint);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean hasPlayer(){
        return player != null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.paint = player.getPaint();
    }

    public Figura getFigura() {
        return figura;
    }

    public void setFigura(Figura figura) {
        this.figura = figura;
    }

    public float getxAbs() {
        return xAbs;
    }

    public void setxAbs(float xAbs) {
        this.xAbs = xAbs;
    }

    public float getyAbs() {
        return yAbs;
    }

    public void setyAbs(float yAbs) {
        this.yAbs = yAbs;
    }

    public int getxRel() {
        return xRel;
    }

    public void setxRel(int xRel) {
        this.xRel = xRel;
    }

    public int getyRel() {
        return yRel;
    }

    public void setyRel(int yRel) {
        this.yRel = yRel;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
