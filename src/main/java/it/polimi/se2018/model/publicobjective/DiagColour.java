package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.exceptions.OutOfWindowPattern;
import it.polimi.se2018.model.WindowPattern;

/**
 * public objective that check how many dice of the same colour are diagonally adjacent are placed in the whole window pattern
 * @author Gabriele Guelfi
 */
public class DiagColour extends PublicObjective {

    /**
     * Class Constructor
     * @param description the short description of the objective
     * @param vp how much points give the objective
     */
    public DiagColour (String description, int vp) {
        super(description, vp);
    }

    /**
     * calculate the score of the windowPattern
     * @param windowPattern the scheme on which the score is calculated
     * @return the score of windowPattern about this objective
     */
    @Override
    public int calcScore(WindowPattern windowPattern) {

        int points=0;
        int [][] checked = new int[3][5];
        boolean found;

        for (int i=0; i<WindowPattern.MAX_ROW-1; i++) {
            for (int j=0; j < WindowPattern.MAX_COL; j++) {
                found = false;
                try {
                    if(windowPattern.getBox(i, j).getDie().getColour() == windowPattern.getBox(i+1, j-1).getDie().getColour()) {
                        found = true;
                        points += 1;
                        checked[i+1][j-1] = 1;
                    }
                } catch (OutOfWindowPattern | NullPointerException e) {}
                try {
                    if(windowPattern.getBox(i, j).getDie().getColour() == windowPattern.getBox(i+1, j+1).getDie().getColour()) {
                        found = true;
                        points += 1;
                        checked[i+1][j+1] = 1;
                    }
                } catch (OutOfWindowPattern | NullPointerException e) {}
                if (found && checked[i][j] != 1)
                    points += 1;
            }
        }
        return points;
    }

}
