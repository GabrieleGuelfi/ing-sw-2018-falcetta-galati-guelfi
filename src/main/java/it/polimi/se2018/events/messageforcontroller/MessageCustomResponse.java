package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

public class MessageCustomResponse extends Message {

    private boolean useCustom;
    private String file;

    public MessageCustomResponse(String nickname, boolean useCustom, String file) {
        super(nickname);
        this.useCustom = useCustom;
        this.file = file;
        this.noTurn = true;
    }

    public boolean isUseCustom() {
        return useCustom;
    }

    public String getFile() {
        return file;
    }

    @Override
    public void accept(VisitorController v) {
        v.visit(this);
    }
}
