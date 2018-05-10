package it.polimi.se2018.model;

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
        //if (d == null) return; //Exception??
        //if (b == null) return; //Exception??

        grid[row][column].setDie(d);
        decreaseEmptyBox();

    }

    private void decreaseEmptyBox(){ emptyBox = emptyBox - 1;}


    public void setBox(Box b, int row, int column){ grid[row][column] = b; } //costruttore..?

    public Box getBox(int row, int column){ return grid[row][column];}

}

