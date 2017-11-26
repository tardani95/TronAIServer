package com.nanproduction;


import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Game extends Thread {

    public static final int MAP_SIZE_X = 50;
    public static final int MAP_SIZE_Y = 35;
    private static Game instance = new Game();
    private static final int NUM_OF_PLAYERS = 4;
    private static final Color[] COLORS = new Color[]{Color.BLUE, Color.GREEN, Color.YELLOW, Color.BROWN};

    private List<Player> players;
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

    synchronized public void addKeyCode(String key) {
        KeyCode keyCode = KeyCode.valueOf(key);
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < 4; j++) {
                if (keyCode == Player.KEY_CUTS[j][players.get(i).getId()]) {
                    players.get(i).setKeyCode(keyCode);
                    break;
                }
            }
        }
    }


    public Achievement getAchievement() {
        return achievement;
    }

    private MainWindowController controller;

    private List<Point> freeCoords;

    private Game() {
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
        players = new ArrayList<>();
        for (int i = 0; i < NUM_OF_PLAYERS; i++) {
            players.add(new Player(getRandFreeCoord(), i));
            players.get(i).setColor(COLORS[i]);
        }
        achievement = new Achievement(getRandFreeCoord());


        start();
    }


    public Point getRandFreeCoord() {
        Random rand = new Random();
        int index = rand.nextInt(freeCoords.size());
        Point point = freeCoords.get(index);
        freeCoords.remove(index);
        return point;
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void movePlayers() {
        for (Player player : players) {
            player.move();
        }
    }

    public void run() {
        while (players.size() > 1) {
            controller.drawPlayers();
            for (Player player : players) {
                player.stepPlayer();
            }
            movePlayers();
            for (int i = 0; i < players.size(); i++) {
                for (int j = 0; j < players.size(); j++) {
                    if (i == j) {
                        continue;
                    }

                    players.get(i).collisionDetection(players.get(j));
                }
            }
            for (Iterator<Player> iter = players.iterator(); iter.hasNext(); ) {
                Player player = iter.next();
                if (player.isGameOver()) {
                    player.deletePlayer(this);
                    iter.remove();
                    //players.remove(player);
                }
            }


            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            controller.setScore(players);
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
