package jxt.app.stockzx.xmpp;

import java.text.SimpleDateFormat;

public class CateGoryMessage implements Comparable<CateGoryMessage>{
	static SimpleDateFormat FORMATTER = 
		new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
	public String category_id;
	public String title;
	public String category_code;
	public String description;

	public CateGoryMessage copy(){
		CateGoryMessage copy = new CateGoryMessage();
		copy.category_id = category_id;
		copy.title = title;
		copy.category_code = category_code;
		copy.description = description;
		return copy;
	}
	
	public int compareTo(CateGoryMessage another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return Integer.parseInt(category_id) - Integer.parseInt(another.category_id);
	}
}