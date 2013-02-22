package jxt.app.stockzx.xmpp;

import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;

/**
 * 文件传输监听
 */
public interface OnFileTransferListener {
	/**
	 * 文件传输中状态
	 * @param status 状态
	 */
	public void onStatus(Status status);

	/**
	 * 文件传输中进度
	 * @param progress 进度
	 */
	public void onProgress(double progress);
}
