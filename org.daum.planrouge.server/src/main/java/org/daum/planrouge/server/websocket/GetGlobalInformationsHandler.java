package org.daum.planrouge.server.websocket;

import org.apache.log4j.helpers.LogLog;
import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.Intervention;
import org.kevoree.planrouge.events.ModelEvent;
import org.kevoree.planrouge.events.ModelTreeListener;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class GetGlobalInformationsHandler extends BaseWebSocketHandler {
    private int connectionCount;
    private Peers peers = null;
    private AdapterFactory adapterFactory;
    private static ContainerRoot containerRoot;


    public GetGlobalInformationsHandler(AdapterFactory adapterFactory, ContainerRoot pContainerRoot) {
        this.adapterFactory = adapterFactory;
        this.containerRoot = pContainerRoot;
        peers = new Peers();

        containerRoot.addModelTreeListener(new ModelTreeListener() {
            @Override
            public void elementChanged(ModelEvent modelEvent) {

                try {
                    String information = getInformations();
                    peers.broadcast(information);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public Peers getPeers() {
        return peers;
    }


    public void onOpen(WebSocketConnection connection) {
        Log.info("Nouvelle connexion");
        peers.addPeer(connection);
        connection.send("GetGlobalInformationsHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        Log.info("GetGlobalInformationsHandler ::: Connexion fermée ");
        peers.removePeer(connection);
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        Log.info("GetGlobalInformationsHandler ::: ON_MESSAGE");
        if (message.equals("getid")) {
            JSONArray jsonArray = new JSONArray();
            List<Intervention> listInterventions = containerRoot.getInterventions();
            for (int i = 0; i < listInterventions.size(); i++) {
                jsonArray.put(listInterventions.get(i).getId());

            }
            connection.send(new JSONObject().put("idIntervention", jsonArray).toString());

        } else {
            String information = getInformations();
            connection.send(information);
        }
    }


    public String getInformations() throws JSONException {

        JSONArray jsonArrayIntervention = new JSONArray();


        List<Intervention> listInterventions = containerRoot.getInterventions();
        JSONArray jsonArray = new JSONArray();
        JSONObject jObject = new JSONObject();
        JSONObject jsonObjectToSend = new JSONObject();
        for (int i = 0; i < listInterventions.size(); i++) {
            jsonArrayIntervention.put(listInterventions.get(i).getId());
            Intervention intervention = listInterventions.get(i);
            JSONArray jArrayVictimes = new JSONArray();
            int nbAgent = intervention.getAffecte().size();
            int nbVictime = intervention.getVictimes().size();
            int nbVictimeC1 = 0;
            int nbVictimeC2 = 0;
            int nbVictimeC3 = 0;
            int nbVictimeC4 = 0;
            int nbVictimeC5 = 0;
            int nbFemmes = 0;
            int nbHommes = 0;
            int age0_5 = 0;
            int age6_10 = 0;
            int age11_20 = 0;
            int age21_30 = 0;
            int age31_40 = 0;
            int age41_60 = 0;
            int age61_80 = 0;
            int age80_ = 0;


            for (int j = 0; j < nbVictime; j++) {

                jArrayVictimes.put(adapterFactory.build(intervention.getVictimes().get(j)));

                if (intervention.getVictimes().get(j).getPriorite() != null) {
                    String cat = intervention.getVictimes().get(j).getPriorite().getId();

                    if (cat.equals("1")) {
                        nbVictimeC1++;
                        Log.info("cat 1");
                    } else if (cat.equals("2")) {
                        nbVictimeC2++;
                        Log.info("cat 2");
                    } else if (cat.equals("3")) {
                        nbVictimeC3++;
                        Log.info("cat 3");
                    } else if (cat.equals("4")) {
                        nbVictimeC4++;
                        Log.info("cat 4");
                    } else if (cat.equals("5")) {
                        nbVictimeC5++;
                        Log.info("cat 5");
                    }
                }
                if (intervention.getVictimes().get(j).getSexe() != "" && intervention.getVictimes().get(j).getSexe() != null) {
                    String sexe = intervention.getVictimes().get(j).getSexe();
                    if (sexe.equals("2")) {
                        nbFemmes++;
                    } else if (sexe.equals("1")) {
                        nbHommes++;
                    }
                }
                if (intervention.getVictimes().get(j).getAge() != 0) {
                    int age = intervention.getVictimes().get(j).getAge();
                    if (age <= 5) {
                        age0_5++;
                    } else if (age <= 10) {
                        age6_10++;
                    } else if (age <= 20) {
                        age11_20++;
                    } else if (age <= 30) {
                        age21_30++;
                    } else if (age <= 40) {
                        age31_40++;
                    } else if (age <= 60) {
                        age41_60++;
                    } else if (age <= 80) {
                        age61_80++;
                    } else {
                        age80_++;
                    }
                }
            }


            // ARRAY Nb Category Victim
            JSONArray jNbVictimeCat = new JSONArray();
            jNbVictimeCat.put(0, nbVictimeC1);
            jNbVictimeCat.put(1, nbVictimeC2);
            jNbVictimeCat.put(2, nbVictimeC3);
            jNbVictimeCat.put(3, nbVictimeC4);
            jNbVictimeCat.put(4, nbVictimeC5);

            JSONArray jNbVictimeAge = new JSONArray();
            jNbVictimeAge.put(0, age0_5);
            jNbVictimeAge.put(1, age6_10);
            jNbVictimeAge.put(2, age11_20);
            jNbVictimeAge.put(3, age21_30);
            jNbVictimeAge.put(4, age31_40);
            jNbVictimeAge.put(5, age41_60);
            jNbVictimeAge.put(6, age61_80);
            jNbVictimeAge.put(7, age80_);


            jObject = new JSONObject();

            jObject.put("nbVictime", nbVictime);
            jObject.put("type", "donnees");
            jObject.put("nbVictimeCat", jNbVictimeCat);
            jObject.put("nbVictimeAge", jNbVictimeAge);
            jObject.put("nbVictimeSexe", new JSONArray().put(0, nbHommes).put(1, nbFemmes));
            jObject.put("intervention", adapterFactory.build(intervention));
            jObject.put("victimes", jArrayVictimes);
            jObject.put("agents", nbAgent);

            jsonArray.put(jObject);

        }
        jsonObjectToSend.put("donnees", jsonArray);
        jsonObjectToSend.put("idIntervention", jsonArrayIntervention);
        Log.info(jsonObjectToSend.toString());

        return jsonObjectToSend.toString();
    }

}
