package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.SagradaVisitor;
import it.polimi.se2018.utils.VisitorClient;

import java.util.Scanner;

import static java.lang.System.*;

public class ViewForClient extends Observable implements Observer {

    private Scanner scanner;

    public ViewForClient() {
        scanner = new Scanner(System.in);
        out.println("Welcome in Sagrada!");
    }

    public String getNickname() {
        out.println("Please, choose your nickname");
        return scanner.nextLine();
    }

    public void wrongNickname() {
        out.println("This nickname is already used: try again.");
    }

    public void showPrivateObjective(String colour) {
        out.println("Your private objective is of colour: " + colour);
    }

    public void showPublicObjective() {
        out.println("Public objectives: ");
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
        m.accept(new VisitorClient());
    }

}
