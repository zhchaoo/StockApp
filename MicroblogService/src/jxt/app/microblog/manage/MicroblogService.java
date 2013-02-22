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
 * ΢������
 * @author ����
 * <p>2011-06-28</p>
 */
public class MicroblogService extends Service {
	/*��������*/
	//��ȡ���ݿ�·��
	private static final String LOAD_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/database";
	//΢�����ݿ����
	private static SQLiteDatabase mBlogDatabase = null;
	//΢��������
	MicroblogTool mTool = null;
	//�и���΢���˵�ID����
	List<String> updateId = new ArrayList<String>();
	//�ж�SD���Ƿ��ȡ�ɹ�
	public static boolean blDSCode = false;
	//ʱ�ӿؼ�
	private Timer timer;
	//ʱ�ӵ�ǰ����
	private int selNum = 0;
	//ʱ���������
	private final int MAXNUM = 6 * 60 * 60;
	
	/**
	 * AIDL�ӿ�
	 */
	private final IMicroblogManager.Stub tManager = new IMicroblogManager.Stub() {
		
		//�����ϵ��
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
			//�����û�ID���ظ��û����û���Ϣ
			Contact contact = mTool.GetContact(mTool.URL_CONTACT + mTool.APPKEY + "&screen_name=" + u_id);
			//Contact contact = mTool.GetContact(mTool.URL_CONTACT + uId + mTool.APPKEY);
			//������ص��û���ϢΪ�շ���False
			if(contact == null){
				return "";
			}
			/*���������Ա�������ݿ�*/
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
	    	
	    	//�������ݿ�
			long iNum = mBlogDatabase.insert("contact", null, values);
			if(iNum == -1){
				return "";
			}
			return contact.getU_id();
		}
		
		//��ȡ������ϵ������΢��
		@Override
		public String GetMicroblog() throws RemoteException {
			// TODO Auto-generated method stub
			/*
			 * ����������ϵ������΢��
			 * ���ؽ��ΪXML��ʽ���ַ���
			 */
			// the below line only for test.
			return LoadMicroblog("select * from news where is_read=0");
			//return LoadMicroblog("select * from news");
		}
		
		//��ȡָ����ϵ������΢��
		@Override
		public String GetMicroblogById(String uId) throws RemoteException {
			// TODO Auto-generated method stub
			/*
			 * ����ָ����ϵ������΢��
			 * ���ؽ��ΪXML��ʽ���ַ���
			 */
			return LoadMicroblog("select * from news where is_read=0 and u_id='" + uId + "'");
		}
		
		//�ֶ�����΢��
		@Override
		public List<String> UpdateMicroblog() throws RemoteException {
			// TODO Auto-generated method stub
			//ִ�и��²���
			UpdateMicroblogInfo();
			//�����и��µ���ϵ�б�
			return updateId;
		}
		
		//ɾ����ϵ��
		@Override
		public boolean DelContacts(String uId) throws RemoteException {
			// TODO Auto-generated method stub
			//�ж����ݿ��Ƿ���Զ�ȡ
			if(blDSCode){
				//ɾ����ϵ�˷�����Ӱ�������
				int numLine = mBlogDatabase.delete("contact", "u_id=?", new String[]{uId});
				//����ϵ�˱�ɾ��
				if(numLine > 0){
					//ɾ������ϵ�˵�������Ϣ
					mBlogDatabase.delete("news", "u_id=?", new String[]{uId});
					//����ɾ���ɹ�
					return true;
				} else {
					//����ɾ��ʧ��
					return false;
				}
			}
			//����ɾ��ʧ��
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
		
		//ʵ����΢��������
		mTool = new MicroblogTool();
		
		//ʵ����ʱ�ӿؼ�
		timer = new Timer();
		
		//ѭ���ӳ�ִ������
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				//�ж����ݿ����Ƿ�Ϊ��
				if(mBlogDatabase == null){
					try {
						//�������ݿ�
						mBlogDatabase = SQLiteDatabase.openOrCreateDatabase(LOAD_FILE + "/Microblog.db", null);//��ģ����������ݿ�
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				//ʱ�ӵ�ǰʱ���1
				selNum++;
				//�ж��Ƿ���ʱ������
				if(selNum == MAXNUM){
					//ʱ�ӵ�ǰʱ������Ϊ0
					selNum = 0;
					//ִ�и��²���
					UpdateMicroblogInfo();
					//���㲥
					Intent intent = new Intent("jxt.app.microblog.update");
					//����������ϵ������
					String[] updateids = new String[updateId.size()];
					//ѭ���洢�и��µ���ϵ��
					for (int i = 0; i < updateId.size(); i++) {
						updateids[i] = updateId.get(i);
					}
					//�洢�и��µ���ϵ���б�
					intent.putExtra("u_id", updateids);
					//�����㲥
	                sendBroadcast(intent);
				}
			}
		}, 0, 1000);
	}
	
	/**
	 * �������µ�΢����Ϣ
	 * @param sql ��ѯ΢����SQL���
	 * @return ����XML�ṹ�ַ���
	 */
	private String LoadMicroblog(String sql){
		String xmlToNews = "";
		if(blDSCode){
			Cursor cur = mBlogDatabase.rawQuery(sql, null);
			//�жϱ����Ƿ��м�¼
			if(cur.getCount() > 0){
				xmlToNews += "<?xml version=\"1.0\" encoding=\"UTF-8\"?> ";
				xmlToNews += "<microblog>";
				//���α��Ƶ���ͷ
				cur.moveToFirst();
				//ѭ����ȡ���е�����
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
				}while(cur.moveToNext());   //���α�����һλ
				xmlToNews += "</microblog>";
			}
			cur.close();
		} else {
			Log.v("DataBase", "SDCode not loaded!");
		}
    	
    	return xmlToNews;
	}
	
	/**
	 * ��������΢��
	 */
	private void UpdateMicroblogInfo(){
		updateId.clear();
		List<String> u_ids = new ArrayList<String>();
		
		List<String> s_ids = new ArrayList<String>();
		
		if(blDSCode){
			Cursor cur1 = mBlogDatabase.rawQuery("select u_id from contact", null);
			//�жϱ����Ƿ��м�¼
			if(cur1.getCount() > 0){
				//���α��Ƶ���ͷ
				cur1.moveToFirst();
				//ѭ����ȡ���е�����
				do{
					u_ids.add(cur1.getString(cur1.getColumnIndex("u_id")));
				}while(cur1.moveToNext());   //���α�����һλ
			}
    	
			cur1.close();
    	
			Cursor cur2 = mBlogDatabase.rawQuery("select s_id from news", null);
			//�жϱ����Ƿ��м�¼
			if(cur2.getCount() > 0){
				//���α��Ƶ���ͷ
				cur2.moveToFirst();
				//ѭ����ȡ���е�����
				do{
					s_ids.add(cur2.getString(cur2.getColumnIndex("s_id")));
				}while(cur2.moveToNext());   //���α�����һλ
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
