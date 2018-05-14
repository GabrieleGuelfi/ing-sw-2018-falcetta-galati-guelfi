package it.polimi.se2018.model.windowpattern;

import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Colour;

public class LuzCelestial extends WindowPattern {

    public LuzCelestial() {
        super(3);
        grid[0][1] = new Box(Colour.YELLOW);
        grid[0][2] = new Box(2);
        grid[1][0] = new Box(6);
        grid[1][3] = new Box(Colour.BLUE);
        grid[2][0] = new Box(Colour.PURPLE);
        grid[2][1] = new Box(4);
        grid[2][3] = new Box(Colour.GREEN);
        grid[2][4] = new Box(3);
        grid[3][2] = new Box(Colour.RED);
        grid[3][3] = new Box(5);
    }
}
