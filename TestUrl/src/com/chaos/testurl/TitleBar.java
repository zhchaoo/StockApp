package com.chaos.testurl;

import java.util.List;

import jxt.app.microblog.manage.IMicroblogManager;
import jxt.app.stock.xmpp.XmppConfig;
import jxt.app.voip.SipConfig;
import jxt.app.voip.Voip;

import org.sipdroid.sipua.ui.Receiver;
import org.sipdroid.sipua.ui.Receiver.OnCallRefusedListener;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

/**
 * This class represents a title bar for a particular "tab" or "window" in the
 * browser.
 */
public class TitleBar extends LinearLayout {
    private TextView        mTitle;
    private static final String LOGTAG = "testurl";
    private String mPath;  // Full path to the file to load
    private WebView mWebViewLeft;
    private WebView mWebViewRight;
    //private String data;
    
    // gpsource.bak begin
	/*
    private download mTouchIconLoader;
    public GpDataSource m_source;			
    */
    // gpsource.bak end
    
// jxt new button view.
    private ImageView       mStopButton;        
    private ImageView       mRtButtonBrowser;
    private ImageView       mRtButtonFav;
    private ImageView       mRtButtonMessage;
    private ImageView       mRtButtonIm;
    
    private EditText		mUrlText;
    private Drawable        mStopDrawable;
    private Drawable        mGoDrawable;
    private View.OnClickListener mOnClickListener = (View.OnClickListener) new TitleBarClicklistener();
    private View.OnClickListener mImOnClickListener = (View.OnClickListener) new ImBarClicklistener();
    private CompoundButton.OnCheckedChangeListener mCategoryChangeListener = (CompoundButton.OnCheckedChangeListener) new CategoryBarChangelistener();
    // jxt end.
    // weibo view
    private FrameLayout mMainFrame;
    private SideBar mCategoryBar;
    private GridView mZXList;
    private ToggleButton mZXToggleBtn;
    private ZXListAdapter mZXListAdapter;
    private ZXListAdapterDb mZXListAdapterDb;
    private ZXListAdapterEditDb mZXListAdapterEditDb;
    
    private Gallery mZXGallery;
    private FeedParser weiboparser = new AndroidSaxFeedParser();
    private List<WBMessage> weibomessages;
    
    // IM view
    private SideBar mImBar;
    private ImageView       mRtImBq;
    public ImageView       mRtImYy;
    private ImageView       mRtImJt;
    private ImageView       mRtImCz;
    public ImageView       mRtImYyEnd;
    public ImageView       mRtImYyAc;
    public ImageView       mRtImYyRj;
    private ImageView       mRtImMessage;
    public LinearLayout im_yy_panel;
    public LinearLayout im_yy_panel_call;
    public LinearLayout im_yy_panel_incall;
    
    public ImageView		mImPhoto;
    private TextView 		mImInput;
    //public TextView 		mImOutput;
    
    private TestUrl 		mBrowserActivity;
    private View            mTitleBg;
    
    // xmpp
	private TextView mBrokerView;
	public TextView mClientView;
	public TextView mVoipStatus;
	private EditText mEditText;
	private ImageView mSendButton;
	private ImageView mSendFileButton;
	private ImageView mReceiverButton;
	private ImageView mClearButton;
	
	private jxt.app.stock.xmpp.IXmppService mService;
	private jxt.app.stockzx.xmpp.IXmppService mServicezx;
	private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	private static final int MSG_UPDATE_STATUS = 1;
	private static final int MSG_RECEIVER_VISABLE = 2;
	
	// end xmpp
//    private MyHandler       mHandler;
    private int             mLeftMargin;
    private int             mRightMargin;

   
    IMicroblogManager myService;
    
    private class SideBar extends FrameLayout {
        public SideBar(Context context, int rclayout, int rcenteranim, int rcexitanim) {
            super(context, null);
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(rclayout, this, true);
            mEnterAnim = AnimationUtils.loadAnimation(context, rcenteranim);
            mExitAnim = AnimationUtils.loadAnimation(context, rcexitanim);
            setVisibility(View.GONE);
            mIsShow = false;
        }

        public void showOrHidePicker() {
            if (mIsShow) {
                hide();
            } else {
                show();
            }
        }
        
        public void show() {
            if (!mIsShow) {
                slide(View.VISIBLE);
                mIsShow = true;
                //mHandler.removeMessages(MSG_DISMISS_SIDEBAR);
                //mHandler.sendEmptyMessageDelayed(MSG_DISMISS_SIDEBAR, 5000);
                requestFocus();
            }
        }

        public void hide() {
            if (mIsShow) {
                slide(View.GONE);
                mIsShow = false;
            }
        }

        private void slide(int visibility) {
        	Log.v(LOGTAG,"slie : "+visibility);
            startAnimation(visibility == View.VISIBLE ? mEnterAnim : mExitAnim); 
            setVisibility(visibility);
        }

        public boolean mIsShow;
        private Animation mEnterAnim;
        private Animation mExitAnim;
    }
   
    private AdapterView.OnItemClickListener mListItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // It is possible that the view has been canceled when we get to
            // this point as back has a higher priority
            v.setSelected(true);
            
            // getNewsList.
            try {
            	mServicezx.getNewsList();
            } catch (Exception e) {
            	Log.v(mBrowserActivity.LOGTAG, e.getMessage());
            }
            
            TextView type_view = (TextView)v.findViewById(R.id.zx_type_code);
            String type = (String)type_view.getText();
            String selection = XmppzxMessage.NewsColumns.NEWS_TYPE + "=\"" +type + "\"";
            String order = XmppMessage.Columns.DATE + " DESC";
            Cursor cursor = mBrowserActivity.managedQuery(XmppzxMessage.CONTENT_NEWS, null, selection, null, order);
            mBrowserActivity.mNewsAdapter.changeCursor(cursor);
            // clear the message db table.
            String where = XmppzxMessage.Columns.SUB_TYPE + "=?";
            String[] selectionArgs = { type };
            int ret = 0;
            try {
            ret = mBrowserActivity.getContentResolver().delete(XmppzxMessage.CONTENT_MESSAGE, where, selectionArgs);
            } catch (Exception e) {
            	Log.v("WeiBo",e.getMessage());
            }
            Log.v("WeiBo","del message " + type+ " " + ret);
        }
    };
    
    public void SetActivitiy(TestUrl context) {
    	mBrowserActivity = context;
    	mWebViewLeft = mBrowserActivity.mWebViewLeft;
    	mWebViewRight = mBrowserActivity.mWebViewRight;
        
        // initial weibo service client
    	mBrowserActivity.bindService(new Intent("jxt.app.microblog.manage"),
      	      serviceConnection, Context.BIND_AUTO_CREATE);
    	// initial weibo view
    	mMainFrame = (FrameLayout) mBrowserActivity.findViewById(R.id.main_frame);
    	mCategoryBar = new SideBar(mBrowserActivity, R.layout.category_bar, R.anim.top_bar_enter, R.anim.top_bar_exit);
    	mMainFrame.addView(mCategoryBar);
    	mZXList = (GridView) mCategoryBar.findViewById(R.id.zx_list);
    	mZXToggleBtn = (ToggleButton)  mCategoryBar.findViewById(R.id.zx_toggle_btn);
    	mZXToggleBtn.setOnCheckedChangeListener(mCategoryChangeListener);
    	    	
    	// initial im view
    	mImBar = new SideBar(mBrowserActivity, R.layout.im_bar, R.anim.top_bar_enter, R.anim.top_bar_exit);
    	mMainFrame.addView(mImBar);
    	
    	mRtImBq = (ImageView) mImBar.findViewById(R.id.rt_btn_im_bq);
    	mRtImYy = (ImageView) mImBar.findViewById(R.id.rt_btn_im_yy);
    	mRtImJt = (ImageView) mImBar.findViewById(R.id.rt_btn_im_tp);
    	mRtImCz = (ImageView) mImBar.findViewById(R.id.rt_btn_im_cz);
    	mRtImYyAc = (ImageView) mImBar.findViewById(R.id.rt_btn_im_yy_accept);
    	mRtImYyRj = (ImageView) mImBar.findViewById(R.id.rt_btn_im_yy_reject);
    	mRtImYyEnd = (ImageView) mImBar.findViewById(R.id.rt_btn_im_yy_end);
    	mImPhoto = (ImageView) mImBar.findViewById(R.id.im_top_photo);
    	
    	mRtImMessage = (ImageView) mImBar.findViewById(R.id.rt_btn_send_message);
    	
    	im_yy_panel = (LinearLayout) mImBar.findViewById(R.id.im_yy_panel);
    	im_yy_panel_call = (LinearLayout) mImBar.findViewById(R.id.im_yy_panel_call);
    	im_yy_panel_incall = (LinearLayout) mImBar.findViewById(R.id.im_yy_panel_incall);
    	
    	mImInput = (TextView) mImBar.findViewById(R.id.im_input);
    	//mImOutput = (TextView) mImBar.findViewById(R.id.im_output);
    	
    	mRtImYyAc.setOnClickListener(mImOnClickListener);
    	mRtImYyRj.setOnClickListener(mImOnClickListener);
    	mRtImYyEnd.setOnClickListener(mImOnClickListener);
    	mRtImBq.setOnClickListener(mImOnClickListener);
    	mRtImYy.setOnClickListener(mImOnClickListener);
    	mRtImJt.setOnClickListener(mImOnClickListener);
    	mRtImCz.setOnClickListener(mImOnClickListener);
    	
    	// init voip
    	Voip.checkConfig(mBrowserActivity);
		Voip.on(mBrowserActivity,true);
		Receiver.engine(mBrowserActivity).updateDNS();
		Receiver.setOnCallRefusedListener(mRefusedListener);
        mVoipStatus = (TextView) mImBar.findViewById(R.id.voipstatus);
        
		// init xmpp
        String broker = PreferenceManager.getDefaultSharedPreferences(mBrowserActivity).getString(XmppConfig.BROKER, XmppConfig.DEFAULT_BROKER);
        mBrokerView = (TextView) mImBar.findViewById(R.id.im_broker);
        mBrokerView.setText(broker);
        mClientView = (TextView) mImBar.findViewById(R.id.im_brokerClient);
        
        Intent service = new Intent("jxt.app.stock.xmpp.XmppService");
        mBrowserActivity.startService(service);
        mBrowserActivity.bindService(service, mConn, Context.BIND_AUTO_CREATE);
        // init xmpp 2;
        Intent servicezx = new Intent("jxt.app.stockzx.xmpp.XmppService");
        mBrowserActivity.startService(servicezx);
        mBrowserActivity.bindService(servicezx, mConnzx, Context.BIND_AUTO_CREATE);
        
        // continue xmpp
        ListView listView = (ListView) mImBar.findViewById(R.id.im_output);
        String selection = XmppMessage.Columns.BROKER + "='" + broker + "'";
        String order = XmppMessage.Columns.DATE + " ASC";
        Cursor cursor = mBrowserActivity.managedQuery(XmppMessage.CONTENT_URI, null, selection, null, order);
        MessageAdapter adapter = new MessageAdapter(mBrowserActivity, R.layout.message_item, cursor);
        listView.setAdapter(adapter);
        
        // liuwei
       	try {
    	String selectionzx = "";
        String orderzx = "";
        // adapter for toggle edit
    	Cursor cursorzxedit = mBrowserActivity.managedQuery(XmppzxMessage.CONTENT_CATEGORY, null, selectionzx, null, orderzx);
    	Log.v(LOGTAG,"Cursor:"+cursorzxedit.getCount());
    	mZXListAdapterEditDb = new ZXListAdapterEditDb(mBrowserActivity,R.layout.zx_list_item_edit,cursorzxedit);
    	// adapter for toggle browser.
    	selectionzx = XmppzxMessage.CateGoryColumns.IS_CONCERN + "=1";
    	Cursor cursorzx = mBrowserActivity.managedQuery(XmppzxMessage.CONTENT_CATEGORY, null, selectionzx, null, orderzx);
    	mZXListAdapterDb = new ZXListAdapterDb(mBrowserActivity,R.layout.zx_list_item,cursorzx);
    	mZXList.setNumColumns(cursorzxedit.getCount() < 7 ? 7 : cursorzxedit.getCount());
    	mZXList.setOnItemClickListener(mListItemClickListener);
    	mZXList.setAdapter(mZXListAdapterDb);
    	//mZXList.setAdapter(mZXListAdapterDb);
    	//mZXGallery.setAdapter(mZXListAdapterDb);
       	} catch (Exception e){
       		Log.v(LOGTAG,"db : "+e.getMessage());
       	}
        mEditText = (EditText) mImBar.findViewById(R.id.im_input);
        
        mSendButton = (ImageView) mImBar.findViewById(R.id.rt_btn_send_message);
        mSendButton.setOnClickListener(mImOnClickListener);
        
        mSendFileButton = (ImageView) mImBar.findViewById(R.id.rt_btn_im_jt);
        mSendFileButton.setOnClickListener(mImOnClickListener);
        
        mReceiverButton = (ImageView) mImBar.findViewById(R.id.rt_btn_im_js);
        mReceiverButton.setOnClickListener(mImOnClickListener);
        
        mClearButton = (ImageView) mImBar.findViewById(R.id.rt_btn_im_cz);
        mClearButton.setOnClickListener(mImOnClickListener);
        
    }

    // xmpp listener
	private jxt.app.stock.xmpp.IXmppListener mListener = new jxt.app.stock.xmpp.IXmppListener.Stub() {
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
			Log.d("OnXmppListener", "onTransfeProgress Status:"+progress);
		}

		public void onTransfeStatus(String status) throws RemoteException {
			Log.d("OnXmppListener", "onTransfeStatus Status:"+status);
		}
	};
    
	private ServiceConnection mConn = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = jxt.app.stock.xmpp.IXmppService.Stub.asInterface(service);
			
			try {
				mService.registerXmppListener(mListener);
				
				String status = mService.getBrokerStatus();
				if(status == null) {
					status = mBrowserActivity.getString(R.string.offline);
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

	private jxt.app.stockzx.xmpp.IXmppListener mListenerzx = new jxt.app.stockzx.xmpp.IXmppListener.Stub() {
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
			Log.d("OnXmppListener", "onTransfeProgress Status:"+progress);
		}

		public void onTransfeStatus(String status) throws RemoteException {
			Log.d("OnXmppListener", "onTransfeStatus Status:"+status);
		}
	};
    
	// xmppzx listener
	private ServiceConnection mConnzx = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			mServicezx = jxt.app.stockzx.xmpp.IXmppService.Stub.asInterface(service);
			
			try {
				mServicezx.registerXmppListener(mListenerzx);
				
				String status = mServicezx.getBrokerStatus();
				//set status of xmppzx
				/*
				if(status == null) {
					status = mBrowserActivity.getString(R.string.offline);
				}
				mClientView.setText(status);
				 */
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		

		public void onServiceDisconnected(ComponentName name) {
			try {
				mServicezx.unregisterXmppListener(mListenerzx);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			mServicezx = null;
		}
	};
	
	
	
    // void refused.
	private OnCallRefusedListener mRefusedListener = new OnCallRefusedListener() {
		@Override
		public void onCallRefused(String reason) {
			System.out.println("obj:--------->" + reason);
			if(reason.trim().equals("Service Unavailable")){
				reason = "对方不在线！";
			}else if(reason.trim().equals("Request Terminated")){
				reason = "结束拨号！";
			}
			Message message = mHandler.obtainMessage(1, reason);
			message.sendToTarget();
		}
	};

	// voip handler
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					//mImOutput.append((String)msg.obj);
					mVoipStatus.setText((String)msg.obj);
					break;
				default:
					break;
			}
		}
	};
	
    private ServiceConnection serviceConnection = new ServiceConnection()
    {
     @Override
     public void onServiceConnected(ComponentName name, IBinder service)
     {
      Log.v("WeiBo","onServiceConnected");
      myService = IMicroblogManager.Stub.asInterface(service);
      mRtButtonMessage.setEnabled(true);
     }

     @Override
     public void onServiceDisconnected(ComponentName name)
     {
      // TODO Auto-generated method stub
     }
    };
    
    private void createView(Context context) {
    	LayoutInflater factory = LayoutInflater.from(context);
        factory.inflate(R.layout.title_bar, this);
		
        Resources resources = context.getResources();
	   
        mUrlText = (EditText)findViewById(R.id.url_text);
        //mTitle.setCompoundDrawablePadding(5);

        mTitleBg = findViewById(R.id.title_bg);

// jxt new button.		
        mStopButton = (ImageView) findViewById(R.id.stop);
        mRtButtonBrowser = (ImageView) findViewById(R.id.rt_btn_browser);
        mRtButtonFav = (ImageView) findViewById(R.id.rt_btn_fav);
        mRtButtonMessage = (ImageView) findViewById(R.id.rt_btn_message);
        mRtButtonIm = (ImageView) findViewById(R.id.rt_btn_im);
        mStopDrawable = resources.getDrawable(R.drawable.gp_btn_stop);
        mGoDrawable = resources.getDrawable(R.drawable.gp_btn_go);
        
        mStopButton.setOnClickListener(mOnClickListener);
        mRtButtonBrowser.setOnClickListener(mOnClickListener);
        mRtButtonFav.setOnClickListener(mOnClickListener);
        mRtButtonMessage.setOnClickListener(mOnClickListener);
        mRtButtonIm.setOnClickListener(mOnClickListener);
        mTitleBg.setOnClickListener(mOnClickListener);

        mRtButtonMessage.setEnabled(false);
        
        DisplayMetrics metrics = resources.getDisplayMetrics();
        mLeftMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8f, metrics);
        mRightMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 6f, metrics);
    }
    
    public TitleBar(Context context, AttributeSet attr) {  
        super(context, attr);  
        //至于这个构造函数里边要写一些什么大家就随便啦。
        createView(context);
    }  
    
    public TitleBar(TestUrl context) {
        super(context, null);
        SetActivitiy(context);
        createView(context);
        //mHandler = new MyHandler();
    } 

    private void ResetOtherBtn (ImageView btn) {
    	if (btn!=mRtButtonMessage && mRtButtonMessage.isSelected()) {
    		mRtButtonMessage.setSelected(false);
    		mCategoryBar.hide();   		
    	} else if(btn!=mRtButtonIm && mRtButtonIm.isSelected()) {
    		mRtButtonIm.setSelected(false);
    		mImBar.hide();    		
    	}
    }
    
    private class CategoryBarChangelistener implements CompoundButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked)
				mZXList.setAdapter(mZXListAdapterEditDb);
			else
				mZXList.setAdapter(mZXListAdapterDb);
		}	
    	
    }
    
    private class TitleBarClicklistener implements View.OnClickListener {
        public void onClick(View v) {  
            switch (v.getId()) {
                case R.id.rt_btn_browser:
                    break;
                case R.id.rt_btn_fav:
                    break;
                case R.id.rt_btn_message:
                	Log.v(LOGTAG,"message button : "+mRtButtonMessage.isSelected());
                	if (mRtButtonMessage.isSelected()) {
                		mRtButtonMessage.setSelected(false);
                		mCategoryBar.hide();
                	}else {
                		ResetOtherBtn(mRtButtonMessage);
                		mRtButtonMessage.setSelected(true);
                		mCategoryBar.show();
                		// WeiBo test begin.
                		/*String cat = "";
	                    try {
	                    	//myService.AddContacts("chaoser");
	                    	//myService.AddContacts("lxhjx");
	                    	//myService.AddContacts("yaochen");
	                    	
	            	        myService.UpdateMicroblog();
	                    	Log.v("WeiBo","InWeiBoModel");
	            	        cat = myService.GetMicroblog();

	            	        
	            	        Log.v("WeiBo",cat);
	                    } catch (Exception e) {
	            	        Log.v("WeiBo","Exception null");
	                    }
	                    if (cat == null || cat.length() < 5)
	                    	break;
	                    try {
	                    	Log.v("WeiBo","parser begin");
	                    	weibomessages = weiboparser.parseweibo(new ByteArrayInputStream(cat.getBytes("UTF-8")));
	                    	Log.v("WeiBo","parser end : "+weibomessages.size());
	                    	for (WBMessage msg : weibomessages){
	            	        	Log.v("WeiBo",msg.id);
	            	        	Log.v("WeiBo",msg.u_name);
	            	        	Log.v("WeiBo",msg.text);
	            	        	Log.v("WeiBo",msg.thumbnail_pic);
	            	        	//mImOutput.append(msg.text);
	            	        	
	            	        	// download image.
	            	        	if (msg.thumbnail_pic.length() > 5) {
	            	        		URL myFileUrl = null;   
	            	        		Bitmap bitmap = null;   
	            	        		try {   
	            	        		myFileUrl = new URL(msg.thumbnail_pic);   
	            	        		} catch (Exception e) {   
	            	        			e.printStackTrace();   
	            	        		}   
	            	        		try {   
	            	        			HttpURLConnection conn = (HttpURLConnection) 
	            	        			myFileUrl.openConnection();   
	            	        			conn.setDoInput(true);   
	            	        			conn.connect();   
	            	        			InputStream is = conn.getInputStream();   
	            	        			bitmap = BitmapFactory.decodeStream(is);   
	            	        			is.close();   
	            	        		} catch (Exception e) {   
	            	        			e.printStackTrace();   
	            	        		}
	            	        		mImPhoto.setImageBitmap(bitmap);
	            	        	}
	            	    	}
	                    } catch (Exception e) {
	            	        Log.v("WeiBo","parse Exception");
	            	        e.printStackTrace();
	                    }*/
                		// WeiBo test end.
                	}
                    break;
                case R.id.rt_btn_im:
                	if (mRtButtonIm.isSelected()) {
                		mRtButtonIm.setSelected(false);
                		mImBar.hide();
                	}else {
                		ResetOtherBtn(mRtButtonIm);
                		mRtButtonIm.setSelected(true);
                		mImBar.show();
                	}
                    break;
                case R.id.stop:
                	String text = mUrlText.getText().toString();
                	
                	String selection = XmppzxMessage.NewsColumns.TITLE + "=? OR " + XmppzxMessage.NewsColumns.DESCRIPTION + "=?";
                	String[] selectionArgs = { text, text };
                    String order = XmppMessage.Columns.DATE + " DESC";
                    Cursor cursor = mBrowserActivity.managedQuery(XmppzxMessage.CONTENT_NEWS, null, selection, selectionArgs, order);
                    mBrowserActivity.mNewsAdapter.changeCursor(cursor);
                	// gpsource.bak begin
                	/*
                	String url = mUrlText.getText().toString();
                	mWebViewRight.loadUrl(url);
                	String url = mUrlText.getText().toString();
                    int protol = -1;
                    
                    if ((protol=url.indexOf("app://")) >= 0) {
                    	//mWebViewRight.setVisibility(View.VISIBLE);
    	                mPath = url.substring(protol+6);
    	                Log.v(LOGTAG, "TestUrlGp"+" Loader URl : "+mPath);
    	                m_source = GpDataDriver.getsourcedriver(mBrowserActivity, mPath);
                    } else if ((protol=url.indexOf("guba://")) >= 0) {
                    	//mWebViewRight.setVisibility(View.GONE);
                    	mPath = url.substring(protol+7);
    	                Log.v(LOGTAG, "TestUrlGuba"+" Loader URl : "+mPath);
    	                m_source = GpDataDriver.getgubadriver(mBrowserActivity, mPath);
                    } else {
                    	//mWebViewLeft.setVisibility(View.GONE);
                    	Log.v(LOGTAG, "TestUrl"+" Loader URl : "+url);
                    	mWebViewRight.loadUrl(url);
                    	mWebViewRight.requestFocus();
                    	return;
                    }
                    
                    mTouchIconLoader = new download(mBrowserActivity, m_source);
                    
                    String Code;
                    int pos = mPath.indexOf('/');
                    if (pos<0) {
                    	Code = mPath;
                    } else {
                    	Code = mPath.substring(0, pos);
                    }
                    
                    mTouchIconLoader.execute(Code);
        			*/
                    // gpsource.bak end
                    break;
                default:
                    break;
            } 
        }  
     };


     private class ImBarClicklistener implements View.OnClickListener {
         public void onClick(View v) {  
             switch (v.getId()) {
                 case R.id.rt_btn_im_bq:
                     break;
                 case R.id.rt_btn_im_yy:
     				String broker = SipConfig.BROKER;
    				Receiver.engine(mBrowserActivity).call(broker,true);
    				mBrokerView.setText(broker);
    				//mRtImYy.setEnabled(false);
    				im_yy_panel.setVisibility(View.VISIBLE);
    				//mRtImYyEnd.setVisibility(View.VISIBLE);
                     break;
                 case R.id.rt_btn_im_tp:
                	 break;
                 case R.id.rt_btn_im_jt:
         			try {
        				mService.sendFile(PATH + "/yingwen.txt", "ceshi");
        			} catch (RemoteException e) {
        				e.printStackTrace();
        			}
        			break;
                 case R.id.rt_btn_im_js:
         			try {
        				mService.recieveFile(PATH + "/ceshi.txt");
        			} catch (RemoteException e) {
        				e.printStackTrace();
        			}
        			mReceiverButton.setVisibility(View.INVISIBLE);
         			break;
                 case R.id.rt_btn_im_cz:
         			mBrowserActivity.getContentResolver().delete(XmppMessage.CONTENT_URI, null, null);
                     break;
         		case R.id.rt_btn_send_message:
        			if(mEditText.getText().length() > 0) {
        				try {
        					mService.sendMessage(mEditText.getText().toString());
        				} catch (RemoteException e) {
        					e.printStackTrace();
        				}
            			mEditText.getText().clear();
            		}
        			break;
                 case R.id.rt_btn_im_yy_accept:
     				Receiver.engine(mBrowserActivity).answercall();
     				//mRtImYyEnd.setVisibility(View.VISIBLE);
     				//mRtImYyAc.setVisibility(View.GONE);
     				//mRtImYyRj.setVisibility(View.GONE);
     				im_yy_panel.setVisibility(View.VISIBLE);
                     break;
                 case R.id.rt_btn_im_yy_reject:
     				Receiver.engine(mBrowserActivity).rejectcall();
     				//mRtImYy.setVisibility(View.VISIBLE);
     				//mRtImYyAc.setVisibility(View.GONE);
     				//mRtImYyRj.setVisibility(View.GONE);
     				im_yy_panel.setVisibility(View.GONE);
                     break;
                 case R.id.rt_btn_im_yy_end:
     				Receiver.engine(mBrowserActivity).rejectcall();
     				//mRtImYy.setVisibility(View.VISIBLE);
     				//mRtImYyEnd.setVisibility(View.GONE);
     				im_yy_panel.setVisibility(View.GONE);
                     break;
                 default:
                     break;
             } 
         }  
      };
     
    /* package */ void setDisplayTitle(String title) {
    }

}

