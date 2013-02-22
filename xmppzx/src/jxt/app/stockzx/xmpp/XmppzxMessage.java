package jxt.app.stockzx.xmpp;

import android.net.Uri;
import android.provider.BaseColumns;

public class XmppzxMessage {
	public static final Uri CONTENT_MESSAGE =
        Uri.parse("content://jxt.app.stockzx.xmpp/message");
	public static final Uri CONTENT_CATEGORY =
        Uri.parse("content://jxt.app.stockzx.xmpp/category");
	public static final Uri CONTENT_NEWS =
        Uri.parse("content://jxt.app.stockzx.xmpp/news");
	
	public static final int TYPE_SEND = 1;
	public static final int TYPE_RECEIVE = 2;
	
	public static class Columns implements BaseColumns {
		static final  String TYPE = "type";
		static final  String SUB_TYPE = "sub_type";
		static final  String RECEVIER = "recevier";
		static final  String CONTENT = "content";
		static final  String CREATED_AT = "created_at";
		static final  String IS_READ = "is_read";
		static final  String DATE = "date";
		
		public static final int ID_INDEX = 0;
		public static final int TYPE_INDEX = 1;
		public static final int SUB_TYPE_INDEX = 2;
		public static final int RECEVIER_INDEX = 3;
		public static final int CONTENT_INDEX = 4;
		public static final int CREATED_AT_INDEX = 5;
		public static final int IS_READ_INDEX = 6;
		public static final int DATE_INDEX = 7;
	}
	
	public static class CateGoryColumns implements BaseColumns {
		static final  String TYPE = "type";
		static final  String TITLE = "title";
		static final  String COUNT = "count";
		static final  String UN_READ = "un_read";
		static final  String IS_CONCERN = "is_concern";
		
		public static final int ID_INDEX = 0;
		public static final int TYPE_INDEX = 1;
		public static final int TITLE_INDEX = 2;
		public static final int COUNT_INDEX = 3;
		public static final int UN_READ_INDEX = 4;
		public static final int IS_CONCERN_INDEX = 5;
	}
		
	public static class NewsColumns implements BaseColumns {
		static final  String NEWS_ID = "news_id";
		static final  String TITLE = "title";
		static final  String NEWS_TYPE = "news_type";
		static final  String SOURCE = "source";
		static final  String DESCRIPTION = "description";
		static final  String UPDATED_AT = "updated_at";
		static final  String DATE = "date";
		static final  String IS_READ = "is_read";
		static final  String IS_BOOKMARK = "is_bookmark";
		
		public static final int ID_INDEX = 0;
		public static final int NEWS_ID_INDEX = 1;
		public static final int TITLE_INDEX = 2;
		public static final int NEWS_TYPE_INDEX = 3;
		public static final int SOURCE_INDEX = 4;
		public static final int DESCRIPTION_INDEX = 5;
		public static final int UPDATED_AT_INDEX = 6;
		public static final int DATE_INDEX = 7;
		public static final int IS_READ_INDEX = 8;
		public static final int IS_BOOKMARK_INDEX = 9;
	}
}
