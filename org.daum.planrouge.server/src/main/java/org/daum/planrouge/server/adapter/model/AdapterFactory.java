package org.daum.planrouge.server.adapter.model;


import org.daum.planrouge.server.adapter.model.entity.AdapterGpsPoint;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.GpsPoint;
import org.kevoree.planrouge.PlanrougeFactory;
import org.kevoree.planrouge.Victime;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.factory.MainFactory;
import org.kevoree.planrouge.impl.GpsPointImpl;

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

    public AdapterFactory()
    {
        adapterGpsPoint =  new AdapterGpsPoint();
        adapterGpsPoint.setModelfactory(factory);

    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {

        if(container instanceof GpsPoint)
        {
           return adapterGpsPoint.build(container);
        }   else if(container instanceof Victime)
        {
            // ect
        } else {


        }

        return null;
    }

    @Override
    public  <T> T build(JSONObject json) throws JSONException {

        if(json.getString("type").equals(adapterGpsPoint.getType())){
           return adapterGpsPoint.build(json);
        } else   if(json.getString("type").equals(adapterGpsPoint.getType())){

        }


        return null;
    }


    public PlanrougeFactory getFactory() {
        return factory;
    }


}
