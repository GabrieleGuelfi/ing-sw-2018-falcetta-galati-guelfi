package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.MessageDoNothing;
import it.polimi.se2018.events.messageforcontroller.MessageMoveDie;
import it.polimi.se2018.events.messageforcontroller.MessageSetWP;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforserver.MessagePing;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.network.socket.client.SagradaClient;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.System.out;


public class ViewGui extends Observable implements VisitorView, ViewInterface{

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
    private Text textNickname;

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
    private String lastBeautifulMessage = null;


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
    private ArrayList<GridPane> gridPanePlayer;

    private String nicknamePlayer = null;
    private int connection = 1;
    private String port;
    private boolean initialize = true;
    private boolean gui = true;
    private int first;
    private int second;
    private ThreadGui th;


    private EventHandler<MouseEvent> handleDieEnterMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ImageView imageView = (ImageView) event.getSource();
            imageView.setFitWidth(imageView.getFitWidth() + 20);
            imageView.setFitHeight(imageView.getFitHeight() + 20);

        }
    };

    private EventHandler<MouseEvent> handleDieExitMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ImageView imageView = (ImageView) event.getSource();
            imageView.setFitWidth(imageView.getFitWidth() - 20);
            imageView.setFitHeight(imageView.getFitHeight() - 20);

        }
    };

    private EventHandler<MouseEvent> handleChooseDie = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            dieChoosen = (ImageView) event.getSource();


            for(int i = 0; i < 4; i ++){
                for (int j = 0 ; j < 5; j++){

                        windowPattern.get(0)[i][j].setOnMouseClicked(handleChooseBox);
                        groupDestination.add(windowPattern.get(0)[i][j]);

                }
            }
        }
    };

    private EventHandler<MouseEvent> handleChooseBox = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            targetOfDie = (ImageView) event.getSource();


            for (ImageView imageView : groupDie) imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, handleChooseDie);

            int row;
            int column;
            for(row = 0; row < 4; row ++){
                for(column = 0; column < 5 ; column++){
                   if( windowPattern.get(0)[row][column] == targetOfDie) {
                       notifyObservers(new MessageMoveDie(nicknamePlayer, draftPool.indexOf(dieChoosen), row, column ));
                       out.println("Message Move Die send");
                       break;
                   }
                }
            }
            for(ImageView imageView : groupDestination){
                imageView.setDisable(true);
            }
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

    private EventHandler<ActionEvent> handleButtonEndTurn = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            notifyObserver(new MessageDoNothing(nicknamePlayer));
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
    void handleKeyEnter(KeyEvent event){
         if(event.getCode() == KeyCode.ENTER){
             this.button.fire();
             event.consume();
         }
    }

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
        this.errorText.setFont(Font.font("Segoe UI Black", 12));
        errorText.setFill(Color.gray(1));
        errorText.setStroke(Color.gray(0));
        errorText.setStrokeWidth(2);

        if(this.radioBtnB.isDisable()){
            choise = this.textField.getText();
            if(this.text.getText().equals("Choose IP")) {
                try {
                    this.port = choise;
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
                    this.setChooseIP();
                }
                else {
                    this.connection = 1;
                    this.setGui();
                }
            }
            else {

                if(this.text.getText().equals("Choose")){
                    this.gui = false;
                    this.setChooseIP();
                }
                else {

                    this.connection = 2;
                    this.setGui();
                }
            }

        }
    }

    private void setChooseIP(){
        this.radioBtnA.setDisable(true);
        this.radioBtnB.setDisable(true);
        this.text.setText("Choose IP");
        this.textField = new TextField();
        this.gridPane.add(this.textField, 1, 1);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textField.requestFocus();
            }
        });
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

            th = new ThreadGui(this);
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

        this.radioBtnA.setVisible(true);
        this.radioBtnB.setVisible(true);
        this.button.setVisible(true);
        this.textField.setVisible(true);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textField.requestFocus();
            }
        });

    }

    private void setError(String error){
        //RICONTROLLARE
        this.errorText.setLayoutX(-50);
        this.errorText.setText(error);
        if(this.textField != null) textField.clear();
    }

    private void setGui(){
        this.text.setText("Choose");
        this.radioBtnB.setText("CLI");
        this.radioBtnA.setText("GUI");
    }

    //GAME

    @Override
    public int askConnection() {
        return this.connection;
    }

    @Override
    public String askNickname() {
        return this.nicknamePlayer;
    }

    @Override
    public String getHost() {
        return port;
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
        //Do Nothing
    }

    @Override
    public void visit(MessageNickname message) {
        if(!message.getBoolean()) this.setChooseNickname();
        else{
            if(gui) {
                this.text.setFont(Font.font("Segoe UI Black", 12));
                text.setFill(Color.gray(1));
                text.setStroke(Color.gray(0));
                text.setStrokeWidth(2);

                this.textPrivateObjective.setFont(Font.font("Segoe UI Black", 12));
                textPrivateObjective.setFill(Color.gray(1));
                textPrivateObjective.setStroke(Color.gray(0));
                textPrivateObjective.setStrokeWidth(2);

                this.textPublicObjective.setFont(Font.font("Segoe UI Black", 12));
                textPublicObjective.setFill(Color.gray(1));
                textPublicObjective.setStroke(Color.gray(0));
                textPublicObjective.setStrokeWidth(2);

                this.textTool.setFont(Font.font("Segoe UI Black", 12));
                textTool.setFill(Color.gray(1));
                textTool.setStroke(Color.gray(0));
                textTool.setStrokeWidth(2);

                this.errorText.setFont(Font.font("Segoe UI Black", 12));
                errorText.setFill(Color.gray(1));
                errorText.setStroke(Color.gray(0));
                errorText.setStrokeWidth(2);

                this.news.setFont(Font.font("Segoe UI Black", 12));
                news.setFill(Color.gray(1));
                news.setStroke(Color.gray(0));
                news.setStrokeWidth(2);

                this.textNickname.setFont(Font.font("Segoe UI Black", 12));
                textNickname.setText(this.nicknamePlayer);
                textNickname.setOpacity(1);
                textNickname.setFill(Color.gray(1));
                textNickname.setStroke(Color.gray(0));
                textNickname.setStrokeWidth(2);

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

                //INITIALIZE

                this.draftPool = new ArrayList<>();
                this.groupDie = new ArrayList<>();
                this.groupDestination = new ArrayList<>();
                this.groupOtherImage = new ArrayList<>();
                this.chooseWindowPattern = new ArrayList<>();
                this.tool = new ArrayList<>();
                this.windowPattern = new ArrayList<>();
                this.nickname = new ArrayList<>();
                this.gridPanePlayer = new ArrayList<>();

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
                    i.setOnMouseEntered(handleDieEnterMouse);
                    i.setOnMouseExited(handleDieExitMouse);
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

                this.gridPanePlayer.add(gridpaneClient);
                this.gridPanePlayer.add(gridpanePlayer1);
                this.gridPanePlayer.add(gridpanePlayer2);
                this.gridPanePlayer.add(gridpanePlayer3);

                this.buttonEndTurn.setVisible(true);
                this.buttonEndTurn.setOpacity(1);
                this.news.setText("Wait...");

                buttonEndTurn.setOnAction(handleButtonEndTurn);
                buttonEndTurn.setDisable(false);


                //INITIALIZE CLIENT WINDOW PATTERN
            for(int x = 0; x < 4; x++) {
                this.windowPattern.add(new ImageView[4][5]);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 5; j++) {
                        windowPattern.get(x)[i][j] = new ImageView(new Image("/images/box/WHITE.jpg"));
                        if(x == 0 ) {
                            windowPattern.get(x)[i][j].setFitHeight(50);
                            windowPattern.get(x)[i][j].setFitWidth(50);
                        }
                        else{
                            windowPattern.get(x)[i][j].setFitHeight(25);
                            windowPattern.get(x)[i][j].setFitWidth(25);
                        }
                        windowPattern.get(x)[i][j].setImage(null);
                    }
                }
            }

                Platform.runLater(() -> {
                    for (int x = 0; x < 4; x++) {
                        for(int row = 0; row < 4 ; row ++){
                            for(int column = 0 ; column < 5; column++){
                                gridPanePlayer.get(x).add(windowPattern.get(x)[row][column], column + 1 , row + 1);
                            }
                        }
                    }

                    anchorPane.getChildren().remove(this.gridPane);

                });
                this.nickname.add(nicknamePlayer);


                this.buttonEndTurn.setOnMouseClicked(e -> notifyObservers(new MessageDoNothing(nicknamePlayer)));


            }
            else{
                SagradaClient.setCLI(this.nicknamePlayer);
                Platform.exit();

            }


        }
    }

    @Override
    public void visit(MessagePrivObj message) {
        //search correct image
        this.textTool.setText("Tool");
        this.textPublicObjective.setText("Public Objective");
        this.textPrivateObjective.setText("Private Objective");

    }

    @Override
    public void visit(MessagePublicObj message) {
        Image image = new Image("/images/other/ObjEmpty.jpg");
         if (publicObjective1.getImage() == null) this.publicObjective1.setImage(image);
         else this.publicObjective2.setImage(image);
         out.println("public objective");
    }

    @Override
    public void visit(MessageTool message) {
        Image image = new Image("/images/other/ObjEmpty.jpg");

        int i = 0;
        while ((i<3) && (this.tool.get(i).getImage() != null)){
            i++;
            out.println(i);
        }
        this.tool.get(i).setImage(image);

    }

    @Override
    public void visit(MessageChooseWP message) {
        // WindowPattern w1 = HandleJSON.createWindowPattern(null, first, 0);

        first = message.getFirstIndex();
        second = message.getSecondIndex();

            for (ImageView w : chooseWindowPattern) {
                String path = "/images/windowPattern/";

               if(chooseWindowPattern.indexOf(w) < 2){
                    path = path + first + (chooseWindowPattern.indexOf(w) + 1) + ".jpg";
                }
                else{
                    path = path + second + (chooseWindowPattern.indexOf(w) - 1) + ".jpg";
                }

                w.setImage(new Image(path));
                w.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        windowpatternClient.setImage(new Image("/images/windowPattern/windowpattern.jpg"));
                        windowpatternClient.setRotate(180);
                        int i = chooseWindowPattern.indexOf((ImageView) event.getSource());

                        for (ImageView imageView : chooseWindowPattern) {
                            imageView.setImage(null);
                            imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
                        }

                        if (i < 2) {
                            notifyObservers(new MessageSetWP(nicknamePlayer, first, i%2));
                        } else {
                            notifyObservers(new MessageSetWP(nicknamePlayer, second, i%2));
                        }

                        Platform.runLater(() ->  anchorPane.getChildren().remove(gridpaneWindowPatternToChoose));
                    }
                });
            }

    }

    @Override
    public void visit(MessageWPChanged message) {

        if(!nickname.contains(message.getPlayer())) {
            this.nickname.add(message.getPlayer());
        }

        int i = this.nickname.indexOf(message.getPlayer());
        int size;
        if(i == 0){
            size = 50;
        }
        else{
            size = 25;
        }

        WindowPattern window = message.getWp();
        for(int row = 0; row < 4 ; row++){
            for(int column = 0; column < 5; column++){
                Die die = window.getBox(row, column).getDie();
                Box box = window.getBox(row, column);
                Image image;
                if(die == null) {
                    if(box.getValueRestriction() != 0){
                        image = new Image("/images/box/" + box.getValueRestriction() + ".jpg");
                    }
                    else{
                        image = new Image("/images/box/" + box.getColourRestriction().toString() + ".jpg");
                    }
                }
                else{
                    image = new Image("/images/"+die.getColour()+"/"+die.getValue()+".jpg");
                }
                windowPattern.get(i)[row][column].setImage(image);
                windowPattern.get(i)[row][column].setFitHeight(size);
                windowPattern.get(i)[row][column].setFitWidth(size);
            }
        }

    }

    @Override
    public void visit(MessageTurnChanged message) {

        this.news.setText(message.getNickname() + " is playing.");
    }

    @Override
    public void visit(MessageDPChanged message) {
        DraftPool draft = message.getDraftPool();
        for(ImageView imageView : draftPool) imageView.setImage(null);

        for(Die die : draft.getBag()){
            Colour colour = die.getColour();
            int value = die.getValue();

            for(ImageView i : this.draftPool){
                if(i.getImage() == null) {
                    i.setImage(new Image("/images/"+colour+"/"+value+".jpg"));
                    i.setFitWidth(50);
                    i.setFitHeight(50);
                    break;
                }
            }
        }


    }

    @Override
    public void visit(MessageConfirmMove message) {
        Image image = dieChoosen.getImage();
        dieChoosen.setImage(null);
        targetOfDie.setImage(image);
        dieChoosen = null;
        targetOfDie = null;

        for(ImageView imageView : groupDestination){
            imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, handleChooseBox);
            imageView.setDisable(false);
        }
        groupDestination.clear();
        //E se non fosse draftpool?
        for(ImageView imageView : draftPool) imageView.setDisable(false);
        if(!message.isThereAnotherMove()) buttonEndTurn.fire();

    }

    @Override
    public void visit(MessageErrorMove message) {
        this.news.setText(message.getReason());
        this.dieChoosen = null;
        this.targetOfDie = null;
        for(ImageView imageView : groupDestination){
            imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, handleChooseBox);
            imageView.setDisable(false);
        }
        groupDestination.clear();
        //E se non fosse draftpool?
        for(ImageView imageView : draftPool) imageView.setDisable(false);
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
        out.println("MessageAskMove Arrived");
        if (!message.isHasMovedDie()){
            groupDie.clear();
            for (ImageView imageView : draftPool) {
                imageView.setOnMouseClicked(handleChooseDie);
                groupDie.add(imageView);
            }
            out.println("Handle Choose Die");
    }
        else if(!message.isHasUsedTool()){}

    }

    @Override
    public void visit(MessageForceMove message) {

    }

    @Override
    public void visit(MessageCustomWP message) {

    }

}
