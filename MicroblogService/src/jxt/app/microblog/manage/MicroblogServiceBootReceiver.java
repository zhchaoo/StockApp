package jxt.app.microblog.manage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * ��������΢������
 * @author ����
 * 2011-06-28
 */
public class MicroblogServiceBootReceiver  extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final String action = intent.getAction();
		//��������
		if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
			Intent myIntent = new Intent();
			myIntent.setAction("jxt.app.microblog.manage");
			context.startService(myIntent);
		}
		//�Ѿ�ɨ������ʵ�һ��Ŀ¼
		else if(Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
			
		}
		//��չ���ʱ����룬�����Ѿ�������
		else if(Intent.ACTION_MEDIA_MOUNTED.equals(action))  {
			MicroblogService.blDSCode = true;
		}
		//��ý��ɨ����ɨ����ļ�����ӵ�ý�����ݿ⡣
		else if(action.equals(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE))  {
			
		}
		//�û���Ҫ�Ƴ���չ���ʣ��ε���չ����
		else if(action.equals(Intent.ACTION_MEDIA_EJECT))  {
			MicroblogService.blDSCode = false;
		}
	}

}
