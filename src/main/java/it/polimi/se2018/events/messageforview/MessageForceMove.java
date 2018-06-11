package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.view.VisitorView;

public class MessageForceMove extends Message {

    private Die die;
    private WindowPattern windowPattern;

    public MessageForceMove(String nickname, Die die, WindowPattern windowPattern) {
        super(nickname);
        this.die = die;
        this.windowPattern = windowPattern;
    }

    public WindowPattern getWindowPattern() {
        return windowPattern;
    }

    public Die getDie() {
        return die;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
