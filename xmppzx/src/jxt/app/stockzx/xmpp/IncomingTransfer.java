package jxt.app.stockzx.xmpp;

import java.io.File;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

/**
 * �����ļ�����
 */
public class IncomingTransfer{
	private FileTransferRequest mRequest;
	private IncomingFileTransfer inComing;
	private OnFileTransferListener mListener;

	/**
	 * ���캯��
	 * @param request FileTransferRequest
	 */
	public IncomingTransfer(FileTransferRequest request) {
		mRequest = request;
	}

	/**
	 * ��ȡ�ļ���
	 * @return �ļ���
	 */
	public String getFileName() {
		return mRequest.getFileName();
	}
	
	/**
	 * ��ȡ�ļ�����
	 * @return �ļ�����
	 */
	public String getDescription() {
		return mRequest.getDescription();
	}
	
	/**
	 * ��ȡ�ļ�����
	 * @return �ļ�����
	 */
	public String getFileType() {
		return mRequest.getMimeType();
	}
	
	/**
	 * ��ȡ�ļ���С
	 * @return �ļ���С
	 */
	public Long getFileSize() {
		return mRequest.getFileSize();
	}
	
	/**
	 * �ܾ������ļ�
	 */
	public void rejectFile() {
		mRequest.reject();
	}
	
	/**
	 * ȡ���ļ�����
	 */
	public void cancel() {
		if(inComing != null) {
			inComing.cancel();
		}
	}
	
	/**
	 * �����ļ�
	 * @param path �ļ��洢·��
	 */
	public void recieveFile(String path) {
		inComing = mRequest.accept();
		try {
			inComing.recieveFile(new File(path));
			FileListenerThread thread = new FileListenerThread(inComing, mListener);
			thread.start();
		} catch (XMPPException e) {
			e.printStackTrace();
			if(mListener != null) {
				mListener.onStatus(inComing.getStatus());
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
