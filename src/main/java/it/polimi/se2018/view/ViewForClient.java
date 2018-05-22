package it.polimi.se2018.view;

import java.util.Scanner;

import static java.lang.System.*;

public class ViewForClient {

    private Scanner scanner = new Scanner(in);

    public ViewForClient() {
        System.out.println("Welcome in Sagrada!");
    }

    public String getNickname() {
        System.out.println("Please, choose your nickname");
        return scanner.nextLine();
    }

}
