package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.model.WindowPattern;

/**
 * abstract class for public objectives with short description and score, here is calculated a score for a window pattern
 * @author Gabriele Guelfi
 */
public abstract class PublicObjective {

    private String description;
    int vp;
    int points;

    /**
     * Class Constructor
     * @param description the short description of the objective
     * @param vp how much points give the objective
     */
    public PublicObjective(String description, int vp) {
        this.description = description;
        this.vp = vp;
    }

    /**
     * Create the right type of Public Objective
     * @param n identify the ten type of Objective
     * @return a new PublicObjective of the right type
     */
    public static PublicObjective factory(int n) {
        switch(n) {
            case 1: return new DiffRow("Rows with no repeated colors", 6, true);
            case 2: return new DiffColumn("Column with no repeated colours", 5, true);
            case 3: return new DiffRow("Rows with no repeated values", 5, false);
            case 4: return new DiffColumn("Column with no repeated values", 4, false);
            case 5: return new SetShades("Sets of 1 & 2 values anywhere", 2, 1);
            case 6: return new SetShades("Sets of 3 & 4 values anywhere", 2, 3);
            case 7: return new SetShades("Sets of 5 & 6 values anywhere", 2, 5);
            case 8: return new DiffEverywhere("Sets of one of each value anywhere", 5, false);
            case 9: return new DiagColour("Count of diagonally adjacent same color dice");
            case 10: return new DiffEverywhere("Sets of one of each color anywhere", 4, true);
            default: throw new IllegalArgumentException("Invalid parameter!");
        }
    }

    /**
     * @return the short description of the objective
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the points given by the objective
     */
    public int getVp() {
        return vp;
    }

    /**
     * calculate the score of the windowPattern
     * @param windowPattern the scheme on which the score is calculated
     * @return score of the windowPattern
     */
    public abstract int calcScore(WindowPattern windowPattern);
}
