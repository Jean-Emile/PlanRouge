package com.phonegap.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.phonegap.adapter.entity.AdapterCategory;
import com.phonegap.adapter.entity.AdapterGpsHours;
import com.phonegap.adapter.entity.AdapterIdentity;
import com.phonegap.plugins.nfc.NFC_Mifare_classic;

public class ReadAll {

	private byte[] key = new NFC_Mifare_classic().hexStringToByteArray("FFFFFFFFFFFF");
	private NFC_Mifare_classic puceNFC;
	private JSONObject victimeObject;

	public JSONObject readAll(NFC_Mifare_classic puceNFC, String matriculeAgent,String idPuce) throws JSONException {
		this.puceNFC = puceNFC;
		victimeObject = new JSONObject();
		readIdentity();
		
		JSONArray arrayGps = readDateHoursGPS();
		victimeObject.put("id", idPuce);
		victimeObject.put("posRef", arrayGps.get(0));
		victimeObject.put("posDestination", arrayGps.get(1));
		victimeObject.put("categorie", readVictimCategory());
		victimeObject.put("type", "AdapterVictime");
		victimeObject.put("agent",new JSONObject().put("matricule", matriculeAgent).put("type", "AdapterAgent"));
		
		Log.e("TAG ERREUR CATEGORY",victimeObject.toString());
		return victimeObject;
	}

	private JSONObject readVictimCategory() throws JSONException {
		
		AdapterCategory adapterCategory = new AdapterCategory(null);
		String array  = adapterCategory.read(null, null, key, puceNFC);
		
		JSONArray jArray = new JSONArray(array);
			
		JSONObject categoryObject = new JSONObject();
		categoryObject.put("code", jArray.get(0));
		categoryObject.put("type", "AdapterCategorie");
		
		return categoryObject;
	}

	private JSONObject readIdentity() throws JSONException {
	
		AdapterIdentity adapterIdentity = new AdapterIdentity(null);
		String array = adapterIdentity.read(null, null, key, puceNFC);
		JSONArray jArray = new JSONArray(array);
		
		victimeObject.put("nom", jArray.get(0));
		victimeObject.put("prenom", jArray.get(1));
	
		victimeObject.put("sexe", jArray.get(2));
		if(jArray.getInt(3)!=255){
			victimeObject.put("age", jArray.getInt(3));		
		}
	
		victimeObject.put("dateNaissance", jArray.get(4));
		JSONObject victimeIdentity = new JSONObject();

		return victimeIdentity;
	}

	private JSONArray readDateHoursGPS() throws JSONException {

		AdapterGpsHours adapterGpsHours = new AdapterGpsHours(null);
		String array = adapterGpsHours.read(null, null, key, puceNFC);
		JSONArray jArray = new JSONArray(array);
		
		JSONObject gpsRefObject = new JSONObject();
		JSONArray gpsDestObject = new JSONArray();
		
		gpsRefObject.put("heure", jArray.get(0));
		gpsRefObject.put("latitude", jArray.getInt(1));
		gpsRefObject.put("longitude", jArray.getInt(2));
		gpsRefObject.put("precision", jArray.get(3));
		gpsRefObject.put("type", "AdapterGpsPoint");

		JSONObject position = new JSONObject();

		position.put("heure", jArray.get(4));
		position.put("latitude", jArray.getInt(5));
		position.put("longitude", jArray.getInt(6));
		position.put("precision", jArray.get(7));
		position.put("type", "AdapterGpsPoint");

		gpsDestObject.put(0, new JSONObject().put("gpsPoint", position));

		position = new JSONObject();

		position.put("heure", jArray.get(8));
		position.put("latitude", jArray.getInt(9));
		position.put("longitude", jArray.getInt(10));
		position.put("precision", jArray.get(11));
		position.put("type", "AdapterGpsPoint");
		gpsDestObject.put(1, new JSONObject().put("gpsPoint", position));

		position = new JSONObject();
		position.put("heure", jArray.get(12));
		position.put("latitude", jArray.getInt(13));
		position.put("longitude", jArray.getInt(14));
		position.put("precision", jArray.get(15));
		position.put("type", "AdapterGpsPoint");
		gpsDestObject.put(2, new JSONObject().put("gpsPoint", position));

		JSONArray jsonArray = new JSONArray();

		jsonArray.put(0, new JSONObject().put("gpsPoint", gpsRefObject));
		jsonArray.put(1, gpsDestObject);
		
		return jsonArray;
	}
}
