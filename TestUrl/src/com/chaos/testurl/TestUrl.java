package com.chaos.testurl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.sipdroid.sipua.UserAgent;
import org.sipdroid.sipua.ui.Receiver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

public class TestUrl extends Activity {

	
    public static final String LOGTAG = "testurl";
    //private static final String GP_LOAD_FOLDER = "/data/data/com.test.file.write/";
    //private static final String GP_LOAD_FILE = "self_stock_code.txt";
    public static final String NEWS_URL = "http://192.168.1.211/news/get_news.jsp?news_id=";
    public static final String CATEGORY_SYNC_URL = "http://192.168.1.211/favorite/news.jsp";
    
    private String mPath;  // Full path to the file to load
    public WebView mWebViewLeft;
    public WebView mWebViewRight;
    public ListView mNewsListView;
    public NewsView mNewsView;
    
    private NotificationManager mNM;
    private Notification mNotification;
    
    private TitleBar mTitlebar;
  
    
    public NewsAdapterDb mNewsAdapter;
	
	static int notify_id = 0;
	
	// gpsource.bak begin
	/*
    public final class GpJavaScriptInterface{
        GpJavaScriptInterface(){
        
        }
        
        public String loadotherpanel(String url, int win, String type, String params){
        	 String data = url+" "+win+" "+type+" "+params;
             mNotification.contentView.setTextViewText(R.id.text, data);
             mNM.cancel(notify_id);
             mNM.notify(notify_id, mNotification);
             notify_id = (notify_id+1)%4;
             Log.v(LOGTAG, data);
             
             if (url!=null) {
            	 switch (win) {
            	 case 0: //mWebViewLeft.setVisibility(View.VISIBLE);
            	 		 Log.v(LOGTAG,"loadotherpanle"+url);
            	 		 mWebViewLeft.loadUrl(url);
            	 		 break;
            	 case 1: //mWebViewLeft.setVisibility(View.VISIBLE);
		    	 		 mWebViewRight.loadUrl(url);
		    	 		 break;
            	 case 2: //mWebViewLeft.setVisibility(View.GONE);
		    	 		 mWebViewRight.loadUrl(url);
		    	 		 break;
		    	 default:break;
            	 }
             }
             
             return data;
        }
    }
    */
    // gpsource.bak end
	// gpsource.bak begin
    /*
    public class StocklClient extends WebViewClient {
        // The main WebViewClient.
    	
    	// gpsource.bak begin
    	//private linkdownload mLinkLoader;
    	// gpsource.bak end
    	

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	view.loadUrl(url);
        	return true;
        	
        	int protol = -1;
            if (url.indexOf("app://")<0 && url.indexOf("guba://")<0)
            	return false;
            
            Log.v(LOGTAG, "TestUrl"+" shouldOverrideUrlLoading : "+mPath);
            
            if (mTitlebar.m_source == null) {
            	 if ((protol=url.indexOf("app://")) >= 0) {
                 	//mWebViewRight.setVisibility(View.VISIBLE);
 	                mPath = url.substring(protol+6);
 	                Log.v(LOGTAG, "TestUrlGp"+" Loader URl : "+mPath);
 	               mTitlebar.m_source = GpDataDriver.getsourcedriver(TestUrl.this, mPath);
                 } else if ((protol=url.indexOf("guba://")) >= 0) {
                 	//mWebViewRight.setVisibility(View.GONE);
                 	mPath = url.substring(protol+7);
 	                Log.v(LOGTAG, "TestUrlGuba"+" Loader URl : "+mPath);
 	               mTitlebar.m_source = GpDataDriver.getgubadriver(TestUrl.this, mPath);
                 } else {
                	return false;
                 }
            }
            
            mLinkLoader = new linkdownload(TestUrl.this, mTitlebar.m_source);
            mLinkLoader.execute(mPath);
            
        	return true;

        }
    }
	*/
    // gpsource.bak end
	// gpsource.bak start
	/*
    public void findAndSetWebViews() {
    	// left webview.

    	mWebViewLeft= (WebView)findViewById(R.id.web_view_left);
    	mWebViewLeft.setWebViewClient(new StocklClient());
    	mWebViewLeft.addJavascriptInterface(new GpJavaScriptInterface(), "gp");
        WebSettings  webSettings = mWebViewLeft.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        //是否允许在webview中执行javascript
        webSettings.setJavaScriptEnabled(true);
    	
    	// right webview.
        mWebViewRight= (WebView)findViewById(R.id.web_view_right);
        mWebViewRight.setWebViewClient(new StocklClient());
        mWebViewRight.addJavascriptInterface(new GpJavaScriptInterface(), "gp");
        WebSettings webSettings = mWebViewRight.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        //是否允许在webview中执行javascript
        webSettings.setJavaScriptEnabled(true);
    }
    */
    // gpsource.bak end
    /** Called when the activity is first created. */
    
    /** 执行Linux命令，并返回执行结果。 */
    public static String exec(String[] args) {
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }
    
    private AdapterView.OnItemClickListener mListItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // It is possible that the view has been canceled when we get to
            // this point as back has a higher priority
            v.setSelected(true);

            TextView id_view = (TextView)v.findViewById(R.id.news_id);
            String news_id = id_view.getText().toString();
            mNewsView.SetNewsItemByNewsId(news_id);
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // gpsource.bak begin
        // findAndSetWebViews();
        // gpsource.bak end
        
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification(R.drawable.icon, "some messages" ,System.currentTimeMillis());
        mNotification.contentView = new RemoteViews(getPackageName(),R.layout.notification); 
        mNotification.contentView.setProgressBar(R.id.pb, 100,0, false);
        mNotification.contentView.setTextViewText(R.id.text, "web message");
        Intent notificationIntent = new Intent(this, TestUrl.class); 
    	PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,0); 
    	mNotification.contentIntent = contentIntent;   
    	
    	mNewsListView = (ListView)findViewById(R.id.news_list_view);
    	String selection = "";
        String order = XmppMessage.Columns.DATE + " DESC";
        Cursor cursor = managedQuery(XmppzxMessage.CONTENT_NEWS, null, selection, null, order);
    	mNewsAdapter = new NewsAdapterDb(this, R.layout.news_item, cursor);
    	mNewsListView.setOnItemClickListener(mListItemClickListener);
    	mNewsListView.setAdapter(mNewsAdapter);
    	
    	mNewsView = (NewsView)findViewById(R.id.news_view);
    	mNewsView.SetActivitiy(TestUrl.this);
    	
        mTitlebar =(TitleBar)findViewById(R.id.title_bar);
        mTitlebar.SetActivitiy(TestUrl.this);

        ((View)findViewById(R.id.result_text)).setVisibility(View.GONE);        
    }
    
    @Override
    protected void onResume() {
		super.onResume();
		switch (Receiver.call_state) {
		case UserAgent.UA_STATE_INCOMING_CALL:
			mTitlebar.mVoipStatus.setText(Receiver.ccConn.getAddress());
			//mTitlebar.mRtImYy.setVisibility(View.GONE);
			mTitlebar.mRtImYy.setEnabled(false);
			//mTitlebar.mRtImYyEnd.setVisibility(View.GONE);
			//mTitlebar.mRtImYyAc.setVisibility(View.VISIBLE);
			//mTitlebar.mRtImYyRj.setVisibility(View.VISIBLE);
			mTitlebar.im_yy_panel.setVisibility(View.VISIBLE);
			mTitlebar.im_yy_panel_call.setVisibility(View.VISIBLE);
			mTitlebar.im_yy_panel_incall.setVisibility(View.GONE);
			break;
		case UserAgent.UA_STATE_INCALL:
			mTitlebar.mVoipStatus.setText(Receiver.ccConn.getAddress());
			//mTitlebar.mRtImYy.setVisibility(View.GONE);
			mTitlebar.mRtImYy.setEnabled(false);
			//mTitlebar.mRtImYyEnd.setVisibility(View.VISIBLE);
			//mTitlebar.mRtImYyAc.setVisibility(View.GONE);
			//mTitlebar.mRtImYyRj.setVisibility(View.GONE);
			mTitlebar.im_yy_panel.setVisibility(View.VISIBLE);
			mTitlebar.im_yy_panel_incall.setVisibility(View.VISIBLE);
			mTitlebar.im_yy_panel_call.setVisibility(View.GONE);
			break;
		case UserAgent.UA_STATE_IDLE:
			//mTitlebar.mImOutput.append(null);
			//mTitlebar.mRtImYy.setVisibility(View.VISIBLE);
			mTitlebar.mRtImYy.setEnabled(true);
			//mTitlebar.mRtImYyEnd.setVisibility(View.GONE);
			//mTitlebar.mRtImYyAc.setVisibility(View.GONE);
			//mTitlebar.mRtImYyRj.setVisibility(View.GONE);
			mTitlebar.im_yy_panel.setVisibility(View.GONE);
			break;
		}
    }
    
    
   @Override
    protected void onNewIntent(Intent intent) {
        //final String action = intent.getAction();
        final int flags = intent.getFlags();
        Uri url = intent.getData();
        
        if (url == null) {
        	super.onNewIntent(intent);
        	return;
        }
        
        String name = intent.getStringExtra("name");
        Log.v(LOGTAG, "Intent Data : "+url.toString()+" flags : "+flags+" name="+name);
        mWebViewRight.loadUrl(url.toString());
        mWebViewRight.requestFocus();
        ((EditText)TestUrl.this.findViewById(R.id.url_text)).setText(url.toString());
    }
    
}