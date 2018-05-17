package it.polimi.se2018.model.windowpattern;

import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Colour;

public class FractalDrops extends WindowPattern {

    public FractalDrops() {
        super(3);
        grid[0][0] = new Box(Colour.BLUE);
        grid[0][1] = new Box(Colour.YELLOW);
        grid[1][2] = new Box(Colour.RED);
        grid[1][3] = new Box(Colour.PURPLE);
        grid[1][4] = new Box(1);
        grid[2][0] = new Box(Colour.RED);
        grid[2][2] = new Box(2);
        grid[3][1] = new Box(4);
        grid[3][3] = new Box(Colour.YELLOW);
        grid[3][4] = new Box(6);
    }
}
