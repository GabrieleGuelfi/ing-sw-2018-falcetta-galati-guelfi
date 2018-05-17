package it.polimi.se2018.utils;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MoveDie;

public interface Observer {

    void update(MoveDie m);

    void update(Message m);

}
