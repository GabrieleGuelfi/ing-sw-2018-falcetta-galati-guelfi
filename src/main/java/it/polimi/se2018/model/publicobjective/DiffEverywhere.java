package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.exceptions.OutOfWindowPattern;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.WindowPattern;

/**
 * public objective that check how many dice with different colour or value's are placed in the whole window pattern
 * @author Gabriele Guelfi
 */
public class DiffEverywhere extends PublicObjective{

    private boolean isColour;

    /**
     * Class Constructor
     * @param description the short description of the objective
     * @param vp how much points give the objective
     * @param isColour establish if calculate the different colour or shade
     */
    public DiffEverywhere(String description, int vp, boolean isColour) {
        super(description, vp);
        this.isColour = isColour;
    }

    /**
     * calculate the score of the windowPattern
     * @param windowPattern the scheme on which the score is calculated
     * @return the score of windowPattern about this objective
     */
    @Override
    public int calcScore(WindowPattern windowPattern) {

        int count1=0;
        int count2=0;
        int count3=0;
        int count4=0;
        int count5=0;
        int count6=0;

        for (int i = 0; i<WindowPattern.MAX_ROW; i++){
            for (int j=0; j<WindowPattern.MAX_COL; j++) {
                try {
                    if (isColour) {
                        if (windowPattern.getBox(i, j).getDie().getColour() == Colour.BLUE)
                            count1++;
                        else if (windowPattern.getBox(i, j).getDie().getColour() == Colour.GREEN)
                            count2++;
                        else if (windowPattern.getBox(i, j).getDie().getColour() == Colour.PURPLE)
                            count3++;
                        else if (windowPattern.getBox(i, j).getDie().getColour() == Colour.RED)
                            count4++;
                        else if (windowPattern.getBox(i, j).getDie().getColour() == Colour.YELLOW)
                            count5++;
                    }
                    else {
                        if (windowPattern.getBox(i, j).getDie().getValue() == 1)
                            count1++;
                        else if (windowPattern.getBox(i, j).getDie().getValue() == 2)
                            count2++;
                        else if (windowPattern.getBox(i, j).getDie().getValue() == 3)
                            count3++;
                        else if (windowPattern.getBox(i, j).getDie().getValue() == 4)
                            count4++;
                        else if (windowPattern.getBox(i, j).getDie().getValue() == 5)
                            count5++;
                        else if (windowPattern.getBox(i, j).getDie().getValue() == 6)
                            count6++;
                    }
                } catch (OutOfWindowPattern | NullPointerException e) {}
            }
        }
        if (isColour)
            return vp *(Math.min(count1, Math.min(count2, Math.min(count3, Math.min(count4, count5)))));
        return vp *(Math.min(count1, Math.min(count2, Math.min(count3, Math.min(count4, Math.min(count5, count6))))));
    }
}
