package it.polimi.se2018.model;

import java.io.Serializable;

public class WindowPattern implements Serializable {

    public static final int MAX_ROW = 4;
    public static final int MAX_COL = 5;
    private Box[][] grid;
    private int difficulty;
    private int emptyBox;
    private String name;

    public WindowPattern(String name, int difficulty){
        int i;
        int j;

        grid = new Box[MAX_ROW][MAX_COL];
        for(i = 0; i<4; i++) {
            for (j = 0; j < 5; j++) {
                grid[i][j] = new Box(0, Colour.WHITE);
            }
        }
        this.difficulty = difficulty;
        this.name = name;
        //placedDie = new Die[16];
        emptyBox = 20;

    }

    /**
     * @return name of this window pattern
     */
    public String getName() {
        return name;
    }

    /**
     * @return difficulty of this window pattern
     */
    public int getDifficulty() {
        return (difficulty);
    }

    /**
     * @return empty box of this window pattern
     */
    public int getEmptyBox(){
        return emptyBox;
    }

    /**
     * put a die in the row and column of this window pattern
     * @param d placed die
     * @param row row in which you want to put the die
     * @param column column in which you want to put the die
     */
    public void putDice(Die d, int row, int column){

        grid[row][column].setDie(d); //exception?
        decreaseEmptyBox();

    }

    /**
     * decrease by one the number of empty box
     */
    private void decreaseEmptyBox(){
        emptyBox = emptyBox - 1;
    }

    /**
     * put a box in row and column
     * @param b box you want to put
     * @param row in which you want to put the box
     * @param column column in which you want to put the box
     */
    public void setBox(Box b, int row, int column){
        grid[row][column] = b;
    }

    /**
     * @param row row of searched Box
     * @param column column of the searched Box
     * @return a box of the window pattern
     */
    public Box getBox (int row, int column) {
        if (row < 0 || row >= MAX_ROW || column < 0 || column >= MAX_COL) throw new IllegalArgumentException("Out of Window Pattern");
        return grid[row][column];
    }

    /**
     *
     * @return Copy of this Object
     */
    public WindowPattern copy(){
        int i;
        int j;
        WindowPattern w = new WindowPattern(this.name, this.difficulty);
        w.emptyBox = this.emptyBox;
        for(i = 0; i<4; i++){
            for(j = 0; j<5; j++){
                w.grid[i][j] = this.grid[i][j].copy();
            }
        }

        return w;
    }
}

