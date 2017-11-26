package com.nanproduction.Server;

import com.nanproduction.Game;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends BaseWebSocketHandler {

    private Game game;
    private int connections = 0;
    private static Map<WebSocketConnection,String> players = new ConcurrentHashMap<>();

    @Override
    public void onOpen(WebSocketConnection connection) {
        this.connections++;
        connection.send("Welcome to the Arena!\n"
                + "Total No. of racers: " + this.connections + ".");
        System.out.println("Someone connected. Connections: "
                + this.connections);
        game = Game.getInstance();
    }

    @Override
    public void onClose(WebSocketConnection connection) {
        this.connections--;
        System.out.println("SomeOne Disconnected. Connections: "
                + this.connections);
    }

    @Override
    public void onMessage(WebSocketConnection connection, String message) {
        //if the value of the players map is null, then adds the message, otherwise do nothing - first message must be the player name
        //players.putIfAbsent(connection, message);
        String userID = players.get(connection);
        if(userID == null){
            players.put(connection,message);
            System.out.println("User connected: "+ message);
        }else {
            System.out.println(userID + ": " + message/*+" time:"+ System.currentTimeMillis()%10000*/);
            //Game.getInstance().addKeyCode(message);
            game.addKeyCode(message);
        }
        //connection.send("The server has received the following message:"+ message);
    }

}
