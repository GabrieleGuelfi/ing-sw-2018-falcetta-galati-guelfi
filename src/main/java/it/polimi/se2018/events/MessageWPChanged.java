package it.polimi.se2018.events;

import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.utils.SagradaVisitor;
import it.polimi.se2018.view.VisitorView;

public class MessageWPChanged extends Message {

    private String player;
    private WindowPattern wp;

    public MessageWPChanged(String player, WindowPattern wp) {
        super();
        this.wp = wp;
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public WindowPattern getWp() {
        return wp;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }
}
