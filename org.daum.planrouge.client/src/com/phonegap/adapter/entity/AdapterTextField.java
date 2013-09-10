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

public class AdapterTextField implements NFC_adapter {

	private AdapterFactory adapterFactory;

	public AdapterTextField(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}
	
	@Override
	public String write(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC, ReadWritePlugin nfcPlugin)
			throws JSONException {
		String id = puceNFC.getId();
		
		String textfield = puceNFC.toHex(data.getString(1));

		while (textfield.length() < 576) {
			textfield = textfield + "0";
		}
		
		
		try {

			puceNFC.writeInASector(10, textfield.substring(0, 96), key, false);
			puceNFC.writeInASector(11, textfield.substring(96, 192), key, false);
			puceNFC.writeInASector(12, textfield.substring(192, 288), key, false);
			puceNFC.writeInASector(13, textfield.substring(288, 384), key, false);
			puceNFC.writeInASector(14, textfield.substring(384, 480), key, false);
			puceNFC.writeInASector(15, textfield.substring(480, 576), key, false);

		} catch (TagActionException e) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
			e.printStackTrace();
		}
		
		String matriculeAgent = data.getString(10);	
		adapterFactory.getConsumerWebSocket().addMessage(new ReadAll().readAll(puceNFC, matriculeAgent, id).toString());
		
		nfcPlugin.setWriteExecution(false);
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Transmission Réussie"));
		return null;
	}

	@Override
	public String read(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC) throws JSONException {
		String textfield = "";
		try {
			// Get All data of TEXTFIELD
			textfield = (puceNFC.readASector(10, key, false)).substring(0, 96);
			textfield = textfield + (puceNFC.readASector(11, key, false)).substring(0, 96);
			textfield = textfield + (puceNFC.readASector(12, key, false)).substring(0, 96);
			textfield = textfield + (puceNFC.readASector(13, key, false)).substring(0, 96);
			textfield = textfield + (puceNFC.readASector(14, key, false)).substring(0, 96);
			textfield = textfield + (puceNFC.readASector(15, key, false)).substring(0, 96);

			// HEX to ASCII
			textfield = puceNFC.hexToAscii(textfield);
		} catch (TagActionException e) {
			// IF error return callbackcontext error
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.getMessage()));
			e.printStackTrace();

		}

		JSONArray array = new JSONArray();
		array.put(0, textfield);

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));
		
		return array.toString();
	}

}
