package org.daum.planrouge.server.adapter.model;


import org.daum.planrouge.server.adapter.model.entity.*;
import org.daum.planrouge.server.utils.LRUMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
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
    private LRUMap adapterLRU   = new LRUMap(4);

    private static AdapterFactory singleton = null;


    public static AdapterFactory getInstance(){
        if(singleton == null){
            singleton   = new AdapterFactory();
        }
        return singleton;
    }

    public  synchronized IAdapter getAdapter(Entities id){
        IAdapter instance =null;
        if(adapterLRU.containsKey(id)){
            return(IAdapter) adapterLRU.get(id);
        } else
        {
            switch (id)
            {
                case  AdapterGpsPoint:
                    instance  =  new AdapterGpsPoint();
                    break;
                case AdapterPositionCivile:
                    instance  =new AdapterPositionCivile();
                    break;
                case AdapterCategorie:
                    instance  =new AdapterCategorie();
                    break;
                case AdapterVictime:
                    instance  =new AdapterVictime();
                    break;
                case AdapterIntervention:
                    instance  =new AdapterIntervention();
                    break;
            }
            adapterLRU.put(id,instance);
            return instance;
        }
    }
    @Override
    public JSONObject build(KMFContainer container) throws JSONException {

        if (container instanceof GpsPoint) {
            return getAdapter(Entities.AdapterGpsPoint).build(container);
        } else if (container instanceof PositionCivil) {
            return getAdapter(Entities.AdapterPositionCivile).build(container);
        } else if (container instanceof Categorie) {
            return getAdapter(Entities.AdapterCategorie).build(container);
        } else if (container instanceof Victime) {
            return getAdapter(Entities.AdapterVictime).build(container);
        } else if(container instanceof InterventionImpl){
            return   getAdapter(Entities.AdapterIntervention).build(container);
        }

        return null;
    }



    @Override
    public <T> T build(JSONObject json) throws JSONException {

        if (json.getString("type").equals(Entities.AdapterGpsPoint.toString())) {
            return getAdapter(Entities.AdapterGpsPoint).build(json);
        } else  if (json.getString("type").equals(Entities.AdapterPositionCivile.toString())) {
            return getAdapter(Entities.AdapterPositionCivile).build(json);
        } else  if (json.getString("type").equals(Entities.AdapterIntervention.toString())) {
            return getAdapter(Entities.AdapterIntervention).build(json);
        } else  if (json.getString("type").equals(Entities.AdapterVictime.toString())) {
            return getAdapter(Entities.AdapterVictime).build(json);
        }  else  if (json.getString("type").equals(Entities.AdapterCategorie.toString())) {
            return getAdapter(Entities.AdapterCategorie).build(json);
        }
        return null;
    }


    public PlanrougeFactory getFactory() {
        return factory;
    }


}
