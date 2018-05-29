package it.polimi.se2018.events;

import it.polimi.se2018.model.dicecollection.DraftPool;

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
    public void accept(SagradaVisitor v) {
        v.visit(this);
    }
}
