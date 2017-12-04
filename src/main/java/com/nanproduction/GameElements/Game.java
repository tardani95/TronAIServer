package com.nanproduction.GameElements;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.scene.paint.Color;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Game {

    public static final int MAP_SIZE_X = 50;
    public static final int MAP_SIZE_Y = 35;
    private static Game instance = new Game();
    //private static final int NUM_OF_PLAYERS = 4;
    public static final String[] COLORS = new String[]{"#000099ff", "#339933ff", "#ffcc00ff", "#663300ff", "#000000ff", "#33ccffff", "#ff9966ff"};
    private GameStateEnum gameState;
    private List<Point> freeCoords;
    public static boolean STOP = false;
    public long timeInMillis;


    private static Map<WebSocketConnection, Player> players = new ConcurrentHashMap<>();
    private static List<WebSocketConnection> watchers = new ArrayList<>();
    private Achievement achievement;

    private int numOfActivePlayers;

    private Thread thread;


    private Game() {
    }

    public Achievement getAchievement() {
        return achievement;
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

    private boolean sameColor(String color1, String color2,int threshold){
        if(Integer.parseInt(color1.substring(1,3),16)/threshold!=Integer.parseInt(color2.substring(1,3),16)/threshold){
            return false;
        }
        if(Integer.parseInt(color1.substring(3,5),16)/threshold!=Integer.parseInt(color2.substring(3,5),16)/threshold){
            return false;
        }
        if(Integer.parseInt(color1.substring(5,7),16)/threshold!=Integer.parseInt(color2.substring(5,7),16)/threshold){
            return false;
        }
        return true;
    }

    private String randomColor(){
        Random random = new Random();
        Color color = new Color(random.nextDouble(),random.nextDouble(), random.nextDouble(), 1.0);
        return "#"+color.toString().substring(2);
    }


    public void init() {
        players = new ConcurrentHashMap<>();
        thread = new Thread(new GameThread());
        thread.start();
    }

    synchronized public void addNewPlayer(WebSocketConnection connection, String jsonString) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        String name = jsonObject.get("name").getAsString();
        String color = jsonObject.get("color").getAsString();



        boolean hasSameColor=false;
        do {
            hasSameColor=false;
            if(sameColor(color,"#"+ Color.RED.toString().substring(2),10)){
                color=randomColor();
            }
            if(sameColor(color,"#FFFFFF",10)){
                color=randomColor();
            }
            for (Player player:players.values()){
                if(sameColor(color,player.getColor(),10)){
                    color=randomColor();
                    hasSameColor=true;
                    break;
                }
            }
        }while (hasSameColor);

        Player player = new Player(getRandFreeCoord(), connection.hashCode(), color, name);
        players.put(connection, player);
        if(!watchers.contains(connection)){
            watchers.add(connection);
        }
        numOfActivePlayers++;
    }

    synchronized public void readdNewPlayer(WebSocketConnection connection) {
        Player player=players.get(connection);
        player.reinitPlayer(getRandFreeCoord(), connection.hashCode());
        player.setReady(true);
        players.put(connection,player);
        numOfActivePlayers++;
    }

    public void addWatcher(WebSocketConnection connection) {
        watchers.add(connection);
    }

    public void removeWatcher(WebSocketConnection connection) {
        watchers.remove(connection);
    }

    synchronized public void removePlayer(WebSocketConnection connection) {
        int playerId=players.get(connection).getId();
        for(Player player:players.values()){
            if(player.getId()>playerId){
                player.decreaseId();
            }
        }
        players.remove(connection);
        numOfActivePlayers--;
    }

    public Player getPlayer(WebSocketConnection connection) {
        return players.get(connection);
    }

    public int getNumOfPlayers() {
        return players.size();
    }

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

    private void printDiedPlayer(Player player){
        System.out.println("Player "+player.getId()+" is died, more " + (numOfActivePlayers-1));
    }

    private void killPlayers(){
        for(Player player:players.values()) {
            player.killPlayer();
        }
    }

    private void movePlayers() {
        for (Player player : players.values()) {
            if(!player.isGameOver()) {
                player.move();
                if(player.isGameOver()){
                    printDiedPlayer(player);
                    numOfActivePlayers--;
                }
            }
        }
    }

    synchronized private boolean readyToPlay() {
        if(numOfActivePlayers>1) {
            if (System.currentTimeMillis() - timeInMillis > 20000) {
                for (WebSocketConnection connection : players.keySet()) {
                    if (!players.get(connection).isReady()) {
                        players.remove(connection);
                        numOfActivePlayers--;
                    }
                }
                timeInMillis = System.currentTimeMillis();
                return true;
            }
        }else{
            timeInMillis=System.currentTimeMillis();
        }

        for (Player player : players.values()) {
            if (!player.isReady()) {
                return false;
            }
        }
        return true;
    }

    private void refreshGUI() {
        JsonObject json = new JsonObject();
        json.add("players", new Gson().toJsonTree(players.values()));
        json.add("achievements", new Gson().toJsonTree(achievement));
        String jsonString = new Gson().toJson(json);
        for (WebSocketConnection watcher : watchers) {
            watcher.send(jsonString);
        }
//        for (WebSocketConnection player : players.keySet()) {
//            player.send(jsonString);
//        }
    }


    public void removeFreeCoord(Point point) {
        freeCoords.remove(point);
    }

    public void addFreeCoord(Point point) {
        freeCoords.add(point);
    }

    public void stopThread() {
        thread.stop();
    }

    public class GameThread extends Thread {

        private int tmpNumOfActivePlayers;
        private int deathValue;

        private void initThread(){
            killPlayers();
            numOfActivePlayers=0;
            tmpNumOfActivePlayers =0;
            timeInMillis=System.currentTimeMillis();
            freeCoords = new ArrayList<>();
            for (int i = 0; i < MAP_SIZE_X; i++) {
                for (int j = 0; j < MAP_SIZE_Y; j++) {
                    freeCoords.add(new Point(i, j));
                }
            }

            achievement = new Achievement(getRandFreeCoord());
            gameState = GameStateEnum.WAITING_FOR_PLAYERS;
            System.out.println("New Game - Waiting for players");
        }

        @Override
        public void run() {
            while (!STOP) {
                initThread();
                while (!readyToPlay() || players.size() < 2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    refreshGUI();
                    //controller.drawPlayers(players.values());
                }

                gameState = GameStateEnum.PLAYING;
                System.out.println("Game is starting");

                while (numOfActivePlayers > 1) {
                    refreshGUI();
                    tmpNumOfActivePlayers =numOfActivePlayers;
                    //controller.drawPlayers(players.values());
                    for (Player player : players.values()) {
                        if (!player.isGameOver()) {
                            player.stepPlayer();
                        }
                    }
                    movePlayers();
                    for (Player player1 : players.values()) {
                        if (!player1.isGameOver()) {
                            for (Player player2 : players.values()) {
                                if (!player2.isGameOver()) {
                                    if (player1.getId() == player2.getId()) {
                                        continue;
                                    }

                                    player1.collisionDetection(player2);
                                    if (player1.isGameOver()) {
                                        printDiedPlayer(player1);
                                        numOfActivePlayers--;
                                    }
                                }
                            }
                        }
                    }
                /*for (Map.Entry<WebSocketConnection, Player> entry : players.entrySet()) {
                    Player player = entry.getValue();
                    if (player.isGameOver()) {
                        player.deletePlayer(Game.this);
                        players.remove(entry.getKey());
                        //players.remove(player);
                    }
                }*/


                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(tmpNumOfActivePlayers >numOfActivePlayers){
                        deathValue=tmpNumOfActivePlayers-numOfActivePlayers;
                        for (Player player:players.values()){
                            if(!player.isGameOver()){
                                player.increaseScore(deathValue);
                            }
                        }
                    }


                    //controller.setScore(players.values());
                }

                gameState = GameStateEnum.ENDING;
                System.out.println("Game is ended");
                refreshGUI();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //controller.endingGame();
            thread.stop();
        }
    }
}
