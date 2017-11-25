package com.nanproduction.Server;

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

public class WebSocketHandler extends BaseWebSocketHandler {

    private int connections = 0;



    @Override
    public void onOpen(WebSocketConnection connection) {
        this.connections++;
        connection.send("This is the webserver saying Hello! :)<br/>"
                + "Total No. of subscribers: " + this.connections + ".");
        System.out.println("Someone connected. Connections: "
                + this.connections);
    }

    @Override
    public void onClose(WebSocketConnection connection) {
        this.connections--;
        System.out.println("SomeOne Disconnected. Connections: "
                + this.connections);
    }

    @Override
    public void onMessage(WebSocketConnection connection, String message) {
        connection.send("The server has received the following message:<br/>"
                + message);
        System.out.println("Received message: " + message);
        System.out.println(connection.toString());
    }

}
