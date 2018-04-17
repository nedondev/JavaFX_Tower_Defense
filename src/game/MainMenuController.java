package game;

import game.engine.GameManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

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
    }
    public void exitGame(){
        System.exit(1);
    }
}