package jxt.app.stockzx.xmpp;

import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;

/**
 * �ļ��������
 */
public interface OnFileTransferListener {
	/**
	 * �ļ�������״̬
	 * @param status ״̬
	 */
	public void onStatus(Status status);

	/**
	 * �ļ������н���
	 * @param progress ����
	 */
	public void onProgress(double progress);
}
