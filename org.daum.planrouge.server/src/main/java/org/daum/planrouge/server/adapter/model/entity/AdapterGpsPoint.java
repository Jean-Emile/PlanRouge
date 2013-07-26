package org.daum.planrouge.server.adapter.model.entity;

import org.daum.planrouge.server.adapter.model.AbstractAdapter;
import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.daum.planrouge.server.adapter.model.Entities;
import org.daum.planrouge.server.adapter.model.IAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.ContainerRoot;
import org.kevoree.planrouge.GpsPoint;
import org.kevoree.planrouge.PlanrougeFactory;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.impl.GpsPointImpl;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 11/07/13
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
public class AdapterGpsPoint implements IAdapter {
    private AdapterFactory adapterFactory;

    public AdapterGpsPoint() {
        this.adapterFactory =  AdapterFactory.getInstance();
    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {
        JSONObject jsonGps = new JSONObject();
        GpsPoint gpsPoint = (GpsPoint) container;
        jsonGps.put("latitude", gpsPoint.getLatitude());
        jsonGps.put("longitude", gpsPoint.getLongitude());
        jsonGps.put("precision", gpsPoint.getPrecision());
        jsonGps.put("heure", gpsPoint.getHorodatage());
        jsonGps.put("type", getType());
        return jsonGps;
    }

    // Parse JSONObject to GPSPoint
    @Override
    public <T> T build(JSONObject json) throws JSONException {
        GpsPoint gpsPoint = adapterFactory.getFactory().createGpsPoint();

        if (json.has("heure")) {
            gpsPoint.setHorodatage(json.getString("heure").toString());
        }
        if (json.has("longitude")) {
            gpsPoint.setLongitude(Integer.parseInt(json.getString("longitude").toString()));
        }
        if (json.has("latitude")) {
            gpsPoint.setLatitude(Integer.parseInt(json.getString("latitude").toString()));
        }
        if (json.has("precision")) {
            gpsPoint.setPrecision(Integer.parseInt(json.getString("precision").toString()));
        }


        return (T) gpsPoint;
    }

    @Override
    public Entities getType() {
        return Entities.AdapterGpsPoint;
    }
}
