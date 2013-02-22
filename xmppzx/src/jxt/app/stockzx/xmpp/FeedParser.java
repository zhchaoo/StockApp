package jxt.app.stockzx.xmpp;
import java.io.InputStream;
import java.util.List;

public interface FeedParser {
	List<ZXMessage> parseMessage(InputStream stream);
	List<CateGoryMessage> parseCategory(String feedUrl);
	List<NewsMessage> parseNews(String feedUrl);
}
