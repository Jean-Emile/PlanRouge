package com.phonegap.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import com.phonegap.plugins.nfc.NFC_Mifare_classic;
import com.phonegap.plugins.nfc.TagActionException;

public class ReadAll {

	private static byte[] key = new NFC_Mifare_classic().hexStringToByteArray("FFFFFFFFFFFF");
	private static Intent savedIntent;
	private static NFC_Mifare_classic puceNFC; 
	public JSONObject readAll(NFC_Mifare_classic puceNFC) throws JSONException {
		this.puceNFC = puceNFC;
		JSONObject victimeObject = new JSONObject();
		victimeObject.put("identity", readIdentity());
		victimeObject.put("categorie", readVictimCategory());
		victimeObject.put("posRef", readDateHoursGPS().get(0));
		victimeObject.put("posDestination", readDateHoursGPS().get(1));
		victimeObject.put("type", "AdapterVictime");
		return victimeObject;
	}

	private JSONObject readVictimCategory() throws JSONException {

//			NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
//			puceNFC.treatAsNewTag(savedIntent);
	
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

//		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
//
//		if (puceNFC.treatAsNewTag(savedIntent) == -1) {
//
//			return null;
//		}

		String firstname = "";
		String surname = "";
		String age = "";

		String birthday = "";
		String sexe = "";

		try {
			// firstname
			firstname = puceNFC.hexToAscii(puceNFC.readABlock(0, 1, key, false));
			// surname
			surname = puceNFC.hexToAscii(puceNFC.readABlock(0, 2, key, false));

			// Autres
			String identity_infos = puceNFC.hexToAscii(puceNFC.readABlock(1, 0, key, false));
			// age
			if (identity_infos.length() > 12) {
				birthday = identity_infos.substring(0, 8);
				age = identity_infos.substring(8, 11);

				sexe = identity_infos.substring(12, 13);
			}
		} catch (TagActionException e) {

			e.printStackTrace();

		}

		JSONObject victimeIdentity = new JSONObject();
		victimeIdentity.put("nom", surname);
		victimeIdentity.put("prenom", firstname);
		victimeIdentity.put("id", puceNFC.getId());
		victimeIdentity.put("sexe", sexe);
		victimeIdentity.put("age", age);
		victimeIdentity.put("dateNaissance", birthday);
		victimeIdentity.put("type", "AdapterVictime");

		return victimeIdentity;

	}

	private JSONArray readDateHoursGPS() throws JSONException {
//		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
//		puceNFC.treatAsNewTag(savedIntent);

		//

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
		String accuracy3 = null;

		String date4 = "";
		String latitude4 = "";
		String longitude4 = "";
		String signe4 = "";
		String accuracy4 = null;

		try {
			String date12 = puceNFC.readABlock(3, 0, key, false);
			// 1er lieu
			date1 = (date12).substring(0, 14);
			String donneesGPS1 = puceNFC.readABlock(3, 1, key, false);
			latitude1 = donneesGPS1.substring(0, 9);
			longitude1 = donneesGPS1.substring(9, 19);
			signe1 = donneesGPS1.substring(19, 20);
			accuracy1 = donneesGPS1.substring(20, 24);

			// 2eme lieu
			date2 = (date12).substring(14, 28);
			String donneesGPS2 = puceNFC.readABlock(3, 2, key, false);
			latitude2 = donneesGPS2.substring(0, 9);
			longitude2 = donneesGPS2.substring(9, 19);
			signe2 = donneesGPS2.substring(19, 20);
			accuracy2 = donneesGPS2.substring(20, 24);

			String date34 = puceNFC.readABlock(4, 0, key, false);

			// 3eme lieu
			date3 = date34.substring(0, 14);
			String donneesGPS3 = puceNFC.readABlock(4, 1, key, false);
			latitude3 = donneesGPS3.substring(0, 9);
			longitude3 = donneesGPS3.substring(9, 19);
			signe3 = donneesGPS3.substring(19, 20);
			accuracy3 = donneesGPS3.substring(20, 24);

			// 4eme lieu
			date4 = date34.substring(14, 28);
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
		
		array.put(0, new JSONObject().put("gpsPoint", gpsRefObject) );
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
