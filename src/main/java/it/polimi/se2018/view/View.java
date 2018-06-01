package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.MessageDoNothing;
import it.polimi.se2018.events.messageforcontroller.MessageMoveDie;
import it.polimi.se2018.events.messageforcontroller.MessageSetWP;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforserver.MessagePing;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.utils.HandleJSON;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.fusesource.jansi.AnsiConsole;

import static java.lang.System.*;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class View extends Observable implements Observer, VisitorView {

    private static final int MAX_ROW = 4;
    private static final int MAX_COL = 5;

    private static View view;
    private Scanner scanner;
    private String nickname;

    private DraftPool currentDraftPool;
    private WindowPattern currentWindowPattern;

    private View() {
        scanner = new Scanner(System.in);
        System.setProperty("jansi.passthrough", "true");
        AnsiConsole.systemInstall();
        out.println("Welcome in Sagrada!");
    }

    public static View createView() {
        if (view ==null) {
            view = new View();
            return view;
        }
        else {
            return view;
        }
    }

    public String getNicknameForRmi() {
        out.println("Choose your nickname:");
        String nick = scanner.nextLine();
        nickname = nick;
        return nick;
    }

    public String askRmiOrSocket() {
        out.println("Rmi or socket?");
        return scanner.nextLine();
    }

    public void askNickname() {
        out.println("Please, choose your nickname");
        this.nickname = scanner.nextLine();
        notifyObservers(new Message(nickname));
    }

    private void nicknameConfirmation(MessageNickname nicknameMessage) {
        if (nicknameMessage.getBoolean()) {
            out.println("\nAll settled! Wait for the game to begin...");
        }
        else {
            out.println("\nNickname already used: choose another one");
            askNickname();
        }
    }

    private void showPrivateObjective(String colour) {
        out.println("\nYour private objective is of colour: " + colour);
    }

    private void showPublicObjective(List<String> descriptions, List<Integer> points) {
        out.println("\nPublic objectives: ");
        for(int i=0; i<descriptions.size(); i++) {
            out.println(i+1 + ") " + descriptions.get(i) + " VP: " + points.get(i));
        }
    }

    private void askWindowPattern(int firstCard, int secondCard) {

        int choice;
        List<WindowPattern> windowPatterns = new ArrayList<>();

        out.println("\nYou have to choose the window pattern between these four: ");
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
            this.currentWindowPattern = wp;
        }
    }

    private void manageTurn(String playerTurn) {
        if(playerTurn.equals(this.nickname)) {
            askMove(false, false);
        } else {
            out.println("\n" + playerTurn + " is now playing...");
        }
    }

    private void handleErrorMove(String reason, boolean hasMovedDie, boolean hasUsedTool) {
        out.println("Your move has been rejected for this reason: " + reason);
        out.println("Try again...");
        askMove(hasMovedDie, hasUsedTool);
    }

    private void handleSuccessMove(boolean hasMovedDie, boolean hasUsedTool) {
        out.println("Success! Your move has been accomplished.");
        if (hasMovedDie&&hasUsedTool) out.println("Your turn is over.");
        else askMove(hasMovedDie, hasUsedTool);
    }

    private void handleRoundChanged(String nickname, int round) {
        out.println();
        out.println("_|-|_|-|_|-|_|-| ROUND " + round + " |-|_|-|_|-|_|-|_");
        if (nickname.equals(this.nickname)) {
            out.println("It's your turn!");
            askMove(false, false);
        }
        else out.println(nickname + " is currently playing...");

    }

    private void askMove(boolean hasMovedDie, boolean hasUsedTool) {
        out.println("Please, select your move: ");
        int i=1;
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
        if (choice==1 && !hasMovedDie) moveDie();
        if ((choice==1 && hasMovedDie)||(choice==2 && !hasMovedDie && !hasUsedTool)) useTool();
        if (choice==2 && (hasMovedDie || hasUsedTool)) notifyObservers(new MessageDoNothing(this.nickname));
        if (choice==3) notifyObservers(new MessageDoNothing(this.nickname));

    }

    private void moveDie() {
        out.println("\nCurrently, this is your windows pattern:");
        printWindowPattern(currentWindowPattern);
        out.println("\nCurrently, this is the draftpool:");
        printDraftPool(currentDraftPool);
        out.println("\nChoose a die from draftpool:");
        int dieToMove = chooseBetween(1, currentDraftPool.size());
        out.println("\nWhere do you want to place it?");
        out.print("Row: ");
        int row = chooseBetween(1, MAX_ROW);
        out.print("Column: ");
        int column = chooseBetween(1, MAX_COL);
        notifyObservers(new MessageMoveDie(this.nickname, dieToMove-1, row-1, column-1));

    }

    private void useTool() {
        out.println("Please, select the tool you want to use");

    }

    private void printWindowPattern(WindowPattern wp) {
        out.println(wp.getName() + "\nDifficulty: " + wp.getDifficulty());
        out.print("   1   2   3   4   5");
        for (int i=0; i<MAX_ROW; i++) {
            out.println();
            out.print(i+1 +" ");
            for (int j=0; j<MAX_COL; j++) {
                if (wp.getBox(i, j).getDie() == null) {
                    if (wp.getBox(i, j).hasAValueRestriction()) {
                        out.print("[" + wp.getBox(i, j).getValueRestriction() + "] ");
                    } else {
                        out.print("[" + Colour.getFirstLetter(wp.getBox(i, j).getColourRestriction()) + "] ");
                    }
                } else {
                    Colour colour = wp.getBox(i, j).getDie().getColour();
                    if (colour.equals(Colour.RED)) {
                        out.print( ansi().fg(RED).a("[" + wp.getBox(i,j).getDie().getValue() + "] ").reset() );
                    }
                    if (colour.equals(Colour.BLUE)) {
                        out.print( ansi().fg(BLUE).a("[" + wp.getBox(i,j).getDie().getValue() + "] ").reset() );
                    }
                    if (colour.equals(Colour.GREEN)) {
                        out.print( ansi().fg(GREEN).a("[" + wp.getBox(i,j).getDie().getValue() + "] ").reset() );
                    }
                    if (colour.equals(Colour.YELLOW)) {
                        out.print( ansi().fg(YELLOW).a("[" + wp.getBox(i,j).getDie().getValue() + "] ").reset() );
                    }
                    if (colour.equals(Colour.PURPLE)) {
                        out.print( ansi().fg(MAGENTA).a("[" + wp.getBox(i,j).getDie().getValue() + "] ").reset() );
                    }
                }
            }
        }
        out.println();
        out.println();
    }

    private void printDraftPool(DraftPool dp) {
        out.println();
        for(Die d: dp.getBag()) {
            out.print(" " + (dp.getBag().indexOf(d)+1) + "   ");
        }
        out.println();
        for(Die d: dp.getBag()) {
            out.print("[" + Colour.getFirstLetter(d.getColour()) + d.getValue() + "] ");
        }
        out.println();
    }

    private int chooseBetween(int min, int max) {

        int choice;
        choice = scanner.nextInt();
        while(choice<min || choice>max) {
            out.println("Invalid choice: try again.");
            choice = scanner.nextInt();
        }
        return choice;

    }


    @Override
    public void update(Message m) {
        m.accept(this);
    }

    @Override
    public void visit(Message message) {
        out.println("This is a message!");
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
        showPrivateObjective(message.getColour());
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
        currentDraftPool = message.getDraftPool();
        printDraftPool(message.getDraftPool());
    }

    @Override
    public void visit(MessageConfirmMove message) {
        handleSuccessMove(message.hasPlacedDie(), message.hasUsedTool());
    }

    @Override
    public void visit(MessageErrorMove message) {
        handleErrorMove(message.getReason(), message.hasPlacedDie(), message.hasUsedTool());
    }

    @Override
    public void visit(MessagePing message){
        //THIS MESSAGE IS USED TO SERVER FOR VERIFY THE CONNECTION.
    }

    @Override
    public void visit(MessageRoundChanged message) {
        handleRoundChanged(message.getNickname(), message.getNumRound());
    }

}


