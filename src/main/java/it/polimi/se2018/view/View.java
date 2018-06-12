package it.polimi.se2018.view;

import it.polimi.se2018.controller.RequestType;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.*;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforserver.MessagePing;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.network.socket.client.SagradaClient;
import it.polimi.se2018.utils.HandleJSON;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.fusesource.jansi.AnsiConsole;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static java.lang.System.*;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class View extends Observable implements Observer, VisitorView {

    private static final int MAX_ROW = 4;
    private static final int MAX_COL = 5;
    private static final int MAX_DP = 9;
    private static final String ROW = "row";
    private static final String COLUMN = "column";

    private Scanner scanner;
    private String nickname;

    private JSONObject introductionStrings;
    private JSONObject stateUpdateStrings;
    private JSONObject handleStrings;
    private JSONObject askStrings;
    private JSONObject printStrings;

    public View() {
        scanner = new Scanner(System.in);
        initializeJSON();
        System.setProperty("jansi.passthrough", "true");
        AnsiConsole.systemInstall();
        out.println(introductionStrings.get("welcome"));

    }

    private void initializeJSON() {
        InputStream in = HandleJSON.class.getResourceAsStream("/fileutils/viewstrings");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        JSONArray strings = (JSONArray) obj;
        introductionStrings = (JSONObject) strings.get(0);
        stateUpdateStrings = (JSONObject) strings.get(1);
        handleStrings = (JSONObject) strings.get(2);
        askStrings = (JSONObject) strings.get(3);
        printStrings = (JSONObject) strings.get(4);
    }


    public int askConnection() {
        out.println(introductionStrings.get("askConnection"));
        return chooseBetween(1,2);
    }

    public String askNickname() {
        out.println(introductionStrings.get("askNickname"));
        this.nickname = scanner.nextLine();
        return this.nickname;
    }

    private void nicknameConfirmation(MessageNickname nicknameMessage) {
        if (nicknameMessage.getBoolean()) {
            out.println(introductionStrings.get("nicknameIsOk"));
        }
        else {
            out.println(introductionStrings.get("nicknameNotOk"));
            askNickname();
            notifyObservers(new Message(nickname));
        }
    }

    private void showPrivateObjective(String description) {
        out.println(stateUpdateStrings.get("privateObjective") + description);
    }

    private void showPublicObjective(List<String> descriptions, List<Integer> points) {
        out.println(stateUpdateStrings.get("publicObjective"));
        for(int i=0; i<descriptions.size(); i++) {
            out.println(i+1 + ") " + descriptions.get(i) + " VP: " + points.get(i));
        }
    }

    private void showTools(List<String> names) {
        out.println(stateUpdateStrings.get("tools"));
        for(int i=0; i<names.size(); i++) {
            out.println(i+1 + ") " + names.get(i));
        }
    }

    private void showRoundTrack(Map<Integer, List<Die>> roundTrack) {

        if (roundTrack.isEmpty()) {
            out.println(stateUpdateStrings.get("roundtrackEmpty"));
            return;
        }
        out.print(stateUpdateStrings.get("roundtrack"));
        for (Integer j : roundTrack.keySet()) {
            out.print("\n"+j+": ");
            for (Die d : roundTrack.get(j)) {
                for (Color color : Color.values()) {
                    if (color.toString().equals(d.getColour().toString())) {
                        out.print(ansi().fg(color).a("[" + d.getValue() + "] ").reset());
                    }
                }
            }
        }

    }

    private void windowPatternUpdated(String player, WindowPattern wp) {
        if (!player.equals(this.nickname)) {
            out.println(player + stateUpdateStrings.get("otherPlayerWP"));
            printWindowPattern(wp);
        }
        else {
            out.println(stateUpdateStrings.get("myWP"));
            printWindowPattern(wp);
        }
    }

    private void askWindowPattern(int firstCard, int secondCard) {

        int choice;
        List<WindowPattern> windowPatterns = new ArrayList<>();

        out.println(handleStrings.get("askWindowPattern1"));
        windowPatterns.add(HandleJSON.createWindowPattern(null, firstCard, 0));
        windowPatterns.add(HandleJSON.createWindowPattern(null, firstCard, 1));
        windowPatterns.add(HandleJSON.createWindowPattern(null, secondCard, 0));
        windowPatterns.add(HandleJSON.createWindowPattern(null, secondCard, 1));
        for (WindowPattern wp: windowPatterns) {
            out.print(windowPatterns.indexOf(wp)+1 + ") ");
            printWindowPattern(wp);
        }
        out.println(handleStrings.get("askWindowPattern2"));
        choice = chooseBetween(1, 4);

        if (choice<3) {
            notifyObservers(new MessageSetWP(nickname, firstCard, choice-1));
        } else {
            notifyObservers(new MessageSetWP(nickname, secondCard, (choice-1)%2));
        }
    }

    private void handleTurnChange(String playerTurn) {
        if(playerTurn.equals(this.nickname)) {
            out.println( ansi().fg(RED).a(handleStrings.get("yourTurnChange")).reset() );
        } else {
            out.println("\n" + playerTurn + handleStrings.get("otherTurnChange"));
        }
    }

    private void handleErrorMove(String reason) {
        out.println(handleStrings.get("errorMove") + reason);
        out.println(handleStrings.get("tryAgain"));
    }

    private void handleSuccessMove(boolean isThereAnotherMove) {
        out.println(handleStrings.get("successMove"));
        if(!isThereAnotherMove) out.println(handleStrings.get("turnIsOver"));
    }

    private void handleRoundChanged(String nickname, int round, DraftPool draftPool) {
        out.println();
        out.println("_|-|_|-|_|-|_|-|_|-|_|-|_|-|_|-|  ROUND " + round + " |-|_|-|_|-|_|-|_|-|_|-|_|-|_|-|_|\n");
        out.println(handleStrings.get("dpForRound"));
        printDraftPool(draftPool);
        handleTurnChange(nickname);
    }

    private void handleEndMatch(Map<String , Integer> results, boolean isLastPlayer) {

        out.println( ansi().eraseScreen() );
        if(isLastPlayer) {
            out.println(handleStrings.get("lastPlayer"));
        }

        out.println( ansi().fg(RED).a(handleStrings.get("endMatch")).reset());
        out.print(handleStrings.get("winnerIs"));
        out.println(results.keySet().toArray()[0] + "!\n");
        out.println(handleStrings.get("fullClassification"));

        int i = 1;

        for (String playerNickname: results.keySet()) {
            out.println( i +") " + playerNickname + handleStrings.get("with") + results.get(playerNickname) + handleStrings.get("points"));
            i++;
        }

        out.println(handleStrings.get("playAgain"));

        int choice = chooseBetween(1, 2);
        if(choice==1) SagradaClient.newConnection(this.nickname);
        else SagradaClient.closeClient();

    }

    private void askMove(boolean hasMovedDie, boolean hasUsedTool, WindowPattern windowPattern, DraftPool draftPool) {

        boolean moveDieOk = true;
        boolean moveToolOk = true;

        if (windowPattern!=null) {
            out.println(askStrings.get("windowPattern"));
            printWindowPattern(windowPattern);
        }
        if (draftPool!=null) {
            out.println(askStrings.get("draftPool"));
            printDraftPool(draftPool);
        }

        //out.println( ansi().eraseScreen() );
        out.println(askStrings.get("selectMove"));
        int i=1;
        out.println(i + askStrings.get("requestInformation").toString());
        i++;
        if (!hasMovedDie) {
            out.println(i + askStrings.get("moveDie").toString());
            i++;
        }
        if (!hasUsedTool) {
            out.println(i + askStrings.get("useTool").toString());
            i++;
        }
        out.println(i + askStrings.get("doNothing").toString());
        int choice = chooseBetween(1, i);

        if (choice==1) {
            requestInformation();
        }
        if (choice==2 && !hasMovedDie) moveDieOk = moveDie();
        if ((choice==2 && hasMovedDie && !hasUsedTool)||(choice==3 && !hasMovedDie && !hasUsedTool)) moveToolOk = useTool();
        if (choice==3 && (hasMovedDie || hasUsedTool)) notifyObservers(new MessageDoNothing(this.nickname));
        if (choice==4) notifyObservers(new MessageDoNothing(this.nickname));
        if (choice==2 && hasMovedDie && hasUsedTool) notifyObservers(new MessageDoNothing(this.nickname));

        if(!moveDieOk || !moveToolOk) {
            askMove(hasMovedDie, hasUsedTool, windowPattern, draftPool);
        }

    }

    private void requestInformation() {
        out.println(askStrings.get("whichInformation"));
        for (RequestType t: RequestType.values()) {
            out.println((t.ordinal()+1)+") "+t.toString());
        }
        int choice = chooseBetween(1, 6);
        for (RequestType t: RequestType.values())
            if (t.ordinal()+1==choice)
                notifyObservers(new MessageRequest(this.nickname, t));

    }

    private boolean moveDie() {

        out.println(askStrings.get("askDieFromDp"));
        int dieToMove = chooseBetween(0, MAX_DP);
        if(dieToMove==0) return false;

        out.println(askStrings.get("askPositionInWp"));

        out.print(askStrings.get(ROW));
        int row = chooseBetween(0, MAX_ROW);
        if (row==0) return false;

        out.print(askStrings.get(COLUMN));
        int column = chooseBetween(0, MAX_COL);
        if (column==0) return false;

        notifyObservers(new MessageMoveDie(this.nickname, dieToMove-1, row-1, column-1));
        return true;

    }

    private boolean useTool() {
        out.println(askStrings.get("askTool"));
        int choice = chooseBetween(1, 3);
        notifyObservers(new MessageRequestUseOfTool(nickname, choice-1));
        return true;
    }

    private void handleToolUse(MessageToolOrder message) {

        int i;
        int diceFromDp = 0;
        List<Integer[]> diceFromWp = new ArrayList<>();
        List<Integer> diceFromRoundtrack = new ArrayList<>();
        List<Integer[]> positionsInWp = new ArrayList<>();
        int newValue = 0;
        boolean plusOne = false;


        for(i=0; i<message.getDiceFromDp(); i++) {
            out.println(askStrings.get("askDieFromDp"));
            int n = chooseBetween(0, 9);
            if (n==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
           diceFromDp = n-1;
        }

        if(message.getDiceFromWp()>1) {
            out.println(askStrings.get("multipleDice"));
        }

        for(i=0; i<message.getDiceFromWp(); i++) {
            out.println(askStrings.get("diceFromWp"));
            out.print(askStrings.get(ROW));
            int x = chooseBetween(0, MAX_ROW);
            if (x==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            out.print(askStrings.get(COLUMN));
            int y = chooseBetween(0, MAX_COL);
            if (y==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            Integer[] positions = { x-1, y-1 };
            diceFromWp.add(positions);
        }

        for(i=0; i<message.getPositionInWp(); i++) {
            out.println(askStrings.get("askPositionInWp"));
            out.print(askStrings.get(ROW));
            int x = chooseBetween(0, MAX_ROW);
            if (x==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            out.print(askStrings.get(COLUMN));
            int y = chooseBetween(0, MAX_COL);
            if (y==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            Integer[] positions = { x-1, y-1 };
            positionsInWp.add(positions);
        }

        if(message.isAskPlusOrMinusOne()) {
            out.println(askStrings.get("addOrRemove"));
            int choice = chooseBetween(0,2);
            if (choice==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            if (choice==1) plusOne=true;
            else plusOne=false;
        }

        for(i=0; i<message.getNewValue(); i++) {
            out.println(askStrings.get("newValue"));
            newValue = chooseBetween(0, 6);
            if (newValue==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            newValue--;
        }

        notifyObservers(new MessageToolResponse(nickname, diceFromDp, diceFromWp, diceFromRoundtrack, positionsInWp, newValue, plusOne));

    }

    private void forceMove(Die die, WindowPattern windowPattern) {
        out.print(askStrings.get("placeThisDie"));
        for (Color color : Color.values()) {
            if (color.toString().equals(die.getColour().toString())) {
                out.println(ansi().fg(color).a("[" + die.getValue() + "] ").reset());
            }
        }

        printWindowPattern(windowPattern);
        int column=0;
        int row=0;
        while(column == 0) {
            out.print(askStrings.get(ROW));
            row = chooseBetween(1, MAX_ROW);

            out.print(askStrings.get(COLUMN));
            column = chooseBetween(0, MAX_COL);
        }

        notifyObservers(new MessageForcedMove(this.nickname, row-1, column-1));

    }

    private void printWindowPattern(WindowPattern wp) {
        out.println("\n" + wp.getName() + printStrings.get("difficulty") + wp.getDifficulty());
        out.print("   1   2   3   4   5");
        for (int i=0; i<MAX_ROW; i++) {
            out.println();
            out.print(i+1 +" ");
            for (int j=0; j<MAX_COL; j++) {
                if (wp.getBox(i, j).getDie() == null) {
                    printBoxWithoutDie(wp.getBox(i, j));
                } else {
                    printBoxWithDie(wp.getBox(i,j));
                }
            }
        }
        out.println();
        out.println();
    }

    private void printBoxWithoutDie(Box box) {
        if (box.hasNoValueRestriction()) {
            if(box.getValueRestriction()!=0) out.print("[" + box.getValueRestriction() + "] ");
            else out.print("[ ] ");
        } else {
            Colour boxColour = box.getColourRestriction();
            for (Color color : Color.values()) {
                if (color.toString().equals(boxColour.toString())) {
                    out.print(ansi().fg(color).a("[ ] ").reset());
                }
            }
        }
    }

    private void printBoxWithDie(Box box) {
        Colour dieColour = box.getDie().getColour();
        Colour boxColour = box.getColourRestriction();
        for (Color color : Color.values()) {
            if (color.toString().equals(boxColour.toString())) {
                out.print(ansi().fg(color).a("[").reset());
                for (Color colorForDie : Color.values()) {
                    if(colorForDie.toString().equals(dieColour.toString())) out.print(ansi().fg(colorForDie).a(box.getDie().getValue()).reset());
                }
                out.print(ansi().fg(color).a("] ").reset());
            }
        }
    }

    private void printDraftPool(DraftPool dp) {
        out.println();
        for(Die d: dp.getBag()) {
            out.print(" " + (dp.getBag().indexOf(d)+1) + "  ");
        }
        out.println();
        for(Die d: dp.getBag()) {
            for (Color color : Color.values()) {
                if (color.toString().equals(d.getColour().toString())) {
                    out.print(ansi().fg(color).a("[" + d.getValue() + "] ").reset());
                }
            }
        }
        out.println("\n");
    }

    private int chooseBetween(int min, int max) {

        int choice;
        choice = scanner.nextInt();
        while(choice<min || choice>max) {
            out.println(askStrings.get("invalidChoice"));
            choice = scanner.nextInt();
        }
        scanner.nextLine(); // To fix java bug
        return choice;

    }


    @Override
    public void update(Message m)
    {
        m.accept(this);
    }

    @Override
    public void visit(Message message) {
        out.println("This is a message!");
    }

    @Override
    public void visit(MessageTool message) {
        showTools(message.getNames());
    }

    @Override
    public void visit(MessageError message) {
        out.println("This is a message error!");
    }

    @Override
    public void visit(MessageNickname message) {
        nicknameConfirmation(message);
    }

    @Override
    public void visit(MessagePrivObj message) {
        showPrivateObjective(message.getDescription());
    }

    @Override
    public void visit(MessagePublicObj message) {
        showPublicObjective(message.getDescriptions(), message.getPoints());
    }

    @Override
    public void visit(MessageChooseWP message) {
        askWindowPattern(message.getFirstIndex(), message.getSecondIndex());
    }

    @Override
    public void visit(MessageWPChanged message) {
        windowPatternUpdated(message.getPlayer(), message.getWp());
    }

    @Override
    public void visit(MessageTurnChanged message) {
        handleTurnChange(message.getNickname());
    }

    @Override
    public void visit(MessageDPChanged message) {
        //printDraftPool(message.getDraftPool());
    }

    @Override
    public void visit(MessageConfirmMove message) {
        handleSuccessMove(message.isThereAnotherMove());
    }

    @Override
    public void visit(MessageErrorMove message) {
        handleErrorMove(message.getReason());
    }

    @Override
    public void visit(MessagePing message){
        //THIS MESSAGE IS USED TO SERVER FOR VERIFY THE CONNECTION.
    }

    @Override
    public void visit(MessageRoundChanged message) {
        handleRoundChanged(message.getNickname(), message.getNumRound(), message.getDraftPool());
    }

    @Override
    public void visit(MessageRoundTrack message) {
        showRoundTrack(message.getRoundTrack());
    }

    @Override
    public void visit(MessageEndMatch message) {
        handleEndMatch(message.getResults(), message.isLastPlayer());
    }

    @Override
    public void visit(MessageToolOrder message) {
        handleToolUse(message);
    }

    @Override
    public void visit(MessageAskMove message) {
        askMove(message.isHasMovedDie(), message.isHasUsedTool(), message.getWindowPattern(), message.getDraftPool());
    }

    @Override
    public void visit(MessageForceMove message) {
        forceMove(message.getDie(), message.getWindowPattern());
    }
}


