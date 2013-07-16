package org.daum.planrouge.server.adapter.model;


import org.daum.planrouge.server.adapter.model.entity.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.*;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.factory.MainFactory;
import org.kevoree.planrouge.impl.GpsPointImpl;
import org.kevoree.planrouge.impl.InterventionImpl;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 15/07/13
 * Time: 10:27
 * To change this template use File | Settings | File Templates.
 */
public class AdapterFactory implements IAdapterFactory {

    private PlanrougeFactory factory = new MainFactory().getPlanrougeFactory();
    // LRU list
    private AdapterGpsPoint adapterGpsPoint = null;
    private AdapterPositionCivile adapterPositionCivile = null;
    private AdapterCategorie adapterCategorie = null;
    private AdapterVictime adapterVictime = null;
    private AdapterIntervention adapterIntervention = null;


    public AdapterFactory() {

        adapterGpsPoint = new AdapterGpsPoint(this);
        adapterCategorie = new AdapterCategorie(this);
        adapterPositionCivile = new AdapterPositionCivile(this);
        adapterVictime = new AdapterVictime(this);
        adapterIntervention = new AdapterIntervention(this);
    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {

        if (container instanceof GpsPoint) {
            return adapterGpsPoint.build(container);
        } else if (container instanceof PositionCivil) {
            return adapterPositionCivile.build(container);
        } else if (container instanceof Categorie) {
            return adapterCategorie.build(container);
        } else if (container instanceof Victime) {
            return adapterVictime.build(container);
        } else if(container instanceof InterventionImpl){
            return   adapterIntervention.build(container);
        }

        return null;
    }

    @Override
    public <T> T build(JSONObject json) throws JSONException {
        System.out.println(adapterGpsPoint.getType());
        if (json.getString("type").equals(adapterGpsPoint.getType())) {
            return adapterGpsPoint.build(json);
        } else if (json.getString("type").equals(adapterPositionCivile.getType())) {
            return adapterPositionCivile.build(json);
        } else if (json.getString("type").equals(adapterCategorie.getType())) {
            return adapterCategorie.build(json);
        } else if (json.getString("type").equals(adapterVictime.getType())) {
            return adapterVictime.build(json);
        }
        return null;
    }


    public PlanrougeFactory getFactory() {
        return factory;
    }


}
