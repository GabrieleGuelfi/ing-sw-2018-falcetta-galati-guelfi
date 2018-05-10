package it.polimi.se2018.controller.tool;

import it.polimi.se2018.model.*;

public abstract class Tool {

    private String description;

    private boolean used;

    public String getDescription() {
        return description;
    }

    public boolean getUsed() {
        return used;
    }

    public void firstUse() {
        this.used = true;
    }

    public abstract void use(Match match, Player player);

    }

