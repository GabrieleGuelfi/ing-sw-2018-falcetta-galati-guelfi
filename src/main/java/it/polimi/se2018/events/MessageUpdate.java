package it.polimi.se2018.events;

import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.view.View;

import java.util.Observable;

public class MessageUpdate extends Observable{

    public MessageUpdate(View view, Player playerUpdated){

        this.addObserver(view);
        this.notifyObservers(playerUpdated);

    }

    public MessageUpdate(View view, DraftPool draftPool){

        this.addObserver(view);
        this.notifyObservers(draftPool);
    }

}
