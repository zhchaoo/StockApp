package jxt.app.stockzx.xmpp;

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

	public AndroidSaxFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public AndroidSaxFeedParser() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public List<ZXMessage> parseMessage(InputStream stream) {
		final ZXMessage currentMessage = new ZXMessage();
		RootElement root = new RootElement(messagexml.ROOT);
		final List<ZXMessage> messages = new ArrayList<ZXMessage>();
		Element body = root.getChild(messagexml.BODY);
		Element item = body.getChild(messagexml.NOTIFICATION);
		
		Log.v("WeiBo", "parser in begin");		
		
		item.setEndElementListener(new EndElementListener(){
			public void end() {
				currentMessage.format = messagexml.NOTIFICATION;
				messages.add(currentMessage.copy());
			}
		});
		item.getChild(messagexml.TYPE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.type=body;
			}
		});
		item.getChild(messagexml.SUB_TYPE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.sub_type=body;
			}
		});
		item.getChild(messagexml.RECEVIER).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.recevier=body;
			}
		});
		item.getChild(messagexml.CONTENT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.content=body;
			}
		});
		item.getChild(messagexml.CREATED_AT).setEndTextElementListener(new EndTextElementListener(){
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

	public List<CateGoryMessage> parseCategory(String feedUrl) {
		final CateGoryMessage currentMessage = new CateGoryMessage();
		RootElement root = new RootElement(categoryxml.ROOT);
		final List<CateGoryMessage> messages = new ArrayList<CateGoryMessage>();
		Element list = root.getChild(categoryxml.CATEGORY_LIST);
		Element item = list.getChild(categoryxml.CATEGORY);
		
		Log.v("WeiBo", "parser in begin");		
		
		item.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		item.getChild(categoryxml.CATEGORY_ID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.category_id=body;
			}
		});
		item.getChild(categoryxml.TITLE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.title=body;
			}
		});
		item.getChild(categoryxml.CATEGORY_CODE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.category_code=body;
			}
		});
		item.getChild(categoryxml.DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.description=body;
			}
		});
		
		try {
			Xml.parse(getInputStream(feedUrl), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return messages;
	}

	public List<NewsMessage> parseNews(String feedUrl) {
		final NewsMessage currentMessage = new NewsMessage();
		RootElement root = new RootElement(newsxml.ROOT);
		final List<NewsMessage> messages = new ArrayList<NewsMessage>();
		Element list = root.getChild(newsxml.NEWS_LIST);
		Element item = list.getChild(newsxml.NEWS);
		
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
		item.getChild(newsxml.UPDATED_AT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.updated_at=body;
			}
		});
		
		try {
			Xml.parse(getInputStream(feedUrl), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return messages;
	}
	
}
