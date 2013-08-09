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

public class AdapterDestination implements NFC_adapter {

	private AdapterFactory adapterFactory;

	public AdapterDestination(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}
	
	@Override
	public String write(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC, NfcPlugin nfcPlugin)
			throws JSONException {
		
		String evacuation = puceNFC.toHex(data.getString(1));
		String destination = adapterFactory.completeTheBlank(puceNFC.toHex(data.getString(2)));
		String ville = adapterFactory.completeTheBlank(puceNFC.toHex(data.getString(3)));
		String code_postal = puceNFC.toHex(data.getString(4));

		String code_postal_evacutation = adapterFactory.completeTheBlank(evacuation + code_postal);

		String data_destination = code_postal_evacutation + destination + ville;
		try {

			puceNFC.writeInASector(8, data_destination, key, false);
		} catch (TagActionException e) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, e.getMessage()));
			e.printStackTrace();
		}
		nfcPlugin.setWriteExecution(false);
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, true));
		
		return null;
	}

	@Override
	public String read(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC) throws JSONException {
		
		String evacuation = "";
		String destination = "";
		String ville = "";
		String code_postal = "";

		try {
			// ville
			ville = puceNFC.hexToAscii(puceNFC.readABlock(8, 2, key, false));

			// destination
			destination = puceNFC.hexToAscii(puceNFC.readABlock(8, 1, key, false));

			// Autres
			String destination_infos = puceNFC.hexToAscii(puceNFC.readABlock(8, 0, key, false));

			evacuation = destination_infos.substring(0, 1);
			code_postal = destination_infos.substring(1, 6);

		} catch (TagActionException e) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, e.getMessage()));
			e.printStackTrace();

		}

		JSONArray array = new JSONArray();
		array.put(0, evacuation);
		array.put(1, destination);
		array.put(2, ville);
		array.put(3, code_postal);

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));
		
		return array.toString();
	}

}
