package jxt.app.microblog.tool;

import java.util.List;

import jxt.app.microblog.model.News;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class NewsXML extends DefaultHandler {
	private List<News> infos = null;
	private News mvInfo = null;
	private String tagName = null;
	private boolean blUser = true;
	
	public NewsXML(List<News> infos) {
		super();
		this.infos = infos;
	}
	
	public List<News> getInfos() {
		return infos;
	}
	
	public void setInfos(List<News> infos) {
		this.infos = infos;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String temp = new String(ch, start, length);
		if (tagName.equals("created_at") && blUser) {
			mvInfo.setCreated_at(temp);
		} else if (tagName.equals("id") && blUser) {
			mvInfo.setS_id(temp);
		} else if (tagName.equals("text")) {
			mvInfo.setText(temp);
		} else if (tagName.equals("source")) {
			Log.e("source", temp);
			mvInfo.setSource(temp);
		} else if (tagName.equals("thumbnail_pic")) {
			mvInfo.setThumbnail_pic(temp);
		} else if (tagName.equals("bmiddle_pic")) {
			mvInfo.setBmiddle_pic(temp);
		} else if (tagName.equals("original_pic")) {
			mvInfo.setOriginal_pic(temp);
		} else if (tagName.equals("retweeted_status")) {
			mvInfo.setRetweeted_status(temp);
		} else if (tagName.equals("id") && !blUser) {
			mvInfo.setU_id(temp);
		} else if (tagName.equals("screen_name")) {
			mvInfo.setU_name(temp);
		}
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("status")) {
			infos.add(mvInfo);
			blUser = true;
		}
		tagName = "";

	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.tagName = localName;
		if (tagName.equals("status")) {
			mvInfo = new News();
		}
		if (tagName.equals("user")) {
			blUser = false;
		}
	}
}
