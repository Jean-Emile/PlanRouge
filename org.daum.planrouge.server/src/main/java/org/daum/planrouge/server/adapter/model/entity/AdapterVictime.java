package org.daum.planrouge.server.adapter.model.entity;

import org.daum.planrouge.server.adapter.model.AbstractAdapter;
import org.daum.planrouge.server.adapter.model.AdapterFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.*;
import org.kevoree.planrouge.container.KMFContainer;
import org.kevoree.planrouge.impl.VictimeImpl;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 15/07/13
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class AdapterVictime extends AbstractAdapter {

    AdapterFactory adapterFactory;

    public AdapterVictime(AdapterFactory adapterFactory) {
        this.adapterFactory = adapterFactory;
    }

    @Override
    public JSONObject build(KMFContainer container) throws JSONException {

        JSONObject jsonVictime = new JSONObject();
        Victime victime = (Victime) container;
        jsonVictime.put("type", container.getClass().getName());
        //Identité
        JSONObject jsonIdentity = new JSONObject();

        jsonIdentity.put("nom", victime.getNom());
        jsonIdentity.put("prenom", victime.getPrenom());
        jsonIdentity.put("id", victime.getId());
        jsonIdentity.put("age", victime.getAge());
        jsonIdentity.put("dateNaissance", victime.getDateNaissance());

        jsonVictime.put("identite", jsonIdentity);


        if (victime.getPosRef() != null) {
            //Poistion de Référence
            JSONObject position = new JSONObject();

            if (victime.getPosRef() instanceof GpsPoint) {
                //Position GPS
                GpsPoint gpsPoint = (GpsPoint) victime.getPosRef();
                JSONObject jGpsPoint = adapterFactory.build(gpsPoint);

                position.put("gpsPoint", jGpsPoint);

            } else if (victime.getPosRef() instanceof PositionCivil) {
                //Position Civile
                PositionCivil positionCivile = (PositionCivil) victime.getPosRef();

                JSONObject jPositionCivile = adapterFactory.build(positionCivile);

                position.put("positionCivile", jPositionCivile);
            }


            jsonVictime.put("posRef", position);
        }

        if (victime.getPosDestination() != null) {
            //Destination
            JSONObject position = new JSONObject();

            if (victime.getPosRef() instanceof GpsPoint) {
                //Position GPS
                GpsPoint gpsPoint = (GpsPoint) victime.getPosDestination();
                JSONObject jGpsPoint = adapterFactory.build(gpsPoint);

                position.put("gpsPoint", jGpsPoint);

            } else if (victime.getPosRef() instanceof PositionCivil) {
                //Position Civile
                PositionCivil positionCivile = (PositionCivil) victime.getPosDestination();
                JSONObject jPositionCivile = adapterFactory.build(positionCivile);

                position.put("positionCivile", jPositionCivile);
            }

            jsonVictime.put("positionDestination", position);
        }
        if (victime.getPriorite() != null) {
            //Priority
            JSONObject jPriorite = adapterFactory.build(victime.getPriorite());

            jsonVictime.put("categorie", jPriorite);
        }

        Log.debug("VICTIME to JSON " + jsonVictime);

        return jsonVictime;

    }

    @Override
    public <T> T build(JSONObject json) throws JSONException {

        Victime victime = adapterFactory.getFactory().createVictime();
        if (json.has("identity")) {
            Log.info("has identity");
            if (json.getJSONObject("identity").has("prenom")) {
                Log.info("has prenom");
                victime.setPrenom(json.getJSONObject("identity").getString("prenom").toString());
            }
            if (json.getJSONObject("identity").has("nom")) {
                Log.info("has nom");
                victime.setPrenom(json.getJSONObject("identity").getString("nom").toString());
            }
            if (json.getJSONObject("identity").has("id")) {
                Log.info("has id");
                victime.setId(json.getJSONObject("identity").getString("id").toString());
            }
            if (json.has("dateNaissance")) {
                Log.info("has dateNaissance");
                victime.setDateNaissance(json.getJSONObject("identity").getString("dateNaissance").toString());
            }
            if (json.has("sexe")) {
                Log.info("has sexe");
                victime.setSexe(json.getJSONObject("identity").getString("sexe").toString());
            }
            if (json.has("age")) {
                Log.info("has age");
                victime.setAge(json.getJSONObject("identity").getInt("age"));
            }
        }
        if (json.has("categorie")) {
            Log.info("has category");
            victime.setPriorite((Categorie) adapterFactory.build(json.getJSONObject("categorie")));
        }

        if (json.has("positionRef")) {
            Log.info("has positionRef");
            if (json.getJSONObject("positionRef").has("gpsPoint")) {
                Log.info("has gpsPoint");
                victime.setPosRef((GpsPoint) adapterFactory.build(json.getJSONObject("positionRef").getJSONObject("gpsPoint")));
            }
            if (json.getJSONObject("positionRef").has("positionCivile")) {
                Log.info("has positionCivile");
                victime.setPosRef((PositionCivil) adapterFactory.build(json.getJSONObject("positionRef").getJSONObject("positionCivile")));
            }

        }
        if (json.has("positionDestination")) {
            Log.info("has positionDestination");
            if (json.getJSONObject("positionDestination").has("gpsPoint")) {
                Log.info("has gpsPoint");
                victime.setPosRef((GpsPoint) adapterFactory.build(json.getJSONObject("positionDestination").getJSONObject("gpsPoint")));
            }
            if (json.getJSONObject("positionDestination").has("positionCivile")) {
                Log.info("has positionCivile");
                victime.setPosRef((PositionCivil) adapterFactory.build(json.getJSONObject("positionDestination").getJSONObject("positionCivile")));
            }

        }

        if (victime.getId() == null || victime.getId().equals("")) {
            Log.info("ID victime null  :: "+victime.getId());
            return null;
        }

        return (T) victime;


    }

    @Override
    public String getType() {
        return VictimeImpl.class.getName();
    }


}
