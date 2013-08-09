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

public class AdapterBilanUrgence implements NFC_adapter {

	private AdapterFactory adapterFactory;

	public AdapterBilanUrgence(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}
	
	@Override
	public String write(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC, NfcPlugin nfcPlugin)
			throws JSONException {
		String info = data.getString(1); // données du questionnaire
		String perte_Connaissance = Integer.toHexString(data.getInt(2));
		String freq_Respiratoire = Integer.toHexString(data.getInt(3));
		String saturation = Integer.toHexString(data.getInt(4));
		String freq_Cardiaque = Integer.toHexString(data.getInt(5));
		String pression_Arterielle = Integer.toHexString(data.getInt(6));

		// Traitement données questionnaire
		if (info.length() % 2 != 0) {
			info = info + "0";
		}
		String valeurBloc = Integer.toString(Integer.parseInt(info, 2), 16);
		while (valeurBloc.length() < 7) {
			valeurBloc = "0" + valeurBloc;
		}
		while (perte_Connaissance.length() < 2) {
			perte_Connaissance = "0" + perte_Connaissance;
		}
		while (freq_Respiratoire.length() < 2) {
			freq_Respiratoire = "0" + freq_Respiratoire;
		}
		while (saturation.length() < 2) {
			saturation = "0" + saturation;
		}
		while (freq_Cardiaque.length() < 2) {
			freq_Cardiaque = "0" + freq_Cardiaque;
		}
		while (pression_Arterielle.length() < 2) {
			pression_Arterielle = "0" + pression_Arterielle;
		}

		valeurBloc = valeurBloc + perte_Connaissance + freq_Respiratoire + saturation + freq_Cardiaque + pression_Arterielle;

		while (valeurBloc.length() != 30) {
			valeurBloc += "0";
		}

		valeurBloc = adapterFactory.completeTheBlank(valeurBloc);
		// Traitement perte connaissance

		
		try {
			puceNFC.writeInABlock(5, 0, valeurBloc, key, false);
		} catch (TagActionException e) {
			callbackContext.error(e.getMessage());
			e.printStackTrace();

		}
		
		nfcPlugin.setWriteExecution(false);
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Transmission réussie"));

		return null;
	}

	@Override
	public String read(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC) throws JSONException {
		// Traitement perte connaissance
		
		//TODO :: RESULTAT ARRAY !!!!!
		
		String result = "";
		try {
			result = puceNFC.readABlock(5, 0, key, false);
		} catch (TagActionException e) {
			callbackContext.error(e.getMessage());
			e.printStackTrace();

		}

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));

		return result;
	}

}
