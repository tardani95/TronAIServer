package com.nanproduction;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyCode.*;

enum eDirection {STOP, LEFT, RIGHT, UP, DOWN};

public class Player{

    private boolean gameOver;
    private Point head;
    private List<Point> tail;
    private eDirection dir;
    private int score;
    private KeyEvent keyEvent;

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

    Player(Point head) {
        gameOver = false;


        //head.initRand();
        this.head = head;

        tail = new ArrayList<>();
        dir = eDirection.STOP;

        score = 0;
    }

    public void stepPlayer(KeyEvent keyEvent) throws IOException {
        if(keyEvent==null) return;
        KeyCode keyPressed = keyEvent.getCode();
        if (keyPressed != null) {
            switch (keyPressed) {
                case A:
                    dir = eDirection.LEFT;
                    break;
                case D:
                    dir = eDirection.RIGHT;
                    break;
                case W:
                    dir = eDirection.UP;
                    break;
                case S:
                    dir = eDirection.DOWN;
                    break;
                case X:
                    gameOver = true;// kilép
                    break;
                default:
                    break;
            }
        }
    }

    public int getScore() {
        return score;
    }

    void move(Game game) {
        tail.add(0, new Point(head.getX(),head.getY()));
        head.move(dir);
        if (head.outOfBorder()) {
            gameOver = true;
            for (Point point : tail) {
                game.addFreeCoord(point);
            }
        }
        game.removeFreeCoord(head);
        if (game.getAchievement().getCoord().equals(head)) {
            game.setAchievement(new Achievement(game.getRandFreeCoord()));
            score++;
        } else {
            game.addFreeCoord(tail.get(tail.size() - 1));
            tail.remove(tail.size() - 1);
        }
        for(Point point:tail){
            if(head.equals(point)){
                gameOver=true;
            }
        }
    }


}
