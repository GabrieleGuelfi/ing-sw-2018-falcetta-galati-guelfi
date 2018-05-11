package it.polimi.se2018.view;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import it.polimi.se2018.View.Decision;
import it.polimi.se2018.View.MessageNewTurn;
import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.*;
import it.polimi.se2018.controller.*;
import it.polimi.se2018.events.*;

public class View extends Observable implements Observer{

    private static Controller controller;
    private static Match match;
    private static View view = null;

    private ArrayList<Player> player;
    private DraftPool draftPool;
    private ArrayList<Tool> tool;
    private ArrayList<PublicObjective> publicObjective;
    private Round round;

    public static View getView(Controller c, Match m){

        if (view != null){
            return view;
        }
        else{
              view = new View(c, m);
        }
    }

    private View(Controller c, Match m){
        //RICORDARSI DI FARE CORRETTAMENTE TUTTE GLI ADDOBSERVER


        controller = c;
        match = m;

        //ADD ALL OBSERVER
        this.addObserver(controller);

    }

    //UPDATE WHEN CONTROLLER NOTIFY NEW PLAYERTURN

    void update(MessageNewTurn sms, Player player){
        Decision d= new Decision(player, this);
    }


}
