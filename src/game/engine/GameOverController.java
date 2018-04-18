/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.engine;

import game.engine.GameManager;
import game.loadsave.Score;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class GameOverController implements Initializable {
    
    @FXML
	Label labelsm1;
    @FXML
	Label labelsm2;
    @FXML
	Label labelsm3;
    @FXML
	Label label1;
    @FXML
	Label label2;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        if(scoreList.size() > 0 &&scoreList.get(0) == scoreList.get(scoreList.size()-1)){
            label1.setTextFill(Color.YELLOW);
        }
        else if(scoreList.size() > 1 && scoreList.get(1) == scoreList.get(scoreList.size()-1)){
            label1.setTextFill(Color.YELLOW);
        }
        else{
            int i;
            for(i = 2; i < scoreList.size() -1; i++){
                if(scoreList.get(i) == scoreList.get(scoreList.size()-1)){
                    if(labelsm1.getText().equals("")){
                        labelsm1.setText(scoreList.get(i).getName()+ " "+
                            scoreList.get(i).getResources()+ " "+
                            scoreList.get(i).getLevel()+ " "+
                            scoreList.get(i).getScore());
                        labelsm1.setTextFill(Color.YELLOW);
                        break;
                    }
                    else
                    {
                        labelsm2.setText(scoreList.get(i).getName()+ " "+
                            scoreList.get(i).getResources()+ " "+
                            scoreList.get(i).getLevel()+ " "+
                            scoreList.get(i).getScore());
                        labelsm2.setTextFill(Color.YELLOW);
                        break;
                    }
                }
                else{
                    labelsm1.setText(labelsm1.getText()+ " "+
                            scoreList.get(i).getName()+ " "+
                            scoreList.get(i).getResources()+ " "+
                            scoreList.get(i).getLevel()+ " "+
                            scoreList.get(i).getScore()+ "\n");
                }
                    
            }
            for(; i < scoreList.size() -1; i++){
                labelsm1.setText(labelsm3.getText()+ " "+
                            scoreList.get(i).getName()+ " "+
                            scoreList.get(i).getResources()+ " "+
                            scoreList.get(i).getLevel()+ " "+
                            scoreList.get(i).getScore()+ "\n");
            }
        }
        
    }
    
    public void exitGame(){
        System.exit(1);
    }

    
}
