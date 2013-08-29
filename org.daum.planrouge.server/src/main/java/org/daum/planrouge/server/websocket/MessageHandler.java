package org.daum.planrouge.server.websocket;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.adapter.model.Entities;
import org.daum.planrouge.server.adapter.model.IAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.*;
import org.kevoree.planrouge.impl.InterventionImpl;
import org.webbitserver.WebSocketConnection;

import java.lang.reflect.Field;

import java.util.*;

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

                    case GETALL:
                        Log.info("Lecture ARRAY AGENT");
                        JSONArray mJSONArray = new JSONArray();
                        for (int i = 0; i<root.getAgents().size();i++){
                            mJSONArray.put(adapterFactory.build(root.getAgents().get(i)));
                        }

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type","AdapterAgent");
                        jsonObject.put("arrayAgents",mJSONArray);

                        connection.send(jsonObject.toString());

                        return;

                    case DELETE:
                        Log.info("AdapterAgent ::: DELETE");
                        Agent agentDelete = (Agent) obj;
                        if(root.findAgentsByID(agentDelete.getMatricule()) != null){
                            Agent agentModel = root.findAgentsByID(agentDelete.getMatricule());
                            if(agentModel.getIntervention() != null){
                                root.findInterventionsByID(agentModel.getIntervention().getId()).removeAffecte(agentModel);
                            }
                            root.removeAgents(agentModel);

                            connection.send("Agent "+agentDelete.getMatricule() +" supprimé");
                        } else {
                            connection.send("Pas d'Agent trouvé");
                        }

                        return;


                    case GET:
                        if (root.findAgentsByID(agent.getMatricule()) != null) {
                            Agent agentModel = root.findAgentsByID(agent.getMatricule());
                            JSONObject jObject = new JSONObject();
                            jObject.put("type","getAgent");
                            jObject.put("result",adapterFactory.build(agentModel));
                            connection.send(jObject.toString());

                        } else {
                            JSONObject jObject = new JSONObject();
                            jObject.put("type","getAgent");
                            jObject.put("result","undefined");
                            connection.send(jObject.toString());
                        }
                        return;

                    case PUT:
                        root.addAgents(agent);
                        return;
                }
                break;

            case AdapterIntervention:

                switch (action) {

                    case GETALL:

                        Log.info("AdapterIntervention ::: GETALL");
                        JSONArray mJSONArray = new JSONArray();
                        for (int i = 0; i<root.getInterventions().size();i++){
                            mJSONArray.put(adapterFactory.build(root.getInterventions().get(i)));
                        }

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("type","AdapterIntervention");
                        jsonObject.put("arrayInterventions",mJSONArray);

                        connection.send(jsonObject.toString());

                        return;

                    case DELETE:

                        Log.info("AdapterIntervention ::: DELETE");
                        Intervention interventionDelete = (Intervention) obj;
                        if(root.findInterventionsByID(interventionDelete.getId()) != null){
                            root.findInterventionsByID(interventionDelete.getId()).delete();
                        }

                        return;

                    case PUT:
                        Log.info("AdapterIntervention");
                        Random r = new Random();
                        int valeur = 1000 + r.nextInt(9999 - 1000) ;

                        Intervention intervention = (Intervention) obj;

                        if (intervention.getId().length()!=0){   // UPDATE
                            if(root.findInterventionsByID(intervention.getId()) !=null ){
                                Intervention interventionmodel = root.findInterventionsByID(intervention.getId());
                                root.removeInterventions(interventionmodel);
                            }
                            // Add intervention to ContainerRoot
                            Log.info("\n\n\n\n"+adapterFactory.build(intervention).toString()+"\n\n\n\n");
                            root.addInterventions(intervention);
                        }else{
                            while(root.findInterventionsByID(String.valueOf(valeur))!=null){
                                r = new Random();
                                valeur = 1000 + r.nextInt(9999 - 1000) ;
                            }
                            intervention.setId(String.valueOf(valeur));
                            // Add intervention to ContainerRoot
                            root.addInterventions(intervention);
                        }

                        // Add intervention to ContainerRoot

                        String idIntervention = intervention.getId();

                        // AgentList of this intervention
                        List<Agent> agentList = intervention.getAffecte();

                        // Adding this intervention to concerned agents
                        for (int i = 0; i < agentList.size(); i++) {
                            Log.info("Ajout intervention a l'agent ::: " + agentList.get(i).getMatricule() + " de l'intervention :: " + root.findInterventionsByID(intervention.getId()).getId());
                            root.findAgentsByID(agentList.get(i).getMatricule()).setIntervention(root.findInterventionsByID(idIntervention));
                        }
                        return;
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

                        Victime vicmodel = root.findInterventionsByID(agentmodel.getIntervention().getId()).findVictimesByID(victime.getId());

                        if (vicmodel != null) {
                            // merge(vicmodel, victime);
                            for (int i = 0; i < root.getInterventions().size(); i++) {
                                root.getInterventions().get(i).removeVictimes(vicmodel);
                            }
                            victime.addAllIntervenants(vicmodel.getIntervenants());
                            victime.addIntervenants(agentmodel);
                            root.findInterventionsByID(agentmodel.getIntervention().getId()).removeVictimes(vicmodel);
                            root.findInterventionsByID(agentmodel.getIntervention().getId()).addVictimes(victime);


                        } else {
                            for (int i = 0; i < root.getInterventions().size(); i++) {
                                root.getInterventions().get(i).removeVictimes(victime);
                            }
                            victime.addIntervenants(agentmodel);
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
