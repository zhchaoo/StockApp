package jxt.app.stockzx.xmpp;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseFeedParser implements FeedParser {

	// names of the XML tags
	public class messagexml {
		static final  String ROOT = "message";
		static final  String BODY = "body";
		static final  String NOTIFICATION = "notification";
		static final  String TYPE = "type";
		static final  String SUB_TYPE = "sub_type";
		static final  String RECEVIER = "receiver";
		static final  String CONTENT = "content";
		static final  String CREATED_AT = "created_at";
	}

	public class categoryxml {
		static final  String ROOT = "message";
		static final  String CATEGORY_LIST = "category_list";
		static final  String CATEGORY = "category";
		static final  String CATEGORY_ID = "category_id";
		static final  String TITLE = "title";
		static final  String CATEGORY_CODE = "category_code";
		static final  String DESCRIPTION = "description";
	}
	
	public class newsxml {
		static final  String ROOT = "message";
		static final  String NEWS_LIST = "news_list";
		static final  String NEWS = "news";
		static final  String NEWS_ID = "news_id";
		static final  String TITLE = "title";
		static final  String NEWS_TYPE = "news_type";
		static final  String SOURCE = "source";
		static final  String DESCRIPTION = "description";
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
			throw new RuntimeException(e);
		}
	}
}