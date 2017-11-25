package com.nanproduction;

import com.nanproduction.Server.WebSocketHandler;
import javafx.fxml.FXML;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;

public class MainWindowController {


    public MainWindowController() {
        System.out.println("MainWindowController() called");
    }

    @FXML
    void initialize(){
        System.out.println("Initialize() called");
//        myServer = new MultiThreadedServer(8081);
//        new Thread(myServer).start();

//        try {
//            Thread.sleep(20 * 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Stopping Server");
//        server.stop();

        WebServer webServer = WebServers.createWebServer(8090);
        webServer.add(new StaticFileHandler("/static-files"));
        webServer.add("/websocket", new WebSocketHandler());
        webServer.start();
    }
}
