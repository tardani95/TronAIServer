package com.nanproduction;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.event.KeyListener;

public class MainWindowController {

    public static final int CELL_SIZE = 20;
    public static final double BODY_SCALE = 0.8;

    private Game game;




    @FXML
    public Canvas canvas;
    private static GraphicsContext gc;

    public MainWindowController() {
        System.out.println("MainWindowController() called");

    }

    @FXML
    void initialize(){
        System.out.println("Initialize() called");
        gc=canvas.getGraphicsContext2D();

        initWindow();
        drawBase();
        game=new Game(this);

    }

    private void installEventHandler(final Node keyNode) {
        // handler for enter key press / release events, other keys are
        // handled by the parent (keyboard) node handler
        final EventHandler<KeyEvent> keyEventHandler =
                new EventHandler<KeyEvent>() {
                    public void handle(final KeyEvent keyEvent) {
                        if (keyEvent.getCode() == KeyCode.ENTER) {
                            System.out.printf("ENTER");
                        }
                    }
                };

        keyNode.setOnKeyPressed(keyEventHandler);
        keyNode.setOnKeyReleased(keyEventHandler);
    }

    void drawPlayers()
    {
        gc.clearRect(0, 0, Game.MAP_SIZE_X, Game.MAP_SIZE_Y);
        drawBase();
        Player player=game.getPlayer();
        gc.fillRect(CELL_SIZE*player.getHead().getX(),CELL_SIZE*player.getHead().getY(),CELL_SIZE,CELL_SIZE); //Fill the Head

        gc.setFill(Color.RED); //achivemenet
        gc.fillRect(CELL_SIZE*game.getAchievement().getX(),CELL_SIZE*game.getAchievement().getY(),CELL_SIZE,CELL_SIZE);

        gc.setLineWidth(CELL_SIZE*BODY_SCALE);
        if(player.getTail().size()==0){
            return;
        }
        gc.strokeLine(CELL_SIZE*(player.getHead().getX()+0.5), CELL_SIZE*(player.getHead().getY()+0.5), CELL_SIZE*(player.getTail().get(0).getX()+0.5), CELL_SIZE*(player.getTail().get(0).getY()+0.5));
        for(int i=1; i<player.getTail().size(); i++){
           gc.strokeLine(CELL_SIZE*(player.getTail().get(i-1).getX()+0.5), CELL_SIZE*(player.getTail().get(i-1).getY()+0.5), CELL_SIZE*(player.getTail().get(i).getX()+0.5), CELL_SIZE*(player.getTail().get(i).getY()+0.5)); //draw body
        }



    }

    private void initWindow() {
        canvas.setHeight(CELL_SIZE *Game.MAP_SIZE_Y);
        canvas.setWidth(CELL_SIZE*Game.MAP_SIZE_X);
    }

    private void drawBase(){
        gc.setLineWidth(4);
        gc.strokeRect(0,0,canvas.getWidth(),canvas.getHeight());

        gc.setLineWidth(0.5);

        for(int i=0;i<Game.MAP_SIZE_X;i++){
            gc.strokeLine(i*CELL_SIZE,0,i*CELL_SIZE,canvas.getHeight());
        }
        for(int j=0; j<Game.MAP_SIZE_Y; j++){
            gc.strokeLine(0,j* CELL_SIZE,canvas.getWidth(),j*CELL_SIZE);
        }
    }

}
