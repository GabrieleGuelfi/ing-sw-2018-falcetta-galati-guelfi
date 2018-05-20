package it.polimi.se2018;

import it.polimi.se2018.model.publicobjective.PublicObjective;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        List<PublicObjective> p = new ArrayList<>();
        for (int i=1; i<11; i++) {
            p.add(PublicObjective.factory(i));
        }
        for (PublicObjective p1 : p) {
            System.out.println(p1.getDescription()+" "+p1.getVp());
        }
    }
}
