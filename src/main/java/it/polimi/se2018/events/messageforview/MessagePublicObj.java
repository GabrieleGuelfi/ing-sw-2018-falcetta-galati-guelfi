package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

import java.util.List;

public class MessagePublicObj extends Message {

    private List<String> descriptions;
    private List<Integer> points;
    private List<Integer> id;

    public MessagePublicObj(List<String> descriptions, List<Integer> points, List<Integer> id) {
        this.descriptions = descriptions;
        this.points = points;
        this.id = id;
    }

    public MessagePublicObj (String nickname, List<String> descriptions, List<Integer> points, List<Integer> id) {
        super(nickname);
        this.descriptions = descriptions;
        this.points = points;
        this.id = id;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public List<Integer> getId() {
        return id;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }
}
