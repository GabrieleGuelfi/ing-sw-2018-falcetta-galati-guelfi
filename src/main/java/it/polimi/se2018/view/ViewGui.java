package it.polimi.se2018.view;

import it.polimi.se2018.events.Message;
import it.polimi.se2018.events.messageforcontroller.*;
import it.polimi.se2018.events.messageforserver.MessageError;
import it.polimi.se2018.events.messageforserver.MessagePing;
import it.polimi.se2018.events.messageforview.*;
import it.polimi.se2018.model.Box;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.WindowPattern;
import it.polimi.se2018.model.dicecollection.DraftPool;
import it.polimi.se2018.network.client.SagradaClient;
import it.polimi.se2018.utils.HandleJSON;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ImageView publicObjective3;
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
    private Text textRoundTrack;

    @FXML
    private HBox hBox1;
    @FXML
    private HBox hBox2;




    @FXML
    private ImageView image1;
    @FXML
    private ImageView image2;
    @FXML
    private ImageView image3;
    @FXML
    private ImageView image4;

    @FXML
    GridPane roundTrack = new GridPane();



    @FXML
    private GridPane gridpanePlayer3;
    @FXML
    private GridPane gridpanePlayer2;
    @FXML
    private GridPane gridpanePlayer1;
    @FXML
    private GridPane gridpaneClient;
    @FXML
    private GridPane gridPaneGame;

    //Tool
    private Button ok;
    private Button plus;
    private Button minus;

    private VBox vBox;
    private ImageView imageTool;
    private ImageView imageTool2;
    private TextField textFieldTool;

    private int diceFromDp = 0;
    private int diceFromWp = 0;
    private int positionInWp = 0;
    private boolean askPlusOrMinusOne = false;
    private int diceFromRoundtrack = 0;
    private boolean canReduceDiceFromWP;
    private int diceFromTot = 0;
    private int diceDestinationTot = 0;
    @FXML
    private Text news;
    private String lastBeautifulMessage = null;


    private ArrayList<ImageView[][]> windowPattern;
    private ArrayList<String> nickname;

    private ArrayList<ImageView> draftPool;


    private ArrayList<ImageView> dieChoosen;
    private ArrayList<ImageView> targetOfDie;

    private ArrayList<ImageView> groupDie;
    private ArrayList<ImageView> groupDestination;
    private ArrayList<ImageView> groupOtherImage;
    private ArrayList<Node> groupToolObject;
    private ArrayList<ImageView> chooseWindowPattern;
    private ArrayList<ImageView> tool;
    private ArrayList<GridPane> gridPanePlayer;
    private EventHandler<MouseEvent> eventHandlersDie;
    private EventHandler<MouseEvent> eventHandlersDestination;
    private Map<Integer, List<Die>> roundT;

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
            ImageView die = (ImageView) event.getSource();
            die.setFitHeight(die.getFitHeight() + 20);
            die.setFitWidth(die.getFitWidth() + 20);
            dieChoosen.add(die);
            diceFromTot--;
            die.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
            groupDie.remove(die);

            if (diceFromTot <= 0) {

                for (ImageView imageView : groupDie) imageView.setDisable(true);

                for (ImageView imageView : groupDestination) {
                    imageView.setOnMouseClicked(eventHandlersDestination);
                    imageView.setDisable(false);
                }


                news.setText(null);
                news.setText("Tell me where do you want to place it!");

            }
        }
    };

    private EventHandler<MouseEvent> handleChooseBox = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            targetOfDie.add((ImageView) event.getSource());

            int row;
            int column;

                for (row = 0; row < 4; row++) {
                    for (column = 0; column < 5; column++) {
                        if (windowPattern.get(0)[row][column] == targetOfDie.get(0)) {
                            notifyObservers(new MessageMoveDie(nicknamePlayer, draftPool.indexOf(dieChoosen.get(0)), row, column));
                            break;
                        }
                    }
                }

            for(ImageView imageView : groupDestination){
                imageView.setDisable(true);
            }

            for(ImageView imageView : dieChoosen) {
                    imageView.setFitWidth(imageView.getFitWidth() - 20);
                    imageView.setFitHeight(imageView.getFitHeight() - 20);
            }
            news.setText("You made your choise baby!");
        }
    };

    private EventHandler<MouseEvent> handleOtherImageEnterMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ImageView imageView = (ImageView) event.getSource();
            imageTool.setDisable(false);

            imageTool.setImage(imageView.getImage());
            imageTool.setFitHeight(imageView.getFitHeight()*3.7);
            imageTool.setFitWidth(imageView.getFitWidth()*3.7);
        }
    };

    private EventHandler<MouseEvent> getHandleOtherImageExitMouse = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ImageView imageView = (ImageView) event.getSource();
            imageTool.setImage(null);
            imageTool.setDisable(true);
            vBox.setLayoutY(vBox.getLayoutY() - 200);
            imageTool.setFitHeight(imageTool.getFitHeight()/3.7);
            imageTool.setFitWidth(imageTool.getFitWidth()/3.7);
        }
    };

    private EventHandler<MouseEvent> handleButtonEndTurn = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            if(eventHandlersDestination != null){
                for(ImageView imageView : groupDestination){
                    imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlersDestination);
                    imageView.setDisable(true);
                }
            }

            if(eventHandlersDie != null){
                for(ImageView imageView : groupDie){
                    imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlersDie);
                    imageView.setDisable(true);
                }
            }

            dieChoosen.clear();
            targetOfDie.clear();
            groupDie.clear();
            groupDestination.clear();

            lastBeautifulMessage = "End Turn!";
            news.setText(lastBeautifulMessage);
            out.println("buttonEndTurn");

            diceFromDp = 0;
            diceFromWp = 0;
            positionInWp = 0;
            askPlusOrMinusOne = false;
            diceFromRoundtrack = 0;
            canReduceDiceFromWP = false;
            diceDestinationTot = 0;

            notifyObserver(new MessageDoNothing(nicknamePlayer));

        }
    };

    private EventHandler<MouseEvent> handleChooseTool = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            ImageView imageView = (ImageView) event.getSource();

            for(ImageView im : tool) im.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
            for(ImageView im : groupDie) im.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlersDie);
            for(ImageView im : groupDestination) im.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlersDestination);
            eventHandlersDestination = null;
            //eventHandlersDie = null;
            groupDestination.clear();
            groupDie.clear();

            notifyObservers(new MessageRequestUseOfTool(nicknamePlayer, tool.indexOf(imageView)));
        }
    };

    private EventHandler<MouseEvent> handleChooseBoxTool = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            out.println("EVENT HANDLER");
            targetOfDie.add((ImageView) event.getSource());
            groupDestination.remove((ImageView) event.getSource());

            int diceDp = 0;
            List<Integer[]> positionsWp = new ArrayList<>();
            List<Integer[]> diceWp = new ArrayList<>();
            List<Integer> diceRoundtrack = new ArrayList<>();

            text.setText(targetOfDie.size() + "box chosen");
            positionInWp--;

            if(positionInWp <= 0){
                text.setText("You made your choise.");
                for(ImageView imageView : dieChoosen) {
                    imageView.setFitWidth(imageView.getFitWidth() - 20);
                    imageView.setFitHeight(imageView.getFitHeight() - 20);
                }

                for(int row = 0; row < 4; row++){
                    for(int column = 0; column < 5 ; column++){
                        for(ImageView imageView : targetOfDie){
                            if(imageView.equals(windowPattern.get(0)[row][column])){
                                Integer[] num = new Integer[2];
                                num[0] = row;
                                num[1] = column;
                                positionsWp.add(num);
                            }
                        }
                    }
                }
                for(ImageView imageView : dieChoosen){
                    for(ImageView i : draftPool){
                        if(i.equals(imageView)) diceDp = draftPool.indexOf(imageView);
                    }

                    for(int row = 0; row < 4 ; row ++){
                        for(int column = 0; column < 5 ; column++){
                            if(windowPattern.get(0)[row][column].equals(imageView)){
                                Integer[] num = new Integer[2];
                                num[0] = row;
                                num[1] = column;
                                diceWp.add(num);
                            }
                        }
                    }

                }

                notifyObservers(new MessageToolResponse(nicknamePlayer, diceDp, diceWp, diceRoundtrack, positionsWp, false ));
            }

        }
    };

    private EventHandler<MouseEvent> handlePlus = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            out.println("MINUS");
            targetOfDie.add((ImageView) event.getSource());
            news.setText("Choose PLUS or MINUS");

            for(ImageView imageView : groupDestination) imageView.setDisable(true);
            for(ImageView imageView : groupDie) imageView.setDisable(true);

            ok.setOnMouseClicked(e ->{
                for(ImageView imageView : dieChoosen) {
                    imageView.setFitWidth(imageView.getFitWidth() - 20);
                    imageView.setFitHeight(imageView.getFitHeight() - 20);
                }
                notifyObservers(new MessageToolResponse(nicknamePlayer, draftPool.indexOf(dieChoosen.get(0)), null, null, null, askPlusOrMinusOne ));
            });

            plus.setOnMouseClicked(e ->{
                askPlusOrMinusOne = true;
                news.setText("PLUS");
            });

            minus.setOnMouseClicked(e ->{
                askPlusOrMinusOne = false;
                news.setText("MINUS");
            });
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

    private String file = null;
    private int timer = 30;


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

                this.textRoundTrack.setFont(Font.font("Segoe UI Black", 12));
                textRoundTrack.setOpacity(1);
                textRoundTrack.setFill(Color.gray(1));
                textRoundTrack.setStroke(Color.gray(0));
                textRoundTrack.setStrokeWidth(2);

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
                this.groupToolObject = new ArrayList<>();
                this.tool = new ArrayList<>();
                this.windowPattern = new ArrayList<>();
                this.nickname = new ArrayList<>();
                this.gridPanePlayer = new ArrayList<>();
                this.roundTrack = new GridPane();
                this.ok = new Button();
                this.plus = new Button();
                this.minus = new Button();
                this.imageTool = new ImageView();
                this.vBox = new VBox();
                this.textFieldTool = new TextField();
                this.roundTrack = new GridPane();
                this.dieChoosen = new ArrayList<>();
                this.targetOfDie = new ArrayList<>();
                this.roundT = new HashMap<>();


                //setting of Tool object
                ok.setText("Ok");
                plus.setText("+");
                minus.setText("-");
                textFieldTool.setMaxWidth(50);
                imageTool.setFitWidth(50);
                imageTool.setFitHeight(50);
                ok.setOpacity(0);
                ok.setDisable(true);
                plus.setDisable(true);
                plus.setOpacity(0);
                minus.setOpacity(0);
                minus.setDisable(true);
                textFieldTool.setOpacity(0);
                textFieldTool.setDisable(true);
                imageTool.setImage(null);
                imageTool.setDisable(true);


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
                groupOtherImage.add(publicObjective3);
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

                buttonEndTurn.setOnMouseClicked(handleButtonEndTurn);
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
                    vBox.getChildren().add(imageTool);
                    vBox.getChildren().add(textFieldTool);
                    vBox.getChildren().add(plus);
                    vBox.getChildren().add(minus);
                    vBox.getChildren().add(ok);
                    gridPaneGame.add(vBox, 5, 2);



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

        try {
            privateObjective.setImage(new Image("/images/privateObjectiveImage/" + message.getShade() + ".jpg"));
        }
        catch(Exception e){
            privateObjective.setImage(new Image("/images/other/ObjEmpty.jpg"));
        }

    }

    @Override
    public void visit(MessagePublicObj message) {
        Image image1 = null;
        Image image2 = null;
        Image image3 = null;
        try{
            image1 = new Image("/images/publicObjectiveImage/" + message.getId().get(0) + ".jpg");
        }
        catch(Exception e){

            image1 = new Image("/images/other/ObjEmpty.jpg");
        }
        try{
            image2 = new Image("/images/publicObjectiveImage/" + message.getId().get(1) + ".jpg");
        }
        catch(Exception e){

            image2 = new Image("/images/other/ObjEmpty.jpg");
        }
        try{
            image3 = new Image("/images/publicObjectiveImage/" + message.getId().get(2) + ".jpg");
        }
        catch(Exception e){

            image3 = new Image("/images/other/ObjEmpty.jpg");
        }


        publicObjective1.setImage(image1);
        publicObjective2.setImage(image2);
        publicObjective3.setImage(image3);
    }

    @Override
    public void visit(MessageTool message) {
        int i = 0;
        String path = "/images/tool/";
        for(String string : message.getNames()){
            try{
                String s = path + string + ".jpg";
                tool.get(i).setImage(new Image(s));
                i++;
            }
            catch(Exception e){
                tool.get(i).setImage(new Image("/images/other/ObjEmpty.jpg"));
                i++;
            }
        }
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

        dieChoosen.clear();
        targetOfDie.clear();

        for(ImageView imageView : groupDie) {
            imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlersDie);
            imageView.setDisable(false);
        }

        for(ImageView imageView : groupDestination){
            imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlersDestination);
            imageView.setDisable(false);
        }

        groupDestination.clear();
        groupDie.clear();
        eventHandlersDestination = null;
        eventHandlersDie = null;

        diceFromDp = 0;
        diceFromWp = 0;
        positionInWp = 0;
        askPlusOrMinusOne = false;
        diceFromRoundtrack = 0;
        canReduceDiceFromWP = false;
        diceDestinationTot = 0;

        news.setText(null);
        news.setText("OK!");

    }

    @Override
    public void visit(MessageErrorMove message) {

        this.news.setText(message.getReason());

        for(ImageView imageView : groupDestination){
            imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlersDestination);
            imageView.setDisable(false);
        }

        for(ImageView imageView : groupDie){
            imageView.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlersDie);
            imageView.setDisable(false);
        }

        dieChoosen.clear();
        targetOfDie.clear();
        groupDestination.clear();
        groupDie.clear();

        eventHandlersDestination = null;
        eventHandlersDie = null;

        diceFromDp = 0;
        diceFromWp = 0;
        positionInWp = 0;
        askPlusOrMinusOne = false;
        diceFromRoundtrack = 0;
        canReduceDiceFromWP = false;
        diceDestinationTot = 0;

        Runnable runnable = () -> {
                try {
                    for(int i = 10; i > 0; i--){
                            Thread.sleep(1000);
                    }
                    news.setText(null);
                    news.setText(lastBeautifulMessage);
                }catch(InterruptedException e){
                    news.setText(null);
                    news.setText(lastBeautifulMessage);
                    Thread.currentThread().interrupt();
                }
        };

        (new Thread(runnable)).start();
    }

    @Override
    public void visit(MessagePing message) {
        //DO NOTHING
    }

    @Override
    public void visit(MessageRoundChanged message) {

        this.news.setText(message.getNickname() + " is playing.");
        if(message.getDraftPool() != null) this.visit(new MessageDPChanged(message.getDraftPool()));
    }

    @Override
    public void visit(MessageRoundTrack message) {
        out.println("DIOCANE");
        Platform.runLater(()->{
            roundT = message.getRoundTrack();
            roundTrack.getChildren().clear();

            for(Integer i  : roundT.keySet() ){
                int j = 0;
                for(Die die : roundT.get(i)){
                    Image image = new Image("/images/" + die.getColour().toString() + "/" + die.getValue() + ".jpg");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(25);
                    imageView.setFitWidth(25);
                    roundTrack.add(imageView, i, j );
                    j++;
                }
            }
        });

    }

    @Override
    public void visit(MessageEndMatch message) {
        Platform.runLater(() -> {
            anchorPane.getChildren().clear();
            Pane pane = new Pane();
            VBox vBox1 = new VBox();
            anchorPane.getChildren().add(pane);
            anchorPane.getChildren().add(vBox1);

            vBox1.getChildren().add(new Text("The WINNER IS:  " + message.getResults().keySet().toArray()[0]));

            for(String s : message.getResults().keySet()){
                vBox1.getChildren().add(new Text(s +"    "+ message.getResults().get(s)));
            }

        });


    }

    @Override
    public void visit(MessageToolOrder message) {

        news.setText("TOOL");
        diceFromDp = message.getDiceFromDp();
        diceFromWp = message.getDiceFromWp();
        positionInWp = message.getPositionInWp();
        askPlusOrMinusOne = message.isAskPlusOrMinusOne();
        diceFromRoundtrack = message.getDiceFromRoundtrack();
        canReduceDiceFromWP = message.isCanReduceDiceFromWP();
        diceFromTot = diceFromDp + diceFromRoundtrack + diceFromWp;
        eventHandlersDestination = null;

        if(diceFromDp != 0) {
            groupDie.addAll(draftPool);
        }
        if(diceFromWp != 0) {
            for(int row = 0 ; row < 4; row++){
                for(int column = 0; column < 5; column++){
                    groupDie.add(windowPattern.get(0)[row][column]);
                    windowPattern.get(0)[row][column].setDisable(false);
                }
            }
        }
        if(positionInWp != 0) {
            for(int row = 0 ; row < 4; row++){
                for(int column = 0; column < 5; column++){
                    groupDestination.add(windowPattern.get(0)[row][column]);
                }
            }
            this.eventHandlersDestination = handleChooseBoxTool;
        }

        if(askPlusOrMinusOne){
            imageTool.setOpacity(1);
            imageTool.setDisable(false);
            imageTool.setImage(null);

            plus.setOpacity(1);
            plus.setDisable(false);

            minus.setOpacity(1);
            minus.setDisable(false);

            ok.setOpacity(1);
            ok.setDisable(false);

            eventHandlersDestination = handlePlus;

        }
        if(diceFromRoundtrack != 0){

        }
        for(ImageView im : groupDie) im.setOnMouseClicked(handleChooseDie);
        this.eventHandlersDie = handleChooseDie;
        if(eventHandlersDestination == null) notifyObservers(new MessageToolResponse(nicknamePlayer, 0, null, null, null, false));
    }

    @Override
    public void visit(MessageAskMove message) {
        String text = "It's your turn! You can: ";
        out.println("Message Ask Move");

        if(message.getWindowPattern() != null) this.visit( new MessageWPChanged(message.getNickname(), message.getWindowPattern()) );
        if(message.getDraftPool() != null) this.visit(new MessageDPChanged(message.getDraftPool()));

        if (!message.isHasMovedDie()){

            text = text + "-Move a die! ";
            groupDestination.clear();
            groupDie.clear();
            for (ImageView imageView : draftPool) {
                imageView.setOnMouseClicked(handleChooseDie);
                groupDie.add(imageView);
            }

            for(int i = 0; i < 4; i ++){
                for (int j = 0 ; j < 5; j++){
                    groupDestination.add(windowPattern.get(0)[i][j]);
                }
            }
            for(ImageView imageView : groupDestination) imageView.setDisable(true);
            for(ImageView imageView : groupDie) imageView.setDisable(false);

            eventHandlersDie = handleChooseDie;
            eventHandlersDestination = handleChooseBox;
    }
        if(!message.isHasUsedTool()){
            text = text + "-Use Tool!";
            for(ImageView imageView : tool) imageView.setOnMouseClicked(handleChooseTool);
        }
        this.news.setText(null);
        this.news.setText(text);
        this.lastBeautifulMessage = text;
        diceFromTot = 1;

    }

    @Override
    public void visit(MessageForceMove message) {

    }

    public void stopTimer(){buttonEndTurn.fire();}

    private Map<String, Object> showPopupWindow() {
        PopUpController popupController = new PopUpController();

        Platform.runLater(()-> {FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fileutils/Popup.fxml"));
            // initializing the controller

            loader.setController(popupController);
            Parent layout;
            try {
                layout = loader.load();
                Scene scene = new Scene(layout);
                // this is the popup stage
                Stage popupStage = new Stage();
                // Giving the popup controller access to the popup stage (to allow the controller to close the stage)
                popupController.setStage(popupStage);
                popupStage.initOwner(buttonEndTurn.getScene().getWindow());
                popupStage.initModality(Modality.WINDOW_MODAL);
                popupStage.setScene(scene);
                popupStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }});
        Map<String, Object> stringObjectMap = new HashMap<>();
        do {
            stringObjectMap = popupController.getResult();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }while (stringObjectMap.isEmpty());
        return stringObjectMap;
    }

    @Override
    public void visit(MessageCustomWP message) {
        Map<String, Object> resultMap;
        resultMap = showPopupWindow();

        int timer1 = Integer.parseInt((String) resultMap.get("timer"));

        while (timer1<20 || timer1>300)
            resultMap = showPopupWindow();
        try {
            String file1 = HandleJSON.readFile((String)resultMap.get("file"));
            if (file!=null) {
                notifyObservers(new MessageCustomResponse(nicknamePlayer, false, file1, timer1));
                return;
            }
        } catch (FileNotFoundException e) {
            notifyObservers(new MessageCustomResponse(nicknamePlayer, false, null, timer1));
            return;
        }

        notifyObservers(new MessageCustomResponse(nicknamePlayer, false, null, timer1));

    }

}
