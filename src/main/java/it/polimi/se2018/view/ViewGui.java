package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.MessageDoNothing;
import it.polimi.se2018.events.messageforcontroller.MessageSetWP;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforserver.MessagePing;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.utils.HandleJSON;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.in;
import static java.lang.System.out;


public class ViewGui extends Observable implements VisitorView, Observer, ViewInterface{

    //GAME
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private GridPane gridpaneWindowPatternToChoose;
    @FXML
    private ImageView privateObjective;
    @FXML
    private ImageView publicObjective1;
    @FXML
    private ImageView publicObjective2;
    @FXML
    private ImageView tool2;
    @FXML
    private ImageView tool3;
    @FXML
    private ImageView tool1;


    @FXML
    private ImageView windowpatternPlayer3;
    @FXML
    private ImageView windowpatternPlayer2;
    @FXML
    private ImageView windowPatternPlayer1;
    @FXML
    private ImageView windowpatternClient;

    @FXML
    private ImageView draftpool1;
    @FXML
    private ImageView draftpool2;
    @FXML
    private ImageView draftpool3;
    @FXML
    private ImageView draftpool4;
    @FXML
    private ImageView draftpool5;
    @FXML
    private ImageView draftpool6;
    @FXML
    private ImageView draftpool7;
    @FXML
    private ImageView draftpool8;
    @FXML
    private ImageView draftpool9;

    @FXML
    private Button buttonEndTurn;


    @FXML
    private Text textPublicObjective;
    @FXML
    private Text textPrivateObjective;
    @FXML
    private Text textTool;
    @FXML
    private Text textChoosewindowpattern;

    @FXML
    private Text nicknamePlayer1;
    @FXML
    private Text nicknamePlayer2;
    @FXML
    private Text nicknamePlayer3;



    @FXML
    private ImageView image1;
    @FXML
    private ImageView image2;
    @FXML
    private ImageView image3;
    @FXML
    private ImageView image4;



    @FXML
    private GridPane gridpanePlayer3;
    @FXML
    private GridPane gridpanePlayer2;
    @FXML
    private GridPane gridpanePlayer1;
    @FXML
    private GridPane gridpaneClient;

    @FXML
    private Text news;


    private ArrayList<ImageView[][]> windowPattern;
    private ArrayList<String> nickname;

    private ArrayList<ImageView> draftPool;


    private ImageView dieChoosen;
    private ImageView targetOfDie;

    private ArrayList<ImageView> groupDie;
    private ArrayList<ImageView> groupDestination;
    private ArrayList<ImageView> groupOtherImage;
    private ArrayList<ImageView> chooseWindowPattern;
    private ArrayList<ImageView> tool;

    private String nicknamePlayer = null;
    private int connection = 1;
    private int port;
    private boolean initialize = true;
    private boolean gui = true;
    private int first;
    private int second;





    private EventHandler<MouseEvent> handleChooseDie = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            dieChoosen = (ImageView) event.getSource();

            //rimuovere event Handler tool
            for(ImageView imageView : groupDie){
                imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
            }
            for(ImageView imageView : groupDestination){
                imageView.setOnMouseClicked(handleChooseBox);
            }

        }
    };

    private EventHandler<MouseEvent> handleChooseBox = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            targetOfDie = (ImageView) event.getSource();
            for(ImageView imageView : groupDestination){
                imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
            }

            //new messageMoveDie

        }
    };

    private EventHandler<MouseEvent> handleOtherImageEnterMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ImageView imageView = (ImageView) event.getSource();
            imageView.setFitHeight(imageView.getFitHeight()*2);
            imageView.setFitWidth(imageView.getFitWidth()*2);
        }
    };

    private EventHandler<MouseEvent> getHandleOtherImageExitMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ImageView imageView = (ImageView) event.getSource();
            imageView.setFitHeight(imageView.getFitHeight()*0.5);
            imageView.setFitWidth(imageView.getFitWidth()*0.5);
        }
    };

    //LOBBY
    @FXML
    private Button button;

    @FXML
    private RadioButton radioBtnB;

    @FXML
    private RadioButton radioBtnA;

    @FXML
    private Text text;

    @FXML
    private TextField textField;

    @FXML
    private GridPane gridPane;

    @FXML
    private Text errorText;

    private boolean notifyController = false;

    @FXML
    void handleRadioBtnA(ActionEvent event) {
        this.radioBtnA.setSelected(true);
        this.radioBtnB.setSelected(false);

    }

    @FXML
    void handleRadioBtnB(ActionEvent event) {
        this.radioBtnA.setSelected(false);
        this.radioBtnB.setSelected(true);
    }

    @FXML
    void handleButton(ActionEvent event) {
        String choise;
        this.errorText.setText(null);
        if(this.radioBtnB.isDisable()){
            choise = this.textField.getText();
            if(this.text.getText().equals("Choose Port")) {
                try {
                    this.port = Integer.parseInt(choise);
                    this.textField.clear();
                    this.setChooseNickname();
                } catch (NumberFormatException e) {

                    this.setError("Invalid Port");
                }
            }
            else if(this.text.getText().equals("Choose Nickname")){
                this.nicknamePlayer = textField.getText();

                if(this.nicknamePlayer.isEmpty()) this.setError("Insert your nickname, please.");
                else this.setWaiting();
            }
        }
        else {
            this.textField.setVisible(false);
            this.buttonEndTurn.setVisible(false);
            if (this.radioBtnA.isSelected()) {

                if(this.text.getText().equals("Choose")){
                    this.gui = true;
                    this.setChoosePort();
                }
                else {
                    this.connection = 1;
                    this.setGui();
                }
            }
            else {

                if(this.text.getText().equals("Choose")){
                    this.gui = false;
                    this.setChoosePort();
                }
                else {

                    this.connection = 2;
                    this.setGui();
                }
            }

        }
    }

    private void setChoosePort(){
        this.radioBtnA.setDisable(true);
        this.radioBtnB.setDisable(true);
        this.text.setText("Choose Port");
        this.textField = new TextField();
        this.gridPane.add(this.textField, 1, 1);
    }

    private void setWaiting(){

        this.radioBtnA.setVisible(false);
        this.radioBtnB.setVisible(false);
        this.button.setVisible(false);
        this.textField.setVisible(false);

        this.text.setText("Wait...");
        this.text.setLayoutX(0);
        if(this.notifyController) {
            this.notifyObservers(new Message(this.nicknamePlayer));
            this.notifyController = false;
        }

        if(initialize){
            this.initialize = false;

            ThreadGui th = new ThreadGui(this);
            th.start();
            }
        else{
            this.notifyObservers(new Message(this.nicknamePlayer));
        }
    }


    private void setChooseNickname(){
        this.text.setText("Choose Nickname");

        this.textField.setDisable(false);
        this.button.setDisable(false);
        this.radioBtnB.setDisable(true);
        this.radioBtnA.setDisable(true);

    }

    private void setError(String error){
        //RICONTROLLARE
        this.errorText.setLayoutX(-50);
        this.errorText.setText(error);
    }

    private void setGui(){
        this.text.setText("Choose");
        this.radioBtnB.setText("CLI");
        this.radioBtnA.setText("GUI");
    }

    @Override
    public int askConnection() {
        return this.connection;
    }

    @Override
    public String askNickname() {
        return this.nicknamePlayer;
    }

    @Override
    public void addObserver(Observer observer) {
        this.register(observer);
    }

    @Override
    public void notifyObserver(Message message) {
        this.notifyObservers(message);
    }

    @Override
    public void update(Message m) {
        m.accept(this);
    }

    @Override
    public void visit(Message message) {
        //DO NOTHING
    }

    @Override
    public void visit(MessageError message) {
        //DO NOTHING
    }

    @Override
    public void visit(MessageNickname message) {
        if(!message.getBoolean()) this.setChooseNickname();
        else{
            if(gui) {

                this.text.setText("Waiting...");
                this.errorText.setText(null);
                this.radioBtnA.setVisible(false);
                this.radioBtnB.setVisible(false);
                this.textField.setVisible(false);
                this.button.setVisible(false);

                this.gridPane.getChildren().remove(errorText);
                this.gridPane.getChildren().remove(radioBtnB);
                this.gridPane.getChildren().remove(radioBtnA);
                this.gridPane.getChildren().remove(button);
                Platform.runLater(() ->  anchorPane.getChildren().remove(this.gridPane));



                //INITIALIZE

                this.draftPool = new ArrayList<>();
                this.groupDie = new ArrayList<>();
                this.groupDestination = new ArrayList<>();
                this.groupOtherImage = new ArrayList<>();
                this.chooseWindowPattern = new ArrayList<>();
                this.tool = new ArrayList<>();
                this.windowPattern = new ArrayList<>();
                this.nickname = new ArrayList<>();

                this.draftPool.add(draftpool5);
                this.draftPool.add(draftpool4);
                this.draftPool.add(draftpool6);
                this.draftPool.add(draftpool3);
                this.draftPool.add(draftpool7);
                this.draftPool.add(draftpool2);
                this.draftPool.add(draftpool8);
                this.draftPool.add(draftpool1);
                this.draftPool.add(draftpool9);

                for(ImageView i : draftPool){
                    groupDie.add(i);
                }

                groupOtherImage.add(privateObjective);
                groupOtherImage.add(publicObjective1);
                groupOtherImage.add(publicObjective2);
                groupOtherImage.add(tool1);
                groupOtherImage.add(tool2);
                groupOtherImage.add(tool3);

                for(ImageView i : groupOtherImage){
                    i.addEventHandler(MouseEvent.MOUSE_ENTERED, this.handleOtherImageEnterMouse);
                    i.addEventHandler(MouseEvent.MOUSE_EXITED, this.getHandleOtherImageExitMouse);
                }

                textPrivateObjective.setDisable(true);
                textPublicObjective.setDisable(true);
                textTool.setText(null);

                this.chooseWindowPattern.add(image1);
                this.chooseWindowPattern.add(image2);
                this.chooseWindowPattern.add(image3);
                this.chooseWindowPattern.add(image4);

                this.tool.add(tool1);
                this.tool.add(tool2);
                this.tool.add(tool3);

                this.textTool.setText("Tool");
                this.textPublicObjective.setText("Public Objective");
                this.textPrivateObjective.setText("Private Objective");

                this.buttonEndTurn.setVisible(true);
                this.buttonEndTurn.setOpacity(1);
                this.news.setText("Wait...");

                //INITIALIZE CLIENT WINDOW PATTERN
                this.windowPattern.add(new ImageView[4][5]);
                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < 5 ; j++){
                        windowPattern.get(0)[i][j] = new ImageView();
                    }
                 }

                this.nickname.add(nicknamePlayer);

                this.buttonEndTurn.setOnMouseClicked(e ->{
                    notifyObservers(new MessageDoNothing(nicknamePlayer));
                });

            }
            else{
                //gestire caso cli;
            }


        }
    }

    @Override
    public void visit(MessagePrivObj message) {
        //search correct image

        this.privateObjective.setImage(new Image("/images/other/ObjEmpty.jpg"));

    }

    @Override
    public void visit(MessagePublicObj message) {
        Image image = new Image("/images/other/ObjEmpty.jpg");
         if (publicObjective1.getImage() == null) this.publicObjective1.setImage(image);
         else this.publicObjective2.setImage(image);
    }

    @Override
    public void visit(MessageTool message) {
        Image image = new Image("/images/other/ObjEmpty.jpg");

        int i = 0;
        while ((i<3) && (this.tool.get(i).getImage() != null)){
            i++;
        }
        this.tool.get(i).setImage(image);

    }

    @Override
    public void visit(MessageChooseWP message) {
        first = message.getFirstIndex();
        second = message.getSecondIndex();
        out.println(first+"second"+second);
        List<WindowPattern> windowPatterns = new ArrayList<>();

        out.println("\nYou have to choose the window pattern between these four: \n");
        windowPatterns.add(HandleJSON.createWindowPattern(null, first, 0));
        windowPatterns.add(HandleJSON.createWindowPattern(null, first, 1));
        windowPatterns.add(HandleJSON.createWindowPattern(null, second, 0));
        windowPatterns.add(HandleJSON.createWindowPattern(null, second, 1));
        for (WindowPattern wp : windowPatterns) {
            out.print(windowPatterns.indexOf(wp) + 1 + ") ");

            for (ImageView w : chooseWindowPattern) {
                //String path = "/images/windowPattern/";
               /*
               if(chooseWindowPattern.indexOf(w) < 2){
                    path = path + first + (chooseWindowPattern.indexOf(w) + 1) + ".jpg";
                }
                else{
                    path = path + second + (chooseWindowPattern.indexOf(w) - 1) + ".jpg";
                }
                */
                w.setImage(new Image(ViewGui.class.getResourceAsStream("/images/windowPattern/41.jpg")));
                w.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        Image choise = ((ImageView) event.getSource()).getImage();
                        windowpatternClient.setImage(choise);
                        int i = chooseWindowPattern.indexOf((ImageView) event.getSource());

                        for (ImageView imageView : chooseWindowPattern) {
                            imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
                        }

                        if (i < 3) {
                            notifyObservers(new MessageSetWP(nicknamePlayer, first, 1));
                        } else {
                            notifyObservers(new MessageSetWP(nicknamePlayer, second, 1));
                        }

                        Platform.runLater(() ->  anchorPane.getChildren().remove(gridpaneWindowPatternToChoose));
                    }
                });
            }
        }
    }


    @Override
    public void visit(MessageWPChanged message) {

    }

    @Override
    public void visit(MessageTurnChanged message) {
        this.news.setText(message.getNickname() + " is playing.");
    }

    @Override
    public void visit(MessageDPChanged message) {

    }

    @Override
    public void visit(MessageConfirmMove message) {

    }

    @Override
    public void visit(MessageErrorMove message) {
        this.news.setText(message.getReason());
    }

    @Override
    public void visit(MessagePing message) {
        //DO NOTHING
    }

    @Override
    public void visit(MessageRoundChanged message) {
            this.news.setText("New Round!");
    }

    @Override
    public void visit(MessageRoundTrack message) {

    }

    @Override
    public void visit(MessageEndMatch message) {

    }

    @Override
    public void visit(MessageToolOrder message) {

    }

    @Override
    public void visit(MessageAskMove message) {

    }

    @Override
    public void visit(MessageForceMove message) {

    }


}
