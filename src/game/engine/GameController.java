package game.engine;

/*
    handles the button inputs for the game
    and links to fxml ui
 */

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


public class GameController {

    @FXML
    private Label currentName;
    @FXML
    private Label currentScore;
    @FXML
    private Label currentResources;
    @FXML
    private Label currentLevel;
    @FXML
    private Label currentLives;
    @FXML
    private Label timeLabel;
    
    @FXML
    private Button openResumeButton;
    @FXML
    private Button openMenuButton;
    @FXML
    private Button openSaveButton;
    

    private GameManager gameManager;

    public void setGameManager(GameManager gameManager, String name){
        this.gameManager = gameManager;
        this.currentName.setText(name);
    }

    //set mouse clicks to buy and place tower
    public void buyTower(){
        gameManager.getGameScene().setOnMouseClicked(new buyTower());
    }
    public void openMenu(){
        gameManager.pauseGame();
        System.out.println("111");
        openResumeButton.setVisible(true);
        openSaveButton.setVisible(true);
        openMenuButton.setVisible(false);
        //open Game Menu
    }
    
    public void Resume(){
        gameManager.resumeGame();
        System.out.println("222");
        openResumeButton.setVisible(false);
        openSaveButton.setVisible(false);
        openMenuButton.setVisible(true);
        //open Game Menu
    }
    
     public void SaveGame(){
        gameManager.saveGame();
        System.out.println("333");
        openResumeButton.setVisible(false);
        openSaveButton.setVisible(false);
        openMenuButton.setVisible(true);
        //open Game Menu
    }
    
    
    public void updateLabels(String currentLevel , String currentLives , String currentResources , String currentScore , String timeLabel){
        this.currentLevel.setText(currentLevel);
        this.currentLives.setText(currentLives);
        this.currentResources.setText(currentResources);
        this.currentScore.setText(currentScore);
        this.timeLabel.setText(timeLabel);
    }


    //buy tower at mouse click tile
    class buyTower implements EventHandler<MouseEvent> {
        public void handle(MouseEvent me) {
            gameManager.buyTower(me.getX(),me.getY());
            }
        }

}
