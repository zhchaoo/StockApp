package jxt.app.stockzx.xmpp;

import java.io.File;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
/**
 * 发送文件传输
 */
public class OutgoingTransfer{
	private OutgoingFileTransfer mOutgoing;
	private OnFileTransferListener mListener;
	
	/**
	 * 构造函数
	 * @param transfer OutgoingFileTransfer
	 */
	public OutgoingTransfer(OutgoingFileTransfer transfer) {
		mOutgoing = transfer;
	}

	/**
	 * 发送文件
	 * @param file 文件路径
	 * @param description 文件描述
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
	 * 设置文件传输监听
	 * @param listener 监听
	 */
	public void setOnFileTransferListener(OnFileTransferListener listener) {
		mListener = listener;
	}
}
