package it.polimi.se2018.model.windowpattern;

import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Colour;

public class Bellesguard extends WindowPattern {

    public Bellesguard() {
        super(3);
        grid[0][1] = new Box(4);
        grid[0][3] = new Box(1);
        grid[0][4] = new Box(Colour.GREEN);
        grid[1][1] = new Box(5);
        grid[1][2] = new Box(6);
        grid[1][3] = new Box(2);
        grid[2][1] = new Box(3);
        grid[2][2] = new Box(Colour.BLUE);
        grid[3][0] = new Box(Colour.BLUE);
        grid[3][1] = new Box(6);
        grid[3][4] = new Box(Colour.YELLOW);
    }
}
