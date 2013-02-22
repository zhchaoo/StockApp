package com.chaos.testurl;

import java.text.SimpleDateFormat;

public class WBMessage implements Comparable<WBMessage>{
	static SimpleDateFormat FORMATTER = 
		new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
	public String id;
	public String u_id;
	public String u_name;
	public String s_id;
	public String text;
	public String source;
	public String thumbnail_pic;
	public String bmiddle_pic;
	public String original_pic;
	public String retweeted_status;
	public String created_at;
	
	public WBMessage copy(){
		WBMessage copy = new WBMessage();
		copy.id = id;
		copy.u_id = u_id;
		copy.u_name = u_name;
		copy.s_id = s_id;
		copy.text = text;
		copy.source = source;
		copy.thumbnail_pic = thumbnail_pic;
		copy.bmiddle_pic = bmiddle_pic;
		copy.original_pic = original_pic;
		copy.retweeted_status = retweeted_status;
		copy.created_at = created_at;
		
		return copy;
	}
	
	public int compareTo(WBMessage another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return Integer.parseInt(id) - Integer.parseInt(another.id);
	}
}
