package jxt.app.voip;

import org.sipdroid.sipua.ui.Receiver;
import org.zoolu.sip.provider.SipStack;

import android.preference.PreferenceManager;

public class SipConfig {
	public static final String PREF_VERSION = "version";
	public static final String PREF_ON = "on";
	public static final String PREF_USERNAME = "username";
	public static final String PREF_PASSWORD = "password";
	public static final String PREF_SERVER = "server";
	public static final String PREF_CODECS = "codecs_new";
	public static final String PREF_COMPRESSION = "compression";
	public static final String PREF_CALLRECORD = "callrecord";
	public static final String PREF_BLUETOOTH = "bluetooth";
	public static final String PREF_EARGAIN = "eargain";
	public static final String PREF_HEARGAIN = "heargain";
	public static final String PREF_HMICGAIN = "hmicgain";
	public static final String PREF_MICGAIN = "micgain";
	public static final String PREF_OLDVALID = "oldvalid";
	public static final String PREF_OLDVIBRATE = "oldvibrate";
	public static final String PREF_OLDVIBRATE2 = "oldvibrate2";
	public static final String PREF_OLDPOLICY = "oldpolicy";
	public static final String PREF_OLDRING = "oldring";
	public static final String PREF_SETMODE = "setmode";
	public static final String PREF_NODATA = "nodata";
	public static final String PREF_KEEPON = "keepon";
	public static final String PREF_IMPROVE = "improve";
	public static final String PREF_SELECTWIFI = "selectwifi";
	public static final String PREF_MWI_ENABLED = "MWI_enabled";
	public static final String PREF_DOMAIN = "domain";
	public static final String PREF_FROMUSER = "fromuser";
	public static final String PREF_MMTEL = "mmtel";
	public static final String PREF_MMTEL_QVALUE = "mmtel_qvalue";
	public static final String PREF_EDGE = "edge";
	public static final String PREF_3G = "3g";
	public static final String PREF_WLAN = "wlan";
	public static final String PREF_VPN = "vpn";
	public static final String PREF_ACCOUNT = "account";
	public static final String PREF_PROTOCOL = "protocol";
	public static final String PREF_DNS = "dns";
	public static final String PREF_PORT = "port";
	public static final String PREF_CALLBACK = "callback";
	public static final String PREF_POSURL = "posurl";
	public static final String PREF_POS = "pos";
	public static final String PREF_EXCLUDEPAT = "excludepat";
	public static final String PREF_PREFIX = "prefix";
	public static final String PREF_SEARCH = "search";
	public static final String PREF_PAR = "par";
	public static final String PREF_CALLTHRU = "callthru";
	public static final String PREF_CALLTHRU2 = "callthru2";
	public static final String PREF_AUTO_ON = "auto_on";
	public static final String PREF_SIPRINGTONE = "sipringtone";
	public static final String PREF_REGISTRATION = "registration";
	public static final String PREF_NOTIFY = "notify";
	public static final String PREF_AUTO_ONDEMAND = "auto_on_demand";
	public static final String PREF_AUTO_DEMAND = "auto_demand";
	public static final String PREF_OWNWIFI = "ownwifi";
	public static final String PREF_WIFI_DISABLED = "wifi_disabled";
	public static final String PREF_ON_VPN = "on_vpn";
	public static final String PREF_STUN = "stun";
	public static final String PREF_STUN_SERVER = "stun_server";
	public static final String PREF_STUN_SERVER_PORT = "stun_server_port";
	
	public static final int VERSION = 1;
	public static final boolean	DEFAULT_ON = true;
	//本机号码
	public static final String USERNAME = "9003";
	public static final String PASSWORD = "zaq123";
	public static final String SERVER = "jxt-hcdl.gicp.net";
	//经理的号码
	public static final String BROKER = "9001";
	public static final String DEFAULT_PROTOCOL = "udp";
	public static final String	DEFAULT_CODECS = null;
	public static final String	DEFAULT_COMPRESSION = null;
	public static final boolean DEFAULT_CALLRECORD = false;
	public static final boolean DEFAULT_BLUETOOTH = false;
	public static final float	DEFAULT_EARGAIN = (float) 0.25;
	public static final float	DEFAULT_MICGAIN = (float) 0.25;
	public static final float	DEFAULT_HMICGAIN = (float) 1.0;
	public static final boolean	DEFAULT_OLDVALID = false;
	public static final boolean	DEFAULT_SETMODE = false;
	public static final int		DEFAULT_OLDVIBRATE = 0;
	public static final int		DEFAULT_OLDVIBRATE2 = 0;
	public static final int		DEFAULT_OLDPOLICY = 0;
	public static final boolean	DEFAULT_NODATA = false;
	public static final boolean DEFAULT_KEEPON = false;
	public static final boolean	DEFAULT_IMPROVE = false;
	public static final boolean DEFAULT_SELECTWIFI = false;
	public static final boolean	DEFAULT_MWI_ENABLED = true;
	public static final String	DEFAULT_DOMAIN = "";
	public static final String	DEFAULT_FROMUSER = "";
	public static final boolean	DEFAULT_MMTEL = false;
	public static final String	DEFAULT_MMTEL_QVALUE = "1.00";
	public static final boolean	DEFAULT_EDGE = false;
	public static final boolean	DEFAULT_3G = false;
	public static final boolean	DEFAULT_WLAN = true;
	public static final boolean	DEFAULT_VPN = false;
	public static final int     DEFAULT_ACCOUNT = 0;
	public static final String	DEFAULT_DNS = "";
	public static final String	DEFAULT_PORT = "" + SipStack.default_port;
	public static final boolean	DEFAULT_CALLBACK = false;
	public static final String	DEFAULT_POSURL = "";
	public static final boolean	DEFAULT_POS = false;
	public static final String	DEFAULT_EXCLUDEPAT = "";
	public static final String	DEFAULT_PREFIX = "";
	public static final String	DEFAULT_SEARCH = "";
	public static final boolean	DEFAULT_PAR = false;
	public static final boolean	DEFAULT_CALLTHRU = false;
	public static final String	DEFAULT_CALLTHRU2 = "";
	public static final boolean	DEFAULT_AUTO_ON = false;
	public static final boolean DEFAULT_REGISTRATION = true;
	public static final boolean	DEFAULT_NOTIFY = false;
	public static final boolean	DEFAULT_AUTO_ONDEMAND = false;
	public static final boolean	DEFAULT_AUTO_DEMAND = false;
	public static final boolean	DEFAULT_OWNWIFI = false;
	public static final boolean	DEFAULT_WIFI_DISABLED = false;
	public static final boolean DEFAULT_ON_VPN = false;
	public static final boolean	DEFAULT_STUN = false;
	public static final String	DEFAULT_STUN_SERVER = "stun.ekiga.net";
	public static final String	DEFAULT_STUN_SERVER_PORT = "3478";
	
	public static float getEarGain() {
		try {
			return Float.valueOf(PreferenceManager.getDefaultSharedPreferences(Receiver.mContext).getString(Receiver.headset > 0 ? SipConfig.PREF_HEARGAIN : SipConfig.PREF_EARGAIN, "" + SipConfig.DEFAULT_EARGAIN));
		} catch (NumberFormatException i) {
			return SipConfig.DEFAULT_EARGAIN;
		}			
	}
	
	public static float getMicGain() {
		if (Receiver.headset > 0 || Receiver.bluetooth > 0) {
			try {
				return Float.valueOf(PreferenceManager.getDefaultSharedPreferences(Receiver.mContext).getString(PREF_HMICGAIN, "" + DEFAULT_HMICGAIN));
			} catch (NumberFormatException i) {
				return DEFAULT_HMICGAIN;
			}			
		}

		try {
			return Float.valueOf(PreferenceManager.getDefaultSharedPreferences(Receiver.mContext).getString(PREF_MICGAIN, "" + DEFAULT_MICGAIN));
		} catch (NumberFormatException i) {
			return DEFAULT_MICGAIN;
		}			
	}
}
