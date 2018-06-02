package it.polimi.se2018;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        /*
        Random generator = new Random();
        List<Integer> rand = new ArrayList<>();
        int index;
        List<PublicObjective> objectives = new ArrayList<>();

        List<Player> players = new ArrayList<>();
        for (int i=0; i<2; i++) {
            players.add(new Player("zio"+i));
        }
        for (int i=0; i<3; i++) {
            index = generator.nextInt(10)+1;
            while (rand.contains(index))
                index = generator.nextInt(10)+1;
            rand.add(index);
            objectives.add(PublicObjective.factory(index));
        }
        Match match = new Match(new Bag(), players, objectives, null, null);

        for (int i=0; i<10; i++) {
            match.setRoundTrack();
            out.println("\n\ndraftpool: ");
            for (Die d : match.getRound().getDraftPool().getBag()) {
                for (Ansi.Color color : Ansi.Color.values()) {
                    if (color.toString().equals(d.getDescription().toString())) {
                        out.print(ansi().fg(color).a("[" + d.getValue() + "] ").reset());
                    }
                }
                if (d.getDescription().equals(Colour.PURPLE))
                    out.print(ansi().fg(MAGENTA).a("[" + d.getValue() + "] ").reset());
            }
            out.print("\nroundTrack: ");
            for (Integer j : match.getRoundTrack().keySet()) {
                out.print("\n"+j+": ");
                for (Die d : match.getRoundTrack().get(j)) {
                    for (Ansi.Color color : Ansi.Color.values()) {
                        if (color.toString().equals(d.getDescription().toString())) {
                            out.print(ansi().fg(color).a("[" + d.getValue() + "] ").reset());
                        }
                    }
                    if (d.getDescription().equals(Colour.PURPLE))
                        out.print(ansi().fg(MAGENTA).a("[" + d.getValue() + "] ").reset());
                }
            }
            match.setRound();
        }
        */
    }
}
