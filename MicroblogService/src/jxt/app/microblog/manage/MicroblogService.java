package jxt.app.microblog.manage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jxt.app.microblog.model.Contact;
import jxt.app.microblog.model.News;
import jxt.app.microblog.tool.MicroblogTool;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * 微博服务
 * @author 祁毅
 * <p>2011-06-28</p>
 */
public class MicroblogService extends Service {
	/*变量声明*/
	//获取数据库路径
	private static final String LOAD_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/database";
	//微博数据库对象
	private static SQLiteDatabase mBlogDatabase = null;
	//微博工具类
	MicroblogTool mTool = null;
	//有更新微博人的ID集合
	List<String> updateId = new ArrayList<String>();
	//判断SD卡是否读取成功
	public static boolean blDSCode = false;
	//时钟控件
	private Timer timer;
	//时钟当前秒数
	private int selNum = 0;
	//时钟最大秒数
	private final int MAXNUM = 6 * 60 * 60;
	
	/**
	 * AIDL接口
	 */
	private final IMicroblogManager.Stub tManager = new IMicroblogManager.Stub() {
		
		//添加联系人
		@Override
		public String AddContacts(String uId) throws RemoteException {
			// TODO Auto-generated method stub
			String u_id="";
			try {
				u_id = java.net.URLEncoder.encode(uId,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//根据用户ID下载该用户的用户信息
			Contact contact = mTool.GetContact(mTool.URL_CONTACT + mTool.APPKEY + "&screen_name=" + u_id);
			//Contact contact = mTool.GetContact(mTool.URL_CONTACT + uId + mTool.APPKEY);
			//如果下载的用户信息为空返回False
			if(contact == null){
				return "";
			}
			/*储存数据以便插入数据库*/
	    	ContentValues values = new ContentValues();
	    	
	    	if(contact.getU_id() == null){
	    		values.put("u_id", "");
	    	} else {
	    		values.put("u_id", contact.getU_id());
	    	}
	    	if(contact.getU_name() == null){
	    		values.put("u_name", "");
	    	} else {
	    		values.put("u_name", contact.getU_name());
	    	}
	    	values.put("province", contact.getProvince());
	    	values.put("city", contact.getCity());
	    	if(contact.getLocation() == null){
	    		values.put("location", "");
	    	} else {
	    		values.put("location", contact.getLocation());
	    	}
	    	if(contact.getDescription() == null){
	    		values.put("description", "");
	    	} else {
	    		values.put("description", contact.getDescription());
	    	}
	    	if(contact.getUrl() == null){
	    		values.put("url", "");
	    	} else {
	    		values.put("url", contact.getUrl());
	    	}
	    	if(contact.getProfile_image_url() == null){
	    		values.put("profile_image_url", "");
	    	} else {
	    		values.put("profile_image_url", contact.getProfile_image_url());
	    	}
	    	if(contact.getDomain() == null){
	    		values.put("domain", "");
	    	} else {
	    		values.put("domain", contact.getDomain());
	    	}
	    	if(contact.getGender() == null){
	    		values.put("gender", "");
	    	} else {
	    		values.put("gender", contact.getGender());
	    	}
	    	values.put("followers_count", contact.getFollowers_count());
	    	values.put("friends_count", contact.getFriends_count());
		    values.put("statuses_count", contact.getStatuses_count());
	    	values.put("favourites_count", contact.getFavourites_count());
	    	if(contact.getCreated_at() == null){
	    		values.put("created_at", "");
	    	} else {
	    		values.put("created_at", contact.getCreated_at());
	    	}
	    	if(contact.getVerified() == null){
	    		values.put("verified", "");
	    	} else {
	    		values.put("verified", contact.getVerified());
	    	}
	    	
	    	//插入数据库
			long iNum = mBlogDatabase.insert("contact", null, values);
			if(iNum == -1){
				return "";
			}
			return contact.getU_id();
		}
		
		//获取所有联系人最新微博
		@Override
		public String GetMicroblog() throws RemoteException {
			// TODO Auto-generated method stub
			/*
			 * 返回所有联系人最新微博
			 * 返回结果为XML格式的字符串
			 */
			// the below line only for test.
			return LoadMicroblog("select * from news where is_read=0");
			//return LoadMicroblog("select * from news");
		}
		
		//获取指定联系人最新微博
		@Override
		public String GetMicroblogById(String uId) throws RemoteException {
			// TODO Auto-generated method stub
			/*
			 * 返回指定联系人最新微博
			 * 返回结果为XML格式的字符串
			 */
			return LoadMicroblog("select * from news where is_read=0 and u_id='" + uId + "'");
		}
		
		//手动更新微博
		@Override
		public List<String> UpdateMicroblog() throws RemoteException {
			// TODO Auto-generated method stub
			//执行更新操作
			UpdateMicroblogInfo();
			//返回有更新的联系列表
			return updateId;
		}
		
		//删除联系人
		@Override
		public boolean DelContacts(String uId) throws RemoteException {
			// TODO Auto-generated method stub
			//判断数据库是否可以读取
			if(blDSCode){
				//删除联系人返回受影响的行数
				int numLine = mBlogDatabase.delete("contact", "u_id=?", new String[]{uId});
				//有联系人被删除
				if(numLine > 0){
					//删除该联系人的所有信息
					mBlogDatabase.delete("news", "u_id=?", new String[]{uId});
					//返回删除成功
					return true;
				} else {
					//返回删除失败
					return false;
				}
			}
			//返回删除失败
			return false;
		}
		
	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return tManager;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();	
		
		//实例化微博工具类
		mTool = new MicroblogTool();
		
		//实例化时钟控件
		timer = new Timer();
		
		//循环延迟执行任务
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				//判断数据库类是否为空
				if(mBlogDatabase == null){
					try {
						//创建数据库
						mBlogDatabase = SQLiteDatabase.openOrCreateDatabase(LOAD_FILE + "/Microblog.db", null);//打开模拟器里的数据库
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				//时钟当前时间加1
				selNum++;
				//判断是否到了时钟上线
				if(selNum == MAXNUM){
					//时钟当前时间重设为0
					selNum = 0;
					//执行更新操作
					UpdateMicroblogInfo();
					//发广播
					Intent intent = new Intent("jxt.app.microblog.update");
					//创建更新联系人数组
					String[] updateids = new String[updateId.size()];
					//循环存储有更新的联系人
					for (int i = 0; i < updateId.size(); i++) {
						updateids[i] = updateId.get(i);
					}
					//存储有更新的联系人列表
					intent.putExtra("u_id", updateids);
					//发出广播
	                sendBroadcast(intent);
				}
			}
		}, 0, 1000);
	}
	
	/**
	 * 返回最新的微博信息
	 * @param sql 查询微博的SQL语句
	 * @return 返回XML结构字符串
	 */
	private String LoadMicroblog(String sql){
		String xmlToNews = "";
		if(blDSCode){
			Cursor cur = mBlogDatabase.rawQuery(sql, null);
			//判断表中是否有记录
			if(cur.getCount() > 0){
				xmlToNews += "<?xml version=\"1.0\" encoding=\"UTF-8\"?> ";
				xmlToNews += "<microblog>";
				//将游标移到开头
				cur.moveToFirst();
				//循环获取表中的数据
				do{
					xmlToNews += "<item>";
					xmlToNews += "<id>" + cur.getString(cur.getColumnIndex("id")) + "</id>"; 
					ContentValues values = new ContentValues();
					values.put("is_read", "1");
					mBlogDatabase.update("news", values, "id=?", new String[]{cur.getString(cur.getColumnIndex("id"))});
					xmlToNews += "<u_id>" + cur.getString(cur.getColumnIndex("u_id")) + "</u_id>";
					xmlToNews += "<u_name>" + cur.getString(cur.getColumnIndex("u_name")) + "</u_name>";
					xmlToNews += "<s_id>" + cur.getString(cur.getColumnIndex("s_id")) + "</s_id>";
					xmlToNews += "<text>" + cur.getString(cur.getColumnIndex("text")) + "</text>";
					xmlToNews += "<source>" + cur.getString(cur.getColumnIndex("source")) + "</source>";
					xmlToNews += "<thumbnail_pic>" + cur.getString(cur.getColumnIndex("thumbnail_pic")) + "</thumbnail_pic>";
					xmlToNews += "<bmiddle_pic>" + cur.getString(cur.getColumnIndex("bmiddle_pic")) + "</bmiddle_pic>";
					xmlToNews += "<original_pic>" + cur.getString(cur.getColumnIndex("original_pic")) + "</original_pic>";
					xmlToNews += "<retweeted_status>" + cur.getString(cur.getColumnIndex("retweeted_status")) + "</retweeted_status>";
					xmlToNews += "<created_at>" + cur.getString(cur.getColumnIndex("created_at")) + "</created_at>";
					xmlToNews += "</item>";
				}while(cur.moveToNext());   //将游标下移一位
				xmlToNews += "</microblog>";
			}
			cur.close();
		} else {
			Log.v("DataBase", "SDCode not loaded!");
		}
    	
    	return xmlToNews;
	}
	
	/**
	 * 更新新浪微博
	 */
	private void UpdateMicroblogInfo(){
		updateId.clear();
		List<String> u_ids = new ArrayList<String>();
		
		List<String> s_ids = new ArrayList<String>();
		
		if(blDSCode){
			Cursor cur1 = mBlogDatabase.rawQuery("select u_id from contact", null);
			//判断表中是否有记录
			if(cur1.getCount() > 0){
				//将游标移到开头
				cur1.moveToFirst();
				//循环获取表中的数据
				do{
					u_ids.add(cur1.getString(cur1.getColumnIndex("u_id")));
				}while(cur1.moveToNext());   //将游标下移一位
			}
    	
			cur1.close();
    	
			Cursor cur2 = mBlogDatabase.rawQuery("select s_id from news", null);
			//判断表中是否有记录
			if(cur2.getCount() > 0){
				//将游标移到开头
				cur2.moveToFirst();
				//循环获取表中的数据
				do{
					s_ids.add(cur2.getString(cur2.getColumnIndex("s_id")));
				}while(cur2.moveToNext());   //将游标下移一位
			}
    	
			cur2.close();
    	
			int u_id_num = u_ids.size();
			boolean blTrue = true;
			for (int i = 0; i < u_id_num; i++) {
				List<News> news = mTool.GetNews(mTool.URL_NEWS + mTool.APPKEY + "&user_id=" + u_ids.get(i));
				int newsNum = news.size();
				for (int j = 0; j < newsNum; j++) {
					int sidNum = s_ids.size();
					for (int j2 = 0; j2 < sidNum; j2++) {
						if(news.get(j).getS_id().equals(s_ids.get(j2))) {
							blTrue = false;
							break;
						}
					}
					if(blTrue){
						boolean blUpdate = true;
						for (int k = 0; k < u_ids.size(); k++) {
							if(updateId.size() == 0){
								break;
							} else if(updateId.get(k).equals(u_ids.get(i))){
								blUpdate = false;
								break;
							}
    					
						}
						if(blUpdate){
							updateId.add(u_ids.get(i));
						} else {
							blUpdate = true;
						}
						ContentValues values = new ContentValues();
						values.put("u_id", news.get(j).getU_id());
						values.put("u_name", news.get(j).getU_name());
						values.put("s_id", news.get(j).getS_id());
						values.put("text", news.get(j).getText());
						values.put("source", news.get(j).getSource());
						values.put("thumbnail_pic", news.get(j).getThumbnail_pic());
						values.put("bmiddle_pic", news.get(j).getBmiddle_pic());
						values.put("original_pic", news.get(j).getOriginal_pic());
						values.put("retweeted_status", news.get(j).getRetweeted_status());
						values.put("created_at", news.get(j).getCreated_at());
						values.put("is_read", 0);
						mBlogDatabase.insert("news", null, values);
					} else {
						blTrue = true;
					}
				}
			}
		} else {
			Log.v("DataBase", "SDCode not loaded!");
		}
	}
}
