package com.example.myapplication;

import android.graphics.Canvas;

public class PointsArray {
    private int OX;
    private int OY;
    private Point[][] points;

    public PointsArray(int OX, int OY, float xmin, float ymin, float side) {
        this.points = new Point[OX][OY];
        for(int i = 0; i < OX; i++){
            for(int j = 0; j < OY; j++){
                this.points[i][j] = new Point(xmin+side*i, ymin+side*j, i, j, side/2);
            }
        }
        this.OX = OX;
        this.OY = OY;
    }

    public PointsArray(PointsArray other) {
        this.OX = other.OX;
        this.OY = other.OY;
        this.points = other.points;
    }

    public Point get(int x, int y){
        return points[x][y];
    }

    public void draw(Canvas canvas){
        for(int i = 0; i < OX; i++)
            for(int j = 0; j < OY; j++) {
                if(points[i][j].hasPlayer()) {
                    points[i][j].draw(canvas);
                }
            }
    }

    public int getOX() {
        return OX;
    }

    public int getOY() {
        return OY;
    }

    public int[][] getUsed(){
        int[][] used = new int[getOX()][getOY()];
        for(int i = 0; i < getOX(); i++){
            for(int j = 0; j < getOY(); j++) {
                used[i][j] = 0;
            }
        }
        return used;
    }
}
