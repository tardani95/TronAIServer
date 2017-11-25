package com.nanproduction;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application implements Handler{

    private static final int SCREEN_SIZE_X = 1280;
    private static final int SCREEN_SIZE_Y = 750;

    private Scene scene;
    private KeyEvent keyEvent;

    public static void main(String[] args) {
        System.out.println("main()");
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("start()");
        Parent root = FXMLLoader.load(getClass().getResource("/mainWindow.fxml"));
        primaryStage.setTitle("TronAIServerGUI");
        scene=new Scene(root, SCREEN_SIZE_X, SCREEN_SIZE_Y);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.printf("press");
                keyEvent=event;
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stop()");
        super.stop();
    }


    @Override
    public void giveKeyEvent(KeyEvent keyEvent) {
        this.keyEvent=keyEvent;
    }

    @Override
    public KeyEvent getKeyEvent() {
        return keyEvent;
    }
}
