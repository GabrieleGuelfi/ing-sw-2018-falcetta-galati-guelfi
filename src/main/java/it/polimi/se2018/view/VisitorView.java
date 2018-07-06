package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforserver.MessagePing;
import it.polimi.se2018.events.messageforview.*;

public interface VisitorView {

    /**
     * First method to inspect message
     * @param message message to inspect
     */
    void visit(Message message);

    /**
     * Sent when a generic error has been reported
     * @param message message to inspect
     */
    void visit(MessageError message);

    /**
     * Used to verify is nickname has been accepted or it is required to choose another one
     * @param message message to inspect
     */
    void visit(MessageNickname message);

    /**
     * Updates and shows private objective to user
     * @param message message to inspect
     */
    void visit(MessagePrivObj message);

    /**
     * Updates and shows private objective to user
     * @param message message to inspect
     */
    void visit(MessagePublicObj message);

    /**
     * Updates and show tool to user
     * @param message message to inspect
     */
    void visit(MessageTool message);

    /**
     * Message used to make the user choice a window pattern
     * It contains two int: the first is the Card index, the second one is for the face of the card
     * @param message message to inspect
     */
    void visit(MessageChooseWP message);

    /**
     * Updates and show a window pattern of a player, to the user
     * @param message message to inspect
     */
    void visit(MessageWPChanged message);

    /**
     * It informs user that turn has changed
     * @param message message to inspect
     */
    void visit(MessageTurnChanged message);

    /**
     * Updates DraftPool
     * @param message message to inspect
     */
    void visit(MessageDPChanged message);

    /**
     * It informs the user that move has been made; eventually, it will tell to user that its turn is finished
     * @param message message to inspect
     */
    void visit(MessageConfirmMove message);

    /**
     * Move rejected + reason
     * @param message message to inspect
     */
    void visit(MessageErrorMove message);

    /**
     * Used to check if a client is still online, used only in RMI
     * @param message message to inspect
     */
    void visit(MessagePing message);

    /**
     * Inform user that round is changed
     * @param message message to inspect
     */
    void visit(MessageRoundChanged message);

    /**
     * Updates roundtrack
     * @param message message to inspect
     */
    void visit(MessageRoundTrack message);

    /**
     * Used to inform user that match is ended with the results
     * @param message message to inspect
     */
    void visit(MessageEndMatch message);

    /**
     * Used to ask user all the data required to perform a tool use
     * @param message message to inspect
     */
    void visit(MessageToolOrder message);

    /**
     * Used to ask to a user to make a move, with boolean to clarify the possible choices
     * @param message message to inspect
     */
    void visit(MessageAskMove message);

    /**
     * Used after a tool has been used to force user to finish the tool move
     * @param message message to inspect
     */
    void visit(MessageForceMove message);

    /**
     * Start of the game: ask the user if he wants to use FA
     * @param message message to inspect
     */
    void visit(MessageCustomWP message);
}
