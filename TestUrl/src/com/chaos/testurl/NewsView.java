package com.chaos.testurl;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsView extends LinearLayout {
    private TestUrl 		mBrowserActivity;
    private int             mLeftMargin;
    private int             mRightMargin;
    private TextView mTitleView;
    private TextView mSourceView;
    private TextView mContentView;
    private TextView mDescriptionView;
    private TextView mDateView;
    private TextView mIdView;
    
    private ImageView mRtnFont;
    private ImageView mRtnAdd;
    private ImageView mRtnClear;
    
    private View.OnClickListener mNewsViewOnClickListener = (View.OnClickListener) new NewsViewClicklistener();
    
    private FeedParser xmlparser = new AndroidSaxFeedParser();
    private List<NewsViewMessage> newsmessages;
    
    private class NewsViewClicklistener implements View.OnClickListener {
        public void onClick(View v) {  
            switch (v.getId()) {
                case R.id.news_rtn_font:
                    break;
                case R.id.news_rtn_add:
                    break;
            } 
        }  
    };
   
    public NewsView(Context context, AttributeSet attr) {  
        super(context, attr);  
        //至于这个构造函数里边要写一些什么大家就随便啦。
        createView(context);
    }  
    
    private void createView(Context context) {
    	LayoutInflater factory = LayoutInflater.from(context);
        factory.inflate(R.layout.news_view, this);
		
        Resources resources = context.getResources();
	   
        mTitleView = (TextView)findViewById(R.id.news_title);
		mDescriptionView = (TextView)findViewById(R.id.news_description);
		mContentView = (TextView)findViewById(R.id.news_content);
		mSourceView = (TextView)findViewById(R.id.news_source);
		mDateView = (TextView)findViewById(R.id.news_date);
		mIdView = (TextView)findViewById(R.id.news_id);
		
		mRtnFont = (ImageView)findViewById(R.id.news_rtn_font);
		mRtnAdd = (ImageView)findViewById(R.id.news_rtn_add);
		mRtnClear = (ImageView)findViewById(R.id.news_rtn_clear);
		
		mRtnFont.setOnClickListener(mNewsViewOnClickListener);
		mRtnAdd.setOnClickListener(mNewsViewOnClickListener);
		
        DisplayMetrics metrics = resources.getDisplayMetrics();
        mLeftMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8f, metrics);
        mRightMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 6f, metrics);
    }
    
    public void SetActivitiy(TestUrl context) {
    	mBrowserActivity = context;        
    }

    public void SetNewsItemByNewsId(String news_id) {
    	// Initial.
    	mIdView.setText(news_id);
    	mTitleView.setText("");
    	mDescriptionView.setText("");
    	mContentView.setText(R.string.wait_news);
    	mSourceView.setText("");
    	mDateView.setText("");
    	
    	newsmessages = xmlparser.parseNews(mBrowserActivity.NEWS_URL+news_id);
    	
    	Log.v("WeiBo", "News : "+news_id+" "+newsmessages.size());
    	// return immediately if if empty.
    	if(newsmessages.isEmpty()) return;
    	
    	NewsViewMessage msg = newsmessages.get(0);
    	// fill the data.
    	mTitleView.setText(msg.title);
    	mDescriptionView.setText(msg.description);
    	mContentView.setText(msg.content);
    	mSourceView.setText(msg.source);
    	mDateView.setText(msg.updated_at);
    }    
}
