package com.nanproduction;


import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Thread {

    public static final int MAP_SIZE_X = 50;
    public static final int MAP_SIZE_Y = 35;
    private static Game instance=new Game();

    private Player player;
    private Achievement achievement;

    public void setKeyEvent(KeyEvent keyEvent) {
        this.keyEvent = keyEvent;
    }

    private static KeyEvent keyEvent;



    public Achievement getAchievement() {
        return achievement;
    }

    private MainWindowController controller;

    private List<Point> freeCoords;

    private Game(){}

    public static Game getInstance(){return instance;}

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

        player = new Player(getRandFreeCoord());
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

    public Player getPlayer() {
        return player;
    }

    private void movePlayers(){


        player.move(this);


    }

    public void run() {
        while (!player.isGameOver()) {
            controller.drawPlayers();
            try {
                player.stepPlayer(keyEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            movePlayers();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            controller.setScore(player.getScore());
        }
        controller.endingGame();
        stop();
    }

    public void removeFreeCoord(Point point){
        freeCoords.remove(point);
    }

    public  void addFreeCoord(Point point){
        freeCoords.add(point);
    }
}
