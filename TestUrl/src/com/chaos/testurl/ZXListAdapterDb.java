package com.chaos.testurl;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class ZXListAdapterDb extends ResourceCursorAdapter {
	private TestUrl mActivity;
	public static final Uri CONTENT_NEWS =
        Uri.parse("content://jxt.app.stockzx.xmpp/new");
	public static final Uri CONTENT_MESSAGE =
        Uri.parse("content://jxt.app.stockzx.xmpp/message");

	
	public ZXListAdapterDb(Context context, int layout, Cursor c) {
		super(context, layout, c);
		mActivity = (TestUrl) context;
		Log.v(TestUrl.LOGTAG,"ZXListAdapterDb");
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.v(TestUrl.LOGTAG,"bindView");
		String title = cursor.getString(XmppzxMessage.CateGoryColumns.TITLE_INDEX);
		int count = cursor.getInt(XmppzxMessage.CateGoryColumns.COUNT_INDEX);
		int un_read = cursor.getInt(XmppzxMessage.CateGoryColumns.UN_READ_INDEX);
		String type_code = cursor.getString(XmppzxMessage.CateGoryColumns.TYPE_INDEX);
		
		((TextView)view.findViewById(R.id.zx_title)).setText(title);
		((TextView)view.findViewById(R.id.zx_info)).setText(String.valueOf(count)+mActivity.getResources().getString(R.string.zx_info_posfix));
		((TextView)view.findViewById(R.id.zx_tips)).setText(String.valueOf(un_read));
		((TextView)view.findViewById(R.id.zx_type_code)).setText(type_code);
	}
	
}
