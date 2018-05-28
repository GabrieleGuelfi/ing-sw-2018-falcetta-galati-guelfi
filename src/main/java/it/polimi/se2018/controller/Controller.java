package it.polimi.se2018.controller;


import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.events.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.model.dicecollection.*;
import it.polimi.se2018.model.publicobjective.PublicObjective;
import it.polimi.se2018.utils.HandleJSON;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.SagradaVisitor;
import it.polimi.se2018.view.VirtualView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.System.*;

public class Controller implements SagradaVisitor, Observer {

    private Match match;
    private VirtualView virtualView;

    public Controller() {
        this.match = null;
    }

    public void startGame(List<String> nickname, VirtualView view) {

        List<Player> players = new ArrayList<>();
        List<PublicObjective> objectives = new ArrayList<>();
        List<Tool> tools = new ArrayList<>();
        Random generator = new Random();
        List<Integer> rand = new ArrayList<>();
        int index;

        this.virtualView = view;

        view.register(this);

        for (String p : nickname) {
            Player player = new Player(p);
            players.add(player);
        }

        for (int i=0; i<3; i++) {
            index = generator.nextInt(10)+1;
            while (rand.contains(index))
                index = generator.nextInt(10)+1;
            rand.add(index);
            objectives.add(PublicObjective.factory(index));
        }

        givePrivateObjective(players);

        // Create the match...
        this.match = new Match(new Bag(), players, objectives, tools, view);

        for(String player: nickname) {
            HandleJSON.chooseWP(player, virtualView);
        }

        for(Player p: players) {
            while(p.getWindowPattern()==null) {

            }
        }

        Player firstPlayer = match.getRound().getPlayerTurn();
        this.match.notifyObservers(new MessageDPChanged(match.getRound().getDraftPool().copy()));
        this.match.notifyObservers(new MessageTurnChanged(firstPlayer.getNickname(), firstPlayer.isPlacedDie(), firstPlayer.isUsedTool()));


        // TOOLS PART!

        /*for (int i = 0; i < 10; i++) {
            match.nextNumRound();
            //manageRound(i % match.getPlayers().size());
        }*/


    }



    private void givePrivateObjective(List<Player> players) {
        Random generator = new Random();
        List<Integer> rand = new ArrayList<>();
        Colour[] colours = Colour.values();

        for (Player p : players) {
            int index = generator.nextInt(colours.length);
            while (rand.contains(index) || colours[index].equals(Colour.WHITE))
                index = generator.nextInt(colours.length);
            rand.add(index);
            p.setPrivateObjective(new PrivateObjective(colours[index]));
            virtualView.send(new MessagePrivObj(p.getNickname(), colours[index].toString()));
        }
    }



    /*
    private void manageMoveDie(MoveDie m) {
        if (m.getPlayer().isPlacedDie()) { //maybe we can place this in manageRound
            //notify(view, "die already placed");
            return;
        }
        if (m.getRow() < 1 || m.getRow() > 4 || m.getColumn() < 1 || m.getColumn() > 5) {
            //error input not valid
            return;
        }
        if (isNearDie(m) && verifyColor(m) && verifyNumber(m)){
            m.getPlayer().getWindowPattern().putDice(m.getDie(), m.getRow(), m.getColumn());
            //notify(view, "die placed");
            return;
        }
        //3 if for search the restrictions violated and notify all to users
        if (!isNearDie(m)) {
            //error
        }
        if (!verifyColor(m)) {
            //error
        }
        if (!verifyNumber(m)) {
            //error
        }
        return;
    }
    */


    /**
     * Verify if there is another Die near the position chosen by user
     * @param m move performed by user
     * @return false if there is another die near
     */
    /*
    private boolean isNearDie(MoveDie m) {

        WindowPattern windowPattern = m.getPlayer().getWindowPattern();
        int row = m.getRow();
        int column = m.getColumn();

        if (windowPattern.getEmptyBox() == 20) {
            return (row == 1 || row == WindowPattern.MAX_ROW || column == 1 || column == WindowPattern.MAX_COL);
        }
        for(int i = row-2; i < row+1; i++ ) {
            for(int j = column-2; j < column+1; j++) {
                if (i != row-1 || j != column-1) {
                    try {
                        if (windowPattern.getBox(i, j).getDie() != null)
                            return true;
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        }
        return false;
    }
    */

    /**
     * verify if there is another Die with the same colour near, or if there is colourRestriction in the Box
     * @param m move performed by user
     * @return false if Die break colour restriction
     */
    /*
    private boolean verifyColor(MoveDie m) {

        WindowPattern windowPattern = m.getPlayer().getWindowPattern();
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn()).getColourRestriction() != m.getDie().getColour() && windowPattern.getBox(m.getRow(), m.getColumn()).getColourRestriction() != Colour.WHITE)
                return false;
        } catch (IllegalArgumentException | NullPointerException e){}
        try {
            if (windowPattern.getBox(m.getRow() - 2, m.getColumn() - 1).getDie().getColour() == m.getDie().getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn() - 2).getDie().getColour() == m.getDie().getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn()).getDie().getColour() == m.getDie().getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn() - 1).getDie().getColour() == m.getDie().getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        return true;
    }
    */

    /**
     * verify if there is another Die with the same number near, or if there is numberRestriction in the Box
     * @param m move performed by user
     * @return false if Die break number restriction
     */
    /*
    private boolean verifyNumber( MoveDie m) {

        WindowPattern windowPattern = m.getPlayer().getWindowPattern();
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn()).getValueRestriction() != m.getDie().getValue() && windowPattern.getBox(m.getRow(), m.getColumn()).getValueRestriction() != -1) //-1 equals to no restriction
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 2, m.getColumn() - 1).getDie().getValue() == m.getDie().getValue()) //if null?
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn() - 2).getDie().getValue() == m.getDie().getValue())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow() - 1, m.getColumn()).getDie().getValue() == m.getDie().getValue())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (windowPattern.getBox(m.getRow(), m.getColumn() - 1).getDie().getValue() == m.getDie().getValue())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        return true;
    }
    */

    /**
     * calculate the score of a player, based on his private objective and the three public ones
     * @param player player whose score is calculated
     * @param p public objective of the match
     */
    private void calcResults(Player player, List<PublicObjective> p) {

        WindowPattern windowPattern = player.getWindowPattern();

        //privateObjective
        for(int i=0; i<WindowPattern.MAX_ROW; i++) {
            for (int j=0; j<WindowPattern.MAX_COL; j++) {
                try {
                    if (windowPattern.getBox(i, j).getDie().getColour() == player.getPrivateObjective().getShade())
                        player.addPoints(windowPattern.getBox(i, j).getDie().getValue());
                } catch (IllegalArgumentException | NullPointerException e) {}
            }
        }

        //publicObjectives
        for (PublicObjective publicObjective : p) {
            player.addPoints(publicObjective.calcScore(player.getWindowPattern()));
        }

    }

    /*
    public void update(Message message) {
        System.out.println(message.getString());
        // Here the controller should take the news from the view and handle them.
    }
    */

    /*
    public void update(MessageDie message) {
        System.out.println("UPDATE PER MESSAGEDIE");
    }
    */

    /* @Override
    public void update(MoveDie m) {

        if (m.getPlayer() != match.getRound().getPlayerTurn()) {
            //notifyObservers(new Message(TypeMessage.ERROR_TURN, ((MoveDie) o).getPlayer()));
            return;
        }
        if (m.getPlayer().isPlacedDie()) {
            //notifyObservers(new Message(TypeMessage.ERROR_DIE, ((MoveDie) o).getPlayer()));
            return;
        }
        manageMoveDie(m);
        m.getPlayer().setPlacedDie(true);
        if (m.getPlayer().isPlacedDie() && m.getPlayer().isUsedTool()) {
            m.getPlayer().setPlacedDie(false);
            m.getPlayer().setUsedTool(false);
            match.getRound().nextTurn(match.getPlayers());
            //notifyObservers(new Message(TypeMessage.NEW_TURN, match.getRound().getPlayerTurn()));
        }
        else {
            //notifyObservers(new Message(TypeMessage.CHOOSE_MOVE, match.getRound().getPlayerTurn()));
        }

        if (match.getRound().getNumTurn() == 2*match.getActivePlayers().size()) {
            try {
                match.setRound();
                //notifyObservers(new Message(TypeMessage.NEW_ROUND, match.getRound().getPlayerTurn()));
            } catch (IllegalStateException e) {
                for(Player player: match.getActivePlayers()) {
                    calcResults(player, match.getPublicObjectives());
                }
                //notifyObservers(new Message(TypeMessage.END_MATCH, null));
            }
        }
    }
    */

    @Override
    public void update(Message message) {
        message.accept(this);
    }


    @Override
    public void visit(Message message) {

    }

    @Override
    public void visit(MessageError messageError) {
    }

    @Override
    public void visit(MessageNickname message) {

    }

    @Override
    public void visit(MessagePrivObj message) {

    }

    @Override
    public void visit(MessagePublicObj message) {

    }

    @Override
    public void visit(MessageChooseWP message) {

        Player player = null;
        for (Player p: match.getPlayers()) {
            if (p.getNickname().equals(message.getNickname()))
                player = p;
        }
        if (player!=null) {
            player.setWindowPattern(HandleJSON.createWindowPattern(player.getNickname(), message.getFirstIndex(), message.getSecondIndex()));
            match.notifyObservers(new MessageWPChanged(player.getNickname(), player.getWindowPattern()));
        }
        else {
            out.println("nickname non valido");
        }
    }

    @Override
    public void visit(MessageWPChanged message) {

    }

    @Override
    public void visit(MessageTurnChanged message) {

    }

    @Override
    public void visit(MessageDPChanged message) {

    }
}
