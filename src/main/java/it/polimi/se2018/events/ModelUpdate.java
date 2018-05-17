package it.polimi.se2018.events;

import it.polimi.se2018.model.Match;


// I thought to pass the whole Match object. Let's see it it works.
public class ModelUpdate {

    private Match match;

    public ModelUpdate(Match m) {
        this.match = m;
    }
}
