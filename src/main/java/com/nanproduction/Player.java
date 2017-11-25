package com.nanproduction;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import javax.swing.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyCode.*;

enum eDirection {STOP, LEFT, RIGHT, UP, DOWN};


public class Player {

    public static final KeyCode[][] KEY_CUTS = {
            {A, LEFT},
            {D, RIGHT},
            {W, UP},
            {S, DOWN}
    };

    private int id;
    private boolean gameOver;

    public Color getColor() {
        return color;
    }

    private Point head;
    private List<Point> tail;
    private eDirection dir;
    private int score;
    private Color color;
    private KeyCode keyCode;


    public void setKeyCode(KeyCode keyCode) {
        this.keyCode = keyCode;
    }



    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Point getHead() {
        return head;
    }

    public List<Point> getTail() {
        return tail;
    }

    public eDirection getDir() {
        return dir;
    }

    Player(Point head, int id) {
        gameOver = false;
        this.id = id;

        //head.initRand();
        this.head = head;

        tail = new ArrayList<>();
        dir = eDirection.STOP;

        score = 0;
    }

    synchronized public void stepPlayer() {
        if (keyCode == null) return;
        if(keyCode==KEY_CUTS[0][id]){
            dir = eDirection.LEFT;
            return;
        }
        if(keyCode==KEY_CUTS[1][id]){
            dir = eDirection.RIGHT;
            return;
        }
        if(keyCode==KEY_CUTS[2][id]){
            dir = eDirection.UP;
            return;
        }
        if(keyCode==KEY_CUTS[3][id]){
            dir = eDirection.DOWN;
            return;
        }
//        switch (keyCode) {
//            case KEY_CUTS[id][0]:
//                dir = eDirection.LEFT;
//                break;
//            case D:
//                dir = eDirection.RIGHT;
//                break;
//            case W:
//                dir = eDirection.UP;
//                break;
//            case S:
//                dir = eDirection.DOWN;
//                break;
//            case X:
//                gameOver = true;// kilép
//                break;
//            default:
//                break;
//        }
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    void move() {
        Game game = Game.getInstance();
        tail.add(0, new Point(head.getX(), head.getY()));
        head.move(dir);
        if (head.outOfBorder()) {
            gameOver = true;
        }
        game.removeFreeCoord(head);
        if (game.getAchievement().getCoord().equals(head)) {
            game.setAchievement(new Achievement(game.getRandFreeCoord()));
            score++;
        } else {
            game.addFreeCoord(tail.get(tail.size() - 1));
            tail.remove(tail.size() - 1);
        }
        for (Point point : tail) {
            if (head.equals(point)) {
                gameOver = true;

            }
        }
    }

    void deletePlayer(Game game) {
        for (Point point : tail) {
            game.addFreeCoord(point);
        }
    }

    void collisionDetection(Player other) {
        if (head.equals(other.getHead())) {
            if (id < other.getId()) {
                Game.getInstance().addFreeCoord(head);
            }
            gameOver = true;
        }
        for (Point point : other.getTail()) {
            if (head.equals(point)) {
                gameOver = true;
            }
        }
    }


}
