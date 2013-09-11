package com.phonegap.adapter.entity;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.phonegap.adapter.AdapterFactory;
import com.phonegap.adapter.NFC_adapter;
import com.phonegap.adapter.ReadAll;
import com.phonegap.api.nfc.NFC_Mifare_classic;
import com.phonegap.api.nfc.TagActionException;
import com.phonegap.plugins.ReadWritePlugin;

public class AdapterLesion implements NFC_adapter {

	private AdapterFactory adapterFactory;

	public AdapterLesion(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}
	
	@Override
	public String write(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC, ReadWritePlugin nfcPlugin)
			throws JSONException {
		
		String id = puceNFC.getId();
		
		
		String lesion = data.getString(1);
		String lesion1 = lesion.substring(0, 40);
		String lesion2 = lesion.substring(40, 80);
		lesion1 = Long.toString(Long.parseLong(lesion1, 2), 16);
		lesion2 = Long.toString(Long.parseLong(lesion2, 2), 16);

		while (lesion1.length() < 10) {
			lesion1 = "0" + lesion1;

		}
		while (lesion2.length() < 10) {
			lesion2 = "0" + lesion2;

		}

		lesion = lesion1 + lesion2;

		String bilan_comp = null;
		try {
			bilan_comp = puceNFC.readABlock(5, 1, key, false);
		} catch (TagActionException e1) {
			callbackContext.error(e1.getMessage());
			e1.printStackTrace();
			return null;
		}

		String valeurBloc = lesion + bilan_comp.substring(20, 32);

		while (valeurBloc.length() < 32) {
			valeurBloc += "0";
		}

		// Traitement perte connaissance

		
		try {
			puceNFC.writeInABlock(5, 1, valeurBloc, key, false);
		} catch (TagActionException e) {
			callbackContext.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		String matriculeAgent = data.getString(10);
		
		adapterFactory.getConsumerWebSocket().addMessage(new ReadAll().readAll(puceNFC, matriculeAgent, id).toString());
		nfcPlugin.setWriteExecution(false);
		Log.e("NFC PLUGIN", "IS NOT WRITE EXECUTION");
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Transmission Réussie"));
		return null;
	}

	@Override
	public String read(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC) throws JSONException {
		boolean error = false;
		// Traitement perte connaissance

		String result = "";

		try {
			result = puceNFC.readABlock(5, 1, key, false);

		} catch (TagActionException e) {
			callbackContext.error(e.getMessage());
			e.printStackTrace();
			error = true;
		}

		JSONArray array = new JSONArray();
		if (!error) {
			String result1 = Long.toBinaryString(Long.parseLong(result.substring(0, 10), 16));
			String result2 = Long.toBinaryString(Long.parseLong(result.substring(10, 20), 16));

			while (result1.length() < 40) {
				result1 = "0" + result1;
			}
			while (result2.length() < 40) {
				result2 = "0" + result2;
			}

			result = result1 + result2;

			array.put(0, result);
		}
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));
		
		return array.toString();
	}

}
