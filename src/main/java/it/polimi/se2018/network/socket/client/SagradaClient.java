package it.polimi.se2018.network.socket.client;

import it.polimi.se2018.events.MessageDie;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.network.socket.server.ServerInterface;

import java.util.Scanner;

public class SagradaClient {

    private static final int PORT = 1111;
    private static final String HOST = "localhost";

    public static void main(String[] args) {

        System.out.println("Welcome in Sagrada");

        ServerInterface server = new NetworkHandler(HOST, PORT, new ClientImplementation());

        Scanner scanner = new Scanner(System.in);

        boolean gameRunning = true;

        while(gameRunning) {

            System.out.println("Chose a number");
            int n = scanner.nextInt();

            Die newDie = new Die(Colour.YELLOW);
            newDie.setValue(n);

            MessageDie message = new MessageDie(newDie);
            server.send(message);
        }
    }
}
