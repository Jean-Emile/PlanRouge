package org.daum.planrouge.server.adapter.model.entity;

import org.daum.planrouge.server.adapter.model.AbstractAdapter;
import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.adapter.model.Entities;
import org.daum.planrouge.server.adapter.model.IAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.Categorie;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.impl.CategorieImpl;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 15/07/13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class AdapterCategorie implements IAdapter {

    private AdapterFactory adapterFactory;

    public AdapterCategorie() {
        this.adapterFactory = AdapterFactory.getInstance();
    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {
        JSONObject jsonCategorie = new JSONObject();
        Categorie categorie = (Categorie) container;
        jsonCategorie.put("code", categorie.getId());
        jsonCategorie.put("type", getType());
        return jsonCategorie;
    }

    @Override
    public <T> T build(JSONObject json) throws JSONException {
        Categorie categorie = adapterFactory.getFactory().createCategorie();

        if (json.has("code")) {
            categorie.setId(json.getString("code").toString());
        }

        return (T) categorie;
    }

    public Entities getType() {
        return Entities.AdapterCategorie;
    }
}
