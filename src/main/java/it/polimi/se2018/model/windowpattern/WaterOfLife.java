package it.polimi.se2018.model.windowpattern;

import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Colour;

public class WaterOfLife extends WindowPattern {

    public WaterOfLife() {
        super(6);
        grid[0][0] = new Box(Colour.GREEN);
        grid[0][1] = new Box(6);
        grid[0][2] = new Box(Colour.YELLOW);
        grid[0][3] = new Box(3);
        grid[0][4] = new Box(Colour.PURPLE);
        grid[1][0] = new Box(4);
        grid[1][1] = new Box(Colour.RED);
        grid[1][2] = new Box(2);
        grid[1][3] = new Box(Colour.BLUE);
        grid[2][1] = new Box(5);
        grid[2][2] = new Box(Colour.PURPLE);
        grid[3][0] = new Box(6);
        grid[3][1] = new Box(Colour.BLUE);
        grid[3][4] = new Box(1);
    }
}
