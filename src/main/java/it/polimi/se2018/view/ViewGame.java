package it.polimi.se2018.view;

import it.polimi.se2018.utils.HandleJSON;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.omg.CORBA.IMP_LIMIT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.*;

public class ViewGame extends Application {


    @Override
    public void start(Stage stage){

        HandleJSON.newGame();

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/fileutils/sagradaMatch.fxml"));
            root.setStyle("-fx-background-color: yellow");

            Scene scene = new Scene(root, 1000, 1000);
            stage.setTitle("SAGRADA");
            stage.setScene(scene);
            stage.setOnCloseRequest(e -> System.exit(0));
            stage.setMaximized(true);
            stage.setResizable(true);


        }
        catch(IOException e){
            final Logger logger = Logger.getLogger(this.getClass().getName());
            logger.log(Level.WARNING, e.getMessage());
        }
        stage.show();
    }






}
