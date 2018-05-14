package it.polimi.se2018.view;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageUpdate;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.dicecollection.DiceCollection;
import it.polimi.se2018.model.publicobjective.PublicObjective;
import it.polimi.se2018.network.SagradaServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

/**
 * Class View is the representation of the Model.
 * Handle the comunication with Controller through class MessageNewTurn and with Model through MessageUpdate.
 * View is a Singleton.
 * @author Federico Galati
 *
 *
 */
public class View extends Observable implements Observer{


    private static Controller controller;
    private static Match match;
    private static View view = null;
    private static SagradaServer sagradaServer;

    private ArrayList<Player> player;
    private DiceCollection draftPool;
    private ArrayList<Tool> tool;
    private ArrayList<PublicObjective> publicObjective;

    public static View createView(Controller c, Match m, SagradaServer s){

        if (view != null){
            return view;
        }
        else{
              view = new View(c, m, s);
              return view;
        }
    }

    public static View getView(){

        return view;

    }

    private View(Controller c, Match m, SagradaServer s){
        //REMEMBER TO ADD COPY ON TOOL AND OBJECTIVE.

        controller = c;
        match = m;
        sagradaServer = s;

        for(Player playerMatch : match.getPlayers()) {

            this.player.add(playerMatch.copy()); //Copy of all players.
        }

        //ADD ALL OBSERVER
        this.addObserver(controller);

    }

    //UPDATE WHEN CONTROLLER NOTIFY NEW PLAYER_TURN


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

    public ArrayList<Player> getPlayer(){return this.player;};

    public void update(Observable observable, Object o) {
        if(o instanceof Message){
           // switch(((Message) o).getTypeMessage())
        }


    }


}
