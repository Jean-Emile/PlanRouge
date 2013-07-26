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

            case AdapterAgent:
                Log.info("AdapterAgent");
                Agent agent = (Agent) obj;

                switch (action) {
                    case GET:

                        if (root.findAgentsByID(agent.getMatricule()) != null) {
                            connection.send("true");

                        } else {
                            connection.send("false");
                        }
                        return;

                    case PUT :
                        root.addAgents(agent);
                        return;
                }
               break;

            case AdapterIntervention:
                Log.info("AdapterIntervention");
                Intervention intervention = (Intervention) obj;

                // Add intervention to ContainerRoot
                root.addInterventions(intervention);
                String idIntervention = intervention.getId();

                // AgentList of this intervention
                List<Agent> agentList = intervention.getAffecte();

                // Adding this intervention to concerned agents
                for (int i = 0; i < agentList.size(); i++) {
                    Log.info("Ajout intervention a l'agent ::: "+agentList.get(i).getMatricule()+" de l'intervention :: "+root.findInterventionsByID(intervention.getId()).getId());
//                    for (int j = 0; j< root.getInterventions().size(); j++){
//                        if (root.getInterventions().get(j).getAffecte().contains(agentList.get(i))){
//                            root.getInterventions().get(j).removeAffecte(agentList.get(i));
//                        }
//                    }
                    root.findAgentsByID(agentList.get(i).getMatricule()).setIntervention(root.findInterventionsByID(idIntervention));
                }
                break;


            case AdapterVictime:
                Victime victime = (Victime) obj;
                if (victime.getId().length() == 0) {
                    Log.error("AdapterVictime ::: No ID " + victime.getId().length() + " " + action);
                    return;
                }
                switch (action) {

                    case GET:

                        victime = root.findInterventionsByID(victime.getIntervenants().get(0).getIntervention().getId()).findVictimesByID(victime.getId());
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

                        Agent agentmodel = root.findAgentsByID(victime.getIntervenants().get(0).getMatricule());
                        victime.addIntervenants(agentmodel);
                        Victime vicmodel = root.findInterventionsByID(agentmodel.getIntervention().getId()).findVictimesByID(victime.getId());

                        if (vicmodel != null) {
                            // merge(vicmodel, victime);
                            for(int i = 0 ; i<root.getInterventions().size();i++){
                                root.getInterventions().get(i).removeVictimes(vicmodel);
                            }
                            root.findInterventionsByID(agentmodel.getIntervention().getId()).removeVictimes(vicmodel);
                            root.findInterventionsByID(agentmodel.getIntervention().getId()).addVictimes(victime);

                        } else {
                            for(int i = 0 ; i<root.getInterventions().size();i++){
                                root.getInterventions().get(i).removeVictimes(victime);
                            }
                            root.findInterventionsByID(agentmodel.getIntervention().getId()).addVictimes(victime);
                        }

                        Victime test = root.findInterventionsByID(agentmodel.getIntervention().getId()).findVictimesByID(victime.getId());
                        JSONObject jtest = AdapterFactory.getInstance().build(test);
                        Log.info(jtest.toString());
                        break;
                }


                connection.send("ack");
                break;


        }
    }
}
