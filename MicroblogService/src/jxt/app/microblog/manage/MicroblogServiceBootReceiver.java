package jxt.app.microblog.manage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 开机启动微博服务
 * @author 祁毅
 * 2011-06-28
 */
public class MicroblogServiceBootReceiver  extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final String action = intent.getAction();
		//开机启动
		if(Intent.ACTION_BOOT_COMPLETED.equals(action)){
			Intent myIntent = new Intent();
			myIntent.setAction("jxt.app.microblog.manage");
			context.startService(myIntent);
		}
		//已经扫描完介质的一个目录
		else if(Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
			
		}
		//扩展介质被插入，而且已经被挂载
		else if(Intent.ACTION_MEDIA_MOUNTED.equals(action))  {
			MicroblogService.blDSCode = true;
		}
		//请媒体扫描仪扫描的文件并添加到媒体数据库。
		else if(action.equals(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE))  {
			
		}
		//用户想要移除扩展介质（拔掉扩展卡）
		else if(action.equals(Intent.ACTION_MEDIA_EJECT))  {
			MicroblogService.blDSCode = false;
		}
	}

}
