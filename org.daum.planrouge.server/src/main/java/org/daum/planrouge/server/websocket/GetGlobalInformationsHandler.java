package org.daum.planrouge.server.websocket;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class GetGlobalInformationsHandler extends BaseWebSocketHandler {
    private int connectionCount;
    private Peers peers=null;
    private AdapterFactory adapterFactory;
    private static ContainerRoot containerRoot;


    public GetGlobalInformationsHandler(AdapterFactory adapterFactory, ContainerRoot pContainerRoot) {
        this.adapterFactory = adapterFactory;
        this.containerRoot = pContainerRoot;
        peers = new Peers();
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
        int nbVictime = this.containerRoot.findInterventionsByID("1").getVictimes().size();
        int nbVictimeC1 = 0;
        int nbVictimeC2 = 0;
        int nbVictimeC3 = 0;
        int nbVictimeC4 = 0;
        int nbVictimeC5 = 0;


        for (int i = 0; i < nbVictime; i++) {
            if (this.containerRoot.findInterventionsByID("1").getVictimes().get(i).getPriorite() != null) {
                String cat = this.containerRoot.findInterventionsByID("1").getVictimes().get(i).getPriorite().getId();
                if (cat.equals("1")) {
                    nbVictimeC1++;
                } else if (cat.equals("2")) {
                    nbVictimeC2++;
                } else if (cat.equals("3")) {
                    nbVictimeC3++;
                } else if (cat.equals("4")) {
                    nbVictimeC4++;
                } else if (cat.equals("5")) {
                    nbVictimeC5++;
                }
            }
        }

        // /List victimes;

        JSONArray informations = new JSONArray();
        informations.put(0, "graph");
        informations.put(1, nbVictime);
        informations.put(2, nbVictimeC1);
        informations.put(3, nbVictimeC2);
        informations.put(4, nbVictimeC3);
        informations.put(5, nbVictimeC4);
        informations.put(6, nbVictimeC5);
        informations.put(7, containerRoot.findInterventionsByID("1").getDescription());
        Log.debug(informations.toString());
        connection.send(informations.toString()); // echo back message in upper case

    }


    public String test() throws JSONException {
        int nbVictime = containerRoot.findInterventionsByID("1").getVictimes().size();
        int nbVictimeC1 = 0;
        int nbVictimeC2 = 0;
        int nbVictimeC3 = 0;
        int nbVictimeC4 = 0;
        int nbVictimeC5 = 0;


        for (int i = 0; i < nbVictime; i++) {
            if (containerRoot.findInterventionsByID("1").getVictimes().get(i).getPriorite() != null) {
                String cat = containerRoot.findInterventionsByID("1").getVictimes().get(i).getPriorite().getId();
                if (cat.equals("1")) {
                    nbVictimeC1++;
                } else if (cat.equals("2")) {
                    nbVictimeC2++;
                } else if (cat.equals("3")) {
                    nbVictimeC3++;
                } else if (cat.equals("4")) {
                    nbVictimeC4++;
                } else if (cat.equals("5")) {
                    nbVictimeC5++;
                }
            }
        }

        // /List victimes;

        JSONArray informations = new JSONArray();
        informations.put(0, "graph");
        informations.put(1, nbVictime);
        informations.put(2, nbVictimeC1);
        informations.put(3, nbVictimeC2);
        informations.put(4, nbVictimeC3);
        informations.put(5, nbVictimeC4);
        informations.put(6, nbVictimeC5);
        informations.put(7, containerRoot.findInterventionsByID("1").getDescription());
        return  informations.toString();
    }
}
