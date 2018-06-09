package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.view.VisitorView;

public class MessageAskMove extends Message {

    private boolean hasUsedTool;
    private boolean hasMovedDie;
    private WindowPattern windowPattern;
    private DraftPool draftPool;

    public MessageAskMove(String nickname, boolean hasUsedTool, boolean hasMovedDie, WindowPattern windowPattern, DraftPool draftPool) {
        super(nickname);
        this.hasUsedTool = hasUsedTool;
        this.hasMovedDie = hasMovedDie;
        this.windowPattern = windowPattern;
        this.draftPool = draftPool;
    }

    public MessageAskMove(String nickname, boolean hasUsedTool, boolean hasMovedDie) {
        super(nickname);
        this.hasUsedTool = hasUsedTool;
        this.hasMovedDie = hasMovedDie;
        windowPattern = null;
        draftPool = null;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }

    public WindowPattern getWindowPattern() {
        return windowPattern;
    }

    public boolean isHasUsedTool() {
        return hasUsedTool;
    }

    public boolean isHasMovedDie() {
        return hasMovedDie;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
