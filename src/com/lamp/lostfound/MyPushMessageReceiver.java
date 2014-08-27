package com.lamp.lostfound;

import java.io.IOException;
import java.io.StringReader;

import com.google.gson.Gson;

import cn.bmob.push.PushConstants;
import cn.bmob.v3.listener.GetServerTimeListener;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.JsonReader;
import android.util.Log;
/**
 * 自定义的推送消息广播接收器
 * @author xyzj9_000
 *
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
	private NotificationManager nm;
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
            String msg = parseJson(intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
            
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification n = new Notification();
			n.icon = R.drawable.ic_launcher;
			n.tickerText = msg;
			//设置一个延迟的Intent用来在通知栏点击事件发生时要做的操作
			PendingIntent contentIntent =  PendingIntent.getActivity(context, 0,intent , PendingIntent.FLAG_UPDATE_CURRENT);
			
			//设置事件处理
			n.setLatestEventInfo(context, "通知", msg,contentIntent );
			//n.setLatestEventInfo(context, contentTitle, contentText, contentIntent)
			nm.notify(1,n);
		}
	}
	
	/**
	 * 解析json
	 * @param msg
	 * @return
	 */
	private String parseJson(String msg){
		if(!"".equals(msg)){
			JsonReader jr = new JsonReader(new StringReader(msg));
			try {
				jr.beginObject();
				while(jr.hasNext()){
					String names = jr.nextName();
					if(names.equals("alert")){
						return jr.nextString();
					}
				}
				jr.endObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return null;
	}
	
}
