package com.chaos.testurl;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class NewsAdapterDb extends ResourceCursorAdapter {

	private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	
	public NewsAdapterDb(Context context, int layout, Cursor c) {
		super(context, layout, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		String title = cursor.getString(XmppzxMessage.NewsColumns.TITLE_INDEX);
		String description = cursor.getString(XmppzxMessage.NewsColumns.DESCRIPTION_INDEX);
		String source = cursor.getString(XmppzxMessage.NewsColumns.SOURCE_INDEX);
		String date = cursor.getString(XmppzxMessage.NewsColumns.UPDATED_AT_INDEX);
		String news_id = cursor.getString(XmppzxMessage.NewsColumns.NEWS_ID_INDEX);
		
		TextView title_view = (TextView) view.findViewById(R.id.news_title);
		TextView description_view = (TextView) view.findViewById(R.id.news_description);	
		TextView source_view = (TextView) view.findViewById(R.id.news_source);
		TextView date_view = (TextView) view.findViewById(R.id.news_date);
		TextView id_view = (TextView) view.findViewById(R.id.news_id);
		
		title_view.setText(title);
		description_view.setText(description);
		source_view.setText(source);
		date_view.setText(date);
		id_view.setText(news_id);
	}
	
	private String formatDate(long date) {
		return mFormat.format(new Date(date));
	}
}
