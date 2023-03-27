package com.example.myapplication;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

//цель этого потока заключается в том, чтобы обновлять класс GameView по ходу игры
public class MainThread extends Thread {
    //вспомогательный класс, помогающий canvas рисовать
    private GameView gameView;
    //нужный для синхронизации потоков класс
    private SurfaceHolder surfaceHolder;
    //при running == true поток выполняет свои действия в методе run, иначе бездействует
    private boolean running;
    //холст на котором программа рисует
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {

        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;

    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    @Override
    public void run() {
        while (running) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder) {
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {} finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean isRunning() {
        return running;
    }
}
