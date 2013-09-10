package com.phonegap.adapter.entity;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.phonegap.adapter.AdapterFactory;
import com.phonegap.adapter.NFC_adapter;
import com.phonegap.adapter.ReadAll;
import com.phonegap.plugins.ReadWritePlugin;
import com.phonegap.plugins.nfc.NFC_Mifare_classic;
import com.phonegap.plugins.nfc.TagActionException;

public class AdapterGpsHours implements Runnable, NFC_adapter {

	private AdapterFactory adapterFactory;
	private String dataToSend;

	public AdapterGpsHours(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}

	@Override
	public String write(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC, ReadWritePlugin nfcPlugin) throws JSONException {


		String id = puceNFC.getId();
		
		String newDate = data.getString(1);
		String latitude = data.getString(2);
		String longitude = data.getString(3);
		String signe = data.getString(4);
		String accuracy = data.getString(5);
		while (latitude.length() < 9) {
			latitude = "0" + latitude;
		}
		while (longitude.length() < 10) {
			longitude = "0" + longitude;
		}
		while (accuracy.length() < 4) {
			accuracy = "0" + accuracy;
		}
		String donneesGPSAEnvoyer = latitude + longitude + signe + accuracy;
		donneesGPSAEnvoyer = adapterFactory.completeTheBlank(donneesGPSAEnvoyer);
		String PremierEnregistrement = "";

		// Récupération données
		String date3 = null;
		String date2 = null;
		String date1 = null;
		String donneesGPS2 = null;
		String donneesGPS3 = null;

		try {
			donneesGPS2 = puceNFC.readABlock(3, 2, key, false);
			donneesGPS3 = puceNFC.readABlock(4, 1, key, false);
			String date12 = puceNFC.readABlock(3, 0, key, false);
			date1 = date12.substring(0, 13);
			date2 = date12.substring(13, 26);
			date3 = (puceNFC.readABlock(4, 0, key, false)).substring(0, 13);
		} catch (TagActionException e1) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e1.getMessage()));
			e1.printStackTrace();
		}

		// nouvelle dates secteur 4
		String date34 = adapterFactory.completeTheBlank(date2 + date3);
		String date12 = adapterFactory.completeTheBlank(date1 + newDate);
		try {
			PremierEnregistrement = puceNFC.readABlock(3, 1, key, false);
			if (PremierEnregistrement.equals("00000000000000000000000000000000")) {
				puceNFC.writeInABlock(3, 0, adapterFactory.completeTheBlank(newDate), key, false);
				puceNFC.writeInABlock(3, 1, donneesGPSAEnvoyer, key, false);
			} else {

				puceNFC.writeInABlock(4, 0, date34, key, false);

				puceNFC.writeInABlock(4, 1, donneesGPS2, key, false);
				puceNFC.writeInABlock(4, 2, donneesGPS3, key, false);

				puceNFC.writeInABlock(3, 2, donneesGPSAEnvoyer, key, false);
				puceNFC.writeInABlock(3, 0, date12, key, false);
			}
		} catch (TagActionException e) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.getMessage()));
			e.printStackTrace();
		}
	

		String matriculeAgent = data.getString(10);
		dataToSend = new ReadAll().readAll(puceNFC, matriculeAgent, id).toString();

		this.run();
		
		nfcPlugin.setWriteExecution(false);
		JSONArray result = new JSONArray();
		result.put(0,"Transmission réussie");
		result.put(1,"GPS");
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
		return null;

	}

	@Override
	public String read(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC) throws JSONException {


		boolean error = false;
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
			if (callbackContext != null){
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.getMessage()));
			}
			e.printStackTrace();
			error = true;
		}

		JSONArray array = new JSONArray();

		if (!error) {
			Integer[] latlong1 = getLatitudeLongitude(latitude1, longitude1, signe1);
			Integer[] latlong2 = getLatitudeLongitude(latitude2, longitude2, signe2);
			Integer[] latlong3 = getLatitudeLongitude(latitude3, longitude3, signe3);
			Integer[] latlong4 = getLatitudeLongitude(latitude4, longitude4, signe4);

			array.put(0, date1);
			array.put(1, latlong1[0].toString());
			array.put(2, latlong1[1].toString());
			array.put(3, Integer.parseInt(accuracy1));

			array.put(4, date2);
			array.put(5, latlong2[0].toString());
			array.put(6, latlong2[1].toString());
			array.put(7, Integer.parseInt(accuracy2));

			array.put(8, date3);
			array.put(9, latlong3[0].toString());
			array.put(10, latlong3[1].toString());
			array.put(11, Integer.parseInt(accuracy3));

			array.put(12, date4);
			array.put(13, latlong4[0].toString());
			array.put(14, latlong4[1].toString());
			array.put(15, Integer.parseInt(accuracy4));

		}
		if (callbackContext != null){
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));
		}
		return array.toString();
	}

	private Integer[] getLatitudeLongitude(String latitude, String longitude, String signe) {

		int signe1 = Integer.parseInt(signe);
		int latitude1 = Integer.parseInt(latitude);
		int longitude1 = Integer.parseInt(longitude);
	
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
	
	@Override
	public void run() {
		adapterFactory.getConsumerWebSocket().addMessage(dataToSend);
	}
}
