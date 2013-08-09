package com.phonegap.adapter.entity;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.chariotsolutions.nfc.plugin.NfcPlugin;
import com.phonegap.adapter.AdapterFactory;
import com.phonegap.adapter.NFC_adapter;
import com.phonegap.plugins.nfc.NFC_Mifare_classic;
import com.phonegap.plugins.nfc.TagActionException;

public class AdapterProgression implements NFC_adapter {

	private AdapterFactory adapterFactory;

	public AdapterProgression(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}

	@Override
	public String write(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC, NfcPlugin nfcPlugin)
			throws JSONException {

		// On récupère les données a ecrire et on les formatte pour la puce
		int progression = data.getInt(1);

		// On récupère le bloc ou l'on souhaite ajouter des informations
		String infos_victime = null;
		try {
			infos_victime = puceNFC.readABlock(1, 0, key, false);
		} catch (TagActionException e1) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e1.getMessage()));
			e1.printStackTrace();
		}
		// On récupère les informations que l'on souhaite garder et on rajoute l'information que l'on souhaite ajouter
		String valeurBloc = infos_victime.substring(0, 30) + progression + infos_victime.substring(31, 32);

		// On écrit le nouveau bloc
		try {
			puceNFC.writeInABlock(1, 0, valeurBloc, key, false);
		} catch (TagActionException e) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.getMessage()));
			e.printStackTrace();
		}

		return valeurBloc;
	}

	@Override
	public String read(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC) throws JSONException {
		String vital_urgency = "";
		try {
			vital_urgency = puceNFC.readABlock(1, 0, key, false);
		} catch (TagActionException e) {
			e.printStackTrace();
		}
		String progression = "";
		if (vital_urgency.length() > 31) {
			progression = vital_urgency.substring(30, 31);
		} 

		JSONArray array = new JSONArray();

		array.put(0, progression);


		return array.toString();
	}

}
