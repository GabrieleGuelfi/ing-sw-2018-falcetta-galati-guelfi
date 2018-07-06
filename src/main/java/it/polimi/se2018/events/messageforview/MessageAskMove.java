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
    private int favorTokens;

    public MessageAskMove(String nickname, boolean hasUsedTool, boolean hasMovedDie, WindowPattern windowPattern, DraftPool draftPool, int favorTokens) {
        super(nickname);
        this.hasUsedTool = hasUsedTool;
        this.hasMovedDie = hasMovedDie;
        this.windowPattern = windowPattern;
        this.draftPool = draftPool;
        this.favorTokens = favorTokens;
    }

    public MessageAskMove(String nickname, boolean hasUsedTool, boolean hasMovedDie, int favorTokens) {
        super(nickname);
        this.hasUsedTool = hasUsedTool;
        this.hasMovedDie = hasMovedDie;
        windowPattern = null;
        draftPool = null;
        this.favorTokens = favorTokens;
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

    public int getFavorTokens() {
        return favorTokens;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
