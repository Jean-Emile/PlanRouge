package org.daum.planrouge.server.websocket;


import org.kevoree.planrouge.PlanrougeFactory;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.List;


public class Peers {

    private List<WebSocketConnection> connections;

    private NotifyConnection notifyConnection = new NotifyConnection();

    public Peers( ){
        this.connections = new ArrayList<WebSocketConnection>();

    }

    public void addPeer(WebSocketConnection webSocketConnection){
        connections.add(webSocketConnection);
        notifyConnection.notifyConnection(webSocketConnection);
    }

    public void removePeer(WebSocketConnection webSocketConnection){
        connections.remove(webSocketConnection);
        notifyConnection.notifyConnection(webSocketConnection);
    }


    public void broadcast(String msg)
    {
        for (WebSocketConnection connection : connections)
        {
            connection.send(msg);
        }
    }


    public void update(){
        notifyConnection.notifyObservers();
    }

}
