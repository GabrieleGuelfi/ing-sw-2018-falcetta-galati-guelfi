package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.view.VisitorView;

public class MessageForceMove extends Message {

    private Die die;
    private WindowPattern windowPattern;
    private boolean newValue;
    private boolean placedDie;

    public MessageForceMove(String nickname, Die die, WindowPattern windowPattern, boolean newValue, boolean placedDie) {
        super(nickname);
        this.die = die;
        this.windowPattern = windowPattern;
        this.newValue = newValue;
        this.placedDie = placedDie;
    }

    public WindowPattern getWindowPattern() {
        return windowPattern;
    }

    public Die getDie() {
        return die;
    }

    public boolean isNewValue() {
        return newValue;
    }

    public boolean isPlacedDie() {
        return placedDie;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
