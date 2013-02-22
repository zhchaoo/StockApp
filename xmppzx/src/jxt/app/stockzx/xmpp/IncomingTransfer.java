package jxt.app.stockzx.xmpp;

import java.io.File;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

/**
 * 接收文件传输
 */
public class IncomingTransfer{
	private FileTransferRequest mRequest;
	private IncomingFileTransfer inComing;
	private OnFileTransferListener mListener;

	/**
	 * 构造函数
	 * @param request FileTransferRequest
	 */
	public IncomingTransfer(FileTransferRequest request) {
		mRequest = request;
	}

	/**
	 * 获取文件名
	 * @return 文件名
	 */
	public String getFileName() {
		return mRequest.getFileName();
	}
	
	/**
	 * 获取文件描述
	 * @return 文件描述
	 */
	public String getDescription() {
		return mRequest.getDescription();
	}
	
	/**
	 * 获取文件类型
	 * @return 文件类型
	 */
	public String getFileType() {
		return mRequest.getMimeType();
	}
	
	/**
	 * 获取文件大小
	 * @return 文件大小
	 */
	public Long getFileSize() {
		return mRequest.getFileSize();
	}
	
	/**
	 * 拒绝接收文件
	 */
	public void rejectFile() {
		mRequest.reject();
	}
	
	/**
	 * 取消文件接收
	 */
	public void cancel() {
		if(inComing != null) {
			inComing.cancel();
		}
	}
	
	/**
	 * 接收文件
	 * @param path 文件存储路径
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
	 * 设置文件传输监听
	 * @param listener 监听
	 */
	public void setOnFileTransferListener(OnFileTransferListener listener) {
		mListener = listener;
	}
}
