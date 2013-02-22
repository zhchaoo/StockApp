package jxt.app.stockzx.xmpp;

import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;

/**
 * 文件传输进度及状态线程
 */
public class FileListenerThread extends Thread {
	private boolean isUpdate;
	private OnFileTransferListener mListener;
	private FileTransfer mTransfer;
	
	public FileListenerThread(FileTransfer transfer, OnFileTransferListener listener) {
		mTransfer = transfer;
		mListener = listener;
	}
	
	@Override
	public void run() {
		isUpdate = true;
		while (isUpdate) {
			if (mListener != null) {
				Status status = mTransfer.getStatus();
				mListener.onStatus(status);

				if (status == Status.in_progress) {
					mListener.onProgress(mTransfer.getProgress());
				}

				if (status == Status.cancelled || status == Status.complete
						|| status == Status.error
						|| status == Status.refused) {
					isUpdate = false;
				}

				try {
					sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
