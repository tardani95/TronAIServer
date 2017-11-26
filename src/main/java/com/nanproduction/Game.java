package com.nanproduction;


import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.webbitserver.WebSocketConnection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Game extends Thread {

    public static final int MAP_SIZE_X = 50;
    public static final int MAP_SIZE_Y = 35;
    private static Game instance = new Game();
    //private static final int NUM_OF_PLAYERS = 4;
    public static final Color[] COLORS = new Color[]{Color.BLUE, Color.GREEN, Color.YELLOW, Color.BROWN, Color.BLACK, Color.CYAN, Color.BEIGE};
    private GameState gameState;

    private static Map<WebSocketConnection,Player> players = new ConcurrentHashMap<>();
    private Achievement achievement;

    synchronized public void addKeyEvent(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < 4; j++) {
                if (keyCode == Player.KEY_CUTS[j][players.get(i).getId()]) {
                    players.get(i).setKeyCode(keyCode);
                    break;
                }
            }
        }
    }

    /*synchronized public void addKeyCode(String key) {
        KeyCode keyCode = KeyCode.valueOf(key);
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < 4; j++) {
                if (keyCode == Player.KEY_CUTS[j][players.get(i).getId()]) {
                    players.get(i).setKeyCode(keyCode);
                    break;
                }
            }
        }
    }*/


    public Achievement getAchievement() {
        return achievement;
    }

    private MainWindowController controller;

    private List<Point> freeCoords;

    private Game() {
    }

    public GameState getGameState() {
        return gameState;
    }

    public static Game getInstance() {
        return instance;

    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public void init(MainWindowController controller) {
        this.controller = controller;
        freeCoords = new ArrayList<>();
        for (int i = 0; i < MAP_SIZE_X; i++) {
            for (int j = 0; j < MAP_SIZE_Y; j++) {
                freeCoords.add(new Point(i, j));
            }
        }

        achievement = new Achievement(getRandFreeCoord());
        gameState=GameState.WAITING_FOR_PLAYERS;
        start();
    }

    public void addNewPlayer(WebSocketConnection connection){
        Player player=new Player(getRandFreeCoord(), players.size());
        players.put(connection,player);
    }

    public void removePlayer(WebSocketConnection connection){
        players.remove(connection);
    }

    public Player getPlayer(WebSocketConnection connection){
        return players.get(connection);
    }

    public int getNumOfPlayers(){return players.size();}

   /* public void initPlayer(){
        for (int i = 0; i < players.size(); i++) {
            players.add(new Player(getRandFreeCoord(), i));
        }
    }*/


    public Point getRandFreeCoord() {
        Random rand = new Random();
        int index = rand.nextInt(freeCoords.size());
        Point point = freeCoords.get(index);
        freeCoords.remove(index);
        return point;
    }

    /*public List<Player> getPlayers() {
        return players;
    }*/

    private void movePlayers() {
        for (Player player : players.values()) {
            player.move();
        }
    }

    private boolean readyToPlay(){
        for(Player player:players.values() ){
            if(!player.isReady()){return false;}
        }
        return true;
    }

    public void run() {

        while (!readyToPlay() || players.size()<2){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            controller.drawPlayers(players.values());
        }

        gameState=GameState.PLAYING;

        while (players.size() > 1) {
            controller.drawPlayers(players.values());
            for (Player player : players.values()) {
                player.stepPlayer();
            }
            movePlayers();
            for (Player player1:players.values()) {
                for (Player player2:players.values()) {
                    if (player1.getId()==player2.getId()) {
                        continue;
                    }

                    player1.collisionDetection(player2);
                }
            }
            for (Map.Entry<WebSocketConnection,Player> entry:players.entrySet()) {
                Player player = entry.getValue();
                if (player.isGameOver()) {
                    player.deletePlayer(this);
                    players.remove(entry.getKey());
                    //players.remove(player);
                }
            }


            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            controller.setScore(players.values());
        }
        controller.endingGame();
        stop();
    }

    public void removeFreeCoord(Point point) {
        freeCoords.remove(point);
    }

    public void addFreeCoord(Point point) {
        freeCoords.add(point);
    }
}
