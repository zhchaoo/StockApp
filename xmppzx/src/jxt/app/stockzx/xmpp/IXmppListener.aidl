package jxt.app.stockzx.xmpp;

/**
 * Example of defining an interface for calling on to a remote service
 * (running in another process).
 */
interface IXmppListener {
	/**
	 * �յ�����Ϣ
	 * @param broker ������
	 * @param msg ����Ϣ
	 * @param date ʱ��
	 */
    void onNewMessage(String broker, String msg, long date);
    
    /**
	 * Xmpp״̬��STATEö��
	 * @param Status ��ǰ״̬
	 */
    void onXmppStatus(boolean Status);
    
    /**
	 * ����������״̬
	 * @param status ״̬
	 */
    void onBrokerStatus(String status);
    
    /**
	 * �ļ�������״̬
	 * @param status ״̬
	 */
	void onTransfeStatus(String status);

	/**
	 * �ļ������н���
	 * @param progress ����
	 */
	void onTransfeProgress(double progress);
	
	/**
	 * �ļ���������
	 * @param name �ļ���
	 * @param description �ļ�����
	 * @param type �ļ�����
	 * @param size �ļ���С
	 */
	void onFileRequest(String name, String description, String type, long size);
}
