package it.polimi.se2018.events;

public class MessageTool extends Message {

    private String description;

    public MessageTool(String nickname, String description) {
        super(nickname);
        this.description = description;
    }

    @Override
    public void accept(SagradaVisitor v){
        v.visit(this);
    }

}
