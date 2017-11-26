package com.nanproduction;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    public static final String[] COLORS = new String[]{"#000099", "#339933", "#ffcc00", "#663300", "#000000", "#33ccff", "#ff9966"};
    private GameStateEnum gameState;

    private static Map<WebSocketConnection,Player> players = new ConcurrentHashMap<>();
    private static List<WebSocketConnection> watchers=new ArrayList<>();
    private Achievement achievement;



    public Achievement getAchievement() {
        return achievement;
    }


    private List<Point> freeCoords;

    private Game() {
    }

    public GameStateEnum getGameState() {
        return gameState;
    }

    public static Game getInstance() {
        return instance;

    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public void init() {
        freeCoords = new ArrayList<>();
        for (int i = 0; i < MAP_SIZE_X; i++) {
            for (int j = 0; j < MAP_SIZE_Y; j++) {
                freeCoords.add(new Point(i, j));
            }
        }

        achievement = new Achievement(getRandFreeCoord());
        gameState= GameStateEnum.WAITING_FOR_PLAYERS;
        start();
    }

    public void addNewPlayer(WebSocketConnection connection){
        Player player=new Player(getRandFreeCoord(), players.size());
        players.put(connection,player);
    }

    public void addWatcher(WebSocketConnection connection){
        watchers.add(connection);
    }

    public void removeWatcher(WebSocketConnection connection){
        watchers.remove(connection);
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

    private void refreshGUI(){
        JsonObject json=new JsonObject();
        json.add("players", new Gson().toJsonTree(players.values()));
        json.add("achievements", new Gson().toJsonTree(achievement));
        String jsonString=new Gson().toJson(json);
        for(WebSocketConnection watcher:watchers){
            watcher.send(jsonString);
        }
    }

    public void run() {

        while (!readyToPlay() || players.size()<2){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            refreshGUI();
            //controller.drawPlayers(players.values());
        }

        gameState= GameStateEnum.PLAYING;

        while (players.size() > 1) {
            refreshGUI();
            //controller.drawPlayers(players.values());
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
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //controller.setScore(players.values());
        }

        gameState= GameStateEnum.ENDING;
        refreshGUI();
        //controller.endingGame();
        stop();
    }

    public void removeFreeCoord(Point point) {
        freeCoords.remove(point);
    }

    public void addFreeCoord(Point point) {
        freeCoords.add(point);
    }
}
