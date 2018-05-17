package it.polimi.se2018.model.windowpattern;

import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Colour;

public class LuxAstram extends WindowPattern {

    public LuxAstram(){
        super(5);
        grid[1][0] = new Box(1);
        grid[1][1] = new Box(Colour.GREEN);
        grid[1][2] = new Box(5);
        grid[1][3] = new Box(3);
        grid[1][4] = new Box(Colour.PURPLE);
        grid[2][0] = new Box(6);
        grid[2][1] = new Box(Colour.PURPLE);
        grid[2][2] = new Box(2);
        grid[2][3] = new Box(5);
        grid[2][4] = new Box(Colour.GREEN);
        grid[3][1] = new Box(1);
        grid[3][2] = new Box(Colour.GREEN);
        grid[3][3] = new Box(Colour.PURPLE);
        grid[3][4] = new Box(4);
    }
}
