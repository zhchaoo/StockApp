package jxt.app.stockzx.xmpp;

import java.io.File;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
/**
 * �����ļ�����
 */
public class OutgoingTransfer{
	private OutgoingFileTransfer mOutgoing;
	private OnFileTransferListener mListener;
	
	/**
	 * ���캯��
	 * @param transfer OutgoingFileTransfer
	 */
	public OutgoingTransfer(OutgoingFileTransfer transfer) {
		mOutgoing = transfer;
	}

	/**
	 * �����ļ�
	 * @param file �ļ�·��
	 * @param description �ļ�����
	 */
	public void transfer(String file, String description) {
		if (mOutgoing != null) {
			try {
				mOutgoing.sendFile(new File(file), description);
				FileListenerThread thread = new FileListenerThread(mOutgoing, mListener);
				thread.start();
			} catch (XMPPException e) {
				e.printStackTrace();
				if (mListener != null) {
					mListener.onStatus(mOutgoing.getStatus());
				}
			}
		}
	}
	
	/**
	 * �����ļ��������
	 * @param listener ����
	 */
	public void setOnFileTransferListener(OnFileTransferListener listener) {
		mListener = listener;
	}
}
