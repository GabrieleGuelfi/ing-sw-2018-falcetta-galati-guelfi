package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.view.VisitorView;

public class MessageRoundChanged extends Message {

    private int numRound;
    private DraftPool draftPool;

    public MessageRoundChanged(String nickname, int numRound, DraftPool draftPool) {
        super(nickname);
        this.numRound = numRound;
        this.draftPool = draftPool;
    }

    public int getNumRound() {
        return numRound;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }

    @Override
    public void accept(VisitorView v) {
        v.visit(this);
    }
}
