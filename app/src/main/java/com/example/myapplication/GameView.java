package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

//Этот класс позволяет рисовать на игровом холсте.
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Board board;
    public GameView(Context context, Board board) {
        super(context);

        this.board = board;
        //Добавив Callback, мы можем перехватывать события (events).
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        thread = new MainThread(getHolder(), this);
        thread.start();
        /*if(thread.getState() == Thread.State.NEW) {
            thread.start();
        }*/
        thread.setRunning(true);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }




    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void over(){
        board.over();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            board.draw(canvas);
        }
    }


}
