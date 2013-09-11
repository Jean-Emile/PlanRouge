package com.phonegap.adapter.entity;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.LOG;
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

public class AdapterIdentity implements Runnable, NFC_adapter {

	private AdapterFactory adapterFactory;
	private String dataToSend;

	public AdapterIdentity(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}

	@Override
	public String write(final JSONArray data, final CallbackContext callbackContext, final byte[] key, final NFC_Mifare_classic puceNFC,
			final ReadWritePlugin nfcPlugin) throws JSONException {
		String id = puceNFC.getId();

		String firstname = null;
		String surname = null;
		String sexe = null;
		String ageHex = null;
		int age = 255;
		String birthday = null;
		try {
			firstname = adapterFactory.completeTheBlank(puceNFC.toHex(data.getString(1)));
			surname = adapterFactory.completeTheBlank(puceNFC.toHex(data.getString(2)));
			sexe = data.getString(3);
			age = data.getInt(4);
			birthday = data.getString(5);
			LOG.e("ADAPTERIDENTITY", "sexe :: " + sexe + "   age ::: " + age);
		} catch (JSONException e2) {
			e2.printStackTrace();
		}

		Log.i("AdapterVictim", " 1 :::" + age);
		ageHex = Integer.toHexString(age);
		if (ageHex.length() == 1) {
			ageHex = "0" + ageHex;
		} else if (ageHex.length() > 2) {
			ageHex = "FF";
		}
		Log.i("AdapterVictim", " 2 :::" + ageHex);
		String victim_category = "0";
		Log.i("AdapterVictim", " 1 :::" + birthday + "  " + ageHex + "  " + sexe);
		try {
			victim_category = puceNFC.readABlock(1, 0, key, false).substring(31, 32);
		} catch (TagActionException e1) {
			Log.e("ERREUR WRITE", "ERREUR WRITE IDENTITY");
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, e1.getMessage()));
			e1.printStackTrace();

		}

		String identity_infos = adapterFactory.completeTheBlank(birthday + ageHex + sexe);
		identity_infos = identity_infos.substring(0, 31) + victim_category;
		String firstAndSurname = firstname + surname;
		try {
			puceNFC.writeInASector(0, firstAndSurname, key, false);
			puceNFC.writeInABlock(1, 0, identity_infos, key, false);
		} catch (TagActionException e) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION, e.getMessage()));
			e.printStackTrace();
		}
		String matriculeAgent = data.getString(10);
		dataToSend = new ReadAll().readAll(puceNFC, matriculeAgent, id).toString();

		this.run();
		
		String gpsHoursArray = adapterFactory.read(new JSONArray().put(0, "gpsHours"), null, key, puceNFC);
		
		nfcPlugin.setWriteExecution(false);
		JSONArray result = new JSONArray();
		result.put(0,"Transmission réussie");
		result.put(1,new JSONArray(gpsHoursArray));
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
		return null;
	}

	@Override
	public String read(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC) throws JSONException {
		String firstname = "";
		String surname = "";
		int age = 255;
		String birthday = "";
		String sexe = "";

		try {
			// firstname
			firstname = puceNFC.hexToAscii(puceNFC.readABlock(0, 1, key, false));
			// surname
			surname = puceNFC.hexToAscii(puceNFC.readABlock(0, 2, key, false));

			// other
			String identity_infos = puceNFC.readABlock(1, 0, key, false);
			Log.i("AdapterVictim", " 3 :::" + identity_infos);
			
			// age
			birthday = identity_infos.substring(0, 8);
			age = Integer.parseInt(identity_infos.substring(8, 10), 16);

			//sexe
			sexe = identity_infos.substring(10, 11);
			Log.i("AdapterVictim", " 4 :::" + age);

		} catch (TagActionException e) {
			if (callbackContext != null) {
				callbackContext.error(e.getMessage());
			}

			e.printStackTrace();

		}

		JSONArray array = new JSONArray();
		array.put(0, firstname);
		array.put(1, surname);
		array.put(2, sexe);
		array.put(3, age);
		array.put(4, birthday);
		if (callbackContext != null) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));
		}
		return array.toString();
	}

	@Override
	public void run() {
		adapterFactory.getConsumerWebSocket().addMessage(dataToSend);
	}

}
