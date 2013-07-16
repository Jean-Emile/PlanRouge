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

    AdapterFactory adapterFactory;

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
        Iterator iteratorNewObject = json.keys();
        while (iteratorNewObject.hasNext()) {
            String keyPositionCivile = iteratorNewObject.next().toString();
            String value = null;
            try {
                value = json.getString(keyPositionCivile);
            } catch (JSONException e) {
                Log.debug(e.getMessage());
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return null;
            }
            if (keyPositionCivile.equals("cp")) {
                positionCivile.setCp(value);
            } else if (keyPositionCivile.equals("nomRue")) {
                positionCivile.setNomRue(value);
            } else if (keyPositionCivile.equals("numeroRue")) {
                positionCivile.setNumeroRue(value);
            } else if (keyPositionCivile.equals("pays")) {
                positionCivile.setPays(value);
            } else if (keyPositionCivile.equals("heure")) {
                positionCivile.setHorodatage(value);
            }
        }
        return (T) positionCivile;
    }

    @Override
    public String getType() {
        return PositionCivilImpl.class.getName();
    }
}
