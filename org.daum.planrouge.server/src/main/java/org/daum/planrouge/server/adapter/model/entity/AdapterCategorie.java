package org.daum.planrouge.server.adapter.model.entity;

import org.daum.planrouge.server.adapter.model.AbstractAdapter;
import org.daum.planrouge.server.adapter.model.AdapterFactory;
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
public class AdapterCategorie extends AbstractAdapter {

    AdapterFactory adapterFactory;

    public AdapterCategorie(AdapterFactory adapterFactory) {
        this.adapterFactory = adapterFactory;
    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {
        JSONObject jsonCategorie = new JSONObject();
        Categorie categorie = (Categorie) container;

        jsonCategorie.put("code", categorie.getId());
        jsonCategorie.put("type", container.getClass().getName());
        return jsonCategorie;
    }

    @Override
    public <T> T build(JSONObject json) throws JSONException {
        Categorie categorie = adapterFactory.getFactory().createCategorie();

        Iterator iteratorNewObject = json.keys();
        while (iteratorNewObject.hasNext()) {
            String keyPositionCivile = iteratorNewObject.next().toString();
            String value = null;
            try {
                value = json.getString(keyPositionCivile);
            } catch (JSONException e) {
                Log.debug(e.getMessage());
                e.printStackTrace();
                return null;
            }
            if (keyPositionCivile.equals("code")) {
                categorie.setId(value);
                Log.debug(value);
            }
        }
        return (T) categorie;
    }

    @Override
    public String getType() {
       return CategorieImpl.class.getName();
    }
}
