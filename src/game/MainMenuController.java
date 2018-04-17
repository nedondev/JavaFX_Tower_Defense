package game;

import game.engine.GameManager;

import java.io.IOException;

/**
 * Created by jelly on 4/16/14.
 */
public class MainMenuController {

    GameManager gameManager = new GameManager();
    public void startNewGame(){
        try{
            gameManager.initialize();}catch (IOException ex){ex.printStackTrace();}
        System.out.println("stop");
    }
    public void exitGame(){
        System.exit(1);
    }
}