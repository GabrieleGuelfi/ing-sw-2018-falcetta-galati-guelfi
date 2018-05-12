package it.polimi.se2018.model;

import it.polimi.se2018.exceptions.OutOfWindowPattern;

public class WindowPattern {

    public static final int Max_ROW = 4;
    public static final int MAX_COL = 5;
    private Box[][] grid;
    private int difficulty;
    private int emptyBox;

    public WindowPattern(int difficulty){

        grid = new Box[Max_ROW][MAX_COL];
        this.difficulty = difficulty;
        //placedDie = new Die[16];
        emptyBox = 20;

    }


    public int getDifficulty() { return (difficulty); }

    public int getEmptyBox(){ return emptyBox;}

    public void putDice(Die d, int row, int column){

        grid[row][column].setDie(d);
        decreaseEmptyBox();

    }

    private void decreaseEmptyBox(){ emptyBox = emptyBox - 1;}


    public void setBox(Box b, int row, int column){ grid[row][column] = b; } //costruttore..?

    public Box getBox (int row, int column) throws OutOfWindowPattern {
        if (row < 0 || row >= Max_ROW || column < 0 || column >= MAX_COL) throw new OutOfWindowPattern();
        return grid[row][column];
    }

    /**
     *
     * @return Copy of this Object
     */
    public WindowPattern copy(){

        WindowPattern w = new WindowPattern(this.difficulty);
        w.emptyBox = this.emptyBox;
        for(int i = 0; i<4; i++){
            for(int j = 0; j<5; j++){
                w.grid[i][j] = this.grid[i][j].copy();
            }
        }

        return w;
    }
}

