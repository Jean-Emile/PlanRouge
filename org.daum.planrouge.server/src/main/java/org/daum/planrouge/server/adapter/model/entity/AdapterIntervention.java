package org.daum.planrouge.server.adapter.model.entity;

import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.adapter.model.Entities;
import org.daum.planrouge.server.adapter.model.IAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.*;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.impl.InterventionImpl;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 16/07/13
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
public class AdapterIntervention implements IAdapter {

    private AdapterFactory adapterFactory;


    public AdapterIntervention() {
        this.adapterFactory = AdapterFactory.getInstance();

    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {
        JSONObject jsonIntervention = new JSONObject();
        JSONArray arrayAgents = new JSONArray();
        JSONArray arrayVictimes = new JSONArray();
        InterventionImpl intervention = (InterventionImpl) container;
        jsonIntervention.put("id", intervention.getId());
        jsonIntervention.put("description", intervention.get_description());
        jsonIntervention.put("position", adapterFactory.build(intervention.get_position()));
        for (int i = 0; i < intervention.getVictimes().size(); i++) {
            arrayVictimes.put(adapterFactory.build((Victime) intervention.getVictimes().get(i)));

        }
        jsonIntervention.put("victimes", arrayVictimes);
        for (int i = 0; i < intervention.getAffecte().size(); i++) {
           arrayAgents.put(adapterFactory.build((Agent) intervention.getAffecte().get(i)));

        }
        jsonIntervention.put("agents",arrayAgents);
        jsonIntervention.put("type", getType());

        return jsonIntervention;
    }

    @Override
    public <T> T build(JSONObject json) throws JSONException {
        Intervention intervention = adapterFactory.getFactory().createIntervention();

        if (json.has("id")) {
            Log.info(json.getString("id").toString());
            intervention.setId(json.getString("id").toString());
        }
        if (json.has("description")) {
            intervention.setDescription(json.getString("description").toString());
        }
        if (json.has("agents")) {
            JSONArray jsonAgents = json.getJSONArray("agents");
            for (int i = 0; i < jsonAgents.length(); i++) {
                 Log.debug(jsonAgents.get(i).toString());
                intervention.addAffecte((Agent) adapterFactory.build(jsonAgents.getJSONObject(i)));
            }
        }
        if (json.has("victimes")) {
            JSONArray jsonVictimes = json.getJSONArray("victimes");
            for (int i = 0; i < jsonVictimes.length(); i++) {
                Log.debug(jsonVictimes.get(i).toString());
                intervention.addVictimes((Victime) adapterFactory.build(jsonVictimes.getJSONObject(i)));
            }
        }
        if (json.has("position")) {
            intervention.setPosition((Position) adapterFactory.build(json.getJSONObject("position")));
        }

        return (T) intervention;
    }


    @Override
    public Entities getType() {
        return Entities.AdapterIntervention;
    }
}
