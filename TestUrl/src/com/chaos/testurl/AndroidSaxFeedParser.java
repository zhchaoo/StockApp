package com.chaos.testurl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;

public class AndroidSaxFeedParser extends BaseFeedParser {

	static final String ROOT = "microblog";
	public AndroidSaxFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public AndroidSaxFeedParser() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public List<WBMessage> parseweibo(InputStream stream) {
		final WBMessage currentMessage = new WBMessage();
		RootElement root = new RootElement(ROOT);
		final List<WBMessage> messages = new ArrayList<WBMessage>();
		Element item = root.getChild(weiboxml.ITEM);
		
		Log.v("WeiBo", "parser in begin");		
		
		item.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		item.getChild(weiboxml.ID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.id=body;
			}
		});
		item.getChild(weiboxml.U_ID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.u_id=body;
			}
		});
		item.getChild(weiboxml.U_NAME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.u_name=body;
			}
		});
		item.getChild(weiboxml.S_ID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.s_id=body;
			}
		});
		item.getChild(weiboxml.TEXT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.text=body;
			}
		});
		item.getChild(weiboxml.SOURCE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.source=body;
			}
		});
		item.getChild(weiboxml.THUMBNAIL_PIC).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.thumbnail_pic=body;
			}
		});
		item.getChild(weiboxml.BMIDDLE_PIC).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.bmiddle_pic=body;
			}
		});
		item.getChild(weiboxml.RETWEETED_STATUS).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.retweeted_status=body;
			}
		});
		item.getChild(weiboxml.ORIGINAL_PIC).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.original_pic=body;
			}
		});
		item.getChild(weiboxml.CREATED_AT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.created_at=body;
			}
		});
		
		try {
			Xml.parse(stream, Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return messages;
	}
	

	public List<NewsViewMessage> parseNews(String feedUrl) {
		final NewsViewMessage currentMessage = new NewsViewMessage();
		RootElement root = new RootElement(newsxml.ROOT);
		final List<NewsViewMessage> messages = new ArrayList<NewsViewMessage>();
		Element item = root.getChild(newsxml.NEWS);
		
		Log.v("WeiBo", "parser in begin");		
		
		item.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		item.getChild(newsxml.NEWS_ID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.news_id=body;
			}
		});
		item.getChild(newsxml.TITLE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.title=body;
			}
		});
		item.getChild(newsxml.NEWS_TYPE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.news_type=body;
			}
		});
		item.getChild(newsxml.SOURCE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.source=body;
			}
		});
		item.getChild(newsxml.DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.description=body;
			}
		});
		item.getChild(newsxml.CONTENT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.content=body;
			}
		});
		item.getChild(newsxml.UPDATED_AT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.updated_at=body;
			}
		});
		
		try {
			Xml.parse(getInputStream(feedUrl,"<message><news><news_id>000000</news_id><title></title><content>\t您所访问的资讯不存在..</content></news></message>"), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return messages;
	}
	
	
}
