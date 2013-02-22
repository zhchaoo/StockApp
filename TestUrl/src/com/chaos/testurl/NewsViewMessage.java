package com.chaos.testurl;

import java.text.SimpleDateFormat;

public class NewsViewMessage implements Comparable<NewsViewMessage>{
	static SimpleDateFormat FORMATTER = 
		new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
	public String news_id;
	public String title;
	public String news_type;
	public String source;
	public String description;
	public String content;
	public String updated_at;

	public NewsViewMessage copy(){
		NewsViewMessage copy = new NewsViewMessage();
		copy.news_id = news_id;
		copy.title = title;
		copy.news_type = news_type;
		copy.source = source;
		copy.description = description;
		copy.content = content;
		copy.updated_at = updated_at;
		return copy;
	}
	
	public int compareTo(NewsViewMessage another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return Integer.parseInt(news_id) - Integer.parseInt(another.news_id);
	}
}