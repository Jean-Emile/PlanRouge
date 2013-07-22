package com.phonegap.adapter;

import org.apache.cordova.api.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import com.chariotsolutions.nfc.plugin.NfcPlugin;
import com.phonegap.plugins.nfc.NFC_Mifare_classic;
import com.phonegap.websocket.ConsumerWebSocket;

import android.content.Intent;
import android.util.Log;

public class AdapterFactory implements NFC_adapter {

	private ConsumerWebSocket consumerWebSocket;

	public AdapterFactory(ConsumerWebSocket consumerWebSocket) {
		this.setConsumerWebSocket(consumerWebSocket);
	}

	@Override
	public String write(JSONArray data, CallbackContext callbackContext, byte[] key, NFC_Mifare_classic intent, NfcPlugin nfcPlugin) throws JSONException {
		if (data.get(0).equals("category")) {
			Log.i("AdapterFactory", "write Category");
			return new AdapterCategory(this).write(data, callbackContext, key, intent, nfcPlugin);
		} else if (data.get(0).equals("identity")) {
			Log.i("AdapterFactory", "write Identity");
			return new AdapterIdentity(this).write(data, callbackContext, key, intent, nfcPlugin);
		} else if (data.get(0).equals("gpsHours")) {
			Log.i("AdapterFactory", "write gpsHours");
			return new AdapterGpsHours(this).write(data, callbackContext, key, intent, nfcPlugin);
		}

		return "";

	}

	@Override
	public String read(JSONArray data, CallbackContext callbackContext, byte[] key,  NFC_Mifare_classic intent) throws JSONException {
		
		if (data.get(0).equals("category")) {
			Log.i("AdapterFactory", "read Category");
			return new AdapterCategory(this).read(data, callbackContext, key, intent);

		} else if (data.get(0).equals("identity")) {
			Log.i("AdapterFactory", "read Identity");
			return new AdapterIdentity(this).read(data, callbackContext, key, intent);
		} else if (data.get(0).equals("gpsHours")) {
			Log.i("AdapterFactory", "read gpsHours");
			return new AdapterGpsHours(this).read(data, callbackContext, key, intent);
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
