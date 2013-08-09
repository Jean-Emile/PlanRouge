package com.phonegap.adapter;

import org.apache.cordova.api.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.chariotsolutions.nfc.plugin.NfcPlugin;
import com.phonegap.adapter.entity.AdapterBilanComp;
import com.phonegap.adapter.entity.AdapterBilanUrgence;
import com.phonegap.adapter.entity.AdapterCategory;
import com.phonegap.adapter.entity.AdapterDestination;
import com.phonegap.adapter.entity.AdapterGpsHours;
import com.phonegap.adapter.entity.AdapterIdentity;
import com.phonegap.adapter.entity.AdapterLesion;
import com.phonegap.adapter.entity.AdapterTextField;
import com.phonegap.plugins.nfc.NFC_Mifare_classic;
import com.phonegap.websocket.ConsumerWebSocket;

public class AdapterFactory implements NFC_adapter {

	private ConsumerWebSocket consumerWebSocket;

	public AdapterFactory(ConsumerWebSocket consumerWebSocket) {
		this.setConsumerWebSocket(consumerWebSocket);
	}

	@Override
	public String write(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC, NfcPlugin nfcPlugin)
			throws JSONException {
		if (data.get(0).equals("category")) {
			Log.i("AdapterFactory", "write Category");
			return new AdapterCategory(this).write(data, callbackContext, key, puceNFC, nfcPlugin);
			
		} else if (data.get(0).equals("identity")) {
			Log.i("AdapterFactory", "write Identity");
			return new AdapterIdentity(this).write(data, callbackContext, key, puceNFC, nfcPlugin);
			
		} else if (data.get(0).equals("gpsHours")) {
			Log.i("AdapterFactory", "write gpsHours");
			return new AdapterGpsHours(this).write(data, callbackContext, key, puceNFC, nfcPlugin);
			
		} else if (data.get(0).equals("textField")) {
			Log.i("AdapterFactory", "write textField");
			return new AdapterTextField(this).write(data, callbackContext, key, puceNFC, nfcPlugin);
			
		} else if (data.get(0).equals("lesion")) {
			Log.i("AdapterFactory", "write lesion");
			return new AdapterLesion(this).write(data, callbackContext, key, puceNFC, nfcPlugin);
			
		} else if (data.get(0).equals("destination")) {
			Log.i("AdapterFactory", "write destination");
			return new AdapterDestination(this).write(data, callbackContext, key, puceNFC, nfcPlugin);
			
		} else if (data.get(0).equals("bilanUrgence")) {
			Log.i("AdapterFactory", "write bilanUrgence");
			return new AdapterBilanUrgence(this).write(data, callbackContext, key, puceNFC, nfcPlugin);
			
		} else if (data.get(0).equals("bilanComplementaire")) {
			Log.i("AdapterFactory", "write bilanComplementaire");
			return new AdapterBilanComp(this).write(data, callbackContext, key, puceNFC, nfcPlugin);
		}

		return "";

	}

	@Override
	public String read(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic puceNFC) throws JSONException {

		if (data.get(0).equals("category")) {
			Log.i("AdapterFactory", "read Category");
			return new AdapterCategory(this).read(data, callbackContext, key, puceNFC);

		} else if (data.get(0).equals("identity")) {
			Log.i("AdapterFactory", "read Identity");
			return new AdapterIdentity(this).read(data, callbackContext, key, puceNFC);
			
		} else if (data.get(0).equals("gpsHours")) {
			Log.i("AdapterFactory", "read gpsHours");
			return new AdapterGpsHours(this).read(data, callbackContext, key, puceNFC);
			
		} else if (data.get(0).equals("textField")) {
			Log.i("AdapterFactory", "read textField");
			return new AdapterTextField(this).read(data, callbackContext, key, puceNFC);
			
		} else if (data.get(0).equals("lesion")) {
			Log.i("AdapterFactory", "read lesion");
			return new AdapterLesion(this).read(data, callbackContext, key, puceNFC);
			
		} else if (data.get(0).equals("destination")) {
			Log.i("AdapterFactory", "read destination");
			return new AdapterDestination(this).read(data, callbackContext, key, puceNFC);
			
		} else if (data.get(0).equals("bilanUrgence")) {
			Log.i("AdapterFactory", "read bilanUrgence");
			return new AdapterBilanUrgence(this).read(data, callbackContext, key, puceNFC);
			
		} else if (data.get(0).equals("bilanComplementaire")) {
			Log.i("AdapterFactory", "read bilanComplementaire");
			return new AdapterBilanComp(this).read(data, callbackContext, key, puceNFC);
		}

		return "";
	}

	// COMPLETER AVEC DES "0" LES BLOCS NON COMPLET
	public String completeTheBlank(String hexString) {
		while (hexString.length() < 32) {
			hexString += "0";
		}
		return hexString;
	}

	public ConsumerWebSocket getConsumerWebSocket() {
		return consumerWebSocket;
	}

	public void setConsumerWebSocket(ConsumerWebSocket consumerWebSocket) {
		this.consumerWebSocket = consumerWebSocket;
	}

}
