package com.dating.reveal.chat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.dating.reveal.R;
import com.dating.reveal.main.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import com.second.sosoapp.pages.LoginActivity;

public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
//	      if( Foreground.get().isForeground() )
//	        	return;
	 
	    String newMessage = intent.getExtras().getString(Const.EXTRA_MESSAGE);
	    JSONObject data;
	    
		try {
			data = new JSONObject(newMessage);
			addNotification(arg0, data);
		} catch (JSONException e) {
			e.printStackTrace();
		}          
	}
	
	static class CancelButtonListener extends BroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	context.unregisterReceiver(this);
	    	NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
	    	notificationmanager.cancelAll();
	    }
	}
	
	public static void addNotification(Context context, JSONObject data)
	{
    	if( context == null || data == null )
    		return;
    	
    	String message = data.optString(Const.BODY, "");
        String nickname = data.optString(Const.NICKNAME, "");
         
		// Using RemoteViews to bind custom layouts into Notification
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.fragment_notify);
		
		// Open NotificationView Class on Notification Click
		Intent intent = getStartIntentFromPush(context, data );
	    // Open NotificationView.java Activity
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				// Set Icon
				.setSmallIcon(R.drawable.ic_launcher)
				// Set Ticker Message
				.setTicker(message)
				// Dismiss Notification
				.setAutoCancel(true)
				// Set PendingIntent into Notification
				.setContentIntent(pIntent)
				.setSmallIcon(R.drawable.app_small_icon)
				// Set RemoteViews into Notification
				.setContent(remoteViews);
		

		// Locate and set the Image into customnotificationtext.xml ImageViews
//		remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.ic_launcher);
//		remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.androidhappy);
		
		// Locate and set the Text into customnotificationtext.xml TextViews
		remoteViews.setTextViewText(R.id.txt_name,nickname);		
		remoteViews.setTextViewText(R.id.txt_message, message );
		
		SimpleDateFormat format = new SimpleDateFormat("HH:MM");
		remoteViews.setTextViewText(R.id.txt_time, format.format(new Date()));
		
		CancelButtonListener cancelButtonListener = new CancelButtonListener();
		context.registerReceiver(cancelButtonListener, new IntentFilter("Cancel"));
		Intent cancelIntent = new Intent("Cancel");
	    PendingIntent pendingCancelIntent = PendingIntent.getBroadcast(context, 0, cancelIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.btn_cancel, pendingCancelIntent);
		
		// Create Notification Manager
		NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
		notificationmanager.cancelAll();
		
		// Build Notification with Notification Manager

//		if( DataUtils.getPreference(Const.child_group_name[1][1], true) == true )
		{
			Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			builder.setSound(alarmSound);
		}
		 
		Notification notification = builder.build();
		notification.when = System.currentTimeMillis();
		
//		if( DataUtils.getPreference(Const.child_group_name[1][2], true) == true )
		{
			notification.vibrate = new long[] { 500, 100, 500, 100 };
//			notification.sound = Uri.parse("/system/com.dating.reveal.media/audio/notifications/20_Cloud.ogg");
		}
		
//		long id = DBManager.addNotification(context, message);
		
		try {
			notificationmanager.notify(0, notification);
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
    
	public static Intent getStartIntentFromPush(Context context, JSONObject data )
	{
		if( context == null || data == null )
			return null;
				
		Intent intent = null;
		
//		intent = new Intent(context, LoginActivity.class);
//
//		Bundle bundle = new Bundle();
//
//		bundle.putString(BaseView.INTENT_EXTRA, data.toString());
//		intent.putExtras(bundle);
//
//		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP );
//
//
		return intent;
	}

	public static void showGCMNotification(Context context, Intent intent)
	{
		if( intent == null )
			return;
		
//		if( DataUtils.getPreference(Const.child_group_name[1][0], true) == false )
//			return;
		
		JSONObject data = new JSONObject();
		String title, msg, ticker;
		try {
			
			title = intent.getStringExtra("title");
			title = "Contact2W";
			msg = intent.getStringExtra("msg");
			ticker = intent.getStringExtra("ticker");
			
	        if(msg.contains("+")){
	        	msg = msg.replace("+", " ");
	        	try {
	        		msg = java.net.URLDecoder.decode(msg, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
	        }
			
			data.put(Const.NICKNAME, title);
			data.put(Const.BODY, msg);
			data.put(Const.GROUP_TYPE, 0);
		} catch (Exception e ) {
			e.printStackTrace();
		}
		
		addNotification(context, data);
	}
	
//	private static PowerManager.WakeLock wakeLock;
//	   
//    public static void acquireWakeLock(Context context) {
//       if (wakeLock != null) wakeLock.release();
//
//       PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//       
//       wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
//               PowerManager.ACQUIRE_CAUSES_WAKEUP |
//               PowerManager.ON_AFTER_RELEASE, "WakeLock");
//       
//       wakeLock.acquire();
//    }
//
//    public static void releaseWakeLock() {
//       if (wakeLock != null) wakeLock.release(); wakeLock = null;
//    }
	   
}
