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
 * �Զ����������Ϣ�㲥������
 * @author xyzj9_000
 *
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
	private NotificationManager nm;
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Log.d("bmob", "�ͻ����յ��������ݣ�"+intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
            String msg = parseJson(intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
            
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification n = new Notification();
			n.icon = R.drawable.ic_launcher;
			n.tickerText = msg;
			//����һ���ӳٵ�Intent������֪ͨ������¼�����ʱҪ���Ĳ���
			PendingIntent contentIntent =  PendingIntent.getActivity(context, 0,intent , PendingIntent.FLAG_UPDATE_CURRENT);
			
			//�����¼�����
			n.setLatestEventInfo(context, "֪ͨ", msg,contentIntent );
			//n.setLatestEventInfo(context, contentTitle, contentText, contentIntent)
			nm.notify(1,n);
		}
	}
	
	/**
	 * ����json
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
