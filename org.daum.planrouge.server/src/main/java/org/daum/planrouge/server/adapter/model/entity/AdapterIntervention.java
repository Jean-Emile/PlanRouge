package org.daum.planrouge.server.adapter.model.entity;

import org.daum.planrouge.server.adapter.model.AbstractAdapter;
import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.Categorie;
import org.kevoree.planrouge.Intervention;
import org.kevoree.planrouge.Position;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.impl.InterventionImpl;
import org.kevoree.planrouge.impl.VictimeImpl;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 16/07/13
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
public class AdapterIntervention extends AbstractAdapter {

    private AdapterFactory adapterFactory;

    public AdapterIntervention(AdapterFactory adapterFactory) {
        this.adapterFactory = adapterFactory;
    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {
        JSONObject jsonCategorie = new JSONObject();
        InterventionImpl intervention = (InterventionImpl)container;
        jsonCategorie.put("id", intervention.getId());
        jsonCategorie.put("description", intervention.get_description());
        jsonCategorie.put("position", adapterFactory.build(intervention.get_position()));
        jsonCategorie.put("type", container.getClass().getName());

        return jsonCategorie;
    }

    @Override
    public <T> T build(JSONObject json) throws JSONException {
        Intervention intervention = adapterFactory.getFactory().createIntervention();
        intervention.setId(json.getString("id").toString());
        intervention.setDescription(json.getString("description").toString());
        return null;
    }


    @Override
    public String getType() {
        return InterventionImpl.class.getName();
    }

}