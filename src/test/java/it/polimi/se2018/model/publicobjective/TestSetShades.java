package it.polimi.se2018.model.publicobjective;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPattern;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestSetShades {

    @Test
    public void TestCalcScoreOne() {
        WindowPattern w = new WindowPattern("foo", 5);
        PublicObjective setShades = PublicObjective.factory(5);
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

        w.putDice(dice.get(0), 2, 3);
        w.putDice(dice.get(1), 2, 4);

        w.putDice(dice.get(5), 3, 0);
        w.putDice(dice.get(4), 3, 1);
        w.putDice(dice.get(1), 3, 2);
        w.putDice(dice.get(2), 3, 3);
        w.putDice(dice.get(3), 3, 4);

        assertEquals(setShades.getVp()*2, setShades.calcScore(w));
    }

    @Test
    public void TestCalcScoreThree() {
        WindowPattern w = new WindowPattern("foo", 5);
        PublicObjective setShades = PublicObjective.factory(6);
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

        w.putDice(dice.get(0), 2, 3);
        w.putDice(dice.get(1), 2, 4);

        w.putDice(dice.get(5), 3, 0);
        w.putDice(dice.get(4), 3, 1);
        w.putDice(dice.get(1), 3, 2);
        w.putDice(dice.get(2), 3, 3);
        w.putDice(dice.get(3), 3, 4);

        assertEquals(setShades.getVp()*3, setShades.calcScore(w));
    }

    @Test
    public void TestCalcScoreFive() {
        WindowPattern w = new WindowPattern("foo", 5);
        PublicObjective setShades = PublicObjective.factory(7);
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

        w.putDice(dice.get(0), 2, 3);
        w.putDice(dice.get(1), 2, 4);

        w.putDice(dice.get(5), 3, 0);
        w.putDice(dice.get(4), 3, 1);
        w.putDice(dice.get(1), 3, 2);
        w.putDice(dice.get(2), 3, 3);
        w.putDice(dice.get(3), 3, 4);

        assertEquals(setShades.getVp()*2, setShades.calcScore(w));
    }

}
