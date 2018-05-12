package it.polimi.se2018.model.publicObjective;

import it.polimi.se2018.exceptions.OutOfWindowPattern;
import it.polimi.se2018.model.WindowPattern;

public class SetShades extends PublicObjective{

    private int set;
    private int numberSet1;
    private int numberSet2;

    public SetShades (String description, int PV, int set) {
        super(description, PV);
        this.set = set;
    }

    @Override
    public int calcScore(WindowPattern windowPattern) {
        numberSet1 = 0;
        numberSet2 = 0;
        for (int i=0; i<windowPattern.Max_ROW; i++) {
            for (int j = 0; j < windowPattern.MAX_COL; j++) {
                try {
                    if (windowPattern.getBox(i, j).getDie().getValue() == set)
                        numberSet1++;
                    if (windowPattern.getBox(i, j).getDie().getValue() == set + 1)
                        numberSet2++;
                } catch (OutOfWindowPattern | NullPointerException e) {}
            }
        }
        return PV*Math.min(numberSet1, numberSet2);
    }
}
