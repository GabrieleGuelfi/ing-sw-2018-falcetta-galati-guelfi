package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPattern;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDiffRow {

    @Test
    public void TestCalcScoreColour() {
        WindowPattern w = new WindowPattern("foo", 5);
        PublicObjective diffRow = PublicObjective.factory(1);

        w.putDice(new Die(Colour.BLUE), 0, 0);
        w.putDice(new Die(Colour.YELLOW), 0, 1);
        w.putDice(new Die(Colour.RED), 0, 2);
        w.putDice(new Die(Colour.GREEN), 0, 3);
        w.putDice(new Die(Colour.MAGENTA), 0, 4);

        w.putDice(new Die(Colour.BLUE), 3, 0);
        w.putDice(new Die(Colour.YELLOW), 3, 1);
        w.putDice(new Die(Colour.RED), 3, 2);
        w.putDice(new Die(Colour.GREEN), 3, 3);
        w.putDice(new Die(Colour.MAGENTA), 3, 4);

        w.putDice(new Die(Colour.BLUE), 2, 0);
        w.putDice(new Die(Colour.BLUE), 2, 1);
        w.putDice(new Die(Colour.RED), 2, 2);
        w.putDice(new Die(Colour.RED), 2, 3);
        w.putDice(new Die(Colour.MAGENTA), 2, 4);

        assertEquals(diffRow.getVp()*2, diffRow.calcScore(w));
    }

    @Test
    public void TestCalcScoreValues() {
        WindowPattern w = new WindowPattern("foo", 5);
        PublicObjective diffRow = PublicObjective.factory(3);
        List<Die> dice = new ArrayList<>();
        for (int i=0; i<6; i++)
            dice.add(i, new Die(Colour.WHITE));

        for (int i=0; i<6; i++)
            dice.get(i).setValue((i%6)+1);

        w.putDice(dice.get(0), 0, 0);
        w.putDice(dice.get(1), 0, 1);
        w.putDice(dice.get(2), 0, 2);
        w.putDice(dice.get(3), 0, 3);
        w.putDice(dice.get(4), 0, 4);

        w.putDice(dice.get(5), 1, 0);
        w.putDice(dice.get(5), 1, 1);
        w.putDice(dice.get(1), 1, 2);
        w.putDice(dice.get(2), 1, 3);
        w.putDice(dice.get(3), 1, 4);

        w.putDice(dice.get(5), 3, 0);
        w.putDice(dice.get(4), 3, 1);
        w.putDice(dice.get(1), 3, 2);
        w.putDice(dice.get(2), 3, 3);
        w.putDice(dice.get(3), 3, 4);

        assertEquals(diffRow.getVp()*2, diffRow.calcScore(w));
    }

}
