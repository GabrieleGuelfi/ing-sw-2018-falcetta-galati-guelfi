package it.polimi.se2018;

import java.util.*;

public class WindowPattern {

    private Box[][] grid ;
    private int favorToken;
    //private Die[] placedDie ;
    private int whiteBox;

    public WindowPattern(){

        grid = new Box[4][4];
        favorToken = 0;
        //placedDie = new Die[16];
        whiteBox = 16;

    }

    private void setFavorToken(int n){
        //Useless? Maybe.
        if (n<0) return;

        favorToken = n;

    }
    public int getFavorTokens() { return (favorToken); }

    public int getWhiteBox(){ return whiteBox;}

    public void putDice(Die d, Box b){
        if (d == null) return; //Exception??
        if (b == null) return; //Exception??

        b.setDie(d);

    }

    public void decreaseWhiteBox(){ whiteBox = whiteBox - 1;}
    public void decreaseFavorToken() { favorToken = favorToken - 1;}
    public void setBox(Box b, int row, int column){ grid[row][column] = b; }
    public Box getBox(int row, int column){ return grid[row][column];}


    public void showGrid(){}
    public void showPlacedDice(){}


}

