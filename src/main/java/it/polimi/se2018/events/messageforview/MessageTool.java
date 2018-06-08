package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

import java.util.List;

public class MessageTool extends Message {

    private List<String> names;

    public MessageTool(String nickname, List<String> names) {
        super(nickname);
        this.names = names;
    }

    public MessageTool(List<String> names) {
        this.names = names;
    }

    public List<String> getNames() {
        return names;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }

}
