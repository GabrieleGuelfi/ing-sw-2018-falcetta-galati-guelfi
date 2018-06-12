package it.polimi.se2018.model;

import it.polimi.se2018.model.publicobjective.PublicObjective;
import it.polimi.se2018.utils.HandleJSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestPrivateObjective {

    @Test
    public void testConstructorPositive() {
        for (Colour c: Colour.values()) {
            PrivateObjective p = HandleJSON.createPrivateObjective(c);
            assertEquals(c, p.getShade());
            assertEquals("Shades of " + c + ": Private\nSum of values on "+c+" dice\n", p.getDescription());
        }
    }

    @Test
    public void testCalcScore() {

        WindowPattern w = new WindowPattern("foo", 5);
        List<PrivateObjective> privates = new ArrayList<>();

        Die d = new Die(Colour.YELLOW);
        d.setValue(6);
        w.putDice(d, 0, 0);
        d = new Die(Colour.RED);
        d.setValue(4);
        w.putDice(d, 0, 1);
        d = new Die(Colour.GREEN);
        d.setValue(2);
        w.putDice(d, 0, 2);
        d = new Die(Colour.MAGENTA);
        d.setValue(4);
        w.putDice(d, 0, 3);

        d = new Die(Colour.BLUE);
        d.setValue(6);
        w.putDice(d, 1, 1);
        d = new Die(Colour.YELLOW);
        d.setValue(3);
        w.putDice(d, 1, 2);
        d = new Die(Colour.MAGENTA);
        d.setValue(1);
        w.putDice(d, 1, 3);
        d = new Die(Colour.GREEN);
        d.setValue(2);
        w.putDice(d, 1, 4);

        d = new Die(Colour.BLUE);
        d.setValue(1);
        w.putDice(d, 2, 0);
        d = new Die(Colour.RED);
        d.setValue(5);
        w.putDice(d, 2, 1);
        d = new Die(Colour.YELLOW);
        d.setValue(6);
        w.putDice(d, 2, 2);
        d = new Die(Colour.MAGENTA);
        d.setValue(3);
        w.putDice(d, 2, 3);
        d = new Die(Colour.YELLOW);
        d.setValue(3);
        w.putDice(d, 2, 4);

        d = new Die(Colour.YELLOW);
        d.setValue(1);
        w.putDice(d, 3, 0);
        d = new Die(Colour.RED);
        d.setValue(3);
        w.putDice(d, 3, 1);
        d = new Die(Colour.GREEN);
        d.setValue(6);
        w.putDice(d, 3, 2);
        d = new Die(Colour.BLUE);
        d.setValue(6);
        w.putDice(d, 3, 3);

        for (int i=1; i<6; i++) {
            PrivateObjective p = HandleJSON.createPrivateObjective(Colour.values()[i]);
            //PrivateObjective p = new PrivateObjective(Colour.values()[i]);
            privates.add(p);
        }
        assertEquals(10, privates.get(0).calcScore(w));
        assertEquals(12, privates.get(1).calcScore(w));
        assertEquals(19, privates.get(2).calcScore(w));
        assertEquals(8, privates.get(3).calcScore(w));
        assertEquals(13, privates.get(4).calcScore(w));

    }
}