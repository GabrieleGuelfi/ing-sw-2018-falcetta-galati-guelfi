package it.polimi.se2018.model.publicObjective;

import it.polimi.se2018.exceptions.OutOfWindowPattern;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.WindowPattern;

import java.util.EnumMap;

public class DiffEverywhere extends PublicObjective{

    private boolean isColour;

    public DiffEverywhere(String description, int PV, boolean isColour) {
        super(description, PV);
        this.isColour = isColour;
    }

    @Override
    public int calcScore(WindowPattern windowPattern) {

        int count1=0, count2=0, count3=0, count4=0, count5=0, count6=0;

        for (int i=0; i<WindowPattern.Max_ROW; i++){
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
            return PV*(Math.min(count1, Math.min(count2, Math.min(count3, Math.min(count4, count5)))));
        return PV*(Math.min(count1, Math.min(count2, Math.min(count3, Math.min(count4, Math.min(count5, count6))))));
    }
}
