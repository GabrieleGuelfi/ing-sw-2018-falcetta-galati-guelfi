package it.polimi.se2018.model.publicObjective;

import it.polimi.se2018.model.WindowPattern;

public class DiagColour extends PublicObjective {

    public DiagColour (String description, int PV) {
        super(description, PV);
    }

    @Override
    public int calcScore(WindowPattern windowPattern) {
        return 0;
    }
}
