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
import it.polimi.se2018.utils.HandleJSON;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

import java.util.*;

import org.fusesource.jansi.AnsiConsole;

import static java.lang.System.*;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class View extends Observable implements Observer, VisitorView {

    private static final int MAX_ROW = 4;
    private static final int MAX_COL = 5;
    private static final int MAX_DP = 9;

    private static View view;
    private Scanner scanner;
    private String nickname;

    private View() {
        scanner = new Scanner(System.in);
        System.setProperty("jansi.passthrough", "true");
        AnsiConsole.systemInstall();
        out.println("Welcome in Sagrada!");

    }

    public static View createView() {
        if (view == null) {
            view = new View();
            return view;
        }
        else {
            return view;
        }
    }

    public int askConnection() {
        out.println("Choose method connection between:");
        out.println("1) Socket");
        out.println("2) RMI");
        return chooseBetween(1,2);
    }

    public String askNickname() {
        out.println("Please, choose your nickname");
        this.nickname = scanner.nextLine();
        return this.nickname;
    }

    private void nicknameConfirmation(MessageNickname nicknameMessage) {
        if (nicknameMessage.getBoolean()) {
            out.println("\nAll settled! Wait for the game to begin...");
        }
        else {
            out.println("\nNickname already used: choose another one");
            askNickname();
            notifyObservers(new Message(nickname));
        }
    }

    private void showPrivateObjective(String description) {
        out.println("\nYour private objective is:\n" + description);
    }

    private void showPublicObjective(List<String> descriptions, List<Integer> points) {
        out.println("\nPublic objectives: ");
        for(int i=0; i<descriptions.size(); i++) {
            out.println(i+1 + ") " + descriptions.get(i) + " VP: " + points.get(i));
        }
    }

    private void showTools(List<String> names) {
        out.println("\nTools: ");
        for(int i=0; i<names.size(); i++) {
            out.println(i+1 + ") " + names.get(i));
        }
    }

    private void showRoundTrack(Map<Integer, List<Die>> roundTrack) {

        if (roundTrack.isEmpty()) {
            out.println("\nRound Track empty! This is the First Turn!");
            return;
        }
        out.print("\nRound Track: ");
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

    private void askWindowPattern(int firstCard, int secondCard) {

        int choice;
        List<WindowPattern> windowPatterns = new ArrayList<>();

        out.println("\nYou have to choose the window pattern between these four: \n");
        windowPatterns.add(HandleJSON.createWindowPattern(null, firstCard, 0));
        windowPatterns.add(HandleJSON.createWindowPattern(null, firstCard, 1));
        windowPatterns.add(HandleJSON.createWindowPattern(null, secondCard, 0));
        windowPatterns.add(HandleJSON.createWindowPattern(null, secondCard, 1));
        for (WindowPattern wp: windowPatterns) {
            out.print(windowPatterns.indexOf(wp)+1 + ") ");
            printWindowPattern(wp);
        }
        out.println("Write the number of the window pattern you want to use.");
        choice = chooseBetween(1, 4);

        if (choice<3) {
            notifyObservers(new MessageSetWP(nickname, firstCard, choice-1));
        } else {
            notifyObservers(new MessageSetWP(nickname, secondCard, (choice-1)%2));
        }
    }

    private void windowPatternUpdated(String player, WindowPattern wp) {
        if (!player.equals(this.nickname)) {
            out.println("Player " + player + " windows pattern is now:");
            printWindowPattern(wp);
        }
        else {
            out.println("Your window pattern is now: ");
            printWindowPattern(wp);
        }
    }

    private void manageTurn(String playerTurn) {
        if(playerTurn.equals(this.nickname)) {
            out.println( ansi().fg(RED).a("\nIt's your turn!\n").reset() );
        } else {
            out.println("\n" + playerTurn + " is now playing...\n");
        }
    }

    private void handleErrorMove(String reason) {
        out.println("\nYour move has been rejected for this reason: " + reason);
        out.println("Try again...");
    }

    private void handleSuccessMove(boolean isThereAnotherMove) {
        out.println("\nSuccess! Your move has been accomplished.");
        if(!isThereAnotherMove) out.println("\nYour turn is over.\n");
    }

    private void handleRoundChanged(String nickname, int round, DraftPool draftPool) {
        out.println();
        out.println("_|-|_|-|_|-|_|-|_|-|_|-|_|-|_|-|  ROUND " + round + " |-|_|-|_|-|_|-|_|-|_|-|_|-|_|-|_|\n");
        out.println("Draftpool for this round: ");
        printDraftPool(draftPool);
        manageTurn(nickname);
    }

    private void handleEndMatch(Map<String , Integer> results) {

        /*
        Map<String , Integer> results = new HashMap<>();
        Map<String , Integer> resultsSorted;

        int i = 0;
        for(String playerNickname: nicknames) {
            results.put(playerNickname, points.get(i));
            i++;
        }

        resultsSorted = results.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        */

        out.println( ansi().eraseScreen() );
        out.println( ansi().fg(RED).a("End of the match!").reset());
        out.println("The winner is... " + results.keySet().toArray()[0] + "!\n");
        out.println("Full classification: ");


        int i = 1;

        for (String playerNickname: results.keySet()) {
            out.println( i +") " + playerNickname + " with " + results.get(playerNickname) + " points");
            i++;
        }

    }

    private void askMove(boolean hasMovedDie, boolean hasUsedTool, WindowPattern windowPattern, DraftPool draftPool) {

        boolean moveDieOk = true;
        boolean moveToolOk = true;

        if (windowPattern!=null) {
            out.println("\nCurrently, this is your windows pattern:");
            printWindowPattern(windowPattern);
        }
        if (draftPool!=null) {
            out.println("\nCurrently, this is the draftpool:");
            printDraftPool(draftPool);
        }

        //out.println( ansi().eraseScreen() );
        out.println("\n\nPlease, select your move: ");
        int i=1;
        out.println(i + ") Request information");
        i++;
        if (!hasMovedDie) {
            out.println(i + ") Move a die");
            i++;
        }
        if (!hasUsedTool) {
            out.println(i + ") Use a tool");
            i++;
        }
        out.println(i + ") Do nothing");
        int choice = chooseBetween(1, i);

        if (choice==1) {
            requestInformation();
        }
        if (choice==2 && !hasMovedDie) moveDieOk = moveDie();
        if ((choice==2 && hasMovedDie)||(choice==3 && !hasMovedDie && !hasUsedTool)) moveToolOk = useTool();
        if (choice==3 && (hasMovedDie || hasUsedTool)) notifyObservers(new MessageDoNothing(this.nickname));
        if (choice==4) notifyObservers(new MessageDoNothing(this.nickname));

        if(!moveDieOk || !moveToolOk) {
            askMove(hasMovedDie, hasUsedTool, windowPattern, draftPool);
        }

    }

    private void requestInformation() {
        out.println("\nWhat do you want to see?");
        for (RequestType t: RequestType.values()) {
            out.println((t.ordinal()+1)+") "+t.toString());
        }
        int choice = chooseBetween(1, 6);
        for (RequestType t: RequestType.values())
            if (t.ordinal()+1==choice)
                notifyObservers(new MessageRequest(this.nickname, t));

    }

    private boolean moveDie() {

        out.println("\nChoose a die from draftpool (0 to escape):");
        int dieToMove = chooseBetween(0, MAX_DP);
        if(dieToMove==0) return false;

        out.println("\nWhere do you want to place it? (0 to escape)");

        out.print("Row: ");
        int row = chooseBetween(0, MAX_ROW);
        if (row==0) return false;

        out.print("Column: ");
        int column = chooseBetween(0, MAX_COL);
        if (column==0) return false;

        notifyObservers(new MessageMoveDie(this.nickname, dieToMove-1, row-1, column-1));
        return true;

    }

    private boolean useTool() {
        out.println("Please, select the tool you want to use");
        int choice = chooseBetween(1, 3);
        notifyObservers(new MessageRequestUseOfTool(nickname, choice-1));
        return true;
    }

    private void handleToolUse(MessageToolOrder message) {

        int i;
        List<Integer> diceFromDp = new ArrayList<>();
        Map<Integer, Integer> diceFromWp = new HashMap<>();
        List<Integer> diceFromRoundtrack = new ArrayList<>();
        Map<Integer, Integer> positionsInWp = new HashMap<>();
        int newValue = 0;
        boolean plusOne = false;

        for(i=0; i<message.getDiceFromDp(); i++) {
            out.println("Choose dice from draftpool");
            int n = chooseBetween(1, 9);
            diceFromDp.add(n-1);
        }

        for(i=0; i<message.getDiceFromWp(); i++) {
            out.println("Choose dice from window pattern");
            out.print("Row: ");
            int x = chooseBetween(1, MAX_ROW);
            out.print("\nColum: ");
            int y = chooseBetween(1, MAX_COL);
            diceFromWp.put(x-1, y-1);
        }

        if(message.isAskPlusOrMinusOne()) {
            out.println("Do you want to add or remove 1?");
            out.println("1) Add");
            out.println("2) Remove");
            int choice = chooseBetween(1,2);
            if (choice==1) plusOne=true;
            else plusOne=false;
        }

        for(i=0; i<message.getNewValue(); i++) {
            out.println("Choose the new value: ");
            newValue = chooseBetween(1, 6)-1;
        }

        notifyObservers(new MessageToolResponse(nickname, diceFromDp, diceFromWp, diceFromRoundtrack, positionsInWp, newValue, plusOne));

    }

    private void printWindowPattern(WindowPattern wp) {
        out.println("\n" + wp.getName() + "\nDifficulty: " + wp.getDifficulty());
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
        for (Color color : Color.values()) {
            if (color.toString().equals(dieColour.toString())) {
                out.print(ansi().fg(color).a("[" + box.getDie().getValue() + "] ").reset());
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
            out.println("Invalid choice: try again.");
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
        manageTurn(message.getNickname());
    }

    @Override
    public void visit(MessageDPChanged message) {
        printDraftPool(message.getDraftPool());
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
        handleEndMatch(message.getResults());
    }

    @Override
    public void visit(MessageToolOrder message) {
        handleToolUse(message);
    }

    @Override
    public void visit(MessageAskMove message) {
        askMove(message.isHasMovedDie(), message.isHasUsedTool(), message.getWindowPattern(), message.getDraftPool());
    }

}


