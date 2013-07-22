package org.daum.planrouge.server.websocket;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.adapter.model.Entities;
import org.daum.planrouge.server.adapter.model.IAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.*;
import org.kevoree.planrouge.impl.InterventionImpl;
import org.webbitserver.WebSocketConnection;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 17/07/13
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */
public class MessageHandler {
    private ContainerRoot root;

    public MessageHandler(ContainerRoot root) {
        this.root = root;
    }



    public void process(WebSocketConnection connection, AdapterFactory adapterFactory, JSONObject message, HandlerWebSocket.ACTION action) throws JSONException {

        Object obj = AdapterFactory.getInstance().build(message);

        switch (adapterFactory.getType(obj)) {

            case AdapterCategorie:
                break;

            case AdapterGpsPoint:
                break;


            case AdapterIntervention:
                Intervention intervention = (Intervention) obj;
                root.addInterventions(intervention);
                break;


            case AdapterVictime:
                Victime victime = (Victime) obj;
                if (victime.getId().length() == 0){
                    Log.error("AdapterVictime ::: No ID "+victime.getId().length()+" "+action);
                    return;
                }
                switch (action) {

                    case GET:

                        victime = root.findInterventionsByID(victime.getIntervention().getId()).findVictimesByID(victime.getId());
                       JSONObject jsonVictime = AdapterFactory.getInstance().build(victime);

                        if (victime != null) {
                            Log.debug(jsonVictime.toString());
                            connection.send(String.valueOf(jsonVictime));

                        } else {
                            connection.send("Pas de victime à cet ID");
                        }

                        break;

                          case GETALL:

                              break;
                    case PUT:

                        Victime vicmodel = root.findInterventionsByID(victime.getIntervention().getId()).findVictimesByID(victime.getId());

                        if (vicmodel != null) {
                           // merge(vicmodel, victime);
                            root.findInterventionsByID(victime.getIntervention().getId()).removeVictimes(vicmodel);
                            root.findInterventionsByID(victime.getIntervention().getId()).addVictimes(victime);

                        } else {
                            root.findInterventionsByID(victime.getIntervention().getId()).addVictimes(victime);
                        }

                        Victime test = root.findInterventionsByID(victime.getIntervention().getId()).findVictimesByID(victime.getId());
                        JSONObject jtest = AdapterFactory.getInstance().build(test);
                        Log.info(jtest.toString());
                        break;
                }


                connection.send("ack");
                break;


        }
    }
}
