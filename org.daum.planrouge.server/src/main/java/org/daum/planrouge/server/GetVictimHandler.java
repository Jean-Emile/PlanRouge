package org.daum.planrouge.server;

import org.json.JSONException;
import org.kevoree.log.Log;
import org.kevoree.planrouge.*;
import org.webbitserver.*;

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

    public GetVictimHandler(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot) {
        this.planrougeFactory = planrougeFactory;
        this.containerRoot = containerRoot;
    }

    public void onOpen(WebSocketConnection connection) {
        System.out.println("Nouvelle connexion");
        connection.send("GetVictimHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        System.out.println("GetVictimHandler ::: Connexion fermée ");
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        System.out.println("GetVictimHandler :::  ON_MESSAGE");

       Victime victime = containerRoot.findInterventionsByID("1").findVictimesByID(message);
        Position postion = victime.getPosRef();
        GpsPoint position2 = (GpsPoint) victime.getPosRef();

        if(victime.getPosRef() instanceof GpsPoint){
            Log.debug("GPSPoint instance");
        }else if ( victime.getPosRef() instanceof PositionCivil){
            Log.debug("PositionCivil instance");
        }



        connection.send("Victime :: "+victime.getNom()+"  priorité :::"+"  GPS :: "); // echo back message in upper case

    }



}
