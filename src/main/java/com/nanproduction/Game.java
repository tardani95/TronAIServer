package com.nanproduction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Thread {

    public static final int MAP_SIZE_X = 50;
    public static final int MAP_SIZE_Y = 35;

    private Player player;
    private Achievement achievement;

    public Achievement getAchievement() {
        return achievement;
    }

    private MainWindowController controller;

    private List<Point> freeCoords;

    public Game(MainWindowController controller) {
        this.controller = controller;
        init();
        start();
    }

    private void init() {
        freeCoords = new ArrayList<>();
        for (int i = 0; i < MAP_SIZE_X; i++) {
            for (int j = 0; j < MAP_SIZE_Y; j++) {
                freeCoords.add(new Point(i, j));
            }
        }

        player = new Player(getRandFreeCoord());
        achievement = new Achievement(getRandFreeCoord());
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
                player.stepPlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            movePlayers();
        }
    }

    public void removeFreeCoord(Point point){
        freeCoords.remove(point);
    }

    public  void addFreeCoord(Point point){
        freeCoords.add(point);
    }
}
