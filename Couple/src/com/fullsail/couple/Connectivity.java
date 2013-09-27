/*
 * project 	Couple
 * 
 * package 	com.fullsail.couple
 * 
 * @author 	William Saults
 * 
 * date 	Sep 26, 2013
 */
package com.fullsail.couple;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/*
 * This class is all about checking for internet connectivity.
 */
public class Connectivity {
	static Boolean _conn = false;
	static String _connectionType = "Unavailable";
	
	private static Connectivity instance = null;
	protected Connectivity() {
		// Exists to defeat instantiation.
	}
	public static Connectivity getInstance() {
		if(instance == null) {
			instance = new Connectivity();
		}
		return instance;
	}
	
	public static String getConnectionType(Context context) {
		netInfo(context);
		return _connectionType;
	}
	
	public static Boolean getConnectionStatus(Context context) {
		netInfo(context);
		return _conn;
	}
	
	/**
	* Gets the state of Airplane Mode.
	* 
	* @param context
	* @return true if enabled.
	*/
	private static boolean isAirplaneModeOn(Context context) {

	   return Settings.Global.getInt(context.getContentResolver(),
	           Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
	}
	
	private static void netInfo(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			if (ni.isConnected()) {
				_connectionType= ni.getTypeName();
				_conn = !isAirplaneModeOn(context);
			}
		} else {
			_conn = !isAirplaneModeOn(context);
		}
	}
}
