package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.utils.Observer;
import javafx.beans.Observable;

public interface ViewInterface extends Observer {

    int askConnection();
    String askNickname();
    String getHost();
    void addObserver(Observer observer);
    void notifyObserver(Message message);
}
