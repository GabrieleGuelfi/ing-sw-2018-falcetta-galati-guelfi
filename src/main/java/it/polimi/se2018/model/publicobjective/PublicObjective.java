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
    PublicObjective(String description, int vp) {
        this.description = description;
        this.vp = vp;
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
