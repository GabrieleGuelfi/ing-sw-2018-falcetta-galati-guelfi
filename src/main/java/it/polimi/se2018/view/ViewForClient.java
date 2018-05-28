package it.polimi.se2018.view;

import it.polimi.se2018.utils.HandleJSON;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageChooseWP;
import it.polimi.se2018.events.MessageNickname;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.*;

public class ViewForClient extends Observable implements Observer {

    private static final int MAX_ROW = 4;
    private static final int MAX_COL = 5;

    private static ViewForClient viewForClient;
    private Scanner scanner;
    private String nickname;
    private WindowPattern windowPattern;

    public static ViewForClient createViewForClient() {
        if (viewForClient==null) {
            viewForClient = new ViewForClient();
            return viewForClient;
        }
        else {
            return viewForClient;
        }
    }

    private ViewForClient() {
        scanner = new Scanner(System.in);
        out.println("Welcome in Sagrada!");
    }

    public void askNickname() {
        out.println("Please, choose your nickname");
        this.nickname = scanner.nextLine();
        notifyObservers(new Message(nickname));
    }

    void nicknameConfirmation(MessageNickname nicknameMessage) {
        if (nicknameMessage.getBoolean()) {
            out.println("All settled! Wait for the game to begin...");
        }
        else {
            out.println("Nickname already used: choose another one");
            askNickname();
        }
    }

    void showPrivateObjective(String colour) {
        out.println("Your private objective is of colour: " + colour);
    }

    void showPublicObjective(List<String> descriptions, List<Integer> points) {
        out.println("Public objectives: ");
        for(int i=0; i<descriptions.size(); i++) {
            out.println(i+1 + ") " + descriptions.get(i) + " VP: " + points.get(i));
        }
    }

    void askWindowPattern(int firstCard, int secondCard) {

        int choice;
        List<WindowPattern> windowPatterns = new ArrayList<>();

        out.println("You have to choose the window pattern between these four: ");
        windowPatterns.add(HandleJSON.createWindowPattern(null, firstCard, 0));
        windowPatterns.add(HandleJSON.createWindowPattern(null, firstCard, 1));
        windowPatterns.add(HandleJSON.createWindowPattern(null, secondCard, 0));
        windowPatterns.add(HandleJSON.createWindowPattern(null, secondCard, 1));
        for (WindowPattern wp: windowPatterns) {
            out.println(windowPatterns.indexOf(wp)+1 + ") " + wp.getName());
            printWindowPattern(wp);
        }
        out.println("Write the number of the window pattern you want to use.");
        choice = scanner.nextInt();
        while(choice<1 || choice>4) {
            out.println("Invalid choice: try again.");
            choice = scanner.nextInt();
        }

        if (choice<3) {
            notifyObservers(new MessageChooseWP(nickname, firstCard, choice-1));
        } else {
            notifyObservers(new MessageChooseWP(nickname, secondCard, (choice-1)%2));
        }
    }

    private void printWindowPattern(WindowPattern wp) {
        for (int i=0; i<MAX_ROW; i++) {
            out.println();
            for (int j=0; j<MAX_COL; j++) {
                if (wp.getBox(i, j).hasAValueRestriction()) {
                    out.print("[" + wp.getBox(i, j).getValueRestriction() + "] ");
                }
                else {
                    out.print("[" + Colour.getFirstLetter(wp.getBox(i, j).getColourRestriction()) + "] ");
                }
            }
        }
        out.println();
        out.println();
    }

    public void askMove(boolean canMoveDie, boolean canUseTool) {
        out.println("Please, select your move: ");
        int i=1;
        if (canMoveDie) {
            out.println(i + ") Move a die");
            i++;
        }
        if (canUseTool) {
            out.println(i + ") Use a tool");
            i++;
        }
        out.println(i + ") Do nothing");
        int choice = scanner.nextInt();
        if (choice==1 && canMoveDie) moveDie();
        if ((choice==1 && !canMoveDie)||(choice==2 && canMoveDie && canUseTool)) useTool();
        if (choice==2 && (!canMoveDie||!canUseTool)); //notifyObservers(new MessageDoNothing());

    }

    private void moveDie() {
        out.println("Please, select the die you want to move from the draftpool");
    }

    private void useTool() {
        out.println("Please, select the tool you want to use");

    }

    @Override
    public void update(Message m) {
        m.accept(new VisitorView());
    }

}
