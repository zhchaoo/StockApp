package com.chaos.testurl;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class MessageAdapter extends ResourceCursorAdapter {

	private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	
	public MessageAdapter(Context context, int layout, Cursor c) {
		super(context, layout, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		int type = cursor.getInt(XmppMessage.Columns.TYPE_INDEX);
		TextView speaker = (TextView) view.findViewById(R.id.im_speaker);
		if(type == XmppMessage.TYPE_RECEIVE) {
			speaker.setText(cursor.getString(XmppMessage.Columns.BROKER_INDEX)+":");
		}
		else {
			speaker.setText("wo:");
		}
		
		TextView message = (TextView) view.findViewById(R.id.im_message);
		message.setText(cursor.getString(XmppMessage.Columns.BODY_INDEX));
		
		TextView date = (TextView) view.findViewById(R.id.im_date);
		long time = cursor.getLong(XmppMessage.Columns.DATE_INDEX);
		date.setText(formatDate(time));
	}
	
	private String formatDate(long date) {
		return mFormat.format(new Date(date));
	}
}
