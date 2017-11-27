package com.nanproduction;

import com.nanproduction.GameElements.Game;
import com.nanproduction.GameElements.GameStateEnum;
import com.nanproduction.Server.WebSocketHandler;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;

public class Main{

    public static final int CELL_SIZE = 20;
    public static final double BODY_SCALE = 0.5;


    private static Game game;

    public static void main(String[] args) {
        System.out.println("main()");

        WebServer webServer = WebServers.createWebServer(8090);
        webServer.add(new StaticFileHandler("src/main/resources/static"));
        webServer.add("/websocket", new WebSocketHandler());
        webServer.start();


        game=Game.getInstance();
        game.init();

        while (game.getGameState()!= GameStateEnum.ENDING){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webServer.stop();
    }




}
