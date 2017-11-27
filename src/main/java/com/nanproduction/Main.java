package com.nanproduction;

import com.nanproduction.GameElements.Game;
import com.nanproduction.Server.WebSocketHandler;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;

public class Main{

    public static final int PORT = 8090;
    public static final int CELL_SIZE = 20;
    public static final double BODY_SCALE = 0.5;


    private static Game game;
    private static WebServer webServer;
    private static boolean STOP = false;

    public static void main(String[] args) {
        System.out.println("main()");

        startServer();

        while (!STOP){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stopServer();
        System.out.println("Application stopped.");
    }

    public static void startServer(){
        webServer = WebServers.createWebServer(PORT);
        webServer.add(new StaticFileHandler("src/main/resources/static"));
        webServer.add("/websocket", new WebSocketHandler());
        webServer.start();

        game=Game.getInstance();
        game.init();
    }

    public static void restartServer(){
        stopServer();
        startServer();
    }

    public static void stopServer(){
        System.err.println("Stopping webSocketServer...");
        if(webServer != null){
            game.stop();
            webServer.stop();
            System.out.println("WebSocketServer stopped.");
            return;
        }
        System.err.println("Failed to stop server.");
    }

    public static void stopServerAndExit(){
        STOP = true;
        System.out.println("Stopping application...");
    }






}
