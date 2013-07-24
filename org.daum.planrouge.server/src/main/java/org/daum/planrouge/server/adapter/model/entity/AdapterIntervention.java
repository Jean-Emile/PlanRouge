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
    private ContainerRoot root;

    public AdapterIntervention() {
        this.adapterFactory = AdapterFactory.getInstance();
//        this.root = containerRoot;
    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {
        JSONObject jsonIntervention = new JSONObject();
        JSONArray arrayAgents = new JSONArray();
        InterventionImpl intervention = (InterventionImpl) container;
        jsonIntervention.put("id", intervention.getId());
        jsonIntervention.put("description", intervention.get_description());
        jsonIntervention.put("position", adapterFactory.build(intervention.get_position()));
        for (int i = 0; i < intervention.getAgents().size(); i++) {
           arrayAgents.put(adapterFactory.build((Agent) intervention.getAgents().get(i)));

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
                intervention.addAgents((Agent) adapterFactory.build(jsonAgents.getJSONObject(i)));

            }
        }

        return (T) intervention;
    }


    @Override
    public Entities getType() {
        return Entities.AdapterIntervention;
    }
}
