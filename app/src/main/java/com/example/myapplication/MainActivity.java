package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.EditText;
import android.widget.TextView;

//меняем AppCompatActivity на Activity, потому что мы не будем использовать Action Bar
public class MainActivity extends Activity {
    private Board board;
    private GameView gameView;
    private PlayersArray players;
    //до скольки очков играем
    private int maxScore = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //добавляет fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        //создаём краски для линий, которые нужны будут для нарисования игрового поля board
        Paint linePaint = new Paint();
        linePaint.setColor(Color.rgb(3, 114, 255));
        Paint dividingLinePaint = new Paint();
        dividingLinePaint.setColor(Color.RED);
        players = new PlayersArray(Color.RED, Color.BLUE);
        board = new Board(10, linePaint, dividingLinePaint, players);
        //наш собственный SurfaceView
        gameView = new GameView(this, board);
        setContentView(gameView);
        Paint playerPaint = new Paint();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!players.gameOver(maxScore)) {
            Point p = board.getPointByAbsCoords(event.getX(), event.getY());
            if (board.addPoint(p, players.getCurrentPlayer())) {
                players.nextPlayer();
                board.search(p);
                System.out.printf("%d %d\n", players.getPlayer(0).getScore(), players.getPlayer(1).getScore());
            }
        }else{
            gameView.over();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        return super.onTouchEvent(event);
    }


    public static int min(int a, int b){
        if(a < b)
            return a;
        return b;
    }

    public static int max(int a, int b){
        if(a < b)
            return b;
        return a;
    }
}