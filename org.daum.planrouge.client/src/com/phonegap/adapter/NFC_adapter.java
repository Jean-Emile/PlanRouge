package com.phonegap.adapter;

import org.apache.cordova.api.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import com.phonegap.plugins.NfcPlugin;
import com.phonegap.plugins.ReadWritePlugin;
import com.phonegap.plugins.nfc.NFC_Mifare_classic;

public interface NFC_adapter {

	public String write(JSONArray data, CallbackContext callbackContext, byte[]key, NFC_Mifare_classic puceNFC,ReadWritePlugin nfcPlugin) throws JSONException;
	public String read(JSONArray data, CallbackContext callbackContext, byte[]key, NFC_Mifare_classic intent ) throws JSONException;
}
