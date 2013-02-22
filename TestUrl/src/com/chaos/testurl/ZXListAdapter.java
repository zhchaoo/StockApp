package com.chaos.testurl;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ZXListAdapter extends BaseAdapter {
	private TestUrl mActivity;
	public static final Uri CONTENT_NEWS =
        Uri.parse("content://jxt.app.stockzx.xmpp/news");
	public static final Uri CONTENT_MESSAGE =
        Uri.parse("content://jxt.app.stockzx.xmpp/message");
	
	public ZXListAdapter(TestUrl activity) {
		// TODO Auto-generated constructor stub
		mActivity = activity;
		registerObserver();
	}

    private void registerObserver() {  
    	mActivity.getContentResolver().registerContentObserver(  
    			CONTENT_MESSAGE, true, mObserver);  
    	/*mActivity.getContentResolver().registerContentObserver(  
                CallLog.Calls.CONTENT_URI, true, mObserver);*/
    }  
  
    private void unregisterObserver() {  
    	mActivity.getContentResolver().unregisterContentObserver(mObserver);  
    }  
  
    private ContentObserver mObserver = new ContentObserver(new Handler()) {  
  
        @Override  
        public void onChange(boolean selfChange) { 
        	Log.v("xmppzx","database changed");
        	notifyDataSetChanged();  
        }  
    };  
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 if (convertView == null) {
             LayoutInflater factory = LayoutInflater.from(mActivity);
             convertView = factory.inflate(R.layout.zx_list_item, null);
             

         }
		 
		 if (position == 0) {
			 String selection = "";//XmppzxMessage.Columns.SUB_TYPE + " = \"finance\"";
		     String order = "";
			 Cursor cursor = mActivity.managedQuery(CONTENT_NEWS, null, selection, null, order);
			 ((TextView)convertView.findViewById(R.id.zx_info)).setText(String.valueOf(cursor.getCount()));
			 selection = XmppzxMessage.Columns.IS_READ + " = 0";
			 cursor = mActivity.managedQuery(CONTENT_NEWS, null, selection, null, order);
			 ((TextView)convertView.findViewById(R.id.zx_tips)).setText(String.valueOf(cursor.getCount()));
			 Log.v("xmppzx","getView : "+cursor.getCount());
		 }
		return convertView;
	}

}
