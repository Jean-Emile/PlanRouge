package com.phonegap.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.phonegap.plugins.nfc.NFC_Mifare_classic;
import com.phonegap.plugins.nfc.TagActionException;

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
		return victimeObject;
	}

	private JSONObject readVictimCategory() throws JSONException {

		String vital_urgency = "";
		try {
			vital_urgency = puceNFC.readABlock(1, 0, key, false);
		} catch (TagActionException e) {
			e.printStackTrace();
		}
		String category = "";
		if (vital_urgency.length() > 31) {
			category = vital_urgency.substring(31, 32);
		}

		JSONObject categoryObject = new JSONObject();
		categoryObject.put("code", category);
		categoryObject.put("type", "AdapterCategorie");
		return categoryObject;
	}

	private JSONObject readIdentity() throws JSONException {

		String firstname = "";
		String surname = "";
		int age = 255 ;
		String birthday = "";
		String sexe = "";

		try {
			// firstname
			firstname = puceNFC.hexToAscii(puceNFC.readABlock(0, 1, key, false));
			// surname
			surname = puceNFC.hexToAscii(puceNFC.readABlock(0, 2, key, false));
			// Autres
			String identity_infos = puceNFC.readABlock(1, 0, key, false);
			// age
				birthday = identity_infos.substring(0, 8);
				age = Integer.parseInt(identity_infos.substring(8, 10),16);
				sexe = identity_infos.substring(10, 11);
		} catch (TagActionException e) {
			e.printStackTrace();

		}

		JSONObject victimeIdentity = new JSONObject();
		victimeObject.put("nom", surname);
		victimeObject.put("prenom", firstname);
	
		victimeObject.put("sexe", sexe);
		if(age!=255){
			victimeObject.put("age", age);		
		}
	
		victimeObject.put("dateNaissance", birthday);
		victimeObject.put("type", "AdapterVictime");

		return victimeIdentity;

	}

	private JSONArray readDateHoursGPS() throws JSONException {

		String date1 = "";
		String latitude1 = "";
		String longitude1 = "";
		String signe1 = "";
		String accuracy1 = "";

		String date2 = "";
		String latitude2 = "";
		String longitude2 = "";
		String signe2 = "";
		String accuracy2 = "";

		String date3 = "";
		String latitude3 = "";
		String longitude3 = "";
		String signe3 = "";
		String accuracy3 = "";

		String date4 = "";
		String latitude4 = "";
		String longitude4 = "";
		String signe4 = "";
		String accuracy4 = "";

		try {
			String date12 = puceNFC.readABlock(3, 0, key, false);
			// 1er lieu
			date1 = (date12).substring(0, 13);
			String donneesGPS1 = puceNFC.readABlock(3, 1, key, false);
			latitude1 = donneesGPS1.substring(0, 9);
			longitude1 = donneesGPS1.substring(9, 19);
			signe1 = donneesGPS1.substring(19, 20);
			accuracy1 = donneesGPS1.substring(20, 24);

			// 2eme lieu
			date2 = (date12).substring(13, 26);
			String donneesGPS2 = puceNFC.readABlock(3, 2, key, false);
			latitude2 = donneesGPS2.substring(0, 9);
			longitude2 = donneesGPS2.substring(9, 19);
			signe2 = donneesGPS2.substring(19, 20);
			accuracy2 = donneesGPS2.substring(20, 24);

			String date34 = puceNFC.readABlock(4, 0, key, false);

			// 3eme lieu
			date3 = date34.substring(0, 13);
			String donneesGPS3 = puceNFC.readABlock(4, 1, key, false);
			latitude3 = donneesGPS3.substring(0, 9);
			longitude3 = donneesGPS3.substring(9, 19);
			signe3 = donneesGPS3.substring(19, 20);
			accuracy3 = donneesGPS3.substring(20, 24);

			// 4eme lieu
			date4 = date34.substring(13, 26);
			String donneesGPS4 = puceNFC.readABlock(4, 2, key, false);
			latitude4 = donneesGPS4.substring(0, 9);
			longitude4 = donneesGPS4.substring(9, 19);
			signe4 = donneesGPS4.substring(19, 20);
			accuracy4 = donneesGPS4.substring(20, 24);

		} catch (TagActionException e) {
			e.printStackTrace();
			return null;
		}

		JSONObject gpsRefObject = new JSONObject();
		JSONArray gpsDestObject = new JSONArray();

		Integer[] latlong1 = getLatitudeLongitude(latitude1, longitude1, signe1);
		Integer[] latlong2 = getLatitudeLongitude(latitude2, longitude2, signe2);
		Integer[] latlong3 = getLatitudeLongitude(latitude3, longitude3, signe3);
		Integer[] latlong4 = getLatitudeLongitude(latitude4, longitude4, signe4);

		gpsRefObject.put("heure", date1);
		gpsRefObject.put("latitude", latlong1[0].toString());
		gpsRefObject.put("longitude", latlong1[1].toString());
		gpsRefObject.put("precision", Integer.parseInt(accuracy1));
		gpsRefObject.put("type", "AdapterGpsPoint");

		JSONObject position = new JSONObject();

		position.put("heure", date2);
		position.put("latitude", latlong2[0].toString());
		position.put("longitude", latlong2[1].toString());
		position.put("precision", Integer.parseInt(accuracy2));
		position.put("type", "AdapterGpsPoint");

		gpsDestObject.put(0, new JSONObject().put("gpsPoint", position));

		position = new JSONObject();

		position.put("heure", date3);
		position.put("latitude", latlong3[0].toString());
		position.put("longitude", latlong3[1].toString());
		position.put("precision", Integer.parseInt(accuracy3));
		position.put("type", "AdapterGpsPoint");
		gpsDestObject.put(1, new JSONObject().put("gpsPoint", position));

		position = new JSONObject();
		position.put("heure", date4);
		position.put("latitude", latlong4[0].toString());
		position.put("longitude", latlong4[1].toString());
		position.put("precision", Integer.parseInt(accuracy4));
		position.put("type", "AdapterGpsPoint");
		gpsDestObject.put(2, new JSONObject().put("gpsPoint", position));

		JSONArray array = new JSONArray();

		array.put(0, new JSONObject().put("gpsPoint", gpsRefObject));
		array.put(1, gpsDestObject);

		return array;
	}

	private Integer[] getLatitudeLongitude(String latitude, String longitude, String signe) {

		int signe1 = Integer.parseInt(signe);
		int latitude1 = Integer.parseInt(latitude);
		int longitude1 = Integer.parseInt(longitude);
		System.out.println(longitude1 + "   " + latitude1 + "   " + latitude + "   " + longitude);
		Integer[] tab = new Integer[2];
		if (signe1 == 0) {
			tab[0] = latitude1;
			tab[1] = longitude1;
		} else if (signe1 == 1) {
			tab[0] = latitude1 * (-1);
			tab[1] = longitude1;
		} else if (signe1 == 2) {
			tab[0] = latitude1;
			tab[1] = longitude1 * (-1);
		} else {
			tab[0] = latitude1 * (-1);
			tab[1] = longitude1 * (-1);
		}
		return tab;
	}
}
