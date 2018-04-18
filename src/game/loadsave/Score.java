/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.loadsave;

import game.engine.GameState;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author miner
 */
public class Score implements Function<ArrayList<GameState>>{

    @Override
    public void save(ArrayList<GameState> gameList) throws Exception{
        GameState currentGameState = gameList.get(0);
        ArrayList<GameState> sortedGameScore = new ArrayList();
        try{
            ArrayList<GameState> preData = load(); 
            if(preData.isEmpty())
                sortedGameScore.add(gameList.get(0));
            else
                for (GameState gameState : preData.subList(0, preData.size()-1)) {
                    if(!gameList.isEmpty() && gameState.getScore() < gameList.get(0).getScore()){
                        sortedGameScore.add(gameList.get(0));
                        sortedGameScore.add(gameState);
                        gameList.clear();
                    }
                    else
                        sortedGameScore.add(gameState);
                }
            if(!gameList.isEmpty())sortedGameScore.add(gameList.get(0));
        }catch(Exception e){
            System.out.println("Save Exception on Score: " + e.getMessage());
        }
        if(sortedGameScore.size() > 10)sortedGameScore.subList(10, sortedGameScore.size()).clear();
        sortedGameScore.add(currentGameState);
//        for (GameState gameState : sortedGameScore) {
//            System.out.println(gameState);
//        }
        java.io.File file = new java.io.File("src/game/loadsave/state/scoreList.txt");
        java.io.PrintWriter output = new java.io.PrintWriter(file);
        for (GameState gameState : sortedGameScore) {
            output.println(gameState.getName()+ " "+ gameState.getResources()+ " "+ gameState.getLevel()+ " "+ gameState.getScore());
        }
        output.close();
    }

    @Override
    public ArrayList<GameState> load() throws Exception{
        java.io.File file = new java.io.File("src/game/loadsave/state/scoreList.txt");
        Scanner input = new Scanner(file);
        ArrayList<GameState> data = new ArrayList();
        while(input.hasNext()){
            data.add(new GameState(input.next(),
                    Integer.parseInt(input.next()),
                    Integer.parseInt(input.next()),
                    Integer.parseInt(input.next())));
        }
//        for (GameState gameState : data) {
//            System.out.println(gameState.getName()+ " "+ gameState.getResources()+ " "+ gameState.getLevel()+ " "+ gameState.getScore());
//        }
        input.close();
        return data;
    }
    
}
