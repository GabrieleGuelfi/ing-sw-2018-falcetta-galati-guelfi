package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.utils.Observer;

public interface ViewInterface {

    int askConnection();
    String askNickname();
    void addObserver(Observer observer);
    void notifyObserver(Message message);
}
