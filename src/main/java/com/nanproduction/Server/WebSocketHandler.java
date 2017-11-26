package com.nanproduction.Server;

import com.nanproduction.*;
import javafx.scene.input.KeyCode;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends BaseWebSocketHandler {

    private Game game= Game.getInstance();


    @Override
    public void onOpen(WebSocketConnection connection) {
        if(game.getGameState()== GameState.WAITING_FOR_PLAYERS) {
            game.addNewPlayer(connection);
            connection.send("Welcome to the Arena!\n"
                    + "Total No. of racers: " + game.getNumOfPlayers() + ".");
            System.out.println("Someone connected. Connections: "
                    + game.getNumOfPlayers());
        }
        else{
            connection.send("The game has been started yet, wait until it will be ended!");
        }


    }

    @Override
    public void onClose(WebSocketConnection connection) {
        game.removePlayer(connection);
        System.out.println("SomeOne Disconnected. Connections: "
                + game.getNumOfPlayers());
    }

    @Override
    public void onMessage(WebSocketConnection connection, String message) {
        //if the value of the players map is null, then adds the message, otherwise do nothing - first message must be the player name
        //players.putIfAbsent(connection, message);

        Player player  = game.getPlayer(connection);

        if(player == null){
            connection.send("Your game is ended!");
        }else {
            if (game.getGameState() == GameState.WAITING_FOR_PLAYERS) {
                if (message.equals("READY")) {
                    player.setReady(true);
                }
                System.out.println("Player " + player.getId()+" is ready!");
            } else {
                System.out.println(player.getId() + ": " + message/*+" time:"+ System.currentTimeMillis()%10000*/);
                //Game.getInstance().addKeyCode(message);
                player.setKeyCode(KeyCode.valueOf(message));
                //}
                //connection.send("The server has received the following message:"+ message);
            }
        }
    }

}
