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
    private boolean canChoose;

    public MessageForceMove(String nickname, Die die, WindowPattern windowPattern, boolean newValue, boolean placedDie, boolean canChoose) {
        super(nickname);
        this.die = die;
        this.windowPattern = windowPattern;
        this.newValue = newValue;
        this.placedDie = placedDie;
        this.canChoose = canChoose;
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

    public boolean isCanChoose() {
        return canChoose;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
