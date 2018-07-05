package it.polimi.se2018.events.messageforcontroller;

import it.polimi.se2018.controller.VisitorController;
import it.polimi.se2018.events.Message;

public class MessageCustomResponse extends Message {

    private boolean useCustom;
    private String file;
    private int timer;

    public MessageCustomResponse(String nickname, boolean useCustom, String file, int timer) {
        super(nickname);
        this.useCustom = useCustom;
        this.file = file;
        this.timer = timer;
        this.noTurn = true;
    }

    public boolean isUseCustom() {
        return useCustom;
    }

    public String getFile() {
        return file;
    }

    public int getTimer() {
        return timer;
    }

    @Override
    public void accept(VisitorController v) {
        v.visit(this);
    }
}
