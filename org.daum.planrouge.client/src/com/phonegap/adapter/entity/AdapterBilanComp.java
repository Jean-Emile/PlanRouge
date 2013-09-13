package com.phonegap.adapter.entity;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.phonegap.adapter.AdapterFactory;
import com.phonegap.adapter.NFC_adapter;
import com.phonegap.api.nfc.NFC_Mifare_classic;
import com.phonegap.api.nfc.TagActionException;
import com.phonegap.plugins.ReadWritePlugin;
import com.phonegap.plugins.manager.ComManager;

public class AdapterBilanComp implements NFC_adapter {

	private AdapterFactory adapterFactory;

	public AdapterBilanComp(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}
	
	@Override
	public String write(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC, ReadWritePlugin nfcPlugin)
			throws JSONException {
		String info = data.getString(1); // données du questionnaire
		String oxygene = Integer.toHexString(data.getInt(2));
		String chocs = Integer.toHexString(data.getInt(3));
		String immobilisation = data.getString(4);
		String extension = data.getString(5);
		String maladies = data.getString(6);
		String hospitalisations = data.getString(7);
		String traitements = data.getString(8);
		String allergies = data.getString(9);

		immobilisation = adapterFactory.completeTheBlank(puceNFC.toHex(immobilisation));
		extension = adapterFactory.completeTheBlank(puceNFC.toHex(extension));
		maladies = adapterFactory.completeTheBlank(puceNFC.toHex(maladies));
		hospitalisations = adapterFactory.completeTheBlank(puceNFC.toHex(hospitalisations));
		traitements = adapterFactory.completeTheBlank(puceNFC.toHex(traitements));
		allergies = adapterFactory.completeTheBlank(puceNFC.toHex(allergies));

		try {
			puceNFC.writeInASector(6, maladies + hospitalisations + traitements, key, false);
			puceNFC.writeInASector(7, extension + immobilisation + allergies, key, false);
		} catch (TagActionException e) {
		
			callbackContext.error(e.getMessage());
			e.printStackTrace();

		}

		String lesion = null;
		try {
			lesion = puceNFC.readABlock(5, 1, key, false);
		} catch (TagActionException e1) {
			callbackContext.error(e1.getMessage());
			e1.printStackTrace();
		}

		// Traitement données questionnaire
		if (info.length() % 2 != 0) {
			info = info + "0";
		}
		String valeurBloc = Integer.toString(Integer.parseInt(info, 2), 16);
		while (valeurBloc.length() < 7) {
			valeurBloc = "0" + valeurBloc;
		}
		while (oxygene.length() < 2) {
			oxygene = "0" + oxygene;
		}
		while (chocs.length() < 2) {
			chocs = "0" + chocs;
		}

		valeurBloc = lesion.substring(0, 20) + valeurBloc + oxygene + chocs;

		while (valeurBloc.length() != 32) {
			valeurBloc += "0";
		}

		// Traitement perte connaissance

		
		try {
			puceNFC.writeInABlock(5, 1, valeurBloc, key, false);
		} catch (TagActionException e) {
			callbackContext.error(e.getMessage());
			e.printStackTrace();

		}
		ComManager comManager = ComManager.getInstance();
		comManager.setWriteExecution(false);
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, "Transmission Réussie"));
		return null;
	}

	@Override
	public String read(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC) throws JSONException {
		boolean error = false;
		// Traitement perte connaissance

		String result = "";
		String maladies = "";
		String hospitalisations = "";
		String traitements = "";
		String extension = "";
		String immobilisation = "";
		String allergies = "";

		try {
			result = puceNFC.readABlock(5, 1, key, false);
			maladies = puceNFC.hexToAscii(puceNFC.readABlock(6, 0, key, false));
			hospitalisations = puceNFC.hexToAscii(puceNFC.readABlock(6, 1, key, false));
			traitements = puceNFC.hexToAscii(puceNFC.readABlock(6, 2, key, false));
			extension = puceNFC.hexToAscii(puceNFC.readABlock(7, 0, key, false));
			immobilisation = puceNFC.hexToAscii(puceNFC.readABlock(7, 1, key, false));
			allergies = puceNFC.hexToAscii(puceNFC.readABlock(7, 2, key, false));

		} catch (TagActionException e) {
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.getMessage()));
			e.printStackTrace();
			error = true;
		}

		JSONArray array = new JSONArray();

		if (!error) {
			array.put(0, (result.subSequence(20, 32)));
			array.put(1, 0);
			array.put(2, maladies);
			array.put(3, hospitalisations);
			array.put(4, traitements);
			array.put(5, extension);
			array.put(6, immobilisation);
			array.put(7, allergies);
		}
		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, array));

		return array.toString();
	}

}
