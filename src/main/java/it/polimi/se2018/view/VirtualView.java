package it.polimi.se2018.view;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.*;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;

import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.network.socket.server.SagradaServer;
import it.polimi.se2018.network.socket.server.ServerImplementation;
import it.polimi.se2018.network.socket.server.VirtualClient;
import it.polimi.se2018.utils.*;

import java.util.ArrayList;

import static it.polimi.se2018.events.TypeMessage.*;

/**
 * @author Federico Galati
 *
 *
 */
public class VirtualView extends Observable implements Observer {


    private Controller controller;
    private ServerImplementation sagradaServer;


    public VirtualView(Controller c, ServerImplementation s) {
        //REMEMBER TO ADD COPY ON TOOL AND OBJECTIVE.

        controller = c;
        this.sagradaServer = s;
        this.register(controller);

    }

    @Override
    public void update(Message message){
        message.accept(new Visitor(sagradaServer));
    }


    /*public void chooseDieDraftPool(Player player){
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
    */
    /*
    public void chooseValue(Player player, String s){
        VirtualClient virtualClient = sagradaServer.searchVirtualClient(player);
        //System.out.println("Virtualclient trovato! glielo mando...");
        virtualClient.notify(new Message(s));
    }
    */
    /*
    public void chooseMove(Player player){
        VirtualClient virtualClient = sagradaServer.searchVirtualClient(player);
        virtualClient.notify(new Message(player));
    }

    public void updateModel(Player player){

    }

    public void updateModel(DraftPool draftPool){

    }
*/

    // CHIAMATA DAL VIRTUALCLIENT
    //public void notifyController(Message message){
      //  message.notifyThis(this);
   // }

}

