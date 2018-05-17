package it.polimi.se2018.view;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.*;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;

import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.network.socket.server.SagradaServer;
import it.polimi.se2018.network.socket.server.VirtualClient;
import it.polimi.se2018.utils.*;

import java.util.ArrayList;

import static it.polimi.se2018.events.TypeMessage.*;

/**
 * @author Federico Galati
 *
 *
 */
public class View extends Observable {


    private static Controller controller;
    private static View view = null;
    private SagradaServer sagradaServer;

    public static View createView(Controller c, SagradaServer s) {

        if (view != null) {
            return view;
        } else {
            view = new View(c,s);
            return view;
        }
    }

    public static View getView() {

        return view;

    }

    private View(Controller c, SagradaServer s) {
        //REMEMBER TO ADD COPY ON TOOL AND OBJECTIVE.

        controller = c;
        this.sagradaServer = s;
        this.register(controller);

    }

    public void chooseDieDraftPool(Player player){
        VirtualClient virtualClient = sagradaServer.searchVirtualClient(player);
        virtualClient.notify(new Message(player));
    }

    public void chooseDieWindowPattern(Player player){
        VirtualClient virtualClient = sagradaServer.searchVirtualClient(player);
        virtualClient.notify(new Message(player));
    }

    public void changeValue(Player player){
        VirtualClient virtualClient = sagradaServer.searchVirtualClient(player);
        virtualClient.notify(new Message(player));
    }

    public void moveDieFromWindowPattern(Player player){
        VirtualClient virtualClient = sagradaServer.searchVirtualClient(player);
        virtualClient.notify(new Message(player));
    }

    public void moveDieFromDraftPool(Player player){
        VirtualClient virtualClient = sagradaServer.searchVirtualClient(player);
        virtualClient.notify(new Message(player));
    }

    public void moveDieFromTrackRound(Player player){
        VirtualClient virtualClient = sagradaServer.searchVirtualClient(player);
        virtualClient.notify(new Message(player));
    }

    public void chooseValue(Player player){
        VirtualClient virtualClient = sagradaServer.searchVirtualClient(player);
        virtualClient.notify(new Message(player));
    }

    public void chooseMove(Player player){
        VirtualClient virtualClient = sagradaServer.searchVirtualClient(player);
        virtualClient.notify(new Message(player));
    }

    public void updateModel(Player player){

    }

    public void updateModel(DraftPool draftPool){

    }

    public void notifyController(Message message){
        this.notifyObservers(message);
    }

    public void notifyController(MoveDie moveDie){
       // this.notifyObservers(moveDie);
    }

    public void notifyController(MessageDie messageDie){
        this.notifyObservers(messageDie);
    }

}

