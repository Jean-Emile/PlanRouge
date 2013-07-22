package com.phonegap.adapter;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chariotsolutions.nfc.plugin.NfcPlugin;
import com.phonegap.plugins.nfc.NFC_Mifare_classic;
import com.phonegap.plugins.nfc.TagActionException;

import android.content.Intent;
import android.util.Log;

public class AdapterIdentity implements NFC_adapter{

	private AdapterFactory adapterFactory;

	public AdapterIdentity(AdapterFactory adapterFactory) {
		this.adapterFactory=adapterFactory;
	}

	@Override
	public String write(JSONArray data, CallbackContext callbackContext, byte[]key, NFC_Mifare_classic puceNFC,NfcPlugin nfcPlugin) throws JSONException {
//		NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
//		puceNFC.treatAsNewTag(intent);

		String firstname = adapterFactory.completeTheBlank(puceNFC.toHex(data.getString(1)));
		String surname = adapterFactory.completeTheBlank(puceNFC.toHex(data.getString(2)));
		String sexe = puceNFC.toHex(data.getString(3));
		String age = data.getString(4);
		while (age.length() < 3) {
			age = "0" + age;

		}
		age = puceNFC.toHex(age);

		String age_unit = puceNFC.toHex(data.getString(5));
		String birthday = puceNFC.toHex(data.getString(6));

		String victim_category = "0";
		try {
			victim_category = puceNFC.readABlock(1, 0, key, false).substring(31, 32);
		} catch (TagActionException e1) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, e1.getMessage()));
			e1.printStackTrace();
		}

		String identity_infos = adapterFactory.completeTheBlank(birthday + age + age_unit + sexe);
		identity_infos = identity_infos.substring(0, 31) + victim_category;
		String firstAndSurname = firstname + surname;
		try {

			puceNFC.writeInASector(0, firstAndSurname, key, false);
			puceNFC.writeInABlock(1, 0, identity_infos, key, false);
		} catch (TagActionException e) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, e.getMessage()));
			e.printStackTrace();
		}

//		JSONArray array = new JSONArray();
//		array.put(0, puceNFC.getId());
//
//		JSONObject obj = new JSONObject();
//		obj.put("identity", new JSONObject().put("id", puceNFC.getId()).put("type", "AdapterVictime").put("nom", surname).put("prenom", firstname)
//				.put("sexe", sexe).put("age", age).put("dateNaissance", birthday));
//		obj.put("type", "AdapterVictime");

		adapterFactory.getConsumerWebSocket().addMessage(new ReadAll().readAll(puceNFC).toString());
		nfcPlugin.setWriteExecution(false);
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Transmission réussie"));

		
		return null;

		
	}

	@Override
	public String read(JSONArray data, CallbackContext callbackContext, byte[]key, NFC_Mifare_classic puceNFC) throws JSONException {

	
//
//		if (intent == null || puceNFC.treatAsNewTag(intent) == -1) {
//			
//			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Pas de connexion avec le Tag"));
//			return null;
//		}

		String firstname = "";
		String surname = "";
		String age = "";
		String age_unit = "";
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
				age_unit = identity_infos.substring(11, 12);
				sexe = identity_infos.substring(12, 13);
			}
		} catch (TagActionException e) {
			// TODO Auto-generated catch block
			callbackContext.error(e.getMessage());
			e.printStackTrace();

		}

		JSONArray array = new JSONArray();
		array.put(0, surname);
		array.put(1, firstname);
		array.put(2, sexe);
		array.put(3, age);
		array.put(4, age_unit);
		array.put(5, birthday);

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));
		return array.toString();
	}



}
