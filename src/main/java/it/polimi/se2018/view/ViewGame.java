package it.polimi.se2018.view;

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
import static java.lang.System.*;

public class ViewGame extends Application {


    @Override
    public void start(Stage stage){
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/fileutils/sagradaMatch.fxml"));

            Scene scene = new Scene(root, 1000, 1000);
            stage.setTitle("SAGRADA");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);

        }
        catch(IOException e){
            e.printStackTrace();
        }
        stage.show();
    }






}
