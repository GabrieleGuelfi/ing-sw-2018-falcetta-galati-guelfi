package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.WindowPattern;

import java.util.*;

/**
 * public objective that check how many dice with different colour or value's are placed in the whole window pattern
 * @author Gabriele Guelfi
 */
public class DiffEverywhere extends PublicObjective {

    private boolean isColour;

    /**
     * Class Constructor
     * @param description the short description of the objective
     * @param vp how much points give the objective
     * @param isColour establish if calculate the different colour or shade
     */
    DiffEverywhere(int id, String description, int vp, boolean isColour) {
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

        List<Integer> values = new ArrayList<>();
        for (int i=0; i<6; i++)
            values.add(0);

        Map<Colour, Integer> colours = new EnumMap<>(Colour.class);
        for (Colour c: Colour.values()) {
            if (c!=Colour.WHITE)
                colours.put(c, 0);
        }
        
        for (int i=0; i<WindowPattern.MAX_ROW; i++){
            for (int j=0; j<WindowPattern.MAX_COL; j++) {
                try {
                    if (isColour) {
                        colours.replace(windowPattern.getBox(i, j).getDie().getColour(), colours.get(windowPattern.getBox(i, j).getDie().getColour())+1);
                    }
                    else {
                        values.set(windowPattern.getBox(i, j).getDie().getValue()-1, values.get(windowPattern.getBox(i, j).getDie().getValue()-1)+1);
                    }
                } catch (NullPointerException e) {}
            }
        }

        if (isColour) {
            List<Integer> v = new ArrayList<>();
            for (Colour c: Colour.values()) {
                if (c!=Colour.WHITE)
                    v.add(colours.get(c));
            }
            Collections.sort(v);
            return v.get(0)*vp;
        }
        Collections.sort(values);
        return values.get(0)*vp;
    }
}
