package org.daum.planrouge.server;

import org.daum.planrouge.server.adapter.AdapterVictime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.*;
import org.kevoree.log.Log;
import org.webbitserver.*;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
public class AddVictimHandler extends BaseWebSocketHandler {

    private int connectionCount;
    private int i = 0;
    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;


    public AddVictimHandler(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot, int i) {
        this.planrougeFactory = planrougeFactory;
        this.containerRoot = containerRoot;
        this.i = i;
    }

    public void onOpen(WebSocketConnection connection) {
        Log.debug("Nouvelle connexion");
        connection.send("AddVictimHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        Log.debug("AddVictimHandler ::: Connexion fermée ");
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        Log.debug("AddVictimHandler ::: ON_MESSAGE");

        AdapterVictime adapterVictime = new AdapterVictime(planrougeFactory,containerRoot);
        Victime victime = adapterVictime.parseJsonToVictime(message);

        if (victime != null) {
            containerRoot.findInterventionsByID("1").addVictimes(victime);
            connection.send("Victime ajouté à la base de données");
        } else {
            connection.send("Erreur");
        }
    }



}

