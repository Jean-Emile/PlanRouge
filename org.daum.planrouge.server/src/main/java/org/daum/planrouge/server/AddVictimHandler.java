package org.daum.planrouge.server;


import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.websocket.Peers;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.*;
import org.kevoree.log.Log;
import org.webbitserver.*;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
public class AddVictimHandler extends BaseWebSocketHandler {

    private int connectionCount;
    private Peers connections ;
    private AdapterFactory adapterFactory;
    private ContainerRoot containerRoot;


    public AddVictimHandler(AdapterFactory adapterFactory, ContainerRoot containerRoot, Peers connections) {
        this.adapterFactory = adapterFactory;
        this.containerRoot = containerRoot;
        this.connections = connections;
    }

    public void onOpen(WebSocketConnection connection) {
        Log.debug("New connection");
        connections.addPeer(connection);
        connection.send("AddVictimHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        Log.debug("AddVictimHandler ::: Connection close ");
        connections.removePeer(connection);
        connectionCount--;

    }

              // Hack due to perf read tag we can do better !!
    public  void merge(Object obj, Object update)   {
        if(!obj.getClass().isAssignableFrom(update.getClass())){
            return;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field f: fields){
            f.setAccessible(true);
            try {
                if(f.get(obj) != null && (f.get(update).equals("") |f.get(update).equals(0))){
                    f.set(update, f.get(obj));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        Log.debug("AddVictimHandler ::: ON_MESSAGE");

        JSONObject jsonVictime = new JSONObject(message);
        Victime victime = adapterFactory.build(jsonVictime);

        if (victime != null) {
            Victime vicmodel =   containerRoot.findInterventionsByID("1").findVictimesByID(victime.getId());

            if(vicmodel !=null){
                merge(vicmodel,vicmodel);
                connection.send("Victim update");
            } else {
                containerRoot.findInterventionsByID("1").addVictimes(victime);
                connection.send("Victim added to database");
            }


        } else {
            connection.send("Erreur");
        }


    }



}

