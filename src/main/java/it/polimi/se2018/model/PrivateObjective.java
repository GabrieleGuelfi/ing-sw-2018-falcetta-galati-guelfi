package it.polimi.se2018.model;

/**
 * Private Objective that give points, one for each player
 * @author Gbriele Guelfi
 */
public class PrivateObjective {

    private Colour shade;
    private String description;

    /**
     * constructor of the class
     * @param shade which colour of die give pints
     */
    public PrivateObjective(Colour shade) {
        this.shade = shade;
        this.description = "Shades of " + shade + ": Private\nSum of values on "+shade+" dice";
    }

    /**
     * @return the description of this private objective
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the colour of die that give points
     */
    public Colour getShade() {
        return shade;
    }

}
