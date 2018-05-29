package it.polimi.se2018.events;

import java.util.List;

public class MessagePublicObj extends Message {

    private List<String> descriptions;
    private List<Integer> points;

    public MessagePublicObj(List<String> descriptions, List<Integer> points) {
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
    public void accept(SagradaVisitor v){
        v.visit(this);
    }
}
