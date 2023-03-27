package com.example.myapplication;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

//этот класс представляет окружение
public class Figura {
    //владелец окружения (первоначальный)
    private Player player;
    //количество точек, которые окружены и принадлжеат другим игрокам
    private int capturedPoints = 0;
    //количество точек игрока, входящих в это окружение
    private int playerPoints = 0;
    //внутренние точки (без точек игрока-владельца)
    private ArrayList<Point> innerPoints;
    //все точки внутри или в кольце окружения, принадлежащие игроку
    private ArrayList<Point> thisPoints;
    private Paint paint;
    private Pair[] pairsX;
    private Pair[] pairsY;
    //private Path path;
    public Figura(){
        this.capturedPoints = 0;
        this.playerPoints = 0;
    }

    public Figura(Player player){
        this.capturedPoints = 0;
        this.playerPoints = 0;
        setPlayer(player);
    }

    public Figura(Player player, int capturedPoints, int playerPoints) {
        setPlayer(player);
        this.capturedPoints = capturedPoints;
        this.playerPoints = playerPoints;
    }

    public void setInnerPoints(ArrayList<Point> innerPoints) {
        capturedPoints = 0;
        playerPoints = 0;
        if(innerPoints != null){
            for(Point p : innerPoints){
                addPoint(p);
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.paint = new Paint();
        paint.setColor(player.getPaint().getColor());
    }

    public int getCapturedPoints() {
        return capturedPoints;
    }

    public void setCapturedPoints(int capturedPoints) {
        this.capturedPoints = capturedPoints;
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    //все внутренние точки, кроме нейтральных
    public int getAllColoredPointInside(){
        return playerPoints + capturedPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }

    public void draw(Canvas canvas){
        updatePairs();
        paint.setStrokeWidth(pairsX[0].max.getRadius());
        paint.setAlpha(100);
        int n = pairsX.length;

        for(int i = 0; i < n; i++){
            canvas.drawLine(pairsX[i].min.getxAbs(), pairsX[i].min.getyAbs(),
                    pairsX[i].max.getxAbs(), pairsX[i].max.getyAbs(), paint);
        }
        for(int i = 0; i < n; i++){
            canvas.drawLine(pairsY[i].min.getxAbs(), pairsY[i].min.getyAbs(),
                    pairsY[i].max.getxAbs(), pairsY[i].max.getyAbs(), paint);
        }
        /*canvas.drawLine(pairsX[0].min.getxAbs(), pairsX[0].min.getyAbs(),
                pairsX[n-1].min.getxAbs(), pairsX[n-1].min.getyAbs(), paint);*/
        /*canvas.drawLine(pairs[0].min.getxAbs(), pairs[i].min.getyAbs(),
                pairs[i].max.getxAbs(), pairs[i].max.getyAbs(), paint);*/
    }


    public void addPoint(Point point){
        if(point.hasPlayer()) {
            if (point.getPlayer() == this.getPlayer())
                increasePlayerPointsInside();
            else
                increaseCapturedPoints();
        }
        if(innerPoints == null)
            innerPoints = new ArrayList<>();
        if(thisPoints == null)
            thisPoints = new ArrayList<>();
        if(point.getPlayer() == player)
            thisPoints.add(point);
        else
            innerPoints.add(point);
    }

    public Point getInnerPoint(int index){
        return innerPoints.get(index);
    }

    private void increaseCapturedPoints(){
        this.capturedPoints++;
    }

    private void increasePlayerPointsInside(){
        this.playerPoints++;
    }

    public int numberOfInnerPoints(){
        if(innerPoints == null)
            return 0;
        return innerPoints.size();
    }
    //"домик" - это такое окружение, которое не имеет внутри себя точек противника
    public boolean isDomik() {
        return capturedPoints == 0;
    }

    /*public void updatePath(){
        path = new Path();
        path.reset();
        path.moveTo(thisPoints.get(0).getxAbs(), thisPoints.get(0).getyAbs());
        for(int i = 1; i < thisPoints.size(); i++){
            path.lineTo(thisPoints.get(i).getxAbs(), thisPoints.get(i).getyAbs());
        }
    }*/

    public void updatePointsStatus(){
        for(Point p : innerPoints){
            p.setFigura(this);
            player.addScore();
        }
        for(Point p : thisPoints){
            if(p.getFigura() != null){
                p.getFigura().getPlayer().subScore();
                p.setFigura(null);
            }
        }
    }

    public void updatePairs(){
        //по x
        int minX = Integer.MAX_VALUE, maxX = -1;
        //System.out.printf("Figura %d\n", thisPoints.size());
        for(Point p : thisPoints){
            minX = MainActivity.min(minX, p.getxRel());
            maxX = MainActivity.max(maxX, p.getxRel());
        }
        pairsX = new Pair[maxX - minX + 1];
        for(int i = 0; i < pairsX.length; i++)
            pairsX[i] = new Pair();
        for(Point p : thisPoints){
            int x = p.getxRel();
            if(pairsX[x - minX].min == null){
                pairsX[x - minX].min = p;
                pairsX[x - minX].max = p;
                continue;
            }
            if(pairsX[x - minX].min.getyRel() > p.getyRel()){
                pairsX[x - minX].min = p;
            }
            if(pairsX[x - minX].max.getyRel() < p.getyRel()){
                pairsX[x - minX].max = p;
            }
        }

        //по y
        int minY = Integer.MAX_VALUE, maxY = -1;
        //System.out.printf("Figura %d\n", thisPoints.size());
        for(Point p : thisPoints){
            minY = MainActivity.min(minY, p.getyRel());
            maxY = MainActivity.max(maxY, p.getyRel());
        }
        pairsY = new Pair[maxY - minY + 1];
        for(int i = 0; i < pairsY.length; i++)
            pairsY[i] = new Pair();
        for(Point p : thisPoints){
            int y = p.getyRel();
            if(pairsY[y - minY].min == null){
                pairsY[y - minY].min = p;
                pairsY[y - minY].max = p;
                continue;
            }
            if(pairsY[y - minY].min.getxRel() > p.getxRel()){
                pairsY[y - minY].min = p;
            }
            if(pairsY[y - minY].max.getxRel() < p.getxRel()){
                pairsY[y - minY].max = p;
            }
        }
    }
}