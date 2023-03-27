package com.example.myapplication;

import android.graphics.Color;
import android.graphics.Paint;

public class PlayersArray {

    private Player[] players;
    private int indexOfCurrentPlayer;
    public PlayersArray(int numberOfPlayers){
        this.players = new Player[numberOfPlayers];
    }

    //конструктор для создания двух игроков (в качестве входных данных используются цвета игроков)
    public PlayersArray(int c1, int c2){
        this.players = new Player[2];
        Paint paint1 = new Paint();
        paint1.setColor(c1);
        this.players[0] = new Player("Player 1", paint1);

        Paint paint2 = new Paint();
        paint2.setColor(c2);
        this.players[1] = new Player("Player 2", paint2);
    }

    public Player getPlayer(int index){
        if(index < 0 || index >= size())
            return null;
        return players[index];
    }

    public void setPlayer(int index, Player player){
        players[index] = player;
    }

    public int size(){
        return players.length;
    }

    public void nextPlayer(){
        this.indexOfCurrentPlayer = (this.indexOfCurrentPlayer + 1)%size();
    }

    public Player getCurrentPlayer(){
        return getPlayer(indexOfCurrentPlayer);
    }

    public boolean gameOver(int maxScore){
        for(int i = 0; i < players.length; i++){
            if(players[i].getScore() >= maxScore){
                return true;
            }
        }
        return false;
    }

    /*public void setTransperency(int alpha){
        for(int i = 0; i < size(); i++){
            players[i].setColor(Graphica.getColorWithTransparency(players[i].getColor(), alpha));
        }
    }*/
}
