package it.polimi.se2018.model.publicObjective;

import it.polimi.se2018.model.WindowPattern;

/**
 * abstract class for public objectives with short description and score, here is calculated a score for a window pattern
 * @author Gabriele Guelfi
 */
public abstract class PublicObjective {

    private String description;

    int PV;
    int points;

    /**
     * Class Constructor
     * @param description the short description of the objective
     * @param PV how much points give the objective
     */
    PublicObjective(String description, int PV) {
        this.description = description;
        this.PV = PV;
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
    public int getPV() {
        return PV;
    }

    /**
     * calculate the score of the WindowPattern
     * @param windowPattern the scheme on which the score is calculated
     * @return score of the windowPattern
     */
    public abstract int calcScore(WindowPattern windowPattern);
}
