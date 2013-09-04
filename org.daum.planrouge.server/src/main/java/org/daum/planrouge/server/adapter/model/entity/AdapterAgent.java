package org.daum.planrouge.server.adapter.model.entity;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.adapter.model.Entities;
import org.daum.planrouge.server.adapter.model.IAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import org.kevoree.planrouge.Agent;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.impl.AgentImpl;
import org.kevoree.planrouge.impl.InterventionImpl;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 24/07/13
 * Time: 09:52
 * To change this template use File | Settings | File Templates.
 */
public class AdapterAgent implements IAdapter{


    private AdapterFactory adapterFactory;

    public AdapterAgent() {
        this.adapterFactory =  AdapterFactory.getInstance();

    }
    @Override
    public JSONObject build(KMFContainer container) throws JSONException {
        JSONObject jsonAgent = new JSONObject();
        AgentImpl agent = (AgentImpl) container;
        jsonAgent.put("matricule", agent.getMatricule());
        jsonAgent.put("type", getType());
        jsonAgent.put("prenom", agent.getPrenom());
        jsonAgent.put("nom", agent.getNom());
        jsonAgent.put("dateNaissance", agent.getDateNaissance());
        jsonAgent.put("sexe", agent.getSexe());

        return jsonAgent;
    }

    @Override
    public <T> T build(JSONObject json) throws JSONException {
      Agent agent = adapterFactory.getFactory().createAgent();

        if(json.has("matricule")){
            agent.setMatricule(json.getString("matricule"));

        }
        if(json.has("prenom")){
            agent.setPrenom(json.getString("prenom"));

        }
        if(json.has("nom")){
            agent.setNom(json.getString("nom"));

        }
        if(json.has("dateNaissance")){
            agent.setDateNaissance(json.getString("dateNaissance"));

        }
        if(json.has("sexe")){
            agent.setSexe(json.getString("sexe"));
        }

        return (T) agent;
    }

    @Override
    public Entities getType() {
        return Entities.AdapterAgent;
    }
}
