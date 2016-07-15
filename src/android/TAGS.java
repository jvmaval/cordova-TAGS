package org.apache.cordova.tags;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcV;


public class TAGS extends CordovaPlugin {
	
	private PendingIntent pendingIntent = null;
  

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("readTAG")) {
			String message = args.getString(0);
			return true;
		}	
		return false;
	}
	

	
	@Override
    public void onNewIntent(Intent intent){
		Log.w("myApp", "PROCESSING INTENT");
		setIntent(intent);
		super.onNewIntent(intent);       
    }
	
	
	
	@Override
	public void onResume(boolean multitasking) {
		super.onResume(multitasking);
		createPendingIntent();
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
		if (nfcAdapter != null) {
			try {
				nfcAdapter.enableForegroundDispatch(getActivity(), getPendingIntent(), null, null);
			} catch (IllegalStateException e) {
				// issue 110 - user exits app with home button while nfc is initializing
				Log.w(TAG, "Illegal State Exception starting NFC. Assuming application is terminating.");
			}
        }		
	}
	
	@Override
	public void onPause(boolean multitasking) {
		Log.w("myApp", "PROCESSING PAUSE");
		super.onPause(multitasking);
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
		if (nfcAdapter != null) {
			try {
				nfcAdapter.disableForegroundDispatch(getActivity());
			} catch (IllegalStateException e) {
				
				Log.w("myApp", "WITHIN CATCH");
			}
        }
    
	}
	
	
	private Activity getActivity() {
        return this.cordova.getActivity();
    }
	
	private Intent getIntent() {
        return getActivity().getIntent();
    }

    private void setIntent(Intent intent) {
        getActivity().setIntent(intent);
    }
	
	
	private void createPendingIntent() {
        if (pendingIntent == null) {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, activity.getClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
        }
    }
		
	
}

