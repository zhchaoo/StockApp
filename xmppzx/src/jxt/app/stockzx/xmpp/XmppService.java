package jxt.app.stockzx.xmpp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.BytestreamsProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.IBBProviders;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.pubsub.provider.EventProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemsProvider;
import org.jivesoftware.smackx.pubsub.provider.PubSubProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.preference.PreferenceManager;

public class XmppService extends Service {
	private SharedPreferences mSetting;
	private XMPPConnection mConnection;
	private FileTransferManager mTransferManager;
	private Chat mChat;
	private String mBroker;
	private String mBrokerClient;
	private boolean mXmppStatus = false;
	private String mBrokerStatus;
	private Presence mPresence;
	private FileTransfer mFileTransfer;
	private FileTransferRequest mFileRequest;
	private FeedParser xmlparser = new AndroidSaxFeedParser();
    private List<ZXMessage> mZXMessage;
    private List<NewsMessage> mNewsMessage;
	
	final RemoteCallbackList<IXmppListener> mXmppListeners
    	= new RemoteCallbackList<IXmppListener>();

	private final IXmppService.Stub mBinder = new IXmppService.Stub() {
		public String getBrokerStatus() throws RemoteException {
			return mBrokerStatus;
		}

		public boolean getXmppStatus() throws RemoteException {
			return mXmppStatus;
		}

		public void registerXmppListener(IXmppListener listener) throws RemoteException {
			if (listener != null) mXmppListeners.register(listener);
		}

		public void sendMessage(String message) throws RemoteException {
			// xmpp zx we shouldn't send message.
			// insertMms(mBroker, message, XmppMessage.TYPE_SEND, System.currentTimeMillis());
			if(mConnection.isAuthenticated()) {
				try {
					mChat.sendMessage(message);
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		}

		public void unregisterXmppListener(IXmppListener listener) throws RemoteException {
			if (listener != null) mXmppListeners.unregister(listener);
		}

		public void cancelTransfer() throws RemoteException {
			if(mFileTransfer != null) {
				mFileTransfer.cancel();
			}
		}

		public void recieveFile(String path) throws RemoteException {
			if(mFileRequest != null) {
				try {
					IncomingFileTransfer ift = mFileRequest.accept();
					ift.recieveFile(new File(path));
					mFileTransfer = ift;
					new FileTransferThread().start();
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
			mFileRequest = null;
		}

		public void rejectFile() throws RemoteException {
			if(mFileRequest != null) {
				mFileRequest.reject();
			}
			mFileRequest = null;
		}

		public void sendFile(String path, String description)
				throws RemoteException {
			if(mFileTransfer == null) {
				try {
					OutgoingFileTransfer oft = mTransferManager.createOutgoingFileTransfer(mBroker+mBrokerClient);
					oft.sendFile(new File(path), description);
					mFileTransfer = oft;
					new FileTransferThread().start();
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void getNewsList() throws RemoteException {
			ContentValues values = new ContentValues();
			try {
				mNewsMessage = xmlparser.parseNews(XmppConfig.NEWS_URL);
				for (NewsMessage msg : mNewsMessage){
					long date = System.currentTimeMillis();
		    		values.put(XmppzxMessage.NewsColumns.NEWS_ID, msg.news_id);
		    		values.put(XmppzxMessage.NewsColumns.TITLE, msg.title);
		    		values.put(XmppzxMessage.NewsColumns.NEWS_TYPE, msg.news_type);
		    		values.put(XmppzxMessage.NewsColumns.SOURCE, msg.source);
		    		values.put(XmppzxMessage.NewsColumns.DESCRIPTION, msg.description);
		    		values.put(XmppzxMessage.NewsColumns.UPDATED_AT,  msg.updated_at);
		    		values.put(XmppzxMessage.NewsColumns.IS_READ,  false);
		    		values.put(XmppzxMessage.NewsColumns.IS_BOOKMARK,  false);
		    		values.put(XmppzxMessage.NewsColumns.DATE, date);
		    		getContentResolver().insert(XmppzxMessage.CONTENT_NEWS, values);
				}
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
			
			
		}
    };
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mSetting = PreferenceManager.getDefaultSharedPreferences(this);
		
		mBroker = mSetting.getString(XmppConfig.BROKER,
				XmppConfig.DEFAULT_BROKER);
		
		configure(ProviderManager.getInstance());
		
		initConnection();
		
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if(info != null && info.isAvailable()) {
			new LoginThread().start();
		}
		
		mPresence = new Presence(Presence.Type.available);
		mPresence.setMode(Presence.Mode.chat);
		//mPresence.setStatus(getString(R.string.online));
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		logout();
		// Unregister all callbacks.
        mXmppListeners.kill();
	}
	
	/**
	 * 初始化连接
	 */
	private void initConnection() {
		ProxyInfo proxy;
		boolean isUseProxy = mSetting.getBoolean(XmppConfig.USE_PROXY, false);
		if (isUseProxy) {
			String pType = mSetting.getString(XmppConfig.PROXY_TYPE, "HTTP");
			String pServer = mSetting.getString(XmppConfig.PROXY_SERVER, "");
			int pPort = mSetting.getInt(XmppConfig.PROXY_PORT, 1080);
			String pUser = mSetting.getString(XmppConfig.PROXY_USERNAME, "");
			String pPass = mSetting.getString(XmppConfig.PROXY_PASSWORD, "");
			proxy = new ProxyInfo(ProxyType.valueOf(pType), pServer, pPort,
					pUser, pPass);
		} else {
			proxy = ProxyInfo.forNoProxy();
		}

		String server = mSetting.getString(XmppConfig.SERVER,
				XmppConfig.DEFAULT_SERVER);
		int port = mSetting.getInt(XmppConfig.PORT, 5222);
		ConnectionConfiguration ccf = new ConnectionConfiguration(server, port,
				proxy);

		if (mSetting.getBoolean(XmppConfig.USE_TLS, false)) {
			ccf.setSecurityMode(SecurityMode.required);
		}

		mConnection = new XMPPConnection(ccf);
		XMPPConnection.DEBUG_ENABLED = true;
	}

	/**
	 * 注销
	 */
	private void logout() {
		if (mConnection.isConnected()) {
			mConnection.disconnect();
		}
	}

	/**
	 * 创建chat
	 */
	private void buildChat() {
		if (mConnection.isAuthenticated()) {
			ChatManager manager = mConnection.getChatManager();
			
			mChat = manager.createChat(mBroker, mMessageListener);
			
			manager.addChatListener(new ChatManagerListener() {
				public void chatCreated(Chat chat, boolean createdLocally) {
					chat.addMessageListener(mMessageListener);
				}
			});
			
			new ServiceDiscoveryManager(mConnection);
			mTransferManager = new FileTransferManager(mConnection);
			mTransferManager.addFileTransferListener(mTransferListener);
			
			Presence p = mConnection.getRoster().getPresence(mBroker);
			mBrokerStatus = p.getStatus();
			if(mBrokerStatus != null) {
				mBrokerClient = p.getFrom().substring(p.getFrom().lastIndexOf("/"));
			}
			
			// Broadcast to all clients the new value.
            final int N = mXmppListeners.beginBroadcast();
            for (int i=0; i<N; i++) {
                try {
                	mXmppListeners.getBroadcastItem(i).onBrokerStatus(mBrokerStatus);
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                }
            }
            mXmppListeners.finishBroadcast();
			
			mConnection.getRoster().addRosterListener(mRosterListener);
		}
		
		mConnection.sendPacket(mPresence);
	}

	/**
	 * 向数据库中插入数据（发送和接收）
	 * @param broker 经纪人
	 * @param body 消息
	 * @param type 类型（发送或接收）
	 * @param date 时间戳
	 */
	private void insertMms(String broker, String body, int type, long date) {
		ContentValues values = new ContentValues();
		// TODO: not effectually.
		try {
			mZXMessage = xmlparser.parseMessage(new ByteArrayInputStream(body.getBytes("UTF-8")));
			for (ZXMessage msg : mZXMessage){
	    		values.put(XmppzxMessage.Columns.TYPE, msg.type);
	    		values.put(XmppzxMessage.Columns.SUB_TYPE, msg.sub_type);
	    		values.put(XmppzxMessage.Columns.RECEVIER, msg.recevier);
	    		values.put(XmppzxMessage.Columns.CONTENT, msg.content);
	    		values.put(XmppzxMessage.Columns.CREATED_AT, msg.created_at);
	    		values.put(XmppzxMessage.Columns.IS_READ, false);
	    		values.put(XmppzxMessage.Columns.DATE, date);
	    		getContentResolver().insert(XmppzxMessage.CONTENT_MESSAGE, values);
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	private void configure(ProviderManager pm) {
		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

		// Privacy
		//pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		// Delayed Delivery only the new version
		pm.addExtensionProvider("delay", "urn:xmpp:delay", new DelayInfoProvider());

		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());
		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

		// Chat State
		ChatStateExtension.Provider chatState = new ChatStateExtension.Provider();
		pm.addExtensionProvider("active", "http://jabber.org/protocol/chatstates", chatState);
		pm.addExtensionProvider("composing", "http://jabber.org/protocol/chatstates",
		    chatState);
		pm.addExtensionProvider("paused", "http://jabber.org/protocol/chatstates", chatState);
		pm.addExtensionProvider("inactive", "http://jabber.org/protocol/chatstates", chatState);
		pm.addExtensionProvider("gone", "http://jabber.org/protocol/chatstates", chatState);
//		// capabilities
//		pm.addExtensionProvider("c", "http://jabber.org/protocol/caps", new CapsProvider());
		//Pubsub
		pm.addIQProvider("pubsub", "http://jabber.org/protocol/pubsub", new PubSubProvider());
		pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub", new ItemsProvider());
		pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub", new ItemsProvider());
		pm.addExtensionProvider("item", "http://jabber.org/protocol/pubsub", new ItemProvider());

		pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub#event", new ItemsProvider());
		pm.addExtensionProvider("item", "http://jabber.org/protocol/pubsub#event", new ItemProvider());
		pm.addExtensionProvider("event", "http://jabber.org/protocol/pubsub#event", new EventProvider());

//		//PEP avatar
//		pm.addExtensionProvider("metadata", "urn:xmpp:avatar:metadata", new AvatarMetadataProvider());
//		pm.addExtensionProvider("data", "urn:xmpp:avatar:data", new AvatarProvider());

//	         PEPProvider pep  = new PEPProvider();
//	         AvatarMetadataProvider avaMeta  = new AvatarMetadataProvider();
//	         pep.registerPEPParserExtension("urn:xmpp:avatar:metadata", avaMeta);
//	         pm.addExtensionProvider("event", "http://jabber.org/protocol/pubsub#event", pep);

		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());
		// Time
		try {
		    pm.addIQProvider("query", "jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
//		    Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Time");
		}
		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster", new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event", new MessageEventProvider());
		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());
		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference", new GroupChatInvitation.Provider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user", new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());
		// Version
		try {
		    pm.addIQProvider("query", "jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
		    // Not sure what's happening here.
//		    Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Version");
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup", "http://www.jivesoftware.org/protocol/sharedgroup",
		    new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses", "http://jabber.org/protocol/address", new MultipleAddressesProvider());
		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());
		pm.addIQProvider("open", "http://jabber.org/protocol/ibb", new IBBProviders.Open());
		pm.addIQProvider("close", "http://jabber.org/protocol/ibb", new IBBProviders.Close());
		pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb", new IBBProviders.Data());

//		pm.addIQProvider("command", COMMAND_NAMESPACE, new AdHocCommandDataProvider());
//		pm.addExtensionProvider("malformed-action", COMMAND_NAMESPACE,
//		    new AdHocCommandDataProvider.MalformedActionError());
//		pm.addExtensionProvider("bad-locale", COMMAND_NAMESPACE,
//		    new AdHocCommandDataProvider.BadLocaleError());
//		pm.addExtensionProvider("bad-payload", COMMAND_NAMESPACE,
//		    new AdHocCommandDataProvider.BadPayloadError());
//		pm.addExtensionProvider("bad-sessionid", COMMAND_NAMESPACE,
//		    new AdHocCommandDataProvider.BadSessionIDError());
//		pm.addExtensionProvider("session-expired", COMMAND_NAMESPACE,
//		    new AdHocCommandDataProvider.SessionExpiredError());
	}
	
	private class LoginThread extends Thread {
		@Override
		public void run() {
			if (!mConnection.isAuthenticated()) {
				try {
					mConnection.connect();
					String username = mSetting.getString(XmppConfig.USERNAME,
							XmppConfig.DEFAULT_USERNAME);
					String password = mSetting.getString(XmppConfig.PASSWORD,
							XmppConfig.DEFAULT_PASSWORD);
					mConnection.login(username, password);
					
					mXmppStatus = true;
					buildChat();
				} catch (XMPPException e) {
					mXmppStatus = false;
//					e.printStackTrace();
				} finally {
					// Broadcast to all clients the new value.
		            final int N = mXmppListeners.beginBroadcast();
		            for (int i=0; i<N; i++) {
		                try {
		                	mXmppListeners.getBroadcastItem(i).onBrokerStatus(mBrokerStatus);
		                } catch (RemoteException e) {
		                    // The RemoteCallbackList will take care of removing
		                    // the dead object for us.
		                }
		            }
		            mXmppListeners.finishBroadcast();
				}
			}
		}
	}
	
	/**
	 * 文件传输进度及状态线程
	 */
	public class FileTransferThread extends Thread {
		@Override
		public void run() {
			if(mFileTransfer != null) {
				boolean update = true;
				while (update) {
					Status status = mFileTransfer.getStatus();
					if (status == Status.cancelled || status == Status.complete
							|| status == Status.error
							|| status == Status.refused) {
						update = false;
						mFileTransfer = null;
					}

					// Broadcast to all clients the new value.
		            final int N = mXmppListeners.beginBroadcast();
		            for (int i=0; i<N; i++) {
		                try {
		                	mXmppListeners.getBroadcastItem(i).onTransfeStatus(status.toString());
		                	
		                	if(status == Status.in_progress) {
		                		mXmppListeners.getBroadcastItem(i).onTransfeProgress(mFileTransfer.getProgress());
		                	}
		                } catch (RemoteException e) {
		                    // The RemoteCallbackList will take care of removing
		                    // the dead object for us.
		                }
		            }
		            mXmppListeners.finishBroadcast();
		            
		            try {
						sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 聊天监听接收到的消息
	 */
	private MessageListener mMessageListener = new MessageListener() {
		public void processMessage(Chat chat, Message msg) {
			String message = msg.getBody();
			long date = System.currentTimeMillis();
			
			if(message != null && message.length() != 0) {
				// Broadcast to all clients the new value.
	            final int N = mXmppListeners.beginBroadcast();
	            for (int i=0; i<N; i++) {
	                try {
	                	mXmppListeners.getBroadcastItem(i).onNewMessage(mBroker, message, date);
	                } catch (RemoteException e) {
	                    // The RemoteCallbackList will take care of removing
	                    // the dead object for us.
	                }
	            }
	            mXmppListeners.finishBroadcast();
				
				insertMms(mBroker, message, XmppzxMessage.TYPE_RECEIVE, date);
			}
		}
	};
	
	/**
	 * 花名册监听
	 */
	private RosterListener mRosterListener = new RosterListener() {
		public void entriesAdded(Collection<String> addresses) {
		}

		public void entriesDeleted(Collection<String> addresses) {
		}

		public void entriesUpdated(Collection<String> addresses) {
		}

		public void presenceChanged(Presence presence) {
			String broker = presence.getFrom().substring(0, presence.getFrom().lastIndexOf("/"));
			if(mBroker.equals(broker)) {
				mBrokerStatus = presence.getStatus();
				// Broadcast to all clients the new value.
	            final int N = mXmppListeners.beginBroadcast();
	            for (int i=0; i<N; i++) {
	                try {
	                	mXmppListeners.getBroadcastItem(i).onBrokerStatus(mBrokerStatus);
	                } catch (RemoteException e) {
	                    // The RemoteCallbackList will take care of removing
	                    // the dead object for us.
	                }
	            }
	            mXmppListeners.finishBroadcast();
				
				mBrokerClient = presence.getFrom().substring(presence.getFrom().lastIndexOf("/"));
			}
		}
		
	};
	
	/**
	 * 文件接收监听
	 */
	private FileTransferListener mTransferListener = new FileTransferListener() {
		public void fileTransferRequest(FileTransferRequest request) {
			mFileRequest = request;
			String name = mFileRequest.getFileName();
			String des = mFileRequest.getDescription();
			String type = mFileRequest.getMimeType();
			long size = mFileRequest.getFileSize();
			// Broadcast to all clients the new value.
            final int N = mXmppListeners.beginBroadcast();
            for (int i=0; i<N; i++) {
                try {
                	mXmppListeners.getBroadcastItem(i).onFileRequest(name, des, type, size);
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                }
            }
            mXmppListeners.finishBroadcast();
		}
	};
}
