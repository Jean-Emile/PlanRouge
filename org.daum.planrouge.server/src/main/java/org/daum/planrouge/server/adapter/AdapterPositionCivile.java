package org.daum.planrouge.server.adapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.PlanrougeFactory;
import org.kevoree.planrouge.PositionCivil;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 11/07/13
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
public class AdapterPositionCivile {
    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;


    public AdapterPositionCivile(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot) {
        this.planrougeFactory = planrougeFactory;
        this.containerRoot = containerRoot;
    }

    // Parse JSONObject to Postition Civile
    public PositionCivil parseJsonToPositionCivile(JSONObject jsonPositionCivile){
        PositionCivil positionCivile = this.planrougeFactory.createPositionCivil();

        Iterator iteratorNewObject = jsonPositionCivile.keys();
        while (iteratorNewObject.hasNext()) {
            String keyPositionCivile = iteratorNewObject.next().toString();
            String value = null;
            try {
                value = jsonPositionCivile.getString(keyPositionCivile);
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
        return positionCivile;
    }

    public JSONObject parsePositionCivileToJson (PositionCivil positionCivile) throws JSONException {

        JSONObject jsonGps = new JSONObject();
        jsonGps.put("numeroRue", positionCivile.getNumeroRue());
        jsonGps.put("cp", positionCivile.getCp());
        jsonGps.put("pays", positionCivile.getPays());
        jsonGps.put("nomRue", positionCivile.getNomRue());
        jsonGps.put("nomVille", positionCivile.getNomVille());
        jsonGps.put("heure", positionCivile.getNomRue());

        return jsonGps;
    }

}
