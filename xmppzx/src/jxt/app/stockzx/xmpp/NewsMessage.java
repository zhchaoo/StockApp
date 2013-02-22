package jxt.app.stockzx.xmpp;

import java.text.SimpleDateFormat;

public class NewsMessage implements Comparable<NewsMessage>{
	static SimpleDateFormat FORMATTER = 
		new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
	public String news_id;
	public String title;
	public String news_type;
	public String source;
	public String description;
	public String updated_at;

	public NewsMessage copy(){
		NewsMessage copy = new NewsMessage();
		copy.news_id = news_id;
		copy.title = title;
		copy.news_type = news_type;
		copy.source = source;
		copy.description = description;
		copy.updated_at = updated_at;
		return copy;
	}
	
	public int compareTo(NewsMessage another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return Integer.parseInt(news_id) - Integer.parseInt(another.news_id);
	}
}