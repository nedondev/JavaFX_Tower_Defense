package game;

import game.engine.GameManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import javafx.scene.control.Alert;

/**
 * Created by jelly on 4/16/14.
 */
public class MainMenuController {
    @FXML
    TextField txtName;

    GameManager gameManager = new GameManager();
    public void startNewGame(){
        if(txtName != null && txtName.getText().length() < 11 && txtName.getText().length() >0)
        try{
            gameManager.initialize(txtName.getText());}catch (IOException ex){ex.printStackTrace();}
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning!!");
                alert.setHeaderText("Name label empty");
                alert.setContentText("You need to fill text in Name label.");
                alert.showAndWait();
        }
    }
    public void exitGame(){
        System.exit(1);
    }
}