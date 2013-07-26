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
                    test();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //      System.out.println(modelEvent);

            }
        });
    }

    public Peers getPeers() {
        return peers;
    }


    public void onOpen(WebSocketConnection connection) {
        Log.debug("Nouvelle connexion");
        peers.addPeer(connection);
        connection.send("GetGlobalInformationsHandler ::: " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        Log.debug("GetGlobalInformationsHandler ::: Connexion fermée ");
        peers.removePeer(connection);
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        Log.debug("GetGlobalInformationsHandler ::: ON_MESSAGE");
        if (message.equals("getid")) {
            JSONArray jsonArray = new JSONArray();
            List<Intervention> listInterventions = containerRoot.getInterventions();
            for (int i = 0; i < listInterventions.size(); i++) {
                jsonArray.put(listInterventions.get(i).getId());

            }
            connection.send(new JSONObject().put("idIntervention",jsonArray).toString());

        } else {
            List<Intervention> listInterventions = containerRoot.getInterventions();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < listInterventions.size(); i++) {
                Intervention intervention = listInterventions.get(i);
                int nbAgent = intervention.getAffecte().size();
                int nbVictime = intervention.getVictimes().size();
                int nbVictimeC1 = 0;
                int nbVictimeC2 = 0;
                int nbVictimeC3 = 0;
                int nbVictimeC4 = 0;
                int nbVictimeC5 = 0;
                String victime = "";

                for (int j = 0; j < nbVictime; j++) {
                    victime += "\n\n\n" + adapterFactory.build(intervention.getVictimes().get(j)).toString();

                    if (intervention.getVictimes().get(j).getPriorite() != null) {
                        String cat = containerRoot.findInterventionsByID(intervention.getId()).getVictimes().get(j).getPriorite().getId();

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
                }

                // /List victimes;
                Log.info("INFORAMTION GET INFORMATIONS victimes :::::::::::::::::::::::::  " + victime);
                JSONArray informations = new JSONArray();
                JSONObject jsonObject = new JSONObject();

                informations.put(0, "graph");
                informations.put(1, nbVictime);
                informations.put(2, nbVictimeC1);
                informations.put(3, nbVictimeC2);
                informations.put(4, nbVictimeC3);
                informations.put(5, nbVictimeC4);
                informations.put(6, nbVictimeC5);
                informations.put(7, intervention.getDescription());
                informations.put(8, nbAgent);
                jsonObject.put("informations", informations);
                jsonObject.put("id", intervention.getId());
                jsonArray.put(jsonObject);
            }

            connection.send(jsonArray.toString()); // echo back message in upper case
        }
    }


    public String test() throws JSONException {

        List<Intervention> listInterventions = containerRoot.getInterventions();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < listInterventions.size(); i++) {
            Intervention intervention = listInterventions.get(i);
            int nbAgent = intervention.getAffecte().size();
            int nbVictime = intervention.getVictimes().size();
            int nbVictimeC1 = 0;
            int nbVictimeC2 = 0;
            int nbVictimeC3 = 0;
            int nbVictimeC4 = 0;
            int nbVictimeC5 = 0;
            String victime = "";

            for (int j = 0; j < nbVictime; j++) {
                victime += "\n\n\n" + adapterFactory.build(intervention.getVictimes().get(j)).toString();

                if (intervention.getVictimes().get(j).getPriorite() != null) {
                    String cat = containerRoot.findInterventionsByID(intervention.getId()).getVictimes().get(j).getPriorite().getId();

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
            }

            // /List victimes;
            Log.info("INFORAMTION GET INFORMATIONS victimes :::::::::::::::::::::::::  " + victime);
            JSONArray informations = new JSONArray();
            JSONObject jsonObject = new JSONObject();

            informations.put(0, "graph");
            informations.put(1, nbVictime);
            informations.put(2, nbVictimeC1);
            informations.put(3, nbVictimeC2);
            informations.put(4, nbVictimeC3);
            informations.put(5, nbVictimeC4);
            informations.put(6, nbVictimeC5);
            informations.put(7, intervention.getDescription());
            informations.put(8, nbAgent);
            jsonObject.put("informations", informations);
            jsonObject.put("id", intervention.getId());
            jsonArray.put(jsonObject);
        }


        peers.broadcast(jsonArray.toString());
        return jsonArray.toString();
    }
}
