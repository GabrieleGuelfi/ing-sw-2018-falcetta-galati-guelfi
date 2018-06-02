package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.view.VisitorView;

import java.util.List;
import java.util.Map;

public class MessageRoundTrack extends Message{

    private Map<Integer, List<Die>> roundTrack;

    public MessageRoundTrack (String nickname, Map<Integer, List<Die>> roundTrack) {
        super(nickname);
        this.roundTrack = roundTrack;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }

    public Map<Integer, List<Die>> getRoundTrack() {
        return roundTrack;
    }
}
