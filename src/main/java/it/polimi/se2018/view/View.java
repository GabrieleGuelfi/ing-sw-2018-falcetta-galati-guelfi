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

import java.util.*;

import it.polimi.se2018.utils.StringJSON;
import org.fusesource.jansi.AnsiConsole;

import static java.lang.System.*;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class View extends Observable implements Observer, VisitorView, ViewInterface {

    private static final int MAX_ROW = 4; //already in WindowPattern, can we use it?
    private static final int MAX_COL = 5;
    private static final int MAX_DP = 9;
    private static final String ROW = "row";
    private static final String COLUMN = "column";

    private static View view;
    private Scanner scanner;
    private String nickname;

    public View(String nickname) {

        this.nickname = nickname;
        scanner = new Scanner(System.in);
        System.setProperty("jansi.passthrough", "true");
        AnsiConsole.systemInstall();
        out.println(StringJSON.printStrings("introductionStrings", "welcome"));

    }

    public int askConnection() {
        out.println(StringJSON.printStrings("introductionStrings", "askConnection"));
        return chooseBetween(1,2);
    }

    public String askNickname() {
        out.println(StringJSON.printStrings("introductionStrings", "askNickname"));
        this.nickname = scanner.nextLine();
        return this.nickname;
    }

    private void nicknameConfirmation(MessageNickname nicknameMessage) {
        if (nicknameMessage.getBoolean()) {
            out.println(StringJSON.printStrings("introductionStrings","nicknameIsOk"));
        }
        else {
            out.println(StringJSON.printStrings("introductionStrings","nicknameNotOk"));
            askNickname();
            notifyObservers(new Message(nickname));
        }
    }

    private void showPrivateObjective(String description) {
        out.println(StringJSON.printStrings("stateUpdateStrings","privateObjective") + description);
    }

    private void showPublicObjective(List<String> descriptions, List<Integer> points) {
        out.println(StringJSON.printStrings("stateUpdateStrings", "publicObjective"));
        for(int i=0; i<descriptions.size(); i++) {
            out.println(i+1 + ") " + descriptions.get(i) + " VP: " + points.get(i));
        }
    }

    private void showTools(List<String> names) {
        out.println(StringJSON.printStrings("stateUpdateStrings","tools"));
        for(int i=0; i<names.size(); i++) {
            out.println(i+1 + ") " + names.get(i));
        }
    }

    private void showRoundTrack(Map<Integer, List<Die>> roundTrack) {

        if (roundTrack.isEmpty()) {
            out.println(StringJSON.printStrings("stateUpdateStrings","roundtrackEmpty"));
            return;
        }
        out.print(StringJSON.printStrings("stateUpdateStrings","roundtrack"));
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
            out.println(player + StringJSON.printStrings("stateUpdateStrings","otherPlayerWP"));
            printWindowPattern(wp);
        }
        else {
            out.println(StringJSON.printStrings("stateUpdateStrings","myWP"));
            printWindowPattern(wp);
        }
    }

    private void askWindowPattern(int firstCard, int secondCard) {

        int choice;
        List<WindowPattern> windowPatterns = new ArrayList<>();

        out.println(StringJSON.printStrings("handleStrings", "askWindowPattern1"));
        windowPatterns.add(HandleJSON.createWindowPattern(null, firstCard, 0));
        windowPatterns.add(HandleJSON.createWindowPattern(null, firstCard, 1));
        windowPatterns.add(HandleJSON.createWindowPattern(null, secondCard, 0));
        windowPatterns.add(HandleJSON.createWindowPattern(null, secondCard, 1));
        for (WindowPattern wp: windowPatterns) {
            out.print(windowPatterns.indexOf(wp)+1 + ") ");
            printWindowPattern(wp);
        }
        out.println(StringJSON.printStrings("handleStrings", "askWindowPattern2"));
        choice = chooseBetween(1, 4);

        if (choice<3) {
            notifyObservers(new MessageSetWP(nickname, firstCard, choice-1));
        } else {
            notifyObservers(new MessageSetWP(nickname, secondCard, (choice-1)%2));
        }
    }

    private void handleTurnChange(String playerTurn) {
        if(playerTurn.equals(this.nickname)) {
            out.println( ansi().fg(RED).a(StringJSON.printStrings("handleStrings","yourTurnChange")).reset() );
        } else {
            out.println("\n" + playerTurn + StringJSON.printStrings("handleStrings","otherTurnChange"));
        }
    }

    private void handleErrorMove(String reason) {
        out.println(StringJSON.printStrings("handleStrings", "errorMove") + reason);
        out.println(StringJSON.printStrings("handleStrings","tryAgain"));
    }

    private void handleSuccessMove(boolean isThereAnotherMove) {
        out.println(StringJSON.printStrings("handleStrings", "successMove"));
        if(!isThereAnotherMove) out.println(StringJSON.printStrings("handleStrings","turnIsOver"));
    }

    private void handleRoundChanged(String nickname, int round, DraftPool draftPool) {
        out.println();
        out.println("_|-|_|-|_|-|_|-|_|-|_|-|_|-|_|-|  ROUND " + round + " |-|_|-|_|-|_|-|_|-|_|-|_|-|_|-|_|\n");
        out.println(StringJSON.printStrings("handleStrings","dpForRound"));
        printDraftPool(draftPool);
        handleTurnChange(nickname);
    }

    private void handleEndMatch(Map<String , Integer> results, boolean isLastPlayer) {

        out.println( ansi().eraseScreen() );
        if(isLastPlayer) {
            out.println(StringJSON.printStrings("handleStrings","lastPlayer"));
        }

        out.println( ansi().fg(RED).a(StringJSON.printStrings("handleStrings","endMatch")).reset());
        out.print(StringJSON.printStrings("handleStrings","winnerIs"));
        out.println(results.keySet().toArray()[0] + "!\n");
        out.println(StringJSON.printStrings("handleStrings","fullClassification"));

        int i = 1;

        for (String playerNickname: results.keySet()) {
            out.println( i +") " + playerNickname + StringJSON.printStrings("handleStrings","with") + results.get(playerNickname) + StringJSON.printStrings("handleStrings","points"));
            i++;
        }

        out.println(StringJSON.printStrings("handleStrings","playAgain"));

        int choice = chooseBetween(1, 2);
        if(choice==1) SagradaClient.newConnection(this.nickname);
        else SagradaClient.closeClient();

    }

    private void askMove(boolean hasMovedDie, boolean hasUsedTool, WindowPattern windowPattern, DraftPool draftPool) {

        boolean moveDieOk = true;
        boolean moveToolOk = true;

        if (windowPattern!=null) {
            out.println(StringJSON.printStrings("askStrings","windowPattern"));
            printWindowPattern(windowPattern);
        }
        if (draftPool!=null) {
            out.println(StringJSON.printStrings("askStrings","draftPool"));
            printDraftPool(draftPool);
        }

        //out.println( ansi().eraseScreen() );
        out.println(StringJSON.printStrings("askStrings","selectMove"));
        int i=1;
        out.println(i + StringJSON.printStrings("askStrings","requestInformation"));
        i++;
        if (!hasMovedDie) {
            out.println(i + StringJSON.printStrings("askStrings","moveDie"));
            i++;
        }
        if (!hasUsedTool) {
            out.println(i + StringJSON.printStrings("askStrings","useTool"));
            i++;
        }
        out.println(i + StringJSON.printStrings("askStrings","doNothing"));
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
        out.println(StringJSON.printStrings("askStrings","whichInformation"));
        for (RequestType t: RequestType.values()) {
            out.println((t.ordinal()+1)+") "+t.toString());
        }
        int choice = chooseBetween(1, 6);
        for (RequestType t: RequestType.values())
            if (t.ordinal()+1==choice)
                notifyObservers(new MessageRequest(this.nickname, t));

    }

    private boolean moveDie() {

        out.println(StringJSON.printStrings("askStrings","askDieFromDp"));
        int dieToMove = chooseBetween(0, MAX_DP);
        if(dieToMove==0) return false;

        out.println(StringJSON.printStrings("askStrings","askPositionInWp"));

        out.print(StringJSON.printStrings("askStrings", ROW));
        int row = chooseBetween(0, MAX_ROW);
        if (row==0) return false;

        out.print(StringJSON.printStrings("askStrings", COLUMN));
        int column = chooseBetween(0, MAX_COL);
        if (column==0) return false;

        notifyObservers(new MessageMoveDie(this.nickname, dieToMove-1, row-1, column-1));
        return true;

    }

    private boolean useTool() {
        out.println(StringJSON.printStrings("askStrings","askTool"));
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
            out.println(StringJSON.printStrings("askStrings","askDieFromDp"));
            int n = chooseBetween(0, 9);
            if (n==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
           diceFromDp = n-1;
        }

        if(message.getDiceFromWp()>1) {
            out.println(StringJSON.printStrings("askStrings","multipleDice"));
        }

        int requestedDiceFromWp = message.getDiceFromWp();
        int requestedPositionInWp = message.getPositionInWp();

        if(message.isCanReduceDiceFromWP()) {
            out.println("How many dice do you want to move? (0 to escape)");
            int choice = chooseBetween(0, 2);
            if(choice==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            requestedDiceFromWp = choice;
            requestedPositionInWp = choice;
        }

        for(i=0; i<requestedDiceFromWp; i++) {
            out.println(StringJSON.printStrings("askStrings","diceFromWp"));
            out.print(StringJSON.printStrings("askStrings", ROW));
            int x = chooseBetween(0, MAX_ROW);
            if (x==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            out.print(StringJSON.printStrings("askStrings", COLUMN));
            int y = chooseBetween(0, MAX_COL);
            if (y==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            Integer[] positions = { x-1, y-1 };
            diceFromWp.add(positions);
        }

        for(i=0; i<requestedPositionInWp; i++) {
            out.println(StringJSON.printStrings("askStrings","askPositionInWp"));
            out.print(StringJSON.printStrings("askStrings", ROW));
            int x = chooseBetween(0, MAX_ROW);
            if (x==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            out.print(StringJSON.printStrings("askStrings", COLUMN));
            int y = chooseBetween(0, MAX_COL);
            if (y==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            Integer[] positions = { x-1, y-1 };
            positionsInWp.add(positions);
        }

        for(i=0; i<message.getDiceFromRoundtrack(); i++) {
            out.println("\nChoose die from Roundtrack (0 to abort)");
            out.print("Turn: ");
            int x = chooseBetween(0, 9);
            if (x==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            diceFromRoundtrack.add(x);
            out.print("Die: ");
            int y = chooseBetween(0, 9);
            if (y==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            diceFromRoundtrack.add(y-1);
        }

        if(message.isAskPlusOrMinusOne()) {
            out.println(StringJSON.printStrings("askStrings","addOrRemove"));
            int choice = chooseBetween(0,2);
            if (choice==0) {
                notifyObservers(new MessageToolResponse(nickname));
                return;
            }
            if (choice==1) plusOne=true;
            else plusOne=false;
        }

        notifyObservers(new MessageToolResponse(nickname, diceFromDp, diceFromWp, diceFromRoundtrack, positionsInWp, plusOne));

    }

    private void forceMove(Die die, WindowPattern windowPattern, boolean chooseNewValue, boolean placedDie, boolean canChoose) {
        int newValue=0;

        if (chooseNewValue)
            out.print("Choose the new value of the die: ");
        else
            out.print("You had to place this die: ");
        for (Color color : Color.values()) {
            if (color.toString().equals(die.getColour().toString())) {
                out.println(ansi().fg(color).a("[" + die.getValue() + "] ").reset());
            }
        }
        printWindowPattern(windowPattern);
        if (chooseNewValue) {
            out.println("Choose the new value: ");
            newValue = chooseBetween(1, 6);
        }

        if (!placedDie && canChoose) {
            out.println("Do you want to place this die?\n1) Yes\n2) No ");
            int choice = chooseBetween(1, 2);
            if (choice == 1)
                out.println("Choose position: ");
            else {
                notifyObservers(new MessageForcedMove(this.nickname, -1, -1, newValue, true));
                return;
            }
        }
        else if (placedDie && canChoose) {
            notifyObservers(new MessageForcedMove(this.nickname, -1, -1, newValue, true));
            return;
        }

        int column=0;
        int row=0;
        while(column == 0) {
            out.print(StringJSON.printStrings("askStrings", ROW));
            row = chooseBetween(1, MAX_ROW);

            out.print(StringJSON.printStrings("askStrings", COLUMN));
            column = chooseBetween(0, MAX_COL);
        }

        notifyObservers(new MessageForcedMove(this.nickname, row-1, column-1, newValue, canChoose));

    }

    private void printWindowPattern(WindowPattern wp) {
        out.println("\n" + wp.getName() + StringJSON.printStrings("printStrings","difficulty") + wp.getDifficulty());
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
            out.println(StringJSON.printStrings("askStrings","invalidChoice"));
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
        forceMove(message.getDie(), message.getWindowPattern(), message.isNewValue(), message.isPlacedDie(), message.isCanChoose());
    }

    public void addObserver(Observer observer){
        this.register(observer);
    }
    public void notifyObserver(Message message){}
}


