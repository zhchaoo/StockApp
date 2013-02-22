package jxt.app.stockzx.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import jxt.app.stockzx.R;
import jxt.app.stockzx.xmpp.XmppzxMessage;
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
		int type = cursor.getInt(XmppzxMessage.Columns.TYPE_INDEX);
		TextView speaker = (TextView) view.findViewById(R.id.speaker);
		if(type == XmppzxMessage.TYPE_RECEIVE) {
			speaker.setText(cursor.getString(XmppzxMessage.Columns.TYPE_INDEX)+":");
		}
		else {
			speaker.setText("wo:");
		}
		
		TextView message = (TextView) view.findViewById(R.id.message);
		message.setText(cursor.getString(XmppzxMessage.Columns.CONTENT_INDEX));
		
		TextView date = (TextView) view.findViewById(R.id.date);
		long time = cursor.getLong(XmppzxMessage.Columns.CREATED_AT_INDEX);
		date.setText(formatDate(time));
	}
	
	private String formatDate(long date) {
		return mFormat.format(new Date(date));
	}
}
