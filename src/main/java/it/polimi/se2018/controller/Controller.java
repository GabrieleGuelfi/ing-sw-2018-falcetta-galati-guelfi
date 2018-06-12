package it.polimi.se2018.controller;


import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.*;
import it.polimi.se2018.events.messageforcontroller.MessageForcedMove;
import it.polimi.se2018.events.messageforserver.MessageRestartServer;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.*;
import it.polimi.se2018.model.dicecollection.*;
import it.polimi.se2018.model.publicobjective.PublicObjective;
import it.polimi.se2018.utils.HandleJSON;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.view.VirtualView;

import java.util.*;

import static java.lang.System.*;

public class Controller implements VisitorController, Observer {

    private Match match;
    private VirtualView virtualView;

    /**
     * Constructor of the class
     * @param nickname List of nickname of the player
     * @param view virtualView used to send message
     */
    public Controller(List<String> nickname, VirtualView view) {
        this.virtualView = view;
        view.register(this);

        prepareGame(nickname);
    }

    /**
     * prepare all utilities to start a match
     * @param nickname List of nickname of the player
     */
    private void prepareGame(List<String> nickname) {

        List<Player> players = new ArrayList<>();
        List<PublicObjective> objectives = new ArrayList<>();
        List<Tool> tools = new ArrayList<>();
        Random generator = new Random();
        List<Integer> rand = new ArrayList<>();
        int index;

        //set players
        for (String p : nickname) {
            Player player = new Player(p);
            players.add(player);
        }

        //Public Objectives
        for (int i=0; i<3; i++) {
            index = generator.nextInt(10);
            while (rand.contains(index))
                index = generator.nextInt(10);
            rand.add(index);
            objectives.add(PublicObjective.factory(index));
        }

        rand = new ArrayList<>();

        // TEST
        //Tool testTool2 = Tool.factory(7);
        //testTool2.setVirtualView(this.virtualView);
        //tools.add(testTool2);

        //Tools
        for (int i=0; i<3; i++) {
            index = generator.nextInt(11);
            index = 10;
            while (rand.contains(index))
                index = generator.nextInt(11);
            rand.add(index);
            Tool tool = Tool.factory(index);
            tool.setVirtualView(this.virtualView);
            tools.add(tool);
        }

        //Private Objective
        givePrivateObjective(players);

        // Create the match...
        this.match = new Match(new Bag(), players, objectives, tools, virtualView);

        for(String player: nickname) {
            List<Integer> patterns = HandleJSON.chooseWP(player);
            virtualView.send(new MessageChooseWP(player, patterns.get(patterns.size()-2), patterns.get(patterns.size()-1)));
        }

    }

    /**
     * @return true if all players chosen their window pattern
     */
    private boolean areWPset() {
        for(Player p: match.getActivePlayers()) {
            if (p.getWindowPattern()==null) return false;
        }
        return true;
    }

    /**
     * start a game with first round
     */
    private void startGame() {
        this.match.notifyObservers(new MessageDPChanged(match.getRound().getDraftPool().copy()));
        this.match.notifyObservers(new MessageRoundChanged(match.getRound().getPlayerTurn().getNickname(), match.getNumRound(), match.getRound().getDraftPool()));
        Player player = match.getRound().getPlayerTurn();
        virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), player.getWindowPattern(), null));
    }

    /**
     * set a private objective for all players
     * @param players List of all players for this match
     */
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
     * @param w window pattern of the user
     * @param row chosen by user
     * @param column chosen by user
     * @return true if there is another die near
     */
    public boolean isNearDie(WindowPattern w, int row, int column) {
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
     * @param w window pattern of the user
     * @param row chosen by user
     * @param column chosen by user
     * @param die which the player want to place
     * @return false if Die break colour restriction
     */
    public boolean verifyColor(WindowPattern w, int row, int column, Die die) {

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
     * @param w window pattern of the user
     * @param row chosen by user
     * @param column chosen by user
     * @param die which the player want to place
     * @return false if Die break number restriction
     */
    public boolean verifyNumber(WindowPattern w, int row, int column, Die die) {

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
     * @param nickname of a player that controller search
     * @return Player with the nickname
     */
    private Player searchNick(String nickname) {
        for (Player p : match.getPlayers()) {
            if (p.getNickname().equals(nickname))
                return p;
        }
        return null;
    }

    /**
     * verify if there is another turn for this round, or another round for this match
     */
    private void nextTurn() {
        try {
            match.getRound().nextTurn(match.getPlayers());
            Player player = match.getRound().getPlayerTurn();
            match.notifyObservers(new MessageTurnChanged(player.getNickname()));
            virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), player.getWindowPattern(), match.getRound().getDraftPool()));
        }
        catch (IllegalStateException e) {
            try {
                match.setRoundTrack();
                match.setRound();
                match.notifyObservers(new MessageRoundChanged(match.getFirstPlayerRound().getNickname(), match.getNumRound(), match.getRound().getDraftPool()));
                Player player = match.getRound().getPlayerTurn();
                virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), player.getWindowPattern(), null));

            }
            catch (IllegalStateException e1) {
                endMatch();
            }
        }
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

        if(player.getPoints()<0)
            player.addPoints(-player.getPoints());
        
    }

    /**
     * decide which player of p1 and p2 win on each other
     * @param p1 player one
     * @param p2 player two
     * @return true if p1 win on p2
     */
    private boolean manageTie(Player p1, Player p2) {
            if (p1.getPrivateObjective().calcScore(p1.getWindowPattern())>p2.getPrivateObjective().calcScore(p2.getWindowPattern()))
                return true;
            if (p1.getFavorTokens()>p2.getFavorTokens())
                return true;
            int i = match.getActivePlayers().indexOf(match.getFirstPlayerRound());
            int turnP1 = match.getActivePlayers().indexOf(p1);
            int turnP2 = match.getActivePlayers().indexOf(p2);
            if ((turnP1>=i && turnP2>=i) || (turnP1<i && turnP2<i))
                return turnP1>turnP2;
            return turnP1<i;
    }

    /**
     * end a match with more than a player
     */
    private void endMatch() {
        List<Integer> points = new ArrayList<>();
        List<String> nicknames = new ArrayList<>();
        Map<String , Integer> results = new HashMap<>();
        int i;
        for (Player p : match.getActivePlayers()) {
            calcResults(p);
            boolean found = false;
            for (i=0; i<points.size() && !found; i++) {
                if (p.getPoints()>points.get(i))
                    found = true;
                else if (p.getPoints()==points.get(i))
                    found = manageTie(p, searchNick(nicknames.get(i)));
            }
            if (i==0) i=1;
            points.add(i-1, p.getPoints());
            nicknames.add(i-1, p.getNickname());
        }
        i = 0;
        for(String playerNickname: nicknames) {
            results.put(playerNickname, points.get(i));
            i++;
        }
        match.notifyObservers(new MessageEndMatch(results, false));
        virtualView.sendToServer(new MessageRestartServer());

    }

    /**
     * end a match with only one player
     * @param player winner
     */
    private void endMatch(Player player) {
        Map<String , Integer> results = new HashMap<>();
        calcResults(player);
        results.put(player.getNickname(), player.getPoints());
        match.notifyObservers(new MessageEndMatch(results, true));
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

        Player player = searchNick(message.getNickname());

        if (player == null) {
            out.println("player doesn't exist");
            return;
        }
        Die d;
        try {
             d = match.getRound().getDraftPool().getBag().get(message.getDieFromDraftPool());
        }
        catch (IndexOutOfBoundsException e) {
            virtualView.send(new MessageErrorMove(message.getNickname(), "Inexistent die in draftpool!"));
            return;
        }
        if (player.isPlacedDie()) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "Die already placed in this turn"));
            virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), player.getWindowPattern(), match.getRound().getDraftPool()));
            return;
        }
        if (!isNearDie(player.getWindowPattern(), message.getRow(), message.getColumn())) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "No dice near the position"));
            virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), player.getWindowPattern(), match.getRound().getDraftPool()));
            return;
        }
        if (!verifyNumber(player.getWindowPattern(), message.getRow(), message.getColumn(), d)) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "Violated Value Restriction!"));
            virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), player.getWindowPattern(), match.getRound().getDraftPool()));
            return;
        }
        if (!verifyColor(player.getWindowPattern(), message.getRow(), message.getColumn(), d)) {
            virtualView.send(new MessageErrorMove(player.getNickname(), "Violated Colour Restriction!"));
            virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), player.getWindowPattern(), match.getRound().getDraftPool()));
            return;
        }
        player.getWindowPattern().putDice(d, message.getRow(), message.getColumn());
        player.setPlacedDie(true);
        boolean isThereAnotherMove = true;
        if (player.isUsedTool() && player.isPlacedDie()) {
            player.setPlacedDie(false);
            player.setUsedTool(false);
            isThereAnotherMove = false;
        }
        virtualView.send(new MessageConfirmMove(player.getNickname(), isThereAnotherMove));
        match.getRound().getDraftPool().removeDie(message.getDieFromDraftPool());
        match.notifyObservers(new MessageWPChanged(player.getNickname(), player.getWindowPattern()));
        match.notifyObservers(new MessageDPChanged(match.getRound().getDraftPool()));

        if(!isThereAnotherMove) nextTurn();
        else virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie()));

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
        virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie()));
    }

    @Override
    public void visit(MessageEndGame message){
        //IN THIS MESSAGE THERE IS THE NAME OF THE WINNER
        this.endMatch(searchNick(message.getNickname()));

    }

    @Override
    public void visit(MessageToolResponse message) {

        Player player = searchNick(message.getNickname());
        Tool toolInUse = null;
        boolean canProceed;

        for(Tool tool: match.getTools())
            if(tool.isBeingUsed()) toolInUse = tool;

        if(toolInUse==null) return;

        if(!message.isConfirmUse()) {
            toolInUse.setBeingUsed(false);
            virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), player.getWindowPattern(), match.getRound().getDraftPool()));
        } else {

            canProceed = toolInUse.use(message, match, player, this);

            if(canProceed) {
                toolInUse.setBeingUsed(false);
                if(player.isPlacedDie() && player.isUsedTool()) {
                    player.setPlacedDie(false);
                    player.setUsedTool(false);
                    nextTurn();
                } else
                    virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), player.getWindowPattern(), match.getRound().getDraftPool()));
            } else {

                toolInUse.setBeingUsed(false);
                boolean toolSixInUse = false;
                for (Die die: match.getRound().getDraftPool().getBag())
                    if (die.isPlacing()) toolSixInUse=true;

                if(!toolSixInUse) nextTurn();

            }
        }
    }

    @Override
    public void visit(MessageRequestUseOfTool message) {
        match.getTools().get(message.getNumberOfTool()).requestOrders(searchNick(message.getNickname()), match);
    }

    @Override
    public void visit(MessageForcedMove message) {
        Player player = searchNick(message.getNickname());
        Die d = null;
        for (Die die: match.getRound().getDraftPool().getBag())
            if (die.isPlacing())
                d = die;

        if (message.getNewValue()!=0)
            d.setValue(message.getNewValue());

        if (message.getRow()!=-1) {
            boolean error = false;
            if (!isNearDie(player.getWindowPattern(), message.getRow(), message.getColumn())) {
                error=true;
                virtualView.send(new MessageErrorMove(player.getNickname(), "No dice near the position"));

            }
            if (!verifyNumber(player.getWindowPattern(), message.getRow(), message.getColumn(), d)) {
                error=true;
                virtualView.send(new MessageErrorMove(player.getNickname(), "Violated Value Restriction!"));
            }
            if (!verifyColor(player.getWindowPattern(), message.getRow(), message.getColumn(), d)) {
                error=true;
                virtualView.send(new MessageErrorMove(player.getNickname(), "Violated Colour Restriction!"));
            }
            if (error) {
                if (message.getNewValue() == 0) {
                    virtualView.send(new MessageForceMove(message.getNickname(), d, player.getWindowPattern(), false, true));
                    return;
                } else {
                    virtualView.send(new MessageForceMove(message.getNickname(), d, player.getWindowPattern(), false, player.isPlacedDie()));
                    return;
                }
            }
            match.getRound().getDraftPool().removeDie(match.getRound().getDraftPool().getBag().indexOf(d));
            player.getWindowPattern().putDice(d, message.getRow(), message.getColumn());
            match.notifyObservers(new MessageWPChanged(player.getNickname(), player.getWindowPattern()));
            match.notifyObservers(new MessageDPChanged(match.getRound().getDraftPool()));
            player.setPlacedDie(false);
            player.setUsedTool(false);
            virtualView.send(new MessageConfirmMove(player.getNickname(), false));
            nextTurn();
            return;
        }
        virtualView.send(new MessageAskMove(player.getNickname(), player.isUsedTool(), player.isPlacedDie(), player.getWindowPattern(), match.getRound().getDraftPool()));

    }
}
