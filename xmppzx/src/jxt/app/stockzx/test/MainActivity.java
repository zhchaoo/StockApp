package jxt.app.stockzx.test;

import jxt.app.stockzx.R;
import jxt.app.stockzx.xmpp.IXmppListener;
import jxt.app.stockzx.xmpp.IXmppService;
import jxt.app.stockzx.xmpp.XmppConfig;
import jxt.app.stockzx.xmpp.XmppzxMessage;
import jxt.app.stockzx.xmpp.XmppService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	private static final int MSG_UPDATE_STATUS = 1;
	private static final int MSG_RECEIVER_VISABLE = 2;
	private IXmppService mService;
	private TextView mBrokerView;
	private TextView mClientView;
	private EditText mEditText;
	private Button mSendButton;
	private Button mSendFileButton;
	private Button mReceiverButton;
	private Button mClearButton;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_UPDATE_STATUS:
					String status = (String)msg.obj;
					if(status == null) {
						status = getString(R.string.offline);
					}
					mClientView.setText(status);
					break;
				case MSG_RECEIVER_VISABLE:
					mReceiverButton.setVisibility(View.VISIBLE);
				default:
					break;
			}
		}
	};
	
	private IXmppListener mListener = new IXmppListener.Stub() {
		public void onBrokerStatus(String status) throws RemoteException {
			Log.d("OnXmppListener", "onBrokerStatus status:"+status);
			Message m = mHandler.obtainMessage(MSG_UPDATE_STATUS, status);
			m.sendToTarget();
		}

		public void onNewMessage(String broker, String msg, long date)
				throws RemoteException {
			Log.d("OnXmppListener", "onNewMessage broker:"+broker+" msg:"+msg);
		}

		public void onXmppStatus(boolean Status) throws RemoteException {
			Log.d("OnXmppListener", "onXmppState Status:"+Status);
		}

		public void onFileRequest(String name, String description, String type,
				long size) throws RemoteException {
			Log.d("OnXmppListener", "onFileRequest name:"+name+" description:"+description+" type:"+type+" size:"+size);
			mHandler.sendEmptyMessage(MSG_RECEIVER_VISABLE);
		}

		public void onTransfeProgress(double progress) throws RemoteException {
			Log.d("OnXmppListener", "onTransfeProgress progress:"+progress);
		}

		public void onTransfeStatus(String status) throws RemoteException {
			Log.d("OnXmppListener", "onTransfeStatus Status:"+status);
		}
	};
	
	private ServiceConnection mConn = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IXmppService.Stub.asInterface(service);
			
			try {
				mService.registerXmppListener(mListener);
				
				String status = mService.getBrokerStatus();
				if(status == null) {
					status = getString(R.string.offline);
				}
				mClientView.setText(status);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		public void onServiceDisconnected(ComponentName name) {
			try {
				mService.unregisterXmppListener(mListener);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			mService = null;
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String broker = PreferenceManager.getDefaultSharedPreferences(this).getString(XmppConfig.BROKER, XmppConfig.DEFAULT_BROKER);
        
        mBrokerView = (TextView) findViewById(R.id.broker);
        mBrokerView.setText(broker);
        
        mClientView = (TextView) findViewById(R.id.brokerClient);
        
        ListView listView = (ListView) findViewById(R.id.list);
        String selection = XmppzxMessage.Columns.TYPE_INDEX + "='" + broker + "'";
        String order = XmppzxMessage.Columns.CREATED_AT_INDEX + " ASC";
        Cursor cursor = managedQuery(XmppzxMessage.CONTENT_MESSAGE, null, selection, null, order);
        MessageAdapter adapter = new MessageAdapter(this, R.layout.message_item, cursor);
        listView.setAdapter(adapter);
        
        mEditText = (EditText) findViewById(R.id.edit);
        
        mSendButton = (Button) findViewById(R.id.send);
        mSendButton.setOnClickListener(this);
        
        mSendFileButton = (Button) findViewById(R.id.send_file);
        mSendFileButton.setOnClickListener(this);
        
        mReceiverButton = (Button) findViewById(R.id.receive_file);
        mReceiverButton.setOnClickListener(this);
        
        mClearButton = (Button) findViewById(R.id.clear);
        mClearButton.setOnClickListener(this);
        
        Intent service = new Intent(this, XmppService.class);
        startService(service);
        bindService(service, mConn, Context.BIND_AUTO_CREATE);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d("onDestroy", "onResume");
    	try {
			if(mService != null) {
				String status = mService.getBrokerStatus();
				if(status == null) {
					status = getString(R.string.offline);
				}
				mClientView.setText(status);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.d("MainActivity", "onDestroy");
    	unbindService(mConn);
    }
    
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			if(mEditText.getText().length() > 0) {
				try {
					mService.sendMessage(mEditText.getText().toString());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
    			mEditText.getText().clear();
    		}
			break;
		case R.id.send_file:
			try {
				mService.sendFile("/mnt/sdcard/yingwen.txt", "ceshi");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case R.id.receive_file:
			try {
				mService.recieveFile("/mnt/sdcard/ceshi.txt");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			mReceiverButton.setVisibility(View.INVISIBLE);
			break;
		case R.id.clear:
			getContentResolver().delete(XmppzxMessage.CONTENT_MESSAGE, null, null);
			break;
		default:
			break;
		}
	}
}