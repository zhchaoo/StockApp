package jxt.app.stockzx.xmpp;

import java.text.SimpleDateFormat;

public class ZXMessage implements Comparable<ZXMessage>{
	static SimpleDateFormat FORMATTER = 
		new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
	public String format;
	public String type;
	public String sub_type;
	public String recevier;
	public String content;
	public String created_at;

	public ZXMessage copy(){
		ZXMessage copy = new ZXMessage();
		copy.format = format;
		copy.type = type;
		copy.sub_type = sub_type;
		copy.recevier = recevier;
		copy.content = content;
		copy.created_at = created_at;
		return copy;
	}
	
	public int compareTo(ZXMessage another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return Integer.parseInt(created_at) - Integer.parseInt(another.created_at);
	}
}
