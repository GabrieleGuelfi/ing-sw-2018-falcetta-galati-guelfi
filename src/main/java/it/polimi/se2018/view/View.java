package it.polimi.se2018.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.*;
import it.polimi.se2018.controller.*;

/**
 * Class View is the representation of the Model.
 * Handle the comunication with Controller through class MessageNewTurn and with Model through MessageUpdate.
 * View is a Singleton.
 * @Author Federico Galati
 *
 *
 */
public class View extends Observable implements Observer{


    private static Controller controller;
    private static Match match;
    private static View view = null;

    private ArrayList<Player> player;
    private DiceCollection draftPool;
    private ArrayList<Tool> tool;
    private ArrayList<PublicObjective> publicObjective;

    private Decision currentDecision;

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
        currentDecision = new Decision(p, this);
    }

    private void update(MessageUpdate sms, Player playerUpdated){
        Iterator<Player> playerIterator = player.iterator();
        //if (player.isEmpty())
        do{
            Player p = playerIterator.next();
            if(p.equals(playerUpdated)) {
                p = playerUpdated.copy();
                player.add(p);
                playerIterator.remove();
                break;
            }
        }while(playerIterator.hasNext());


    }

    private void update(MessageUpdate sms, DraftPool d){ draftPool = d.copy();}

    public void update(Observable observable, Object o) {

    }


}
