package it.polimi.se2018.utils;


import it.polimi.se2018.model.Match;
import it.polimi.se2018.events.*;

public interface Observer {

    void update(MoveDie m);

    void update(Message m);

    void update(MessageDie m);
}
