package it.polimi.se2018.View;

import it.polimi.se2018.model.*;
import it.polimi.se2018.controller.tool.*;
import it.polimi.se2018.events.*;
import it.polimi.se2018.controller.*;
import java.util.*;
import it.polimi.se2018.view.View;

import static it.polimi.se2018.View.TypeDecision.*;


public class Decision  {

    private Die die;
    private int row;
    private int column;
    private int value;
    private TypeDecision type;
    private Tool tool;

    private MoveDie move;
    private Player player;




    public Decision(Player p, View view){
        this.player = p;

        //Start question to User about what he wants to do;
        System.out.println("What do you want do now?");
        System.out.println("1.Die: to move a die. \n 2.Tool: to use a tool. \n 3.Nothing: to do nothing.");
        Scanner in= new Scanner(System.in);
        String typeAnswer= in.nextLine();
        if(typeAnswer.equals("Die")){
            this.setTypeDecision(DIE);

            System.out.println("Which die do you want move?");
            //here has to be passed a Die.
            //this.setDie(d);
            System.out.println("Where do you want place it?");
            //this.setRow(r);
            //this.setColumn(c);
            //this.setValue(d.getValue())

            view.notifyObservers(move = new MoveDie(player, row, column));

        }
        else if(typeAnswer.equals("Tool")){
            this.setTypeDecision(TOOL);

            System.out.println("Which Tool do you want to use?");
            //this.setTool(t);
            //Undrstand what to do and what controller needs in this situation.

        }
        else if(typeAnswer.equals("Nothing")){
            this.setTypeDecision(NOTHING);

            System.out.println("You have passed your turn.");

        }
        else{

            System.out.println("Error.");
            this.setTypeDecision(ERROR);
        }



    }

    public void setDie(Die d){ this.die = d;}
    public void setRow(int r){this.row = r;}
    public void setColumn(int c){this.column = c;}
    public void setValue(int v){this.value= v;}
    public void setTypeDecision(TypeDecision t){this.type= t;}
    public void setTool(Tool t){this.tool = t;}




}
