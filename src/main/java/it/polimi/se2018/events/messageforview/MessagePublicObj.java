package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

import java.util.List;

public class MessagePublicObj extends Message {

    private List<String> descriptions;
    private List<Integer> points;

    public MessagePublicObj(List<String> descriptions, List<Integer> points) {
        this.descriptions = descriptions;
        this.points = points;
    }

    public MessagePublicObj (String nickname, List<String> descriptions, List<Integer> points) {
        super(nickname);
        this.descriptions = descriptions;
        this.points = points;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public List<Integer> getPoints() {
        return points;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }
}
