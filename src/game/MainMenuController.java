package game;

import game.engine.GameManager;
import game.engine.GameState;
import game.loadsave.Score;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Created by jelly on 4/16/14.
 */
public class MainMenuController {
    @FXML
    TextField txtName;
    @FXML
    Label tdlabel;
    @FXML
    Label tdlabel2;
    @FXML
     HBox Hbox1;
    @FXML
     HBox Hbox2;
    @FXML
     VBox Vbox1;
    @FXML
     VBox Vbox2;
    @FXML
     Button exitButton1;
    @FXML
	Label label1;
    @FXML
	Label label2;
    @FXML
	Label labelsm1;
    @FXML
     Button eazy;
    @FXML
     Button normal;
    @FXML
     Button hard;
    @FXML
     GridPane gridPane;

    GameManager gameManager = new GameManager();
    int  difficulty=1;
    
    
    
    public void showRank(){
        gridPane.setAlignment(Pos.TOP_LEFT);
        
        ArrayList<GameState> scoreList = new ArrayList();
        Score score = new Score();
        try{
           scoreList = score.load(); 
        }catch(Exception e){
            e.printStackTrace();
        }
        label1.setText(scoreList.get(0).getName()+ " "+
                scoreList.get(0).getResources()+ " "+
                scoreList.get(0).getLevel()+ " "+
                scoreList.get(0).getScore());
        label2.setText(scoreList.get(1).getName()+ " "+
                scoreList.get(1).getResources()+ " "+
                scoreList.get(1).getLevel()+ " "+
                scoreList.get(1).getScore());
        int i;
            for(i = 2; i < scoreList.size() -1; i++){
                    labelsm1.setText(labelsm1.getText()+ " "+
                            scoreList.get(i).getName()+ " "+
                            scoreList.get(i).getResources()+ " "+
                            scoreList.get(i).getLevel()+ " "+
                            scoreList.get(i).getScore()+ "\n");
            }
        tdlabel.setVisible(false);
        tdlabel2.setVisible(true);
        Hbox1.setVisible(false);
        Hbox2.setVisible(false);
        Vbox1.setVisible(false);
       Vbox2.setVisible(true);
       exitButton1.setVisible(true);
    }
    
    public void startNewGame(){
        if(txtName != null && txtName.getText().length() < 11 && txtName.getText().length() >0)
        try{
            gameManager.initialize(txtName.getText(),difficulty);}catch (IOException ex){ex.printStackTrace();}
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
    
    public void loadGame(){
      try{
          try{
            gameManager.initializeLoad();
          }catch(ClassNotFoundException e){
              e.printStackTrace();
          }
      }catch (IOException ex){ex.printStackTrace();}
    }
    
    public void returnMenu(){
        tdlabel.setVisible(true);
        tdlabel2.setVisible(false);
        Hbox1.setVisible(true);
        Vbox1.setVisible(true);
        Vbox2.setVisible(false);
        label1.setText("");
        label2.setText("");
        labelsm1.setText("");
        exitButton1.setVisible(false);
        Hbox2.setVisible(false);
        gridPane.setAlignment(Pos.TOP_LEFT);
    }
    
    public void returnMenu2(){
        tdlabel.setText("Tower Defense");
        Hbox1.setVisible(true);
        Hbox2.setVisible(false);
        Vbox1.setVisible(true);
        Vbox2.setVisible(false);
        label1.setText("");
        label2.setText("");
        labelsm1.setText("");
        exitButton1.setVisible(false);
        Hbox2.setVisible(false);
        gridPane.setAlignment(Pos.TOP_LEFT);
    }
    
    public void options(){
        tdlabel.setText("Options");
        Hbox1.setVisible(false);
        Hbox2.setVisible(true);
        Vbox1.setVisible(false);
        Vbox2.setVisible(false);
        label1.setText("");
        label2.setText("");
        labelsm1.setText("");
        exitButton1.setVisible(false);
      
    }
    
     public void hardBox(){
         difficulty=5;
       hard.setStyle("-fx-background-color: #ff0000; ");
       normal.setStyle("-fx-background-color: linear-gradient(#444444, #777777);");
       eazy.setStyle("-fx-background-color: linear-gradient(#444444, #777777);");
       
    }
     
     public void normalBox(){
         difficulty=3;
       hard.setStyle("-fx-background-color: linear-gradient(#444444, #777777);");
       normal.setStyle("-fx-background-color: #ff0000; ");
       eazy.setStyle("-fx-background-color: linear-gradient(#444444, #777777);");
    }
     
     public void eazyBox(){
         difficulty=1;
       hard.setStyle("-fx-background-color: linear-gradient(#444444, #777777);");
       normal.setStyle("-fx-background-color: linear-gradient(#444444, #777777);");
       eazy.setStyle("-fx-background-color: #ff0000; ");
    }
}