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

    public void merge(Object obj, Object update) {
        if (!obj.getClass().isAssignableFrom(update.getClass())) {
            return;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                if (f.get(update) != null
                        && (f.get(update).equals("")
                        | f.get(update).equals(0))) {
                    f.set(update, f.get(obj));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public void process(WebSocketConnection connection, AdapterFactory adapterFactory, JSONObject message, HandlerWebSocket.ACTION action) throws JSONException {

        Object obj = AdapterFactory.getInstance().build(message);

        switch (adapterFactory.getType(obj)) {

            case AdapterCategorie:
                break;

            case AdapterGpsPoint:
                break;


            case AdapterIntervention:
                break;


            case AdapterVictime:
                Victime victime = (Victime) obj;
                if (victime.getId().length() == 0){
                    Log.error("AdapterVictime ::: No ID");
                    return;
                }
                switch (action) {

                    case GET:


                        victime = root.findInterventionsByID("1").findVictimesByID(victime.getId());

                        JSONObject jsonVictime = AdapterFactory.getInstance().build(victime);
                        List<Victime> victimeList = new LinkedList();
                        victimeList = root.findInterventionsByID("1").getVictimes();

                        Iterator iterator
                                = victimeList.iterator();
                        while (iterator.hasNext()) {
                            Victime key = (Victime) iterator.next();
                            Log.debug(key.getId());
                        }

                        if (victime != null) {
                            Log.debug(jsonVictime.toString());
                            connection.send(String.valueOf(jsonVictime));


                        } else {
                            connection.send("Pas de victime à cet ID");
                        }

                        break;


                    case PUT:

                        Victime vicmodel = root.findInterventionsByID("1").findVictimesByID(victime.getId());

                        if (vicmodel != null) {
                           // merge(vicmodel, victime);
                            root.findInterventionsByID("1").removeVictimes(vicmodel);
                            root.findInterventionsByID("1").addVictimes(victime);

                        } else {
                            root.findInterventionsByID("1").addVictimes(victime);
                        }

                        Victime test = root.findInterventionsByID("1").findVictimesByID(victime.getId());
                        JSONObject jtest = AdapterFactory.getInstance().build(test);
                        Log.info(jtest.toString());
                        break;
                }


                connection.send("ack");
                break;


        }
    }
}
