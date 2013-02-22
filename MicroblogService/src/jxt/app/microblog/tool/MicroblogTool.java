package jxt.app.microblog.tool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import jxt.app.microblog.model.Contact;
import jxt.app.microblog.model.News;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

/**
 * 微博工具类
 * @author 祁毅
 * 2011-06-28
 */
public class MicroblogTool {
	public final String APPKEY = "2380647221";
	/**
	 * 返回用户最新发表的微博消息列表。
	 */
	public final String URL_NEWS = "http://api.t.sina.com.cn/statuses/user_timeline.xml?source=";
	/**
	 * 按用户ID或昵称返回用户资料以及用户的最新发布的一条微博消息
	 */
	public final String URL_CONTACT = "http://api.t.sina.com.cn/users/show.xml?source=";
	
	/**
	 * 获取服务器上所有内容的最新版本号
	 * @return 返回版本号集合
	 */
	public List<News> GetNews(String url){
		String xmlVersion = download(url);
		List<News> infos = parseNews(xmlVersion);
		return infos;
	}
	
	/**
	 * 获取服务器上所有内容的最新版本号
	 * @return 返回版本号集合
	 */
	public Contact GetContact(String url){
		String xmlVersion = download(url);
		Contact info = parseContact(xmlVersion);
		return info;
	}
	
	/**
	 * 返回用户最新发表的微博消息列表。
	 * @param xmlStr XML字符串
	 * @return 消息列表
	 */
	private List<News> parseNews(String xmlStr) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<News> infos = new ArrayList<News>();
		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();
			NewsXML newsXML = new NewsXML(
					infos);
			xmlReader.setContentHandler(newsXML);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}
	
	/**
	 * 按用户ID或昵称返回用户资料以及用户的最新发布的一条微博消息
	 * @param xmlStr XML字符串
	 * @return 用户资料
	 */
	private Contact parseContact(String xmlStr) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		Contact info = new Contact();
		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();
			ContactXML contactXML = new ContactXML(
					info);
			xmlReader.setContentHandler(contactXML);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	/**
	 * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容
	 * 1.创建一个URL对象
	 * 2.通过URL对象，创建一个HttpURLConnection对象
	 * 3.得到InputStram
	 * 4.从InputStream当中读取数据
	 * @param urlStr
	 * @return
	 */
	public String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			Log.e("URL", urlStr);
			// 创建一个URL对象
			URL url = new URL(urlStr);
			// 创建一个Http连接
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			// 使用IO流读取数据
			buffer = new BufferedReader(new InputStreamReader(urlConn
					.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
