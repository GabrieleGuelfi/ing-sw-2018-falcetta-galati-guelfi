package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

public class MessageDPChanged extends Message {

    private DraftPool draftPool;

    public MessageDPChanged(DraftPool draftPool) {
        super();
        this.draftPool = draftPool;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }
}
