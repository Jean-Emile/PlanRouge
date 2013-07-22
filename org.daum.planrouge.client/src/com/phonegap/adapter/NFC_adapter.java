package com.phonegap.adapter;

import org.apache.cordova.api.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import com.chariotsolutions.nfc.plugin.NfcPlugin;
import com.phonegap.plugins.nfc.NFC_Mifare_classic;

import android.content.Intent;

public interface NFC_adapter {

	public String write(JSONArray data, CallbackContext callbackContext, byte[]key, NFC_Mifare_classic puceNFC,NfcPlugin nfcPlugin) throws JSONException;
	public String read(JSONArray data, CallbackContext callbackContext, byte[]key, NFC_Mifare_classic intent) throws JSONException;
}
