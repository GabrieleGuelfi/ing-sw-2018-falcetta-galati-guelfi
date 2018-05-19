package it.polimi.se2018.model;

public class PrivateObjective {

    private Colour shade;

    private String description;

    public PrivateObjective(String description, Colour shade) {
        this.description = description;
        this.shade = shade;
    }

    public String getDescription() {
        return description;
    }

    public Colour getShade() {
        return shade;
    }
}
