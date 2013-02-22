package com.chaos.testurl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public abstract class BaseFeedParser implements FeedParser {

	// names of the XML tags
	public class weiboxml {
		static final  String ITEM = "item";
		static final  String ID = "id";
		static final  String U_ID = "u_id";
		static final  String U_NAME = "u_name";
		static final  String S_ID = "s_id";
		static final  String TEXT = "text";
		static final  String SOURCE = "source";
		static final  String THUMBNAIL_PIC = "thumbnail_pic";
		static final  String BMIDDLE_PIC = "bmiddle_pic";
		static final  String ORIGINAL_PIC = "original_pic";
		static final  String RETWEETED_STATUS = "retweeted_status";
		static final  String CREATED_AT = "created_at";
	}

	public class newsxml {
		static final  String ROOT = "message";
		static final  String NEWS = "news";
		static final  String NEWS_ID = "news_id";
		static final  String TITLE = "title";
		static final  String NEWS_TYPE = "news_type";
		static final  String SOURCE = "source";
		static final  String DESCRIPTION = "description";
		static final  String CONTENT = "content";
		static final  String UPDATED_AT = "updated_at";
	}
	
	private URL feedUrl;

	protected BaseFeedParser(String feedUrl){
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	protected BaseFeedParser() {
		// TODO Auto-generated constructor stub
		feedUrl = null;
	}

	protected InputStream getInputStream() {
		try {
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected InputStream getInputStream(String Url) {
		try {
			feedUrl = new URL(Url);
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			Log.v("testurl", "Url not exit");
			String defaultret = "";
			return new ByteArrayInputStream(defaultret.getBytes());
		}
	}
	
	protected InputStream getInputStream(String Url, String defaultret) {
		try {
			feedUrl = new URL(Url);
			return feedUrl.openConnection().getInputStream();
		} catch (IOException e) {
			Log.v("WeiBo", "Url not exit : "+defaultret);
			return new ByteArrayInputStream(defaultret.getBytes());
		}
	}
}