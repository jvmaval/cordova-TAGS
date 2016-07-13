package org.apache.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcV;


public class TAGS extends CordovaPlugin {
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("echo")) {
			String message = args.getString(0);
			return true;
		}	
		return false;
	}
	

	
	@Override
    public void onNewIntent(Intent intent){
		super.onNewIntent(intent);
        Log.w("myApp", "PROCESSING INTENT");
        
    }
	
	
	
	
	
}

