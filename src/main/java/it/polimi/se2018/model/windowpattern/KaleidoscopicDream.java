package it.polimi.se2018.model.windowpattern;

import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Colour;

public class KaleidoscopicDream extends WindowPattern {

    public KaleidoscopicDream() {
        super(4);
        grid[0][0] = new Box(2);
        grid[0][3] = new Box(Colour.BLUE);
        grid[0][4] = new Box(Colour.YELLOW);
        grid[1][0] = new Box(3);
        grid[1][2] = new Box(Colour.RED);
        grid[1][4] = new Box(Colour.GREEN);
        grid[2][0] = new Box(Colour.GREEN);
        grid[2][2] = new Box(5);
        grid[2][5] = new Box(4);
        grid[3][0] = new Box(Colour.YELLOW);
        grid[3][1] = new Box(Colour.BLUE);
        grid[3][4] = new Box(1);
    }

}
