package org.daum.planrouge.server.adapter;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.kevoree.log.Log;
import org.kevoree.planrouge.*;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 11/07/13
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
public class AdapterVictime {

    private PlanrougeFactory planrougeFactory;
    private ContainerRoot containerRoot;


    public AdapterVictime(PlanrougeFactory planrougeFactory, ContainerRoot containerRoot) {
        this.planrougeFactory = planrougeFactory;
        this.containerRoot = containerRoot;
    }


    public JSONObject parseVictimeToJson(Victime victime) throws JSONException {

        JSONObject jsonObject = new JSONObject();

        //Identité
        JSONObject jsonIdentity = new JSONObject();

        jsonIdentity.put("nom", victime.getNom());
        jsonIdentity.put("prenom", victime.getPrenom());
        jsonIdentity.put("id", victime.getId());
        jsonIdentity.put("age", victime.getAge());
        jsonIdentity.put("dateNaissance", victime.getDateNaissance());

        jsonObject.put("identite", jsonIdentity);


        if (victime.getPosRef() != null) {
            //Poistion de Référence
            JSONObject position = new JSONObject();

            if (victime.getPosRef() instanceof GpsPoint) {
                //Position GPS
                GpsPoint gpsPoint = (GpsPoint) victime.getPosRef();
                AdapterGpsPoint adapterGpsPoint = new AdapterGpsPoint(planrougeFactory, containerRoot);
                JSONObject jGpsPoint = adapterGpsPoint.parseGPSToJson(gpsPoint);

                position.put("gpsPoint", jGpsPoint);

            } else if (victime.getPosRef() instanceof PositionCivil) {
                //Position Civile
                PositionCivil positionCivile = (PositionCivil) victime.getPosRef();
                AdapterPositionCivile adapterPositionCivile = new AdapterPositionCivile(planrougeFactory, containerRoot);
                JSONObject jPositionCivile = adapterPositionCivile.parsePositionCivileToJson(positionCivile);

                position.put("positionCivile", jPositionCivile);
            }


            jsonObject.put("posRef", position);
        }

        if (victime.getPosDestination() != null) {
            //Destination
            JSONObject position = new JSONObject();

            if (victime.getPosRef() instanceof GpsPoint) {
                //Position GPS
                GpsPoint gpsPoint = (GpsPoint) victime.getPosDestination();
                AdapterGpsPoint adapterGpsPoint = new AdapterGpsPoint(planrougeFactory, containerRoot);
                JSONObject jGpsPoint = adapterGpsPoint.parseGPSToJson(gpsPoint);

                position.put("gpsPoint", jGpsPoint);

            } else if (victime.getPosRef() instanceof PositionCivil) {
                //Position Civile
                PositionCivil positionCivile = (PositionCivil) victime.getPosDestination();
                AdapterPositionCivile adapterPositionCivile = new AdapterPositionCivile(planrougeFactory, containerRoot);
                JSONObject jPositionCivile = adapterPositionCivile.parsePositionCivileToJson(positionCivile);

                position.put("positionCivile", jPositionCivile);
            }

            jsonObject.put("positionDestination", position);
        }
        if (victime.getPriorite() != null) {
            //Priority
            AdapterCategorie adapterCategorie = new AdapterCategorie(planrougeFactory, containerRoot);
            JSONObject jPriorite = adapterCategorie.parseCategorieToJson(victime.getPriorite());

            jsonObject.put("categorie", jPriorite);
        }

        Log.debug("VICTIME to JSON " + jsonObject);

        return new JSONObject();
    }


    public Victime parseJsonToVictime(String jsonMessage) {

        JSONObject jNewObject = null;
        try {
            jNewObject = new JSONObject(jsonMessage);
        } catch (JSONException e) {
            Log.debug(e.getMessage());

            return null;
        }
        Victime victime = this.planrougeFactory.createVictime();

        //iterarateur pour parcourir les différentes valeurs du message reçu
        Iterator iterator = jNewObject.keys();
        while (iterator.hasNext()) {
            // On récupère une clé du message
            String key = iterator.next().toString();

            //Si la clé correspond à Identity
            if (key.equals("identity")) {
                // Get Identity Object
                JSONObject IdentityObject = null;
                try {
                    IdentityObject = jNewObject.getJSONObject(key);
                } catch (JSONException e) {
                    Log.debug(e.getMessage());

                    return null;
                }
                //on crée un iterator pour l'objet identity
                Iterator iteratorNewObject = IdentityObject.keys();
                while (iteratorNewObject.hasNext()) {
                    String keyIdentity = iteratorNewObject.next().toString();
                    String value = null;
                    try {
                        value = IdentityObject.getString(keyIdentity);
                    } catch (JSONException e) {
                        Log.debug(e.getMessage());
                        return null;
                    }
                    if (keyIdentity.equals("id")) {
                        victime.setId(value);
                    } else if (keyIdentity.equals("nom")) {
                        victime.setNom(value);
                    } else if (keyIdentity.equals("prenom")) {
                        victime.setPrenom(value);
                    } else if (keyIdentity.equals("sexe")) {
                        victime.setAge(Integer.parseInt(value));
                    } else if (keyIdentity.equals("dateNaissance")) {
                        victime.setDateNaissance(value);
                    }
                }
            } else if (key.equals("positionRef")) {

                JSONObject jsonPositionRef = null;
                try {
                    jsonPositionRef = jNewObject.getJSONObject(key);
                } catch (JSONException e) {
                    Log.debug(e.getMessage());
                    return null;
                }
                Iterator iteratorPositionRef = jsonPositionRef.keys();
                while (iteratorPositionRef.hasNext()) {
                    String keyPositionRef = iteratorPositionRef.next().toString();

                    if (keyPositionRef.equals("gpsPoint")) {
                        // On récupére l'objet JSON GPSPoint
                        JSONObject jsonGPSPoint = null;
                        try {
                            jsonGPSPoint = jsonPositionRef.getJSONObject(keyPositionRef);
                        } catch (JSONException e) {
                            Log.debug(e.getMessage());
                            return null;
                        }

                        AdapterGpsPoint adapterGpsPoint = new AdapterGpsPoint(planrougeFactory, containerRoot);
                        Position gpsPoint = adapterGpsPoint.parseJsonToGPS(jsonGPSPoint);

                        if (gpsPoint != null) {
                            victime.setPosRef(gpsPoint);
                        }
                    } else if (keyPositionRef.equals("positionCivile")) {
                        // On récupére l'objet JSON PositionCivile
                        JSONObject jsonGPSPoint = null;
                        try {
                            jsonGPSPoint = jsonPositionRef.getJSONObject(keyPositionRef);
                        } catch (JSONException e) {
                            Log.debug(e.getMessage());
                            return null;
                        }
                        AdapterPositionCivile adapterPositionCivile = new AdapterPositionCivile(planrougeFactory, containerRoot);
                        Position positionCivile = adapterPositionCivile.parseJsonToPositionCivile(jsonGPSPoint);
                        if (positionCivile != null) {
                            victime.setPosRef(positionCivile);
                        }
                    }
                }
            } else if (key.equals("positionDestination")) {
                JSONObject jsonPositionDestination = null;
                try {
                    jsonPositionDestination = jNewObject.getJSONObject(key);
                } catch (JSONException e) {
                    Log.debug(e.getMessage());
                    return null;
                }
                Iterator iteratorpositionDestination = jsonPositionDestination.keys();
                while (iteratorpositionDestination.hasNext()) {
                    String keyPositionRef = iteratorpositionDestination.next().toString();
                    if (keyPositionRef.equals("gpsPoint")) {
                        // On récupére l'objet JSON GPSPoint
                        JSONObject jsonGPSPoint = null;
                        try {
                            jsonGPSPoint = jsonPositionDestination.getJSONObject(keyPositionRef);
                        } catch (JSONException e) {
                            Log.debug(e.getMessage());
                            return null;
                        }
                        AdapterGpsPoint adapterGpsPoint = new AdapterGpsPoint(planrougeFactory, containerRoot);
                        Position gpsPoint = adapterGpsPoint.parseJsonToGPS(jsonGPSPoint);
                        if (gpsPoint != null) {
                            victime.addPosDestination(gpsPoint);
                        }
                    } else if (keyPositionRef.equals("positionCivile")) {
                        // On récupére l'objet JSON PositionCivile
                        JSONObject jsonGPSPoint = null;
                        try {
                            jsonGPSPoint = jsonPositionDestination.getJSONObject(keyPositionRef);
                        } catch (JSONException e) {
                            Log.debug(e.getMessage());
                            return null;
                        }

                        AdapterPositionCivile adapterPositionCivile = new AdapterPositionCivile(planrougeFactory, containerRoot);
                        Position positionCivile = adapterPositionCivile.parseJsonToPositionCivile(jsonGPSPoint);
                        if (positionCivile != null) {
                            victime.addPosDestination(positionCivile);
                        }
                    }
                }
            } else if (key.equals("categorie")) {
                // On récupére l'objet JSON Categorie
                JSONObject jsonGPSPoint = null;
                try {
                    jsonGPSPoint = jNewObject.getJSONObject(key);
                } catch (JSONException e) {
                    Log.debug(e.getMessage());
                    return null;
                }
                AdapterCategorie adapterCategorie = new AdapterCategorie(planrougeFactory, containerRoot);
                Categorie categorie = adapterCategorie.parseJsonToCategorie(jsonGPSPoint);
                if (categorie != null) {
                    victime.setPriorite(categorie);
                }
            } else {
                return null;
            }
        }

        if (victime.getId() == null) {
            return null;
        }
        return victime;
    }

    public int getNombreVictimes() {
        return containerRoot.findInterventionsByID("1").getVictimes().size();
    }

    public int[] getNombreVictimeParCategorie() {
        List<Victime> victimes = containerRoot.findInterventionsByID("1").getVictimes();
        int nombreCat1 = 0;
        int nombreCat2 = 0;
        int nombreCat3 = 0;
        int nombreCat4 = 0;
        int nombreCat5 = 0;

        for (int i = 0; i < victimes.size(); i++) {
            if (victimes.get(i).getPriorite().getId().equals("1")) {
                nombreCat1++;
            } else if (victimes.get(i).getPriorite().getId().equals("2")) {
                nombreCat2++;
            } else if (victimes.get(i).getPriorite().getId().equals("3")) {
                nombreCat3++;
            } else if (victimes.get(i).getPriorite().getId().equals("4")) {
                nombreCat4++;
            } else if (victimes.get(i).getPriorite().getId().equals("5")) {
                nombreCat5++;
            }
        }
        int nombreParCategorie[] = {nombreCat1, nombreCat2, nombreCat3, nombreCat4, nombreCat5};
        return nombreParCategorie;
    }

}
