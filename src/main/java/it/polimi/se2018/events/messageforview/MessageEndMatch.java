package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

import java.util.HashMap;
import java.util.Map;

public class MessageEndMatch extends Message {

    private Map<String , Integer> results = new HashMap<>();

    public MessageEndMatch(Map<String , Integer> results) {
        super();
        this.results = results;
    }

    public Map<String, Integer> getResults() {
        return results;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
