package it.polimi.se2018.model.publicobjective;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestPublicObjective {

    @Test
    public void TestFactoryPositive() {
        List<PublicObjective> p = new ArrayList<>();
        for (int i=0; i<10; i++) {
            p.add(PublicObjective.factory(i));
            if (i==0) {
                assertEquals("Rows with no repeated colors", p.get(i).getDescription());
                assertEquals(6, p.get(i).getVp());
            }
            else if (i==1) {
                assertEquals("Column with no repeated colours", p.get(i).getDescription());
                assertEquals(5, p.get(i).getVp());
            }
            else if (i==2) {
                assertEquals("Rows with no repeated values", p.get(i).getDescription());
                assertEquals(5, p.get(i).getVp());
            }
            else if (i==3) {
                assertEquals("Column with no repeated values", p.get(i).getDescription());
                assertEquals(4, p.get(i).getVp());
            }
            else if (i==4) {
                assertEquals("Sets of 1 & 2 values anywhere", p.get(i).getDescription());
                assertEquals(2, p.get(i).getVp());
            }
            else if (i==5) {
                assertEquals("Sets of 3 & 4 values anywhere", p.get(i).getDescription());
                assertEquals(2, p.get(i).getVp());
            }
            else if (i==6) {
                assertEquals("Sets of 5 & 6 values anywhere", p.get(i).getDescription());
                assertEquals(2, p.get(i).getVp());
            }
            else if (i==7) {
                assertEquals("Sets of one of each value anywhere", p.get(i).getDescription());
                assertEquals(5, p.get(i).getVp());
            }
            else if (i==8) {
                assertEquals("Count of diagonally adjacent same color dice", p.get(i).getDescription());
                assertEquals(0, p.get(i).getVp());
            }
            else {
                assertEquals("Sets of one of each color anywhere", p.get(i).getDescription());
                assertEquals(4, p.get(i).getVp());
            }
        }
    }

    @Test
    public void TestFactoryNegative() {
        PublicObjective p = PublicObjective.factory(-1);
        assertEquals(null, p);
        p = PublicObjective.factory(10);
        assertEquals(null, p);
    }

}