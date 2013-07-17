package org.daum.planrouge.server.adapter.model.entity;

import org.daum.planrouge.server.adapter.model.AbstractAdapter;
import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.GpsPoint;
import org.kevoree.planrouge.PositionCivil;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.impl.PositionCivilImpl;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 15/07/13
 * Time: 15:28
 * To change this template use File | Settings | File Templates.
 */
public class AdapterPositionCivile extends AbstractAdapter {

    private AdapterFactory adapterFactory;

    public AdapterPositionCivile(AdapterFactory adapterFactory) {
        this.adapterFactory = adapterFactory;
    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {
        JSONObject jsonGps = new JSONObject();
        PositionCivil positionCivile = (PositionCivil) container;
        jsonGps.put("numeroRue", positionCivile.getNumeroRue());
        jsonGps.put("cp", positionCivile.getCp());
        jsonGps.put("pays", positionCivile.getPays());
        jsonGps.put("nomRue", positionCivile.getNomRue());
        jsonGps.put("nomVille", positionCivile.getNomVille());
        jsonGps.put("heure", positionCivile.getNomRue());
        jsonGps.put("type", container.getClass().getName());
        return jsonGps;
    }

    @Override
    public <T> T build(JSONObject json) throws JSONException {
        PositionCivil positionCivile = adapterFactory.getFactory().createPositionCivil();
        if (json.has("cp")) {
            positionCivile.setCp(json.getString("cp").toString());
        }
        if (json.has("nomRue")) {
            positionCivile.setNomRue(json.getString("nomRue").toString());
        }
        if (json.has("numeroRue")) {
            positionCivile.setNumeroRue(json.getString("numeroRue").toString());
        }
        if (json.has("pays")) {
            positionCivile.setPays(json.getString("pays").toString());
        }
        if (json.has("heure")) {
            positionCivile.setHorodatage(json.getString("heure").toString());
        }
        if (json.has("nomVille")) {
            positionCivile.setHorodatage(json.getString("nomVille").toString());
        }
        return (T) positionCivile;
    }

    @Override
    public AdapterFactory.Adapters getType() {
        return AdapterFactory.Adapters.AdapterPositionCivile;
    }
}
