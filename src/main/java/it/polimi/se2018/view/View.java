package it.polimi.se2018.view;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.SagradaServer;
import it.polimi.se2018.network.ServerThread;

import java.util.Observable;
import java.util.Observer;

import static it.polimi.se2018.events.TypeMessage.*;

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
    private static ViewModel viewModel;

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
        viewModel = new ViewModel();

        for(Player playerMatch : match.getPlayers()) {

            viewModel.getPlayer().add(playerMatch.copy()); //Copy of all players.
        }

        //ADD ALL OBSERVER
        this.addObserver(controller);

    }

    /*
    public void update(Message message, int j){
        int i = 0;
        i++;
    }
    */

    @Override
    public void update(Observable observable, Object o) {

        if(o instanceof Message){
            switch (((Message) o).getTypeMessage()) {
                case UPDATE:
                    //viewModel.setViewModel(match);
                    for(ServerThread serverThread: sagradaServer.getServerThread()){
                        serverThread.setMessage((Message)o);
                    }

                    break;
                case NEW_TURN:
                    for(ServerThread serverThread: sagradaServer.getServerThread()){
                        if(serverThread.getPlayer() == ((Message) o).getPlayer()){
                            serverThread.setMessage((Message)o);
                        }
                    }
                    break;
                case CHOOSE_MOVE:
                    break;
                case MOVE_DIE:
                    break;
                case TOOL:
                    break;
                case NOTHING:
                    break;
                default:
                    break;
            }
        }


    }

    public ViewModel getViewModel(){
        return viewModel;
    }


}
