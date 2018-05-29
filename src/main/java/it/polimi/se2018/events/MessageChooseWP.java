package it.polimi.se2018.events;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.utils.SagradaVisitor;
import it.polimi.se2018.view.VisitorView;

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

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }

    public void accept(VisitorController v) {
        v.visit(this);
    }

}
