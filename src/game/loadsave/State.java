/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.loadsave;

import game.engine.GameState;
import game.engine.characters.Monster;
import game.engine.characters.Tower;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author miner
 */
public class State implements Function<GameState> {

    @Override
    public void save(GameState game) throws IOException{
        java.io.File file = new java.io.File("src/game/loadsave/state/savedGame.txt");
        java.io.PrintWriter output = new java.io.PrintWriter(file);
        output.println(game.getName()+ " "+
                game.getResources()+ " "+
                game.getLevel()+ " "+
                game.getScore()+ " "+
                game.getLives()+ " "+
                game.getDiffculty());
        output.println(game.getMonstersAlive().size());
        for (Monster monster : game.getMonstersAlive()) {
            output.print(monster.getNodeDirection() + " "+ monster.isMoveX()+" ");
            output.println(monster.getView().getCenterX() + " " +
                    monster.getView().getCenterY());
        }
        output.println(game.getPlayerTowers().size());
        for (Tower tower : game.getPlayerTowers()) {
            output.println(tower.getCoords().getTileX() + " " +
                    tower.getCoords().getTileY());
        }
        output.close();
    }

    @Override
    public GameState load() throws IOException{
        Random rand = new Random();
        GameState game ;
        Monster monster ;
        Tower tower;
        int movement=0;
        int health =0;
        double monsterX,monsterY;
        int towerX,towerY;
        int node;
        boolean moveX;
        game = GameState.getNewGame();
       
        java.io.File file = new java.io.File("src/game/loadsave/state/savedGame.txt");
        Scanner input = new Scanner(file);
        game.setName(input.next());
        game.setResources(Integer.parseInt(input.next()));
        game.setLevel(Integer.parseInt(input.next()));
        game.setScore(Integer.parseInt(input.next()));
        game.setLives(Integer.parseInt(input.next()));
        game.setDiffculty(Integer.parseInt(input.next()));
        int monsterCout = Integer.parseInt(input.next());
        for(int i=0 ; i < monsterCout ;i++)
        {
            movement = rand.nextInt(game.getLevel()) +game.getDiffculty();
            health = rand.nextInt(game.getLevel()) +game.getDiffculty();
            node=Integer.parseInt(input.next());
            moveX = Boolean.parseBoolean(input.next());
            monsterX=Double.parseDouble(input.next());
            monsterY=Double.parseDouble(input.next());
            monster = new Monster(health, movement);
            monster.setPosition(monsterX, monsterY);
            monster.setNodeDirection(node);
            monster.setMoveX(moveX);
            game.getMonstersAlive().add(monster);
        }
        int towerCout = Integer.parseInt(input.next());
        for(int i=0 ; i < towerCout ;i++)
        {
            towerX = Integer.parseInt(input.next());
            towerY = Integer.parseInt(input.next());
            game.addTower(new Tower(towerX, towerY));
        }
        
        return game;
    }
    
}
