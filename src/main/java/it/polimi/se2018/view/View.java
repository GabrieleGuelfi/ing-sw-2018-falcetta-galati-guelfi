package it.polimi.se2018.view;

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
import java.util.stream.Collectors;

import org.fusesource.jansi.AnsiConsole;

import static java.lang.System.*;
import static java.util.stream.Collectors.toMap;
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

        List<Integer> results = new ArrayList<>();
        results.add(2);
        results.add(5);
        results.add(6);
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Ale");
        nicknames.add("Gabbo");
        nicknames.add("FedeGal");

        handleEndMatch(nicknames, results);

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
                if (d.getColour().equals(Colour.PURPLE))
                    out.print(ansi().fg(MAGENTA).a("[" + d.getValue() + "] ").reset());
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
            this.currentWindowPattern = wp;
        }
    }

    private void manageTurn(String playerTurn) {
        if(playerTurn.equals(this.nickname)) {
            out.println( ansi().fg(RED).a("\nIt's your turn!\n").reset() );
            askMove(false, false);
        } else {
            out.println("\n" + playerTurn + " is now playing...\n");
        }
    }

    private void handleErrorMove(String reason, boolean hasMovedDie, boolean hasUsedTool) {
        out.println("\nYour move has been rejected for this reason: " + reason);
        out.println("Try again...");
        askMove(hasMovedDie, hasUsedTool);
    }

    private void handleSuccessMove(boolean hasMovedDie, boolean hasUsedTool, boolean movePerformed) {
        if (movePerformed)
            out.println("\nSuccess! Your move has been accomplished.");
        if (hasMovedDie&&hasUsedTool) {
            out.println("\nYour turn is over.");
            return;
        }
        askMove(hasMovedDie, hasUsedTool);
    }

    private void handleRoundChanged(String nickname, int round) {
        out.println();
        out.println("_|-|_|-|_|-|_|-|_|-|_|-|_|-|_|-|  ROUND " + round + " |-|_|-|_|-|_|-|_|-|_|-|_|-|_|-|_|\n");
        out.println("Draftpool for this round: ");
        printDraftPool(currentDraftPool);
        manageTurn(nickname);
    }

    private void handleEndMatch(List<String> nicknames, List<Integer> points) {

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

        out.println( ansi().eraseScreen() );
        out.println( ansi().fg(RED).a("End of the match!").reset());
        out.println("The winner is... " + nicknames.get(0) + "!\n");
        out.println("Full classification: ");


        i = 1;

        for (String playerNickname: resultsSorted.keySet()) {
            out.println( i +") " + playerNickname + " with " + resultsSorted.get(playerNickname) + " points");
            i++;
        }


    }

    private void askMove(boolean hasMovedDie, boolean hasUsedTool) {

        boolean moveDieOk = true;
        boolean moveToolOk = true;

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
            askMove(hasMovedDie, hasUsedTool);
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
        out.println("\nCurrently, this is your windows pattern:");
        printWindowPattern(currentWindowPattern);
        out.println("\nCurrently, this is the draftpool:");
        printDraftPool(currentDraftPool);

        out.println("\nChoose a die from draftpool (0 to escape):");
        int dieToMove = chooseBetween(0, currentDraftPool.size());
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
        return true;
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
        if (box.hasAValueRestriction()) {
            if(box.getValueRestriction()!=0) out.print("[" + box.getValueRestriction() + "] ");
            else out.print("[ ] ");
        } else {
            Colour boxColour = box.getColourRestriction();
            for (Color color : Color.values()) {
                if (color.toString().equals(boxColour.toString())) {
                    out.print(ansi().fg(color).a("[ ] ").reset());
                }
            }
            if(boxColour.equals(Colour.PURPLE)) out.print(ansi().fg(MAGENTA).a("[ ] ").reset());
        }
    }

    private void printBoxWithDie(Box box) {
        Colour dieColour = box.getDie().getColour();
        for (Color color : Color.values()) {
            if (color.toString().equals(dieColour.toString())) {
                out.print(ansi().fg(color).a("[" + box.getDie().getValue() + "] ").reset());
            }
        }

        if(dieColour.equals(Colour.PURPLE)) out.print(ansi().fg(MAGENTA).a("[" + box.getDie().getValue() + "] ").reset());
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
            if (d.getColour().equals(Colour.PURPLE))
                out.print(ansi().fg(MAGENTA).a("[" + d.getValue() + "] ").reset());
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
        currentDraftPool = message.getDraftPool();
    }

    @Override
    public void visit(MessageConfirmMove message) {
        handleSuccessMove(message.hasPlacedDie(), message.hasUsedTool(), message.isMovePerformed());
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

    @Override
    public void visit(MessageRoundTrack message) {
        showRoundTrack(message.getRoundTrack());
    }

    @Override
    public void visit(MessageEndMatch message) {
        handleEndMatch(message.getNicknames(), message.getPoints());
    }

}


