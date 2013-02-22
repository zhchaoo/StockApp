package jxt.app.stockzx.xmpp;

import java.util.List;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class XmppProvider extends ContentProvider {
	public static final String AUTHORITY = "jxt.app.stockzx.xmpp";
	public static final String TABLE_MESSAGE = "message";
	public static final String TABLE_CATEGORY = "category";
	public static final String TABLE_NEWS = "news";
	
	private static final int MATCH_MESSAGE = 1;
	private static final int MATCH_MESSAGE_ID = 11;
	private static final int MATCH_CATEGORY = 2;
	private static final int MATCH_CATEGORY_ID = 12;
	private static final int MATCH_NEWS = 3;
	private static final int MATCH_NEWS_ID = 13;
	// when bigger than 10 means id.
	private static final int MATCH_ID = 10;
	
	private static final UriMatcher sURLMatcher = new UriMatcher(
	        UriMatcher.NO_MATCH);

    static {
        sURLMatcher.addURI(AUTHORITY, TABLE_MESSAGE, MATCH_MESSAGE);
        sURLMatcher.addURI(AUTHORITY, TABLE_MESSAGE + "/#", MATCH_MESSAGE_ID);
        sURLMatcher.addURI(AUTHORITY, TABLE_CATEGORY, MATCH_CATEGORY);
        sURLMatcher.addURI(AUTHORITY, TABLE_CATEGORY + "/#", MATCH_CATEGORY_ID);
        sURLMatcher.addURI(AUTHORITY, TABLE_NEWS, MATCH_NEWS);
        sURLMatcher.addURI(AUTHORITY, TABLE_NEWS + "/#", MATCH_NEWS_ID);
    }
	
    
    // triggers.
	public static final String TRIGGER_MESSAGE_INSERT = "message_insert";
	public static final String TRIGGER_MESSAGE_DELETE = "message_delete";
	public static final String TRIGGER_NEWS_INSERT = "news_insert";
	public static final String TRIGGER_NEWS_DELETE = "news_delete";
	public static final String TRIGGER_BF_UPDATE_OF_IS_READ = "bf_update_of_is_read";
    
	// xml parser
	private FeedParser xmlparser = new AndroidSaxFeedParser();
	List<CateGoryMessage> mCateGoryMessage;
	
	
	// datebase.
	private SQLiteOpenHelper mOpenHelper;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "xmppzx.db";
		
		public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }
		
		private void createTables(SQLiteDatabase db) {
			// create a table for message.
			try {
				db.execSQL("CREATE TABLE " + TABLE_MESSAGE + " (" +
                    "_id INTEGER PRIMARY KEY," +
                    XmppzxMessage.Columns.TYPE + " TEXT, " +
                    XmppzxMessage.Columns.SUB_TYPE + " TEXT, " +
                    XmppzxMessage.Columns.RECEVIER + " TEXT, " +
                    XmppzxMessage.Columns.CONTENT + " TEXT, " +
                    XmppzxMessage.Columns.CREATED_AT + " TEXT, " +
                    XmppzxMessage.Columns.IS_READ + " BOOLEAN, " +
                    XmppzxMessage.Columns.DATE + " LONG);");
			} catch (Exception e) {
				Log.v("xmppzx", e.getMessage());
				e.printStackTrace();
			}
			// create a table for category count.
			try {
				db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
					"_id INTEGER PRIMARY KEY," +
                    XmppzxMessage.CateGoryColumns.TYPE + " TEXT UNIQUE, " +
                    XmppzxMessage.CateGoryColumns.TITLE + " TEXT, " +
                    XmppzxMessage.CateGoryColumns.COUNT + " INTEGER, " +
                    XmppzxMessage.CateGoryColumns.UN_READ + " INTEGER, " +
                    XmppzxMessage.CateGoryColumns.IS_CONCERN + " BOOLEAN);");
			} catch (Exception e) {
				Log.v("xmppzx", e.getMessage());
				e.printStackTrace();
			}
			
			// create a table for news.
			try {
				db.execSQL("CREATE TABLE " + TABLE_NEWS + " (" +
					"_id INTEGER PRIMARY KEY," +
                    XmppzxMessage.NewsColumns.NEWS_ID + " TEXT UNIQUE, " +
                    XmppzxMessage.NewsColumns.TITLE + " TEXT, " +
                    XmppzxMessage.NewsColumns.NEWS_TYPE + " TEXT, " +
                    XmppzxMessage.NewsColumns.SOURCE + " TEXT, " +
                    XmppzxMessage.NewsColumns.DESCRIPTION + " TEXT, " +
                    XmppzxMessage.NewsColumns.UPDATED_AT + " TEXT, " +
                    XmppzxMessage.NewsColumns.DATE + " LONG, " +
                    XmppzxMessage.NewsColumns.IS_READ + " BOOLEAN, " +
                    XmppzxMessage.NewsColumns.IS_BOOKMARK + " BOOLEAN);");
			} catch (Exception e) {
				Log.v("xmppzx", e.getMessage());
				e.printStackTrace();
			}
		}
		
		private void createTriggers(SQLiteDatabase db) {
			// create triggers for category table on message table.
			try {
				db.execSQL("CREATE TRIGGER " + TRIGGER_MESSAGE_INSERT +
                    " BEFORE INSERT ON " + TABLE_MESSAGE + " FOR EACH ROW " + " BEGIN " +
                    /*" UPDATE " + TABLE_CATEGORY + " SET " + 
                    XmppzxMessage.CateGoryColumns.COUNT + "=(" + XmppzxMessage.CateGoryColumns.COUNT + "+1) " +
                    " WHERE " + XmppzxMessage.CateGoryColumns.TYPE + "=new." + XmppzxMessage.Columns.SUB_TYPE + "; " +*/
                    " UPDATE " + TABLE_CATEGORY + " SET " + 
                    XmppzxMessage.CateGoryColumns.UN_READ + "=(" + XmppzxMessage.CateGoryColumns.UN_READ + "+1) " +
                    " WHERE " + XmppzxMessage.CateGoryColumns.TYPE + "=new." + XmppzxMessage.Columns.SUB_TYPE +
                    //" AND " + "new."+ XmppzxMessage.Columns.IS_READ + "=\"false\"" +
					" ;END;");
			} catch (Exception e) {
				Log.v("xmppzx", e.getMessage());
				e.printStackTrace();
			}
		
			try {
				db.execSQL("CREATE TRIGGER " + TRIGGER_MESSAGE_DELETE +
                    " BEFORE DELETE ON " + TABLE_MESSAGE + " FOR EACH ROW " + " BEGIN " +
                    /*" UPDATE " + TABLE_CATEGORY + " SET " + 
                    XmppzxMessage.CateGoryColumns.COUNT + "=(" + XmppzxMessage.CateGoryColumns.COUNT + "-1) " +
                    " WHERE " + XmppzxMessage.CateGoryColumns.TYPE + "=old." + XmppzxMessage.Columns.SUB_TYPE + "; " +*/
                    " UPDATE " + TABLE_CATEGORY + " SET " + 
                    XmppzxMessage.CateGoryColumns.UN_READ + "=(" + XmppzxMessage.CateGoryColumns.UN_READ + "-1) " +
                    " WHERE " + XmppzxMessage.CateGoryColumns.TYPE + "=old." + XmppzxMessage.Columns.SUB_TYPE + 
                    //" AND " + "old."+ XmppzxMessage.Columns.IS_READ + "=\"false\"" + "; " +
                    " ;END;");
			} catch (Exception e) {
				Log.v("xmppzx", e.getMessage());
				e.printStackTrace();
			}
			/*
			try {
				db.execSQL("CREATE TRIGGER " + TRIGGER_BF_UPDATE_OF_IS_READ +
                    " BEFORE UPDATE OF " + XmppzxMessage.Columns.IS_READ +
					" ON " + TABLE_MESSAGE + " FOR EACH ROW " + 
					" WHEN old." + XmppzxMessage.Columns.IS_READ +"=\"false\"" + " AND new." + XmppzxMessage.Columns.IS_READ +"=\"true\"" +
					" BEGIN UPDATE " + TABLE_CATEGORY + " SET " + 
                    XmppzxMessage.CateGoryColumns.COUNT + "=(" + XmppzxMessage.CateGoryColumns.COUNT + "-1) " +
                    " WHERE " + XmppzxMessage.CateGoryColumns.TYPE + "=old." + XmppzxMessage.Columns.SUB_TYPE + "; " +
                    " UPDATE " + TABLE_CATEGORY + " SET " + 
                    XmppzxMessage.CateGoryColumns.UN_READ + "=(" + XmppzxMessage.CateGoryColumns.UN_READ + "-1) " +
                    " WHERE " + XmppzxMessage.CateGoryColumns.TYPE + "=old." + XmppzxMessage.Columns.SUB_TYPE + 
                    " AND old."+ XmppzxMessage.Columns.IS_READ + "=\"false\"" + "; " +
                    " END;");
			} catch (Exception e) {
				Log.v("xmppzx", e.getMessage());
				e.printStackTrace();
			}
			*/
			
			// create triggers for category table on message table.
			try {
				db.execSQL("CREATE TRIGGER " + TRIGGER_NEWS_INSERT +
                    " BEFORE INSERT ON " + TABLE_NEWS + " FOR EACH ROW " + " BEGIN " +
                    " UPDATE " + TABLE_CATEGORY + " SET " + 
                    XmppzxMessage.CateGoryColumns.COUNT + "=(" + XmppzxMessage.CateGoryColumns.COUNT + "+1) " +
                    " WHERE " + XmppzxMessage.CateGoryColumns.TYPE + "=new." + XmppzxMessage.NewsColumns.NEWS_TYPE + 
					" ;END;");
			} catch (Exception e) {
				Log.v("xmppzx", e.getMessage());
				e.printStackTrace();
			}
			
			try {
				db.execSQL("CREATE TRIGGER " + TRIGGER_NEWS_DELETE +
                    " BEFORE DELETE ON " + TABLE_NEWS + " FOR EACH ROW " + " BEGIN " +
                    " UPDATE " + TABLE_CATEGORY + " SET " + 
                    XmppzxMessage.CateGoryColumns.COUNT + "=(" + XmppzxMessage.CateGoryColumns.COUNT + "-1) " +
                    " WHERE " + XmppzxMessage.CateGoryColumns.TYPE + "=old." + XmppzxMessage.NewsColumns.NEWS_TYPE +
                    " ;END;");
			} catch (Exception e) {
				Log.v("xmppzx", e.getMessage());
				e.printStackTrace();
			}
		}

		// jxt.disable create table for old data
		@Override
		public void onCreate(SQLiteDatabase db) {
			createTables(db);
			createTriggers(db);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS ebook");
            onCreate(db);
		}
	}
	
	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		
		// get category.
		mCateGoryMessage = xmlparser.parseCategory(XmppConfig.CATEGORY_URL);
		// insert initialize data of news.
		ContentValues values = new ContentValues();
		for(CateGoryMessage message : mCateGoryMessage) {
			values.put(XmppzxMessage.CateGoryColumns.TYPE, message.category_code);
			values.put(XmppzxMessage.CateGoryColumns.TITLE, message.title);
    		values.put(XmppzxMessage.CateGoryColumns.COUNT, 0);
    		values.put(XmppzxMessage.CateGoryColumns.UN_READ, 0);
    		values.put(XmppzxMessage.CateGoryColumns.IS_CONCERN, true);
    		//SQLiteDatabase db = mOpenHelper.getWritableDatabase();
    		insert(XmppzxMessage.CONTENT_CATEGORY, values);
    		// TODO: create table for each category.
		}
		return true;
	}

	@Override
	public String getType(Uri uri) {
		int match = sURLMatcher.match(uri);
	    switch (match) {
	        case MATCH_MESSAGE:
	            return "vnd.android.cursor.dir/"+TABLE_MESSAGE;
	        case MATCH_MESSAGE_ID:
	        	return "vnd.android.cursor.item/"+TABLE_MESSAGE;
	        case MATCH_CATEGORY:
	            return "vnd.android.cursor.dir/"+TABLE_CATEGORY;
	        case MATCH_CATEGORY_ID:
	        	return "vnd.android.cursor.item/"+TABLE_CATEGORY;
	        case MATCH_NEWS:
	            return "vnd.android.cursor.dir/"+TABLE_NEWS;
	        case MATCH_NEWS_ID:
	        	return "vnd.android.cursor.item/"+TABLE_NEWS;
	        default:
	        	throw new IllegalArgumentException("Unknown URL");
		}
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		
		switch (sURLMatcher.match(uri)) {
			case MATCH_MESSAGE:
			case MATCH_MESSAGE_ID:
				qb.setTables(TABLE_MESSAGE);
				break;
				
			case MATCH_CATEGORY:
			case MATCH_CATEGORY_ID:
				qb.setTables(TABLE_CATEGORY);
				break;
				
			case MATCH_NEWS:
			case MATCH_NEWS_ID:
				qb.setTables(TABLE_NEWS);

				break;
			
			default:break;	
		}
		// for ids.
		if (sURLMatcher.match(uri)>MATCH_ID) {
			qb.appendWhere("_id=");
			qb.appendWhere(uri.getPathSegments().get(1));
		}
		
		cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        if (cursor != null) {
        	cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
		return cursor;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int ret = -1;
        String table="";
		
        switch (sURLMatcher.match(uri)) {
        	case MATCH_MESSAGE:
	        case MATCH_MESSAGE_ID:
	        	table = TABLE_MESSAGE;
	        	break;
	        	
	        case MATCH_CATEGORY:	
	        case MATCH_CATEGORY_ID:
	        	table = TABLE_CATEGORY;
	        	break;
	        	
	        case MATCH_NEWS:
	        case MATCH_NEWS_ID:
	        	table = TABLE_NEWS;
	        	break;
	        	
		    default:break;
        }
        // for ids.
        if (sURLMatcher.match(uri)>MATCH_ID) {
        	String segment = uri.getPathSegments().get(1);
        	selection = "_id=" +segment;
		    if (TextUtils.isEmpty(selection)) {
		    	selection = "_id=" + segment;
			} else {
				selection = "_id=" + segment + " AND (" + selection + ")";
		    }
        }
        try {
		ret = db.update(table, values, selection, selectionArgs);
        } catch (Exception e) {
        	Log.v("WeiBo",e.getMessage());
        }
        getContext().getContentResolver().notifyChange(uri, null);

        // always notify category
        // notify category content.
    	getContext().getContentResolver().notifyChange(XmppzxMessage.CONTENT_CATEGORY, null);
        return ret;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		String table="";
		
        switch (sURLMatcher.match(uri)) {
	    	case MATCH_MESSAGE:
	        case MATCH_MESSAGE_ID:
	        	table = TABLE_MESSAGE;
	        	break;
	        	
	        case MATCH_CATEGORY:	
	        case MATCH_CATEGORY_ID:
	        	table = TABLE_CATEGORY;
	        	break;
	        	
	        case MATCH_NEWS:
	        case MATCH_NEWS_ID:
	        	table = TABLE_NEWS;
	        	break;
	        	
		    default:break;
	    }
		
		if(sURLMatcher.match(uri)>MATCH_ID) {
			String segment = uri.getPathSegments().get(1);
		    if (TextUtils.isEmpty(selection)) {
		    	selection = "_id=" + segment;
			} else {
				selection = "_id=" + segment + " AND (" + selection + ")";
		    }
		}
	        
		count = db.delete(table, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        
        // always notify category
        // notify category content.
    	getContext().getContentResolver().notifyChange(XmppzxMessage.CONTENT_CATEGORY, null);
        return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = 0;
		Uri newUrl = uri;
		
		try {
			switch (sURLMatcher.match(uri)) {
				case MATCH_MESSAGE:
					rowId = db.insert(TABLE_MESSAGE, XmppzxMessage.Columns.TYPE, values);
		        	newUrl = ContentUris.withAppendedId(XmppzxMessage.CONTENT_MESSAGE, rowId);
		        	break;
				case MATCH_CATEGORY:
					rowId = db.insert(TABLE_CATEGORY, XmppzxMessage.CateGoryColumns.TYPE, values);
		        	newUrl = ContentUris.withAppendedId(XmppzxMessage.CONTENT_CATEGORY, rowId);
		        	break;
				case MATCH_NEWS:
					rowId = db.insert(TABLE_NEWS, XmppzxMessage.NewsColumns.NEWS_TYPE, values);
		        	newUrl = ContentUris.withAppendedId(XmppzxMessage.CONTENT_NEWS, rowId);
		        	break;
		        default:break;
			}
		} catch (Exception e) {
			Log.v("xmppzx", e.getMessage());
		}
        getContext().getContentResolver().notifyChange(newUrl, null);
        
        // always notify category
        // notify category content.
    	getContext().getContentResolver().notifyChange(XmppzxMessage.CONTENT_CATEGORY, null);
        return newUrl;
	}
}
