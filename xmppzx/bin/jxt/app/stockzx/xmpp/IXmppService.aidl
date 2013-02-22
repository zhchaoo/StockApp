package jxt.app.stockzx.xmpp;
import jxt.app.stockzx.xmpp.IXmppListener;

/**
 * Example of defining an interface for calling on to a remote service
 * (running in another process).
 */
interface IXmppService {
	/**
	 * ����xmpp����
	 * @param listener ��������
	 */
	void registerXmppListener(IXmppListener listener);  
	
	/**
	 * ȡ��xmpp����
	 * @param listener ��������
	 */
	void unregisterXmppListener(IXmppListener listener);    
	
	/**
	 * ������Ϣ
	 * @param msg ��Ϣ
	 */
    void sendMessage(String message);
    
    /**
	 * ��ȡ��ǰXmpp״̬
	 * @return ��ǰxmpp״̬
	 */
    boolean getXmppStatus();
    
    /**
	 * ��ȡ��ǰ����������״̬
	 * @return ����������״̬
	 */
    String getBrokerStatus();
    
     /**
	 * �����ļ�
	 * @param path ·��
	 * @param description ����
	 */
    void sendFile(String path, String description);
    
    /**
	 * �����ļ�
	 * @param path �ļ��洢·��
	 */
    void recieveFile(String path);
    
    /**
	 * �ܾ������ļ�
	 */
	void rejectFile();
	
	/**
	 * ȡ���ļ�����
	 */
	void cancelTransfer();
	
	/**
	 * ��ȡ����Ѷ
	 */
	void getNewsList();
}
