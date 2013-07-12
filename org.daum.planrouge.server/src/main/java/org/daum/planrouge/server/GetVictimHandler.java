package org.daum.planrouge.server;

import org.daum.planrouge.server.connections.Connections;
import org.json.JSONException;
import org.kevoree.log.Log;
import org.kevoree.planrouge.*;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
public class GetVictimHandler extends BaseWebSocketHandler {
    private int connectionCount;

    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;
    private Connections connections;

    public GetVictimHandler(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot, Connections connections) {
        this.planrougeFactory = planrougeFactory;
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
        if (victime != null) {
            if (victime.getPosRef() != null) {
                connection.send("Victime :: " + victime.getNom() + "  priorité :::" + "  GPS :: ");
            } else {
                connection.send("Victime :: " + victime.getNom() + "  priorité :::");
            }

        } else {
            connection.send("Pas de victime à cet ID");
        }
        try {
            Position position = victime.getPosRef();
        } catch (NullPointerException e) {

        }
        try {
            GpsPoint position2 = (GpsPoint) victime.getPosRef();
            if (victime.getPosRef() instanceof GpsPoint) {
                Log.debug("GPSPoint instance");
            } else if (victime.getPosRef() instanceof PositionCivil) {
                Log.debug("PositionCivil instance");
            }
        } catch (NullPointerException e) {

        }


    }


}
