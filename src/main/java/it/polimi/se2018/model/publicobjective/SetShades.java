package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.model.WindowPattern;

/**
 * public objective that check how many couples of dice with value set and set+1 are placed in the windowPattern
 * @author Gabriele Guelfi
 */
public class SetShades extends PublicObjective {

    private int set;

    /**
     * Class Constructor
     * @param description the short description of the objective
     * @param vp how much points give the objective
     * @param set the min value of the couple of shade
     */
    SetShades (String description, int vp, int set) {
        super(description, vp);
        this.set = set;
    }

    /**
     * calculate the score of the windowPattern
     * @param windowPattern the scheme on which the score is calculated
     * @return the score of windowPattern about this objective
     */
    @Override
    public int calcScore(WindowPattern windowPattern) {

        int numberSet1 = 0;
        int numberSet2 = 0;

        for (int i = 0; i<WindowPattern.MAX_ROW; i++) {
            for (int j = 0; j < WindowPattern.MAX_COL; j++) {
                try {
                    if (windowPattern.getBox(i, j).getDie().getValue() == set)
                        numberSet1++;
                    if (windowPattern.getBox(i, j).getDie().getValue() == set + 1)
                        numberSet2++;
                } catch (NullPointerException e) {}
            }
        }
        return vp *Math.min(numberSet1, numberSet2);
    }
}
