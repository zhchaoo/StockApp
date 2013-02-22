package jxt.app.voip;

import org.sipdroid.sipua.ui.Receiver;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

public class Voip {
	public static void on(Context context,boolean on) {
		Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
		edit.putBoolean(SipConfig.PREF_ON, on);
		edit.commit();
        if (on) Receiver.engine(context).isRegistered();
	}
	
	public static boolean on(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SipConfig.PREF_ON, SipConfig.DEFAULT_ON);
	}
	
	public static String getVersion() {
		return getVersion(Receiver.mContext);
	}
	
	public static String getVersion(Context context) {
		final String unknown = "Unknown";
		
		if (context == null) {
			return unknown;
		}
		
		try {
	    	String ret = context.getPackageManager()
			   .getPackageInfo(context.getPackageName(), 0)
			   .versionName;
	    	if (ret.contains(" + "))
	    		ret = ret.substring(0,ret.indexOf(" + "))+"b";
	    	return ret;
		} catch(NameNotFoundException ex) {}
		
		return unknown;		
	}
	
	public static void checkConfig(Context context) {
		SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
		int version = setting.getInt(SipConfig.PREF_VERSION, 0);
		if(version < SipConfig.VERSION) {
			Editor editor = setting.edit();
			editor.putInt(SipConfig.PREF_VERSION, SipConfig.VERSION);
			editor.putString(SipConfig.PREF_USERNAME, SipConfig.USERNAME);
			editor.putString(SipConfig.PREF_PASSWORD, SipConfig.PASSWORD);
			editor.putString(SipConfig.PREF_SERVER, SipConfig.SERVER);
			editor.putString(SipConfig.PREF_PROTOCOL, SipConfig.DEFAULT_PROTOCOL);
			editor.commit();
		}
	}
}
