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
 * ΢��������
 * @author ����
 * 2011-06-28
 */
public class MicroblogTool {
	public final String APPKEY = "2380647221";
	/**
	 * �����û����·����΢����Ϣ�б�
	 */
	public final String URL_NEWS = "http://api.t.sina.com.cn/statuses/user_timeline.xml?source=";
	/**
	 * ���û�ID���ǳƷ����û������Լ��û������·�����һ��΢����Ϣ
	 */
	public final String URL_CONTACT = "http://api.t.sina.com.cn/users/show.xml?source=";
	
	/**
	 * ��ȡ���������������ݵ����°汾��
	 * @return ���ذ汾�ż���
	 */
	public List<News> GetNews(String url){
		String xmlVersion = download(url);
		List<News> infos = parseNews(xmlVersion);
		return infos;
	}
	
	/**
	 * ��ȡ���������������ݵ����°汾��
	 * @return ���ذ汾�ż���
	 */
	public Contact GetContact(String url){
		String xmlVersion = download(url);
		Contact info = parseContact(xmlVersion);
		return info;
	}
	
	/**
	 * �����û����·����΢����Ϣ�б�
	 * @param xmlStr XML�ַ���
	 * @return ��Ϣ�б�
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
	 * ���û�ID���ǳƷ����û������Լ��û������·�����һ��΢����Ϣ
	 * @param xmlStr XML�ַ���
	 * @return �û�����
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
	 * ����URL�����ļ���ǰ��������ļ����е��������ı��������ķ���ֵ�����ļ����е�����
	 * 1.����һ��URL����
	 * 2.ͨ��URL���󣬴���һ��HttpURLConnection����
	 * 3.�õ�InputStram
	 * 4.��InputStream���ж�ȡ����
	 * @param urlStr
	 * @return
	 */
	public String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			Log.e("URL", urlStr);
			// ����һ��URL����
			URL url = new URL(urlStr);
			// ����һ��Http����
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			// ʹ��IO����ȡ����
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
