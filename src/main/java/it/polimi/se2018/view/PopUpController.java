package it.polimi.se2018.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PopUpController implements Initializable {

    @FXML private TextField usernameTF;
    @FXML private PasswordField passwordPF;
    @FXML private Button connectBtn;
    private Stage stage = null;
    private Map<String, Object> result = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectBtn.setOnAction((event)->{
            result.clear();
            result.put("timer", usernameTF.getText());
            result.put("file", passwordPF.getText());
            closeStage();
        });

    }

    public Map<String, Object> getResult() {
        return this.result;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Closes the stage of this view
     */
    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }

}
