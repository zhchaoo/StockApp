package com.chaos.testurl;

import android.net.Uri;
import android.provider.BaseColumns;

public class XmppMessage {
	public static final Uri CONTENT_URI =
        Uri.parse("content://jxt.app.stock.xmpp/message");
	public static final Uri CONTENT_URIZX =
        Uri.parse("content://jxt.app.stockzx.xmpp/message");
	public static final int TYPE_SEND = 1;
	public static final int TYPE_RECEIVE = 2;
	
	public static class Columns implements BaseColumns {
		public static final String BROKER = "broker";
		public static final String BODY = "body";
		public static final String TYPE = "type";
		public static final String DATE = "date";
		
		public static final int ID_INDEX = 0;
		public static final int BROKER_INDEX = 1;
		public static final int BODY_INDEX = 2;
		public static final int TYPE_INDEX = 3;
		public static final int DATE_INDEX = 4;
	}
}
