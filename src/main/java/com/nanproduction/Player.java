package com.nanproduction;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

enum eDirection {STOP, LEFT, RIGHT, UP, DOWN};

public class Player implements Handler{

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

    public void stepPlayer() throws IOException {
        if(keyEvent==null) return;
        String keyPressed = keyEvent.getCharacter();
        if (keyPressed != null) {
            switch (keyPressed.charAt(0)) {
                case 'a':
                    dir = eDirection.LEFT;
                    break;
                case 'd':
                    dir = eDirection.RIGHT;
                    break;
                case 'w':
                    dir = eDirection.UP;
                    break;
                case 's':
                    dir = eDirection.DOWN;
                    break;
                case 'x':
                    gameOver = true;// kilép
                    break;
                default:
                    break;
            }
        }
    }

    void move(Game game) {
        tail.add(0, head);
        head.move(dir);
        if (head.outOfBorder()) {
            gameOver = true;
            for (Point point : tail) {
                game.addFreeCoord(point);
            }
        }
        game.removeFreeCoord(head);
        if (game.getAchievement().getCoord().equals(head)) {
            score++;
        } else {
            game.addFreeCoord(tail.get(tail.size() - 1));
            tail.remove(tail.size() - 1);
        }
    }

    @Override
    public void giveKeyEvent(KeyEvent keyEvent) {
        this.keyEvent=keyEvent;
    }

    @Override
    public javafx.scene.input.KeyEvent getKeyEvent() {
        return keyEvent;
    }
}
