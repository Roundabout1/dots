package com.example.myapplication;

import android.graphics.Paint;

public class Player {
    //краска которой окрашиваются точки этого игрока
    private Paint paint;
    //имя игрока
    private String name;
    //количество окруженных точек противника
    private int score;
    public Player(String name, Paint paint) {
        this.name = name;
        this.paint = paint;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public Paint getPaint(){
        return paint;
    }

    public void addScore(int sum){
        score += sum;
    }
    public void addScore(){
        score++;
    }
    public void subScore(int sum){
        score -= sum;
    }
    public void subScore(){
        score--;
    }


    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
