package game.engine;

/*
    keeps track of the games state, only one state
    can be active at a time
 */

import game.engine.characters.Monster;
import game.engine.characters.Tower;
import java.io.Serializable;
import java.util.ArrayList;


public class GameState implements Serializable {

    //game state flags
    public static final int IS_RUNNING = 1;    //game is active
    public static final int IS_PAUSED = 2;     //game is temporarily not active
    public static final int IS_STOPPED = 3;    //game is over and halted

    private static GameState playerGame;
    private int state;
    private int resources;                  //used for buying and upgrading tower
    private int level;                      //represents the current wave of monsters that are being spawned
    private int score;                      //score is calculated by awarding kill points and perfect level points
    private int lives;                     //numbers of monster escapes allowed before game ends
    private int diffculty; 

    private ArrayList<Tower> playerTowers;  //holds all tower references on the map
    private ArrayList<Monster> monstersAlive; //holds monster references
    private String name;

    //CONSTRUCTORS
    private GameState(){
        state = IS_RUNNING;
        resources = 75;
        level = 0;
        score = 0;
        lives = 20;
        playerTowers = new ArrayList<Tower>();
        monstersAlive = new ArrayList<Monster>();
    }
    
    public GameState(String name, int resources, int level, int score){
        this.name = name;
        this.resources = resources;
        this.level = level;
        this.score = score;
    }

    //Overwrites current Game State
    public static GameState getNewGame(){
        playerGame = new GameState();
        return playerGame;
    }

    //Throws null exception if new game is never created
    public static GameState getGame() throws NullPointerException{
            return playerGame;
    }

    //SETTERS
    public void setResources(int resources){
        this.resources = resources;
    }
    public void setLevel(int level){
        this.level = level;
    }
    public void setScore(int score){
        this.score = score;
    }
    public void setLives(int lives){
        this.lives = lives;
    }
    public void setState(int state) {
        this.state = state;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDiffculty(int diffculty) {
        this.diffculty = diffculty;
    }

    //GETTERS
    public int getResources(){
        return resources;
    }
    public int getLevel(){
        return level;
    }
    public int getScore(){
        return score;
    }
    public int getLives() {
        return lives;
    }
    public int getState() {
        return state;
    }
    
    public String getName() {
        return name;
    }

    public int getDiffculty() {
        return diffculty;
    }

    

    public boolean isPaused(){
        if(state == IS_PAUSED){
            return true;
        }
        return false;
    }

    public boolean isRunning(){
        if(state == IS_RUNNING){
            return true;
        }
        return false;
    }

    public boolean isStopped(){
        if(state == IS_STOPPED){
            return true;
        }
        return false;
    }

    public ArrayList<Tower> getPlayerTowers(){
        return playerTowers;
    }

    public ArrayList<Monster> getMonstersAlive() {
        return monstersAlive;
    }

    public void addMonster(Monster monster){monstersAlive.add(monster);}
    public void addTower(Tower tower){playerTowers.add(tower);}
    public void removeMonster(Monster monster){monstersAlive.remove(monster);}
    public void removeTower(Tower tower){playerTowers.remove(tower);}
}
