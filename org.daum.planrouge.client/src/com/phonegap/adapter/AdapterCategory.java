package com.phonegap.adapter;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.util.Log;

import com.chariotsolutions.nfc.plugin.NfcPlugin;
import com.phonegap.plugins.nfc.NFC_Mifare_classic;
import com.phonegap.plugins.nfc.TagActionException;

public class AdapterCategory implements NFC_adapter {


	private AdapterFactory adapterFactory;

	public AdapterCategory(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}


	// VICTIM CATEGORY
	public String write(JSONArray data, CallbackContext callbackContext, byte[]key, NFC_Mifare_classic puceNFC, NfcPlugin nfcPlugin) throws JSONException {
		
		// Nouvelle connexion avec la puce
//		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
//		puceNFC.treatAsNewTag(intent);

		// On récupère les données a ecrire et on les formatte pour la puce
		String category = data.getString(1);

		// On récupère le bloc ou l'on souhaite ajouter des informations
		String infos_victime = null;
		try {
			infos_victime = puceNFC.readABlock(1, 0, key, false);
		} catch (TagActionException e1) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e1.getMessage()));
			e1.printStackTrace();
		}
		// On récupère les informations que l'on souhaite garder et on rajoute l'information que l'on souhaite ajouter
		String valeurBloc = infos_victime.substring(0, 31) + category;

		// On écrit le nouveau bloc
		try {
			puceNFC.writeInABlock(1, 0, valeurBloc, key, false);
		} catch (TagActionException e) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.getMessage()));
			e.printStackTrace();
		}

		// JSONArray array = new JSONArray();
		// array.put(0, puceNFC.getId());
		Log.i("WRITE CATEGORY", puceNFC.getId());
		adapterFactory.getConsumerWebSocket().addMessage(new ReadAll().readAll(puceNFC).toString());
		nfcPlugin.setWriteExecution(false);
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, puceNFC.getId()));
		return valeurBloc;
	}

	public String read(JSONArray data, CallbackContext callbackContext, byte[]key, NFC_Mifare_classic puceNFC) throws JSONException {

		String vital_urgency = "";
		try {
			vital_urgency = puceNFC.readABlock(1, 0, key, false);
		} catch (TagActionException e) {
			// TODO Auto-generated catch block
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.getMessage()));
			e.printStackTrace();
		}
		String category = "";
		if (vital_urgency.length() > 31) {
			category = vital_urgency.substring(31, 32);
		} else {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "erreur"));
		}

		JSONArray array = new JSONArray();

		array.put(0, category);

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));
		
		return array.toString();
	}

}
