package jxt.app.stockzx.xmpp;
import jxt.app.stockzx.xmpp.IXmppListener;

/**
 * Example of defining an interface for calling on to a remote service
 * (running in another process).
 */
interface IXmppService {
	/**
	 * 设置xmpp监听
	 * @param listener 监听对象
	 */
	void registerXmppListener(IXmppListener listener);  
	
	/**
	 * 取消xmpp监听
	 * @param listener 监听对象
	 */
	void unregisterXmppListener(IXmppListener listener);    
	
	/**
	 * 发送消息
	 * @param msg 消息
	 */
    void sendMessage(String message);
    
    /**
	 * 获取当前Xmpp状态
	 * @return 当前xmpp状态
	 */
    boolean getXmppStatus();
    
    /**
	 * 获取当前经纪人在线状态
	 * @return 经纪人在线状态
	 */
    String getBrokerStatus();
    
     /**
	 * 发送文件
	 * @param path 路径
	 * @param description 描述
	 */
    void sendFile(String path, String description);
    
    /**
	 * 接收文件
	 * @param path 文件存储路径
	 */
    void recieveFile(String path);
    
    /**
	 * 拒绝接收文件
	 */
	void rejectFile();
	
	/**
	 * 取消文件传输
	 */
	void cancelTransfer();
	
	/**
	 * 获取新资讯
	 */
	void getNewsList();
}
