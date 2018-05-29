package it.polimi.se2018.events;

import it.polimi.se2018.utils.SagradaVisitor;

public class MessageChooseWP extends Message {

    private int firstIndex;
    private int secondIndex;

    public MessageChooseWP(String nickname, int firstIndex, int secondIndex) {
        super(nickname);
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public int getSecondIndex() {
        return secondIndex;
    }

    public void accept(SagradaVisitor v){
        v.visit(this);
    }

}
