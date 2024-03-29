package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.WindowPattern;

import java.util.ArrayList;

/**
 * public objective that check how many column with different colour or value's are placed in a row of the window pattern
 * @author Gabriele Guelfi
 */
public class DiffColumn extends PublicObjective {

    private ArrayList<Integer> shades;
    private ArrayList<Colour> colours;
    private boolean isColour;

    /**
     * Class Constructor
     * @param description the short description of the objective
     * @param vp how much points give the objective
     * @param isColour establish if calculate the different colour or shade
     */
    DiffColumn(int id, String description, int vp, boolean isColour) {
        super(id, description, vp);
        this.isColour = isColour;
    }

    /**
     * calculate the score of the windowPattern
     * @param windowPattern the scheme on which the score is calculated
     * @return the score of windowPattern about this objective
     */
    @Override
    public int calcScore(WindowPattern windowPattern) {

        boolean same;

        for (int j=0; j<WindowPattern.MAX_COL; j++) {
            same = false;
            if (isColour)
                colours = new ArrayList<>();
            else
                shades = new ArrayList<>();
            for (int i = 0; i<WindowPattern.MAX_ROW && !same; i++) {
                try {
                    if (isColour) {
                        if (!colours.contains(windowPattern.getBox(i, j).getDie().getColour())) {
                            colours.add(windowPattern.getBox(i, j).getDie().getColour());
                        }
                        else {
                            same = true;
                        }
                    }
                    else {
                        if (!shades.contains(windowPattern.getBox(i, j).getDie().getValue())) {
                            shades.add(windowPattern.getBox(i, j).getDie().getValue());
                        } else {
                            same = true;
                        }
                    }
                }
                catch (NullPointerException e) {
                    same = true;
                }
            }
            if (!same)
                points = points + vp;
        }
        return points;
    }
}
