package com.chaos.testurl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ZXListAdapterEditDb extends ResourceCursorAdapter {
	private TestUrl mActivity;
	
	public static final Uri CONTENT_NEWS =
        Uri.parse("content://jxt.app.stockzx.xmpp/new");
	public static final Uri CONTENT_MESSAGE =
        Uri.parse("content://jxt.app.stockzx.xmpp/message");

	
	public ZXListAdapterEditDb(Context context, int layout, Cursor c) {
		super(context, layout, c);
		mActivity = (TestUrl) context;
		Log.v(TestUrl.LOGTAG,"ZXListAdapterDb");
	}

	public void bindView(View view, Context context, Cursor cursor) {
		Log.v(TestUrl.LOGTAG,"bindView");
		String title = cursor.getString(XmppzxMessage.CateGoryColumns.TITLE_INDEX);
		String type_code = cursor.getString(XmppzxMessage.CateGoryColumns.TYPE_INDEX);
		int count = cursor.getInt(XmppzxMessage.CateGoryColumns.COUNT_INDEX);
		int un_read = cursor.getInt(XmppzxMessage.CateGoryColumns.UN_READ_INDEX);
		long is_concern = cursor.getLong(XmppzxMessage.CateGoryColumns.IS_CONCERN_INDEX);
		
		((TextView)view.findViewById(R.id.zx_title)).setText(title);
		((TextView)view.findViewById(R.id.zx_info)).setText(String.valueOf(count)+mActivity.getResources().getString(R.string.zx_info_posfix));
		((TextView)view.findViewById(R.id.zx_type_code)).setText(type_code);
		
		ToggleButton concernbtn = (ToggleButton)view.findViewById(R.id.zx_concern_toggle_btn);
		concernbtn.setChecked(is_concern!=0?true:false);
		concernbtn.setOnClickListener(new ZXConcernChangelistener(type_code));

	}
	
    private class ZXConcernChangelistener implements View.OnClickListener {
    	private String mTypeCode;
    	
    	public ZXConcernChangelistener(String type_code) {
    		mTypeCode=type_code;
    	}
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			boolean isChecked = ((ToggleButton)v).isChecked();
			ContentValues values = new ContentValues();
			String where = XmppzxMessage.CateGoryColumns.TYPE + "=?";
			String[] selectionArgs = { mTypeCode };
			if (isChecked)
				values.put(XmppzxMessage.CateGoryColumns.IS_CONCERN, true);
			else
				values.put(XmppzxMessage.CateGoryColumns.IS_CONCERN, false);
			int ret = 0;
			try {
			ret = mActivity.getContentResolver().update(XmppzxMessage.CONTENT_CATEGORY, values, where, selectionArgs);
			} catch (Exception e) {
				Log.v("WeiBo", e.getMessage());
			}
			Log.v("WeiBo","concern change " + mTypeCode+ " " +isChecked+ " " + ret);
			
			// TODO post concern data to Service.
			// it should not be here.
			HttpClient httpclient = new DefaultHttpClient();
			//ÄãµÄURL
			HttpPost httppost = new HttpPost(mActivity.CATEGORY_SYNC_URL);
			try {
			   List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			//Your DATA
			   nameValuePairs.add(new BasicNameValuePair("user_id", "12345"));
			   nameValuePairs.add(new BasicNameValuePair("news_list", "finance"));
			 
			   httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			 
			   HttpResponse response;
			   response=httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			} catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			}
		}	
    	
    }
}
