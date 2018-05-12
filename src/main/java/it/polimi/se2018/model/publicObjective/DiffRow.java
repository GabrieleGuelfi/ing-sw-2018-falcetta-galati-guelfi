package it.polimi.se2018.model.publicObjective;

import it.polimi.se2018.exceptions.OutOfWindowPattern;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.WindowPattern;

import java.util.ArrayList;

public class DiffRow extends PublicObjective {

    private ArrayList<Integer> shades;
    private ArrayList<Colour> colours;
    private boolean same;
    private boolean isColour;

    public DiffRow(String description, int PV, boolean isColour) {
        super(description, PV);
        this.isColour = isColour;
    }

    @Override
    public int calcScore(WindowPattern windowPattern) {

        for (int i=0; i<windowPattern.MAX_COL; i++) {
            if (isColour)
                colours = new ArrayList<>();
            else
                shades = new ArrayList<>();
            same = false;
            for (int j=0; j<windowPattern.Max_ROW; j++) {
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
                catch (OutOfWindowPattern e) {}
                catch (NullPointerException e) {
                    same = true;
                }
            }
            if (!same)
                points = points + PV;
        }
        return points;
    }
}
