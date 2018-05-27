package it.polimi.se2018.view;

import it.polimi.se2018.controller.handleJSON;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageChooseWP;
import it.polimi.se2018.events.MessageNickname;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.*;

public class ViewForClient extends Observable implements Observer {

    private static final int MAX_ROW = 4;
    private static final int MAX_COL = 5;

    private static ViewForClient viewForClient;
    private Scanner scanner;
    private String nickname;

    public static ViewForClient createViewForClient() {
        if (viewForClient==null) {
            viewForClient = new ViewForClient();
            return viewForClient;
        }
        else {
            return viewForClient;
        }
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private ViewForClient() {
        scanner = new Scanner(System.in);
        out.println("Welcome in Sagrada!");
    }

    public String getNickname() {
        out.println("Please, choose your nickname");
        return scanner.nextLine();
    }

    public void nicknameConfirmation(MessageNickname nicknameMessage) {
        if (nicknameMessage.getBoolean()) {
            out.println("All settled! Wait for the game to begin...");

        }
        else {
            out.println("Nickname already used: please, choose another one");
            String nickname = getNickname();
            setNickname(nickname);
            notifyObservers(new Message(nickname));
        }
    }

    public void showPrivateObjective(String colour) {
        out.println("Your private objective is of colour: " + colour);
    }

    public void showPublicObjective(List<String> descriptions, List<Integer> points) {
        out.println("Public objectives: ");
        for(int i=0; i<descriptions.size(); i++) {
            out.println(i+1 + ") " + descriptions.get(i) + " VP: " + points.get(i));
        }
    }

    public void askWindowPattern(int firstCard, int secondCard) {

        int choice;

        out.println("You have to choose the window pattern between these four: ");
        WindowPattern wp1 = handleJSON.createWindowPattern(firstCard, 0);
        WindowPattern wp2 = handleJSON.createWindowPattern(firstCard, 1);
        WindowPattern wp3 = handleJSON.createWindowPattern(secondCard, 0);
        WindowPattern wp4 = handleJSON.createWindowPattern(secondCard, 1);
        out.println("1) " + wp1.getName());
        printWindowPattern(wp1);
        out.println("2) " + wp2.getName());
        printWindowPattern(wp2);
        out.println("3) " + wp3.getName());
        printWindowPattern(wp3);
        out.println("4) " + wp4.getName());
        printWindowPattern(wp4);

        out.println("Write the number of the window pattern you want to use.");
        choice = scanner.nextInt();
        while(choice<1 || choice>4) {
            out.println("Invalid choice: try again.");
            choice = scanner.nextInt();
        }

        switch (choice) {
            case 1: notifyObservers(new MessageChooseWP(nickname, firstCard, 0)); break;
            case 2: notifyObservers(new MessageChooseWP(nickname, firstCard, 1)); break;
            case 3: notifyObservers(new MessageChooseWP(nickname, secondCard, 0)); break;
            case 4: notifyObservers(new MessageChooseWP(nickname, secondCard, 1)); break;
        }
    }

    public void printWindowPattern(WindowPattern wp) {
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
