package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageWPChanged extends Message {

    private String player;
    private WindowPattern wp;

    public MessageWPChanged(String player, WindowPattern wp) {
        super();
        this.wp = wp;
        this.player = player;
    }

    public MessageWPChanged(String player, WindowPattern wp, String wpOwner) {
        super(player);
        this.wp = wp;
        this.player = wpOwner;
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
