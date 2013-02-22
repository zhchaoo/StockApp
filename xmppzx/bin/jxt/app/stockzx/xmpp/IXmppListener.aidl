package jxt.app.stockzx.xmpp;

/**
 * Example of defining an interface for calling on to a remote service
 * (running in another process).
 */
interface IXmppListener {
	/**
	 * 收到新消息
	 * @param broker 经纪人
	 * @param msg 新消息
	 * @param date 时间
	 */
    void onNewMessage(String broker, String msg, long date);
    
    /**
	 * Xmpp状态：STATE枚举
	 * @param Status 当前状态
	 */
    void onXmppStatus(boolean Status);
    
    /**
	 * 经纪人在线状态
	 * @param status 状态
	 */
    void onBrokerStatus(String status);
    
    /**
	 * 文件传输中状态
	 * @param status 状态
	 */
	void onTransfeStatus(String status);

	/**
	 * 文件传输中进度
	 * @param progress 进度
	 */
	void onTransfeProgress(double progress);
	
	/**
	 * 文件传输请求
	 * @param name 文件名
	 * @param description 文件描述
	 * @param type 文件类型
	 * @param size 文件大小
	 */
	void onFileRequest(String name, String description, String type, long size);
}
