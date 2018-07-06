package it.polimi.se2018.events.messageforview;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.view.VisitorView;

import java.util.List;

public class MessageTool extends Message {

    private List<String> names;
    private List<String> descriptions;
    private List<Boolean> used;

    public MessageTool(String nickname, List<String> names, List<String> descriptions, List<Boolean> used) {
        super(nickname);
        this.names = names;
        this.descriptions = descriptions;
        this.used = used;
    }

    public MessageTool(List<String> names, List<String> descriptions, List<Boolean> used) {
        this.names = names;
        this.descriptions = descriptions;
        this.used = used;
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public List<Boolean> getUsed() {
        return used;
    }

    @Override
    public void accept(VisitorView v){
        v.visit(this);
    }

}
