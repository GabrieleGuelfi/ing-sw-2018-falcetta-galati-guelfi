package it.polimi.se2018.controller;

import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.Match;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.publicobjective.PublicObjective;
import it.polimi.se2018.utils.StringJSON;
import it.polimi.se2018.view.VirtualView;

import java.util.ArrayList;
import java.util.List;

public enum RequestType {

    TOOL {
        @Override
        public String toString() {
            return StringJSON.printStrings("request", "tool");
        }

        @Override
        public void performRequest(Player player, VirtualView virtualView, Match match) {
            List<String> names = new ArrayList<>();
            for (Tool t : match.getTools())
                names.add(t.getName());
            virtualView.send(new MessageTool(player.getNickname(), names));
        }
    },
    PRIVATE{
        @Override
        public String toString() {
            return StringJSON.printStrings("request", "private");
        }

        @Override
        public void performRequest(Player player, VirtualView virtualView, Match match) {
            virtualView.send(new MessagePrivObj(player.getNickname(), player.getPrivateObjective().getDescription()));
        }
    },
    PUBLIC {
        @Override
        public String toString() {
            return StringJSON.printStrings("request", "public");
        }

        @Override
        public void performRequest(Player player, VirtualView virtualView, Match match) {
            List<String> publicObjDescriptions = new ArrayList<>();
            List<Integer> publicObjPoints = new ArrayList<>();
            for(PublicObjective p: match.getPublicObjectives()) {
                publicObjDescriptions.add(p.getDescription());
                publicObjPoints.add(p.getVp());
            }
            if(!publicObjDescriptions.isEmpty())
                virtualView.send(new MessagePublicObj(player.getNickname(), publicObjDescriptions, publicObjPoints));
        }
    },
    ALLWP {
        @Override
        public String toString() {
            return StringJSON.printStrings("request", "allWP");
        }

        @Override
        public void performRequest(Player player, VirtualView virtualView, Match match) {
            for (Player p: match.getActivePlayers()) {
                if (!p.getNickname().equals(player.getNickname())) {
                    virtualView.send(new MessageWPChanged(player.getNickname(), p.getWindowPattern(), p.getNickname()));
                }
            }
        }
    },
    MYWP {
        @Override
        public String toString() {
            return StringJSON.printStrings("request", "myWP");
        }

        @Override
        public void performRequest(Player player, VirtualView virtualView, Match match) {
            virtualView.send(new MessageWPChanged(player.getNickname(), player.getWindowPattern(), player.getNickname()));
        }
    },
    ROUNDTRACK {
        @Override
        public String toString() {
            return StringJSON.printStrings("request", "roundTrack");
        }

        @Override
        public void performRequest(Player player, VirtualView virtualView, Match match) {
            virtualView.send(new MessageRoundTrack(player.getNickname(), match.getRoundTrack()));
        }
    };

    public abstract void performRequest(Player player, VirtualView view, Match match);
}
