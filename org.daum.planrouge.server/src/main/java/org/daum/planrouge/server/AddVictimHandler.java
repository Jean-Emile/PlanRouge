package org.daum.planrouge.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webbitserver.*;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: cbriand
 * Date: 09/07/13
 * Time: 13:39
 * To change this template use File | Settings | File Templates.
 */
public class AddVictimHandler extends BaseWebSocketHandler {

    private int connectionCount;

    public void onOpen(WebSocketConnection connection) {
        System.out.println("Nouvelle connexion");
        connection.send("There are " + connectionCount + " other connections active");
        connectionCount++;
    }

    public void onClose(WebSocketConnection connection) {
        System.out.println("Connexion fermée ");
        connectionCount--;

    }

    public void onMessage(WebSocketConnection connection, String message) throws JSONException {
        System.out.println("ON_MESSAGE");
        addinfos(message);
        connection.send("Nombre de victimes :: "+"\n"); // echo back message in upper case

    }

    public void addinfos(String message) throws JSONException {
        JSONObject jObject = new JSONObject(objetJSON);
        JSONArray jarray = jObject.getJSONArray("persons");
        JSONObject jNewObject = new JSONObject(message);
        String id = jNewObject.getString("id");


        // SI L'ID est déja présent dans la base

        for (int i = 0; i < jarray.length(); i++) {
            // On récupère l'objet JSON de la personne à l'id "i"
            JSONObject person = jarray.getJSONObject(i);
            //On  récupère la valeur de l'id
            String id2 = person.getString("id");
            //Si l'id récupéré correspond à celui de la victime à enregistrer
            if (id2.equals(id)) {
                //iterarateur pour parcourir les différentes valeurs du message reçu
                Iterator iterator = jNewObject.keys();
                while (iterator.hasNext()) {
                    // On récupère une clé du message
                    String key = iterator.next().toString();
                    //Si la clé correspond à Identity
                    if (key.equals("identity")) {
                        // On récupére l'objet JSON identity
                        JSONObject IdentityObject = jNewObject.getJSONObject(key);
                        //on crée un iterator pour l'objet identity
                        Iterator iteratorNewObject = IdentityObject.keys();
                        while (iteratorNewObject.hasNext()) {
                            String keyNew = iteratorNewObject.next().toString();
                            String j = IdentityObject.getString(keyNew);
                            person.getJSONObject(key).put(keyNew, j);
                        }
                    } else if (key.equals("GPS_Hours")) {
                        // On récupére l'objet JSON GPS et Heure
                        JSONObject GPSObject = jNewObject.getJSONObject(key);
                        // On récupére le JSONArray GPS_hours du fichier person
                        JSONArray array =person.getJSONArray(key);
                        //On ajoute l'objet en queue de tableau
                        array.put(array.length(),GPSObject) ;
                    } else {
                        String j = jNewObject.getString(key);
                        person.put(key, j);
                    }
                }
                objetJSON = jObject.toString();
                refresh();
                return;
            }


        }

        // Si l'ID n'est pas présent, on crée un nouvel objet

        JSONObject jObjectVierge = new JSONObject(jsonObjetVierge);

        //iterarateur pour parcourir les différentes valeurs du message reçu
        Iterator iterator = jNewObject.keys();
        while (iterator.hasNext()) {
            // On récupère une clé du message
            String key = iterator.next().toString();
            //Si la clé correspond à Identity
            if (key.equals("identity")) {
                // On récupére l'objet JSON identity
                JSONObject IdentityObject = jNewObject.getJSONObject(key);
                //on crée un iterator pour l'objet identity
                Iterator iteratorNewObject = IdentityObject.keys();
                while (iteratorNewObject.hasNext()) {
                    String keyNew = iteratorNewObject.next().toString();
                    String j = IdentityObject.getString(keyNew);
                    jObjectVierge.getJSONObject(key).put(keyNew, j);
                }
            } else if (key.equals("GPS_Hours")) {
                // On récupére l'objet JSON GPS et Heure
                JSONObject GPSObject = jNewObject.getJSONObject(key);
                // On récupére le JSONArray GPS_hours du fichier person
                JSONArray array =jObjectVierge.getJSONArray(key);
                //On ajoute l'objet en queue de tableau
                array.put(array.length(),GPSObject) ;
            } else {
                String j = jNewObject.getString(key);
                jObjectVierge.put(key, j);
            }
        }



        jarray.put(jarray.length(),jObjectVierge);
        objetJSON = jObject.toString();
        System.out.println(jObject.toString());
    }

}
