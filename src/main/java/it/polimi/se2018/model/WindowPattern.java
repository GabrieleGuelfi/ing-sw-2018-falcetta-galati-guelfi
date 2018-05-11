package it.polimi.se2018.model;

import it.polimi.se2018.exceptions.OutOfWindowPattern;

public class WindowPattern {

    private Box[][] grid;
    private int difficulty;
    private int emptyBox;

    public WindowPattern(int difficulty){

        grid = new Box[4][5];
        this.difficulty = difficulty;
        //placedDie = new Die[16];
        emptyBox = 20;

    }


    public int getDifficulty() { return (difficulty); }

    public int getEmptyBox(){ return emptyBox;}

    public void putDice(Die d, int row, int column){
        //if (d == null) return; //exceptions??
        //if (b == null) return; //exceptions??

        grid[row][column].setDie(d);
        decreaseEmptyBox();

    }

    private void decreaseEmptyBox(){ emptyBox = emptyBox - 1;}


    public void setBox(Box b, int row, int column){ grid[row][column] = b; } //costruttore..?

    public Box getBox (int row, int column) throws OutOfWindowPattern {
        if (row < 0 || row > 3 || column < 0 || column > 4) throw new OutOfWindowPattern();
        return grid[row][column];
    }

}

