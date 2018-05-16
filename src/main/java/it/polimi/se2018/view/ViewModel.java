package it.polimi.se2018.view;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.dicecollection.DiceCollection;
import it.polimi.se2018.model.publicobjective.PublicObjective;

import java.util.ArrayList;

public class ViewModel {
    private ArrayList<Player> player;
    private DiceCollection draftPool;
    private ArrayList<Tool> tool;
    private ArrayList<PublicObjective> publicObjective;

    public ArrayList<Player> getPlayer() {
        return player;
    }

    /*
    public void setViewModel(Match match){
        this.publicObjective = match.getPublicObjectives().copy();
        this.draftPool = match.getRound().getDraftPool().copy();
        this.tool = match.getTools().copy();
        this.publicObjective = match.getPublicObjectives().copy();
    }
    */
}
