package it.polimi.se2018.controller;


import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.*;
import it.polimi.se2018.events.messageforserver.MessageRestartServer;
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

    public Controller(List<String> nickname, VirtualView view) {
        prepareGame(nickname, view);
    }

    private void prepareGame(List<String> nickname, VirtualView view) {

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
            index = generator.nextInt(10);
            while (rand.contains(index))
                index = generator.nextInt(10);
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

        // TOOLS PART!

    }

    private boolean areWPset() {
        for(Player p: match.getActivePlayers()) {
            if (p.getWindowPattern()==null) return false;
        }
        return true;
    }

    private void startGame() {
        this.match.notifyObservers(new MessageDPChanged(match.getRound().getDraftPool().copy()));
        this.match.notifyObservers(new MessageRoundChanged(match.getRound().getPlayerTurn().getNickname(), match.getNumRound()));
        //this.match.notifyObservers(new MessageTurnChanged(match.getRound().getPlayerTurn().getNickname()));
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
            virtualView.send(new MessagePrivObj(p.getNickname(), p.getPrivateObjective().getDescription()));
        }
    }

    /**
     * Verify if there is another Die near the position chosen by user
     * performed by user
     * @return true if there is another die near
     */
    private boolean isNearDie(WindowPattern w, int row, int column) {
        if (w.getEmptyBox() == 20) {
            return (row == 0 || row == WindowPattern.MAX_ROW-1 || column == 0 || column == WindowPattern.MAX_COL-1);
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
     * @param
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
            if (w.getBox(row, column).getValueRestriction() != die.getValue() && w.getBox(row, column).getValueRestriction() != 0) //0 equals to no restriction
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
     */
    private void calcResults(Player player) {

        //privateObjective
        player.addPoints(player.getPrivateObjective().calcScore(player.getWindowPattern()));

        //publicObjectives
        for (PublicObjective publicObjective : match.getPublicObjectives()) {
            player.addPoints(publicObjective.calcScore(player.getWindowPattern()));
        }

        //favor tokens
        player.addPoints(player.getFavorTokens());

        //Empty box
        player.addPoints(-player.getWindowPattern().getEmptyBox());

    }

    private Player searchNick(String nickname) {
        for (Player p : match.getPlayers()) {
            if (p.getNickname().equals(nickname))
                return p;
        }
        return null;
    }

    private void nextTurn() {
        try {
            match.getRound().nextTurn(match.getPlayers());
            match.notifyObservers(new MessageTurnChanged(match.getRound().getPlayerTurn().getNickname()));
        }
        catch (IllegalStateException e) {
            try {
                match.setRoundTrack();
                match.setRound();
                match.notifyObservers(new MessageDPChanged(match.getRound().getDraftPool()));
                match.notifyObservers(new MessageRoundChanged(match.getFirstPlayerRound().getNickname(), match.getNumRound()));

            }
            catch (IllegalStateException e1) {
                endMatch();
            }
        }
    }

    private void endMatch() {
        List<Integer> points = new ArrayList<>();
        List<String> nicknames = new ArrayList<>();
        for (Player p : match.getActivePlayers()) {
            calcResults(p);
            points.add(p.getPoints());
            nicknames.add(p.getNickname());
        }
        match.notifyObservers(new MessageEndMatch(points, nicknames));
        virtualView.sendToServer(new MessageRestartServer());

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
            if (areWPset()) startGame();
        }
        else {
            out.println("nickname non valido");
        }
    }

    @Override
    public void visit(MessageMoveDie message) {

        Die d = match.getRound().getDraftPool().getBag().get(message.getDieFromDraftPool());
        Player player = searchNick(message.getNickname());

        if (player == null) {
            out.println("player doesn't exist");
            return;
        }
        if (player.isPlacedDie()) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "Die already placed in this turn", player.isPlacedDie(), player.isUsedTool()));
            return;
        }
        if (!isNearDie(player.getWindowPattern(), message.getRow(), message.getColumn())) {
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
        player.getWindowPattern().putDice(d, message.getRow(), message.getColumn());
        player.setPlacedDie(true);
        if (player.isUsedTool() && player.isPlacedDie()) {
            player.setPlacedDie(false);
            player.setUsedTool(false);
            nextTurn();
        }
        virtualView.send(new MessageConfirmMove(player.getNickname(), player.isPlacedDie(), player.isUsedTool(), true));
        match.getRound().getDraftPool().removeDie(message.getDieFromDraftPool());
        match.notifyObservers(new MessageWPChanged(player.getNickname(), player.getWindowPattern()));
        match.notifyObservers(new MessageDPChanged(match.getRound().getDraftPool()));
    }

    @Override
    public void visit(MessageDoNothing message) {

        Player player = searchNick(message.getNickname());

        if (player == null) {
            out.println("player doesn't exist");
            return;
        }
        player.setPlacedDie(false);
        player.setUsedTool(false);
        nextTurn();

    }

    @Override
    public void visit(MessageRequest message) {
        Player player = searchNick(message.getNickname());
        message.getType().performRequest(player, virtualView, match);
        virtualView.send(new MessageConfirmMove(message.getNickname(), player.isPlacedDie(), player.isUsedTool(), false));
    }
}
