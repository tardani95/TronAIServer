package com.nanproduction;

import javafx.event.EventHandler;
import com.nanproduction.Server.WebSocketHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.awt.event.KeyListener;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;

public class MainWindowController {


/*
    private Game game;


    @FXML
    public Canvas canvas;

    @FXML
    public Text stateText;

    @FXML
    public Text scoreText;

    private static GraphicsContext gc;

    public MainWindowController() {
        System.out.println("MainWindowController() called");
    }

    @FXML
    void initialize(){
        System.out.println("Initialize() called");
        gc=canvas.getGraphicsContext2D();
        stateText.setText("PLAYING");



//        canvas.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                System.out.printf("press");
//            }
//        });


    }

    public void drawPlayers(Collection<Player> playerList)
    {
        gc.clearRect(0, 0, Game.MAP_SIZE_X*CELL_SIZE, Game.MAP_SIZE_Y*CELL_SIZE);
        drawBase();

        gc.setFill(Color.RED); //achivemenet
        gc.fillRect(CELL_SIZE * game.getAchievement().getX(), CELL_SIZE * game.getAchievement().getY(), CELL_SIZE, CELL_SIZE);

        for(Player player: playerList) {
            gc.setFill(player.getColor()); //playerHead
            gc.fillRect(CELL_SIZE * player.getHead().getX(), CELL_SIZE * player.getHead().getY(), CELL_SIZE, CELL_SIZE); //Fill the Head



            gc.setStroke(player.getColor()); //playerBody
            gc.setLineWidth(CELL_SIZE * BODY_SCALE);
            if (player.getTail().size() == 0) {
                continue;
            }
            gc.strokeLine(CELL_SIZE * (player.getHead().getX() + 0.5), CELL_SIZE * (player.getHead().getY() + 0.5), CELL_SIZE * (player.getTail().get(0).getX() + 0.5), CELL_SIZE * (player.getTail().get(0).getY() + 0.5));
            for (int i = 1; i < player.getTail().size(); i++) {
                gc.strokeLine(CELL_SIZE * (player.getTail().get(i - 1).getX() + 0.5), CELL_SIZE * (player.getTail().get(i - 1).getY() + 0.5), CELL_SIZE * (player.getTail().get(i).getX() + 0.5), CELL_SIZE * (player.getTail().get(i).getY() + 0.5)); //draw body
            }
        }



    }

    public void setScore(Collection<Player> playerList){
        StringBuilder stringBuilder=new StringBuilder();
        for(Player player: playerList){
            stringBuilder.append(player.getId()+"\t"+player.getScore()+"\n");
        }
        scoreText.setText(stringBuilder.toString());
    }

    public void endingGame(){
        stateText.setText("GAME OVER!");
    }

    private void initWindow() {
        canvas.setHeight(CELL_SIZE *Game.MAP_SIZE_Y);
        canvas.setWidth(CELL_SIZE*Game.MAP_SIZE_X);
    }

    private void drawBase(){
        gc.setStroke(Color.BLACK);
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


*/
}
