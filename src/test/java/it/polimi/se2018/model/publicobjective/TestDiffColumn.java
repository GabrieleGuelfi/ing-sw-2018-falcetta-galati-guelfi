package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPattern;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestDiffColumn {

    @Test
    public void TestCalcScoreColour() {
        WindowPattern w = new WindowPattern("foo", 5);
        PublicObjective diffRow = PublicObjective.factory(2);

        w.putDice(new Die(Colour.BLUE), 0, 1);
        w.putDice(new Die(Colour.YELLOW), 1, 1);
        w.putDice(new Die(Colour.RED), 2, 1);
        w.putDice(new Die(Colour.GREEN), 3, 1);

        w.putDice(new Die(Colour.BLUE), 0, 3);
        w.putDice(new Die(Colour.BLUE), 1, 3);
        w.putDice(new Die(Colour.RED), 2, 3);
        w.putDice(new Die(Colour.GREEN), 3, 3);

        w.putDice(new Die(Colour.YELLOW), 0, 4);
        w.putDice(new Die(Colour.BLUE), 1, 4);
        w.putDice(new Die(Colour.GREEN), 2, 4);
        w.putDice(new Die(Colour.RED), 3, 4);

        assertEquals(diffRow.getVp()*2, diffRow.calcScore(w));
    }

    @Test
    public void TestCalcScoreValues() {
        WindowPattern w = new WindowPattern("foo", 5);
        PublicObjective diffRow = PublicObjective.factory(4);
        List<Die> dice = new ArrayList<>();
        for (int i=0; i<6; i++)
            dice.add(i, new Die(Colour.WHITE));

        for (int i=0; i<6; i++)
            dice.get(i).setValue((i%6)+1);

        w.putDice(dice.get(0), 0, 0);
        w.putDice(dice.get(1), 1, 0);
        w.putDice(dice.get(2), 2, 0);
        w.putDice(dice.get(3), 3, 0);

        w.putDice(dice.get(5), 0, 2);
        w.putDice(dice.get(5), 1, 2);
        w.putDice(dice.get(1), 2, 2);
        w.putDice(dice.get(2), 3, 2);

        w.putDice(dice.get(5), 0, 4);
        w.putDice(dice.get(4), 1, 4);
        w.putDice(dice.get(1), 2, 4);
        w.putDice(dice.get(2), 3, 4);

        assertEquals(diffRow.getVp()*2, diffRow.calcScore(w));
    }
}
