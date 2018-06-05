package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPattern;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDiagColour {

    @Test
    public void TestCalcScoreColour() {
        WindowPattern w = new WindowPattern("foo", 5);
        PublicObjective diagColour = PublicObjective.factory(8);

        w.putDice(new Die(Colour.YELLOW), 0, 0);
        w.putDice(new Die(Colour.MAGENTA), 0, 1);
        w.putDice(new Die(Colour.YELLOW), 0, 2);
        w.putDice(new Die(Colour.RED), 0, 3);

        w.putDice(new Die(Colour.YELLOW), 1, 1);
        w.putDice(new Die(Colour.RED), 1, 2);
        w.putDice(new Die(Colour.BLUE), 1, 3);
        w.putDice(new Die(Colour.RED), 1, 4);

        w.putDice(new Die(Colour.YELLOW), 2, 0);
        w.putDice(new Die(Colour.BLUE), 2, 1);
        w.putDice(new Die(Colour.YELLOW), 2, 2);
        w.putDice(new Die(Colour.RED), 2, 3);
        w.putDice(new Die(Colour.YELLOW), 2, 4);

        w.putDice(new Die(Colour.BLUE), 3, 0);
        w.putDice(new Die(Colour.RED), 3, 1);
        w.putDice(new Die(Colour.RED), 3, 2);
        w.putDice(new Die(Colour.YELLOW), 3, 3);

        assertEquals(14, diagColour.calcScore(w));
    }

}
