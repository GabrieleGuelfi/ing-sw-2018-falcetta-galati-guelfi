package it.polimi.se2018.controller;

import it.polimi.se2018.events.messageforcontroller.*;
import it.polimi.se2018.events.messageforcontroller.MessageForcedMove;

public interface VisitorController {

    /**
     * set a Window Pattern for the players
     * @param message contains parameters to set the Window Pattern
     */
    void visit(MessageSetWP message);

    /**
     * place a die after verifying all placement restrictions
     * @param message contains positions of the die in the draftpool and in the window pattern
     */
    void visit(MessageMoveDie message);

    /**
     * skip the turn for the player
     * @param message contains nickname of the player that want to skip
     */
    void visit(MessageDoNothing message);

    /**
     * answer to request of info of a player
     * @param message contains the type of request
     */
    void visit(MessageRequest message);

    /**
     * end a game if only one player remain in the match
     * @param message contains the nickname of the winner
     */
    void visit(MessageClientDisconnected message);

    /**
     * manage the usage of a tool
     * @param message contains all parameter necessary to handle the usage of the tool
     */
    void visit(MessageToolResponse message);

    /**
     * verify if the player can use a tool
     * @param message contains the number of tool player want to use
     */
    void visit(MessageRequestUseOfTool message);

    /**
     * manage the second move of a tool
     * @param message contains the choice of the player
     */
    void visit(MessageForcedMove message);

    /**
     * add custom Window Pattern if the first player want it
     * @param message contains the new WindowPattern, or null
     */
    void visit(MessageCustomResponse message);
}
