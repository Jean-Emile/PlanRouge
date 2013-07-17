package org.daum.planrouge.server;


import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.websocket.Peers;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.*;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
public class GetVictimHandler extends BaseWebSocketHandler {
    private int connectionCount;

    private AdapterFactory adapterFactory;
    private ContainerRoot containerRoot;
    private Peers connections;

    public GetVictimHandler(AdapterFactory adapterFactory, ContainerRoot containerRoot, Peers connections) {
        this.adapterFactory = adapterFactory;
        this.containerRoot = containerRoot;
        this.connections = connections;
    }

    public void onOpen(WebSocketConnection connection) {
        System.out.println("Nouvelle connexion");
        connections.getConnectionGetVictim().addConnections(connection);
        connection.send("GetVictimHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        connections.getConnectionGetVictim().removeConnections(connection);
        System.out.println("GetVictimHandler ::: Connexion fermée ");
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        System.out.println("GetVictimHandler :::  ON_MESSAGE");

        Victime victime = containerRoot.findInterventionsByID("1").findVictimesByID(message);

        JSONObject jsonVictime = adapterFactory.build(victime);
        List<Victime> victimeList = new LinkedList();
        victimeList = containerRoot.findInterventionsByID("1").getVictimes();

        Iterator iterator
                = victimeList.iterator();
       while ( iterator.hasNext()){
           Victime key = (Victime) iterator.next();
           Log.debug(key.getId());
       }

        if (victime != null) {
            Log.debug(jsonVictime.toString());
            connection.send(String.valueOf(jsonVictime));


        } else {
            connection.send("Pas de victime à cet ID");
        }


    }


}
