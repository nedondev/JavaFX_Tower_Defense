package game.engine;


import game.MenuNavigator;
import game.engine.characters.Monster;
import game.engine.characters.Projectile;
import game.engine.characters.Tower;
import game.loadsave.Score;
import game.loadsave.State;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import javafx.application.Platform;

/**
 * Responsible for all communications between user interface and underlying
 * frameworks. The initialize method starts the game loop when called through
 * creating or loading a game.
 */
public class GameManager {
    private  TileMap gameMap;                       // The painted map used as the backgrounds layer
    private  Group monsterLayer;                    // Used for the monster graphics
    private  GameState game;                        // Provides basic game states.
    private  Scene gameScene;                       // The main viewport
    private  GameController gameController;         // Handles fxml attributes (buttons and labels)
    private  AnimationTimer gameLoop; 
    

    /**
     * Initializes the game
     *
     * @throws java.io.IOException
     */
    public void initialize(String name,int difficulty) throws java.io.IOException{
        // Initializes the game state
        game = GameState.getNewGame();
        game.setName(name);
        game.setDiffculty(difficulty);

        // Generates the map with the given resolution
        gameMap = new TileMap(1280 ,800);

        // Creates gui hierarchy
        FXMLLoader loader = new FXMLLoader(MenuNavigator.GAMEUI);
        StackPane gamePane = new StackPane();
        Group tilemapGroup = new Group();
        monsterLayer = new Group();
        monsterLayer.getChildren().add(tilemapGroup);
        tilemapGroup.getChildren().add(gameMap);
        gamePane.getChildren().add(monsterLayer);

        // Opens stream to get controller reference
        Node gameUI = (Node)loader.load(MenuNavigator.GAMEUI.openStream());
        gamePane.getChildren().add(gameUI);
        gameScene = new Scene(gamePane);
        gameScene.getStylesheets().add(GameManager.class.getResource("res/menu/gamestyle.css").toExternalForm());
        gameController = loader.<GameController>getController();
        gameController.setGameManager(this, name);

        MenuNavigator.stage.setScene(gameScene);
        Monster.setPath(gameMap.getPath());
        startGameLoop();
    }

    public void initializeLoad () throws java.io.IOException, ClassNotFoundException{
       
        State state = new State();
        gameMap = new TileMap(1280 ,800);
        Monster.setPath(gameMap.getPath());
        game = state.load();
        // Generates the map with the given resolution
        gameMap = new TileMap(1280 ,800);

        // Creates gui hierarchy
        FXMLLoader loader = new FXMLLoader(MenuNavigator.GAMEUI);
        StackPane gamePane = new StackPane();
        Group tilemapGroup = new Group();
        monsterLayer = new Group();
        monsterLayer.getChildren().add(tilemapGroup);
        tilemapGroup.getChildren().add(gameMap);
        gamePane.getChildren().add(monsterLayer);

        // Opens stream to get controller reference
        Node gameUI = (Node)loader.load(MenuNavigator.GAMEUI.openStream());
        gamePane.getChildren().add(gameUI);
        gameScene = new Scene(gamePane);
        gameScene.getStylesheets().add(GameManager.class.getResource("res/menu/gamestyle.css").toExternalForm());
        gameController = loader.<GameController>getController();
        gameController.setGameManager(this, game.getName());
        for (Monster monster : game.getMonstersAlive()) {
             monsterLayer.getChildren().add(monster.getView());
        }
        for (Tower tower: game.getPlayerTowers()) {
             gameMap.setMapNode(tower.getX()/64,tower.getY()/64 , 7);
        }
       

        MenuNavigator.stage.setScene(gameScene);
        startGameLoop();
    }
    
    public  Scene getGameScene(){
        return gameScene;
    }


    /**
     * Attempts to create a tower at the tile clicked on
     * by the user.
     *
     * @param xCords
     * The clicked x coordinate
     * @param yCords
     * The clicked y coordinate
     */
    public void buyTower(double xCords , double yCords){
        // Convert the clicked coordinates to a tile coordinate
        int xTile = (int)(xCords / 64);
        int yTile = (int)(yCords / 64);

        // Verify the node is not occupied
        if(gameMap.nodeOpen(xTile,yTile)){
            // Verify the user can afford the tower
            if(game.getResources() > 49) {
                game.addTower(new Tower(xTile, yTile));
                game.setResources(game.getResources() - 50);
                gameMap.setMapNode(((int) (xCords / 64)), ((int) (yCords / 64)), 7);
            }
        }
    }

    /**
     * Gets the tower at a specific coordinate
     *
     * @param xCords
     * The clicked x coordinate passed from the controller
     * @param yCords
     * The clicked y coordinate passed from the controller
     * @return
     * The tower clicked or null if none exist
     */
    public Tower getTower(double xCords , double yCords){
        Coordinate clickedTiled = new Coordinate(xCords , yCords);
        // Find tower with matching coordinate
        for(Tower tower : game.getPlayerTowers()){
            if(tower.getCoords().equals(clickedTiled)){
                return tower;
            }
        }
        return null;
    }

    /**
     * Upgrades a user tower. Pauses the tower attacker service
     * which is resumed after a set time.
     *
     * @param tower
     * Tower selected for the upgrade.
     */
    public void upgradeTower(Tower tower){
        tower.getTowerAttacker().cancel();
        tower.upgradeTower();
        tower.getTowerAttacker().pollTower(tower.getUpgradeTime());
    }


    /**
     * Creates a monster.
     *
     * @param health
     * The health points for the monster. Increases as
     * the game progresses.
     */
    private void createMonster(int health, int movement){
        game.getMonstersAlive().add(new Monster(health, movement));
        monsterLayer.getChildren().add(game.getMonstersAlive().get(game.getMonstersAlive().size() - 1).getView());
    }

    /**
     * Updates monsters location on the path and removes any
     * monsters which reach the end of the path.
     */
    private void updateLocations(){
        if(!game.getMonstersAlive().isEmpty()){
            Iterator<Monster> monsters = game.getMonstersAlive().iterator();
            Monster monster;
            while(monsters.hasNext()) {
                monster = monsters.next();
                monster.updateLocation(1);
                if(monster.isPathFinished()){
                    removeMonster(monster);
                }
            }
        }
    }

    /**
     * Checks all towers for created projectiles which are then created
     * and animated by the main game loop.
     */
    private void createProjectiles(){
        Path projectilePath;
        PathTransition animation;
        for(Tower tower : game.getPlayerTowers()){
            for(Projectile projectile : tower.getProjectileList()){
                // Create animation path
                projectilePath = new Path(new MoveTo(projectile.getStartX() , projectile.getStartY()));
                projectilePath.getElements().add(new LineTo(projectile.getEndX() , projectile.getEndY()));
                animation = new PathTransition(Duration.millis(300) , projectilePath , projectile);

                // When the animation finishes, hide it and remove it
                animation.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        PathTransition finishedAnimation = (PathTransition) actionEvent.getSource();
                        Projectile finishedProjectile = (Projectile) finishedAnimation.getNode();

                        // Hide and remove from gui
                        finishedProjectile.setVisible(false);
                        monsterLayer.getChildren().remove(finishedProjectile);

                        // Remove monster if they are dead
                        if(finishedProjectile.getTarget().isDead()){
                            removeMonster(finishedProjectile.getTarget());
                        }
                    }
                });
                monsterLayer.getChildren().add(projectile);
                animation.play();
            }
            tower.getProjectileList().clear();
        }

    }


    /**
     * Updates the labels associated with the game state
     *
     * @param timer
     * Time before the next wave of monsters will be spawned.
     */
    private void updateLabels(int timer){
            gameController.updateLabels(
                Integer.toString(game.getLevel()) ,
                Integer.toString(game.getLives()) ,
                Integer.toString(game.getResources()) ,
                Integer.toString(game.getScore()) ,
                Integer.toString(timer)
        );
    }


    /**
     * Stops the game. Used when the player chooses to quit
     * or the losing conditions are met.
     */
    public void stopGame()throws java.io.IOException{
        pauseGame();
        game.setState(GameState.IS_STOPPED);
        gameLoop.stop();
        if(game.getLives() == 0){
            ArrayList gameList = new ArrayList();
            gameList.add(game);
            Score score = new Score();
            try{
                score.save(gameList);
            }catch(Exception e){
                System.out.println("Save Exception on GameManager: " + e.getMessage());
            }
            
            FXMLLoader loader = new FXMLLoader(MenuNavigator.GAMEOVERUI);
            StackPane gamePane = new StackPane();
            Node gameUI = (Node)loader.load(MenuNavigator.GAMEOVERUI.openStream());
            gamePane.getChildren().add(gameUI);
            gameScene = new Scene(gamePane);
            gameScene.getStylesheets().add(GameManager.class.getResource("res/menu/gameover.css").toExternalForm());
            MenuNavigator.stage.setScene(gameScene);
        }
    }

    /**
     * Used to freeze the game and is called before the
     * game is stopped.
     */
    public void pauseGame(){
        game.setState(GameState.IS_PAUSED);
        gameLoop.stop();
    }
    /**
     * Called when the game is started or the
     * game returns from a paused state.
     */
    public void resumeGame(){
        game.setState(GameState.IS_RUNNING);
        gameLoop.start();
    }
    
    public void saveGame() {
        State state = new State();
        try{
            state.save(game);
        }catch(IOException e){
            e.printStackTrace();
        }
        resumeGame();
        /*
        java.io.File file = new java.io.File("src/game/loadsave/state/savegame5.Ojb");
        try {
               
                FileOutputStream f = new FileOutputStream(file);
                 
		ObjectOutputStream o = new ObjectOutputStream(f);
                System.out.println("555");
		o.writeObject(game);
                
		o.close();
                f.close();
		} catch(IOException e){
                    e.printStackTrace();
                }*/
    }

    /**
     * Removes a monster from the graphical interface and from the reference
     * list. The player is rewarded if they defeated the monster or punished
     * if the monster finished the path.
     *
     * @param monster
     * The monster to remove from the game.
     */
    private synchronized void removeMonster(Monster monster){
        // Punish player
        if (monster.isPathFinished()){
            game.setLives((game.getLives()) - 1);
        }
        // Reward player
        else{
            game.setResources((game.getResources()) + monster.getReward());
            game.setScore(game.getScore() + (monster.getReward() * game.getLevel()));
        }

        // Remove monsters graphic and reference
        monster.getView().setVisible(false);
        game.getMonstersAlive().remove(monster);

    }

    /**
     * GAME LOOP
     *
     * Responsible for all graphical updates, including playing
     * animations and updating monster locations.
     */
    private void startGameLoop() {
        final LongProperty secondUpdate = new SimpleLongProperty(0);
        final LongProperty fpstimer = new SimpleLongProperty(0);
        final AnimationTimer timer = new AnimationTimer() {
            int timer = 3;
            int movement;
            int health;
            Random rand = new Random();
            long moreMon = -1;

            @Override
            public void handle(long timestamp) {

                // Times each second
                //System.out.println(timestamp / 100000000+" "+secondUpdate.get());
                if (timestamp/ 1000000000 != secondUpdate.get() ) {
                    timer--;
                    if(timer > 5) {
                        
                        health = rand.nextInt(game.getLevel()) +game.getDiffculty(); 
                        if(health % 2 == 0)moreMon = timestamp/ 100000000 + 2;
                        movement = rand.nextInt(game.getLevel()) +game.getDiffculty();
                        if(movement > 15) movement = 15;
                        
                        createMonster(health, movement);
                    }
                    else if(timer <= 0){
                        game.setLevel(game.getLevel() + 1);
                        timer = 15;
                    }
                }
                if(moreMon == timestamp/ 100000000){
                    health = rand.nextInt(game.getLevel()) +5; 
                    movement = rand.nextInt(game.getLevel()) +game.getDiffculty();
                    if(movement > 20) movement = 20;
                    
                    createMonster(health, movement);
                    moreMon = -1;
                }
                createProjectiles();
                if(game.getLives() == 0){
                    try{
                        stopGame();
                    }catch(java.io.IOException e){
                    }
                }
                if(timestamp / 10000000 != fpstimer.get()){
                    updateLocations();
                }
                fpstimer.set(timestamp / 10000000);
                secondUpdate.set(timestamp / 1000000000);
                updateLabels(timer);
            }
        };
        gameLoop = timer;
        gameLoop.start();
    }
}
