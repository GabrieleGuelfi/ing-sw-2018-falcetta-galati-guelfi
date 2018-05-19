package it.polimi.se2018.events;

import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.utils.SagradaVisitor;

public class MessageUpdateDraftPool extends MessageUpdate {

    private DraftPool draftPool;

    public MessageUpdateDraftPool(DraftPool d){
        super();
        this.draftPool = d;
    }

    public DraftPool getDraftPool() {
        return draftPool;
    }

    public void accept(SagradaVisitor visitor){
        visitor.visit(this);
    }


}
