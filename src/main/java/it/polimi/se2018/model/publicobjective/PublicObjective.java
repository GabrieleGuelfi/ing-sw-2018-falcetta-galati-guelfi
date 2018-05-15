package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.model.windowpattern.WindowPattern;

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
    PublicObjective(String description, int vp) {
        this.description = description;
        this.vp = vp;
    }

    public PublicObjective factory(int n) {
        switch(n) {
            case 1: return new DiffRow("foo", 6, true);
            case 2: return new DiffColumn("foo", 5, true);
            case 3: return new DiffRow("foo", 5, false);
            case 4: return new DiffColumn("foo", 4, false);
            case 5: return new SetShades("foo", 2, 1);
            case 6: return new SetShades("foo", 2, 3);
            case 7: return new SetShades("foo", 2, 5);
            case 8: return new DiffEverywhere("foo", 5, false);
            case 9: return new DiagColour("foo");
            case 10: return new DiffEverywhere("foo", 4, true);
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
