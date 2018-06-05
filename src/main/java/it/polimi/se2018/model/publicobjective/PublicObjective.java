package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.model.WindowPattern;

import java.util.*;

/**
 * abstract class for public objectives with short description and score, here is calculated a score for a window pattern
 * @author Gabriele Guelfi
 */
public abstract class PublicObjective {

    private String description;
    int vp;
    int points;

    /**
     * Class Constructor
     * @param description the short description of the objective
     * @param vp how much points give the objective
     */
    public PublicObjective(String description, int vp) {
        this.description = description;
        this.vp = vp;
    }

    private static final List<Command> PUBOBJ;

    static {
        final List<Command> pubObj = new ArrayList<>();
        pubObj.add(new Command() {
            @Override
            public PublicObjective create() {
                return new DiffRow("Rows with no repeated colors", 6, true);
            }
        });
        pubObj.add(new Command() {
            @Override
            public PublicObjective create() {
                return new DiffColumn("Column with no repeated colours", 5, true);
            }
        });
        pubObj.add(new Command() {
            @Override
            public PublicObjective create() {
                return new DiffRow("Rows with no repeated values", 5, false);
            }
        });
        pubObj.add(new Command() {
            @Override
            public PublicObjective create() {
                return new DiffColumn("Column with no repeated values", 4, false);
            }
        });
        pubObj.add(new Command() {
            @Override
            public PublicObjective create() {
                return new SetShades("Sets of 1 & 2 values anywhere", 2, 1);
            }
        });
        pubObj.add(new Command() {
            @Override
            public PublicObjective create() {
                return new SetShades("Sets of 3 & 4 values anywhere", 2, 3);
            }
        });
        pubObj.add(new Command() {
            @Override
            public PublicObjective create() {
                return new SetShades("Sets of 5 & 6 values anywhere", 2, 5);
            }
        });
        pubObj.add(new Command() {
            @Override
            public PublicObjective create() {
                return new DiffEverywhere("Sets of one of each value anywhere", 5, false);
            }
        });
        pubObj.add(new Command() {
            @Override
            public PublicObjective create() {
                return new DiagColour("Count of diagonally adjacent same color dice");
            }
        });
        pubObj.add(new Command() {
            @Override
            public PublicObjective create() {
                return new DiffEverywhere("Sets of one of each color anywhere", 4, true);
            }
        });
        PUBOBJ = Collections.unmodifiableList(pubObj);
    }

    /**
     * Create the right type of Public Objective
     * @param n identify the ten type of Objective
     * @return a new PublicObjective of the right type
     */
    public static PublicObjective factory(int n) {

        Command command = PUBOBJ.get(n);
        if (command == null) {
            throw new IllegalArgumentException("Invalid input type: "
                    + n);
        }
        return command.create();
    }

    /**
     * @return the short description of the objective
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the points given by the objective
     */
    public int getVp() {
        return vp;
    }

    /**
     * calculate the score of the windowPattern
     * @param windowPattern the scheme on which the score is calculated
     * @return score of the windowPattern
     */
    public abstract int calcScore(WindowPattern windowPattern);

}
