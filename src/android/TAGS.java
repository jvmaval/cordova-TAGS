package org.apache.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TAGS extends CordovaPlugin {
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("echo")) {
			String message = args.getString(0);
			this.echo(message, callbackContext);
			return true;
		}	
		return false;
	}
	
	private void echo(String message, CallbackContext callbackContext) {
		if (message != null && message.length() > 0) {
			callbackContext.success(message);
		} else {
			callbackContext.error("Expected one non-empty string argument.");
		}
	}
	
	@Override
    protected void onNewIntent(Intent intent){
        Log.w("myApp", "PROCESSING INTENT");
        
		Log.w("myApp", "onNewIntent: " + intent);
		String actn = intent.getAction();
        
		PluginResult result = new PluginResult(PluginResult.Status.OK, info);
		result.setKeepCallback(true);
		this.onYourCallback.sendPluginResult(result);
    }
	
}

