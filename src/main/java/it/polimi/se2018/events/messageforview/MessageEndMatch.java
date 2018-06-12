package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

import java.util.HashMap;
import java.util.Map;

public class MessageEndMatch extends Message {

    private Map<String , Integer> results = new HashMap<>();
    private boolean lastPlayer;

    public MessageEndMatch(Map<String , Integer> results, boolean lastPlayer) {
        super();
        this.results = results;
        this.lastPlayer = lastPlayer;
    }

    public Map<String, Integer> getResults() {
        return results;
    }

    public boolean isLastPlayer() {
        return lastPlayer;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
