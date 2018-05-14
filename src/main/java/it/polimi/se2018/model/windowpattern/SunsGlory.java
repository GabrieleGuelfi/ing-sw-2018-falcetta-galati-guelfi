package it.polimi.se2018.model.windowpattern;

import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Colour;

public class SunsGlory extends WindowPattern {

    public SunsGlory() {
        super(6);
        grid[0][1] = new Box(5);
        grid[0][2] = new Box(4);
        grid[0][3] = new Box(2);
        grid[0][4] = new Box(1);
        grid[1][0] = new Box(Colour.YELLOW);
        grid[1][3] = new Box(5);
        grid[1][4] = new Box(3);
        grid[2][0] = new Box(Colour.PURPLE);
        grid[2][1] = new Box(Colour.YELLOW);
        grid[2][4] = new Box(6);
        grid[3][0] = new Box(1);
        grid[3][1] = new Box(Colour.PURPLE);
        grid[3][2] = new Box(Colour.YELLOW);
        grid[3][4] = new Box(4);
    }
}
