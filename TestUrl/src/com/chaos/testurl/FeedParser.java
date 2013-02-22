package com.chaos.testurl;
import java.io.InputStream;
import java.util.List;

public interface FeedParser {
	List<WBMessage> parseweibo(InputStream stream);
	List<NewsViewMessage> parseNews(String feedUrl);
}
