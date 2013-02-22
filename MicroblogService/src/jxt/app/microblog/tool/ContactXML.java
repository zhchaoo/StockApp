package jxt.app.microblog.tool;

import jxt.app.microblog.model.Contact;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

/**
 * 联系人XML解析类
 * @author 祁毅
 * 2011-06-28
 */
public class ContactXML extends DefaultHandler{
	private Contact mvInfo = null;
	private String tagName = null;
	private boolean blTrue = false;
	
	public ContactXML(Contact mvInfo) {
		super();
		this.mvInfo = mvInfo;
	}
	
	public Contact getInfo() {
		return mvInfo;
	}
	
	public void setInfo(Contact mvInfo) {
		this.mvInfo = mvInfo;
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String temp = new String(ch, start, length);
		if (tagName.equals("id") && blTrue) {
			mvInfo.setU_id(temp);
			Log.e("mvInfo.setU_id", mvInfo.getU_id());
		} else if (tagName.equals("screen_name") && blTrue) {
			mvInfo.setU_name(temp);
			Log.e("mvInfo.setU_name", mvInfo.getU_name());
		} else if (tagName.equals("province") && blTrue) {
			mvInfo.setProvince(Integer.parseInt(temp));
			Log.e("mvInfo.setProvince", mvInfo.getProvince()+"");
		} else if (tagName.equals("city") && blTrue) {
			mvInfo.setCity(Integer.parseInt(temp));
			Log.e("mvInfo.setCity", mvInfo.getCity()+"");
		} else if (tagName.equals("location") && blTrue) {
			mvInfo.setLocation(temp);
			Log.e("mvInfo.setLocation", mvInfo.getLocation());
		} else if (tagName.equals("description") && blTrue) {
			mvInfo.setDescription(temp);
			Log.e("mvInfo.setDescription", mvInfo.getDescription());
		} else if (tagName.equals("url") && blTrue) {
			mvInfo.setUrl(temp);
			Log.e("mvInfo.setUrl", mvInfo.getUrl());
		} else if (tagName.equals("profile_image_url") && blTrue) {
			mvInfo.setProfile_image_url(temp);
			Log.e("mvInfo.setProfile_image_url", mvInfo.getProfile_image_url());
		} else if (tagName.equals("domain") && blTrue) {
			mvInfo.setDomain(temp);
			Log.e("mvInfo.setDomain", mvInfo.getDomain());
		} else if (tagName.equals("gender") && blTrue) {
			mvInfo.setGender(temp);
			Log.e("mvInfo.setGender", mvInfo.getGender());
		} else if (tagName.equals("followers_count") && blTrue) {
			mvInfo.setFollowers_count(Integer.parseInt(temp));
			Log.e("mvInfo.setFollowers_count", mvInfo.getFollowers_count()+"");
		} else if (tagName.equals("friends_count") && blTrue) {
			mvInfo.setFriends_count(Integer.parseInt(temp));
			Log.e("mvInfo.setFriends_count", mvInfo.getFriends_count()+"");
		} else if (tagName.equals("statuses_count") && blTrue) {
			mvInfo.setStatuses_count(Integer.parseInt(temp));
			Log.e("mvInfo.setStatuses_count", mvInfo.getStatuses_count()+"");
		} else if (tagName.equals("favourites_count") && blTrue) {
			mvInfo.setFavourites_count(Integer.parseInt(temp));
			Log.e("mvInfo.setFavourites_count", mvInfo.getFavourites_count()+"");
		} else if (tagName.equals("created_at") && blTrue) {
			mvInfo.setCreated_at(temp);
			Log.e("mvInfo.setCreated_at", mvInfo.getCreated_at());
		} else if (tagName.equals("verified") && blTrue) {
			mvInfo.setVerified(temp);
			Log.e("mvInfo.setVerified", mvInfo.getVerified());
		}
	}

	public void endDocument() throws SAXException {
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equals("user")){
			blTrue = false;
		}
		tagName = "";
	}

	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.tagName = localName;
		if (tagName.equals("user")) {
			blTrue = true;
			//mvInfo = new Contact();
		}
	}
}
