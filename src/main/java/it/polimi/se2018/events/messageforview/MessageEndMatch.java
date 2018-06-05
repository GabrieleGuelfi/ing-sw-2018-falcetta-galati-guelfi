package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

import java.util.List;

public class MessageEndMatch extends Message {

    private List<Integer> points;
    private List<String> nicknames;

    public MessageEndMatch(List<Integer> points, List<String> nicknames) {
        super();
        this.points = points;
        this.nicknames = nicknames;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
