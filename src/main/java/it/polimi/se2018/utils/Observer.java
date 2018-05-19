package it.polimi.se2018.utils;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.MessageDie;

public interface Observer {

    //void update(Message m);

   // void update(MessageDie m);

    void update(Message message);
}
