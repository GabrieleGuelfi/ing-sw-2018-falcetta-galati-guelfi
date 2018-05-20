package it.polimi.se2018.model;

public class WindowPattern {

    public static final int MAX_ROW = 4;
    public static final int MAX_COL = 5;
     Box[][] grid;
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

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return (difficulty);
    }

    public int getEmptyBox(){
        return emptyBox;
    }

    public void putDice(Die d, int row, int column){

        grid[row][column].setDie(d); //exception?
        decreaseEmptyBox();

    }

    private void decreaseEmptyBox(){ emptyBox = emptyBox - 1;}


    public void setBox(Box b, int row, int column){ grid[row][column] = b; } //costruttore..?

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

