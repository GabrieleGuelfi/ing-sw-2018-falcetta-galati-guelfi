package it.polimi.se2018.controller;


import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.model.*;
import it.polimi.se2018.model.dicecollection.Bag;
import it.polimi.se2018.model.publicobjective.PublicObjective;
import it.polimi.se2018.utils.Observer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller implements Observer {

    private Match match;

    public Controller() {
        this.match = null;
    }

    public void startGame(List<Player> players, View view) {
        List<PublicObjective> objectives = new ArrayList<>(); // Here we should have the real Public Objectives...

        List<Tool> tools = new ArrayList<>();
        // Create the match...
        this.match = new Match(new Bag(), players, objectives, tools);


        for(Player player: match.getActivePlayers()) {
            givePrivateObjective(player, player.getNickname());
        }

        for(Player player: match.getActivePlayers()) {
            giveWindowPatterns(player);
            giveFavorTokens(player);
        }

        // TOOLS PART!

        /*for (int i = 0; i < 10; i++) {
            match.nextNumRound();
            //manageRound(i % match.getPlayers().size());
        }*/

    }

    /**
     * send four window pattern to player, who choose one of them
     * @param player is the player to send window pattern
     */
    private void giveWindowPatterns(Player player){

        List<WindowPattern> patterns = new ArrayList<>();
        JSONParser parser = new JSONParser();
        List<Integer> rand = new ArrayList<>();
        Integer index;
        try {
            Object obj = parser.parse(new FileReader("./src/main/java/windowpattern/windowpattern"));
            JSONArray schemes = (JSONArray) obj;
            Random generator = new Random();
            for (int i=0; i<2; i++) {
                index = generator.nextInt(12);
                while (rand.contains(index))
                    index = generator.nextInt(12);
                rand.add(index);
                JSONArray schema = (JSONArray) schemes.get(index);
                for (int j = 0; j < 2; j++) {
                    JSONObject schema1 = (JSONObject) schema.get(j);
                    WindowPattern w = new WindowPattern((String) schema1.get("name"), (int) (long) schema1.get("difficulty"));
                    JSONArray grid = (JSONArray) schema1.get("grid");
                    createWindowPattern(grid, w);
                    patterns.add(w);
                }
            }
            /*
            for (int i=0; i<patterns.size(); i++) {
                System.out.println("\nName: "+patterns.get(i).getName()+"\nDifficulty: "+patterns.get(i).getDifficulty() );
                for (int k = 0; k < WindowPattern.MAX_ROW; k++) {
                    System.out.println("\nrow: " + k);
                    for (int z = 0; z < WindowPattern.MAX_COL; z++) {
                        try {
                            System.out.println(patterns.get(i).getBox(k, z).getColourRestriction() + " " + patterns.get(i).getBox(k, z).getValueRestriction());
                        } catch (OutOfWindowPattern e) {
                        }
                    }
                }
            }
            */
            //send(patterns)
        }
        catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
        catch (IOException e) {
            System.out.println("ioexception");
        }
        catch (ParseException e) {
            System.out.println("parseException");
        }

    }

    /**
     * create a window pattern from a json Object
     * @param grid is the grid from json file
     * @param w1 is the window pattern to be created
     */
    private void createWindowPattern (JSONArray grid, WindowPattern w1) {
        for (int k=0; k<WindowPattern.MAX_ROW; k++) {
            JSONArray row = (JSONArray)grid.get(k);
            boolean isNumber = true;
            int j=1;
            for (int i=0; i<10; i++){
                if (i%2==0) {
                    isNumber = "num".equals(row.get(i));
                }
                else {
                    if (isNumber) {
                        w1.setBox(new Box((int)(long)row.get(i)), k, i-j);
                    }
                    else {
                        w1.setBox(new Box(Colour.valueOf((String)row.get(i))), k, i-j);
                    }
                    j++;
                }
            }
        }
    }

    private void givePrivateObjective(Player player, String s) {
        System.out.println("Writing to...... " + s);
        View.getView().chooseValue(player, "Hi! I am the supreme Controller...");
    }

    private void giveFavorTokens(Player player) { //probably useless, we can make this in giveWindowPattern

    }

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

    /**
     * Verify if there is another Die near the position chosen by user
     * @param m move performed by user
     * @return false if there is another die near
     */
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

    /**
     * verify if there is another Die with the same colour near, or if there is colourRestriction in the Box
     * @param m move performed by user
     * @return false if Die break colour restriction
     */
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

    /**
     * verify if there is another Die with the same number near, or if there is numberRestriction in the Box
     * @param m move performed by user
     * @return false if Die break number restriction
     */
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

    public void update(Message message) {
        System.out.println(message.getString());
        // Here the controller should take the news from the view and handle them.
    }

    public void update(MessageDie message) {
        System.out.println("UPDATE PER MESSAGEDIE");
    }

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

    @Override
    public void update(Message message) {
        System.out.println("prova observer");
    } */
}
