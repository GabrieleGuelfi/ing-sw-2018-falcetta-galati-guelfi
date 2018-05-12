package it.polimi.se2018.view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.*;
import it.polimi.se2018.controller.*;

public class View extends Observable implements Observer{

    private static Controller controller;
    private static Match match;
    private static View view = null;

    private ArrayList<Player> player;
    private DiceCollection draftPool;
    private ArrayList<Tool> tool;
    private ArrayList<PublicObjective> publicObjective;


    public static View getView(Controller c, Match m){

        if (view != null){
            return view;
        }
        else{
              view = new View(c, m);
              return view;
        }
    }

    private View(Controller c, Match m){
        //RICORDARSI DI FARE CORRETTAMENTE TUTTE GLI ADDOBSERVER
        //REMEMBER TO ADD COPY ON TOOL AND OBJECTIVE.

        controller = c;
        match = m;

        for(Player playerMatch : match.getPlayers()) {

            this.player.add(playerMatch.copy()); //Copy of all players.
        }

        //ADD ALL OBSERVER
        this.addObserver(controller);

    }

    //UPDATE WHEN CONTROLLER NOTIFY NEW PLAYERTURN

    private void update(MessageNewTurn sms, Player p){
        Decision d= new Decision(p, this);
    }

    private void update(MessageUpdate sms, Player playerUpdated){
        for (Player p : player) {
            if (p.equals(playerUpdated)) p = playerUpdated.copy();
        }
    }

    private void update(MessageUpdate sms, DraftPool d){ draftPool = d.copy();}

    public void update(Observable observable, Object o) {

    }


}
