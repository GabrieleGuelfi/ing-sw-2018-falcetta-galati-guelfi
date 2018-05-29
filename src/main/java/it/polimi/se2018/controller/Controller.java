package it.polimi.se2018.controller;


import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.*;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.model.dicecollection.*;
import it.polimi.se2018.model.publicobjective.PublicObjective;
import it.polimi.se2018.utils.HandleJSON;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.view.VirtualView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.System.*;

public class Controller implements VisitorController, Observer {

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
            List<Integer> patterns = HandleJSON.chooseWP(player);
            view.send(new MessageChooseWP(player, patterns.get(patterns.size()-2), patterns.get(patterns.size()-1)));
        }

        for(Player p: players) {
            while(p.getWindowPattern()==null) {

            }
        }

        Player firstPlayer = match.getRound().getPlayerTurn();
        this.match.notifyObservers(new MessageDPChanged(match.getRound().getDraftPool().copy()));
        this.match.notifyObservers(new MessageTurnChanged(firstPlayer.getNickname()));



        // TOOLS PART!

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

    /**
     * Verify if there is another Die near the position chosen by user
     * @param m move performed by user
     * @return true if there is another die near
     */
    private boolean isNearDie(WindowPattern w, int row, int column) {

        if (w.getEmptyBox() == 20) {
            return (row == 0 || row == WindowPattern.MAX_ROW-1 || column == 0 || column == WindowPattern.MAX_COL);
        }
        for(int i=row-1; i<row+2; i++ ) {
            for(int j=column-1; j<column+2; j++) {
                if (i!=row || j!=column) {
                    try {
                        if (w.getBox(i, j).getDie() != null)
                            return true;
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        }
        return false;

    }

    /**
     * verify if there is another Die with the same colour near, or if there is colourRestriction in the Box
     * @param m
     * @return false if Die break colour restriction
     */
    private boolean verifyColor(WindowPattern w, int row, int column, Die die) {

        try {
            if (w.getBox(row, column).getColourRestriction() != die.getColour() && w.getBox(row, column).getColourRestriction() != Colour.WHITE)
                return false;
        } catch (IllegalArgumentException | NullPointerException e){}
        try {
            if (w.getBox(row - 1, column).getDie().getColour() == die.getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row, column - 1).getDie().getColour() == die.getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row, column + 1).getDie().getColour() == die.getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row + 1, column).getDie().getColour() == die.getColour())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        return true;
    }

    /**
     * verify if there is another Die with the same number near, or if there is numberRestriction in the Box
     * @param w
     * @param row
     * @param column
     * @param die
     * @return false if Die break number restriction
     */
    private boolean verifyNumber(WindowPattern w, int row, int column, Die die) {

        try {
            if (w.getBox(row, column).getValueRestriction() != die.getValue() && w.getBox(row, column).getValueRestriction() != -1) //-1 equals to no restriction
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row - 1, column).getDie().getValue() == die.getValue())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row, column - 1).getDie().getValue() == die.getValue())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row, column + 1).getDie().getValue() == die.getValue())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        try {
            if (w.getBox(row + 1, column).getDie().getValue() == die.getValue())
                return false;
        } catch (IllegalArgumentException | NullPointerException e) {}
        return true;
    }

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

    @Override
    public void update(Message message) {
        message.accept(this);
    }

    @Override
    public void visit(MessageSetWP message) {

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
    public void visit(MessageMoveDie message) {

        Die d = match.getRound().getDraftPool().getBag().get(message.getDieFromDraftPool());
        Player player = null;

        for (Player p : match.getPlayers()) {
            if (p.getNickname().equals(message.getNickname()))
                player = p;
        }
        if (player==null) {
            out.println("player doesn't exist");
            return;
        }
        if (player.isPlacedDie()) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "Die already placed in this turn", player.isPlacedDie(), player.isUsedTool()));
            return;
        }
        if (isNearDie(player.getWindowPattern(), message.getRow(), message.getColumn())) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "No dice near the position", player.isPlacedDie(), player.isUsedTool()));
            return;
        }
        if (!verifyNumber(player.getWindowPattern(), message.getRow(), message.getColumn(), d)) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "Violated Value Restriction!", player.isPlacedDie(), player.isUsedTool()));
            return;
        }
        if (!verifyColor(player.getWindowPattern(), message.getRow(), message.getColumn(), d)) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "Violated Colour Restriction!", player.isPlacedDie(), player.isUsedTool()));
            return;
        }
        if (player.isUsedTool() && player.isPlacedDie()) {
            try {
                match.getRound().nextTurn(match.getPlayers());
                virtualView.send(new MessageTurnChanged(match.getRound().getPlayerTurn().getNickname()));
                return;
            }
            catch (IllegalStateException e) {
                try {
                    match.setRound();
                    return;
                }
                catch (IllegalStateException e1) {
                    //endMatch();
                    return;
                }
            }
        }
        virtualView.send(new MessageConfirmMove(player.getNickname(), player.isPlacedDie(), player.isUsedTool()));

    }

}
