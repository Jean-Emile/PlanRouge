package com.phonegap.plugins.nfc;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;

import android.content.Intent;

import com.chariotsolutions.nfc.plugin.NfcPlugin;

public class Mifare extends Plugin {



	@Override
	public PluginResult execute(String action, JSONArray args, String callbackId) {
		PluginResult.Status status = PluginResult.Status.OK;
		String result = "";

		if (action.equals("getId")) {
			
			Intent intent = new Intent();
			NFC_Mifare_classic puceNFC = new NFC_Mifare_classic();
			puceNFC.treatAsNewTag(intent);
			String id = puceNFC.getId();
			System.out.println("GETID OK !!!   " +id);
		
			PluginResult pluginResult = new PluginResult(status, result);
			pluginResult.setKeepCallback(true);
			return pluginResult;
		}
		
		status = PluginResult.Status.INVALID_ACTION;

		return new PluginResult(status, result);
	}

}
