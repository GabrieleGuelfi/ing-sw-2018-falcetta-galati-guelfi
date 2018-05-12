package it.polimi.se2018.model;

public abstract class PublicObjective {

    private String description;

    int PV;
    int points;

    PublicObjective(String description, int PV) {
        this.description = description;
        this.PV = PV;
    }

    public String getDescription() {
        return description;
    }

    public int getPV() {
        return PV;
    }

    public abstract int calcPoints (WindowPattern windowPattern);
}
