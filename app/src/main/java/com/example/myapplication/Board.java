package com.example.myapplication;

import static java.lang.StrictMath.round;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.EditText;

import java.util.ArrayDeque;
import java.util.ArrayList;

//игровое поле, на котором игроки ставят свои точки
public class Board {
    //количество пунктов (мест куда можно поставить точки) по горизонтали и вертикали соответственно
    private int pointOX;
    private int pointOY;

    //размер стороны клетки
    private float side;

    //мин. и макс. значения координат относительно устройства в пределах которых можно ставить точки
    //xmin - это ширина "красной линии"
    private float xmin;
    private float ymin;
    private float xmax;
    private float ymax;

    //количество незанятых игроками точек
    private int freePoints;

    //краска для линий
    private Paint linePaint;
    //краска для разделяющей линии (красная линия в обычных тетрадях)
    private Paint dividingLinePaint;

    //список всех точек на поле
    private PointsArray points;

    private ArrayList<Figura> figuraArrayList;

    boolean isOver = false;

    private PlayersArray playersArray;

    public Board(int pointOX, Paint linePaint, Paint dividingLinePaint, PlayersArray playersArray) {
        this.pointOX = pointOX;
        xmax = Resources.getSystem().getDisplayMetrics().widthPixels - side;
        ymax = Resources.getSystem().getDisplayMetrics().heightPixels - side;
        side = xmax/(pointOX+5);
        pointOY = (int) ((int)ymax/side - 2);
        //обычно за красной линией находятся 3-4 квадратика по ширине
        xmin = 5*side;
        ymin = side;
        //xmax = xmin + (side * (pointOX - 1));
        //корректируем ymax
        ymax = ymin + (side * (pointOY - 1));
        freePoints = pointOX * pointOY;
        this.linePaint = linePaint;
        this.dividingLinePaint = dividingLinePaint;

        //создаём матрицу точек
        points = new PointsArray(pointOX, pointOY, xmin, ymin, side);

        figuraArrayList = new ArrayList<>();

        this.playersArray = playersArray;
    }

    public void draw(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        canvas.drawLine(xmin-side, 0, xmin-side, ymax+side, dividingLinePaint);
        for(int i = 0; i <= pointOX; i++){
            float a = xmin + i*side;
            canvas.drawLine(a, 0, a, ymax + side, linePaint);
        }
        for(int i = 0; i <= pointOY+1; i++){
            float a = i*side;
            canvas.drawLine(0, a, xmax+side, a, linePaint);
        }
        points.draw(canvas);
        for(int i = 0; i < figuraArrayList.size(); i++){
            if(!figuraArrayList.get(i).isDomik())
                figuraArrayList.get(i).draw(canvas);
        }

        if(isOver){
            Paint fontPaint = new Paint();
            fontPaint.setColor(Color.BLACK);
            fontPaint.setTextSize(side);
            float xDraw = Resources.getSystem().getDisplayMetrics().widthPixels;
            float yDraw = Resources.getSystem().getDisplayMetrics().heightPixels;
            //System.out.printf("ydraw = %f\n", yDraw);
            canvas.drawText("Игра окончена!", xDraw/2-5*side, yDraw/2, fontPaint);
            canvas.drawText("Финальный счёт:", xDraw/2-5*side, yDraw/2+2*side, fontPaint);
            canvas.drawText(String.format("%s: %d | %s: %d",
                    playersArray.getPlayer(0).getName(), playersArray.getPlayer(0).getScore(),
                    playersArray.getPlayer(1).getName(), playersArray.getPlayer(1).getScore()),
                    xDraw/2 - 5*side, yDraw/2+4*side, fontPaint);
        }
    }

    public void addPoint(int xRel, int yRel, Player player){
        points.get(xRel, yRel).setPlayer(player);
        freePoints--;
    }

    //возвращает true, если удалось поставить новую точку на игровом поле
    public boolean addPoint(float xAbs, float yAbs, Player player){
        Point point = getPointByAbsCoords(xAbs, yAbs);
        return addPoint(point, player);
    }

    public boolean addPoint(Point point, Player player){
        if(point != null && !point.hasPlayer()) {
            if(point.getFigura() == null || point.getFigura().isDomik()) {
                point.setPlayer(player);
                freePoints--;
                return true;
            }
        }
        return false;
    }

    //Позволяет получить ближайшую точку относительно xAbs, yAbs. Возвращает null если координаты лежат в недопустимых значениях
    public Point getPointByAbsCoords(float xAbs, float yAbs){
        int xRel = round((xAbs - xmin) / side);
        int yRel = round(((yAbs - ymin) / side));
        if(xRel >= 0 && xRel < pointOX && yRel >= 0 && yRel < pointOY){
            return points.get(xRel, yRel);
        }
        return null;
    }

    //проверяет не ялвяется ли точка start частью окружения (окружений)
    // и если да, то возвращает это окружение (окружения) в качестве ответа, иначе null.
    public ArrayList<Figura> search(Point start){
        //по идее ситуации когда "пустая" точка сюда попадает не должно быть, но это проверка на всякий случай
        if(!start.hasPlayer()){
            return null;
        }
        ArrayList<Figura> figuraList = new ArrayList<>();
        //проверка на случай, если точка попала внутрь "домика"
        Figura figura = start.getFigura();
        if(figura != null){
            figura.addPoint(start);
            figuraList.add(figura);
            return figuraList;
        }

        //подготовка к поиску в ширину (он повторяется 4 раза по разным направлениям)
        int[][] used = points.getUsed();
        ArrayList<Point> near = getNear(start);
        int color = 0;
        for(Point currentPoint : near){
            color++;
            if(currentPoint.getPlayer() == start.getPlayer() || onEdge(currentPoint.getxRel(), currentPoint.getyRel())){
                continue;
            }
            figura = new Figura(start.getPlayer());
            figura.addPoint(currentPoint);
            figura.addPoint(start);
            //здесь начинается поиск в ширину
            ArrayDeque<Point> q = new ArrayDeque<>();
            q.offer(currentPoint);
            //System.out.println(q.offer(currentPoint));
            //System.out.println(q.isEmpty());
            //System.out.printf("%d %d\n", start.getxRel(), start.getyRel());
            used[currentPoint.getxRel()][currentPoint.getyRel()] = color;
            boolean isFigura = true;
            while (isFigura && !q.isEmpty()){
                //System.out.println("hello");
                Point v = q.pollFirst();
                ArrayList<Point> nearV = getNear(v);
                for (Point to : nearV){
                    int u = used[to.getxRel()][to.getyRel()];
                    if(u == 0){
                        figura.addPoint(to);
                        if(to.getPlayer() == start.getPlayer() && to.getFigura() == null){
                            continue;
                        }
                        if(onEdge(to.getxRel(), to.getyRel())){
                            isFigura = false;
                            break;
                        }
                        used[to.getxRel()][to.getyRel()] = color;
                        q.offer(to);
                    }else{
                        if(u == color){
                            continue;
                        }
                        isFigura = false;
                        break;
                    }

                }
            }
            if(isFigura){
                figura.updatePointsStatus();
                figuraArrayList.add(figura);
            }
        }
        return null;
    }

    public boolean isInside(int xRel, int yRel){
        return (xRel >= 0 && yRel >= 0 && xRel < pointOX && yRel < pointOY);
    }
    public boolean onEdge(int xRel, int yRel){
        return (xRel == 0 || yRel == 0 && xRel == pointOX - 1 || yRel == pointOY - 1);
    }

    //возвращает соседние точки по вертикали и горизонтали
    public ArrayList<Point> getNear(Point point){
        int x = point.getxRel();
        int y = point.getyRel();

        ArrayList near = new ArrayList<Point>();

        if(x + 1 < pointOX)
            near.add(points.get(x+1, y));
        if(x - 1 >= 0)
            near.add(points.get(x-1, y));
        if(y + 1 < pointOY)
            near.add(points.get(x, y+1));
        if(y - 1 >= 0)
            near.add(points.get(x, y-1));

        return near;
    }

    public void over(){
        System.out.println("Game over");
        linePaint.setAlpha(100);
        dividingLinePaint.setAlpha(100);
        for(int i = 0; i < playersArray.size(); i++){
            playersArray.getPlayer(i).getPaint().setAlpha(100);
        }
        isOver = true;
    }
}
