package com.dating.reveal.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.dating.reveal.designs.ScreenAdapter;
import com.dating.reveal.network.ServerTask;
import com.dating.reveal.utility.DataUtils;
import com.dating.reveal.utility.MessageUtils;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MyApplication extends MultiDexApplication{

    @Override
    protected  void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
	private Socket mSocket;
	{
		try {
			mSocket = IO.socket(ServerTask.CHAT_SERVER_URL);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public Socket getSocket() {
		return mSocket;
	}

	private void initSocket(){
		mSocket = getSocket();
		mSocket.on("notification", onNotification);
//		mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
//		mSocket.on(SocChatConst.USER_LIST, onUserList);
//		mSocket.on(SocChatConst.USER_JOIN, onUserJoin);
//		mSocket.on(SocChatConst.USER_LEFT, onUserLeft);
//		mSocket.on(SocChatConst.CACHED_MSG,onCachedMSG);
//		mSocket.on(SocChatConst.WHISPER, onWhisper);
//		mSocket.on(SocChatConst.WHISPER_REQ, onWhisperREQ);
//		mSocket.on(SocChatConst.WHISPER_ACCEPT, onWhisperAccept);
//		mSocket.on(SocChatConst.WHISPER_CANCEL,onWhisperCancel);
//		mSocket.on(SocChatConst.CHAT, onChat);
		mSocket.connect();
	}

	private Emitter.Listener onNotification = new Emitter.Listener(){
		@Override
		public void call(Object... args){
			if(args[0] instanceof JSONObject){
				final JSONObject notify = (JSONObject)args[0];
				String strOldNoti = DataUtils.getPreference(Const.NOTIFICATION_CONTENT,"");
				String intOldNoti = DataUtils.getPreference(Const.NOTIFICATION_SIZE,"0");
				String retcode = notify.optString("retcode","");
				// this is single notification.
				try {
					if (retcode.equals("234")) {
						JSONObject jsonObject = notify.optJSONObject("content");
						JSONArray jsonArrayOld;
						if(strOldNoti.equals("")){
							jsonArrayOld = new JSONArray();
						}else{
							jsonArrayOld = new JSONArray(strOldNoti);
						}
						jsonArrayOld.put(jsonObject);
						intOldNoti = String.valueOf(Integer.parseInt(intOldNoti) + 1);
						String newNotificaiton = jsonArrayOld.toString();
						String newNotisize = intOldNoti;
						DataUtils.savePreference(Const.NOTIFICATION_CONTENT, newNotificaiton);
						DataUtils.savePreference(Const.NOTIFICATION_SIZE, newNotisize);
						return;
					}
				}catch (JSONException e){
					return;
				}
				JSONArray retcontent  = notify.optJSONArray("content");
				int notesize = 0;
				if(retcontent != null){
					notesize = 0;//retcontent.length();
					try {
						JSONArray jsonoldnoti;
						if(strOldNoti.equals("")){
							jsonoldnoti = new JSONArray();
						}else{
							jsonoldnoti = new JSONArray(strOldNoti);
						}

						for(int x=0; x < retcontent.length(); x++){
							boolean isNew = true;
							String newsender = retcontent.optJSONObject(x).optString("sender","");
							String newnotekind = retcontent.optJSONObject(x).optString("notekind","");
							String newfeedval  = retcontent.optJSONObject(x).optString("feedval","");
							for(int y = 0; y < jsonoldnoti.length(); y++){
								String oldsender = jsonoldnoti.optJSONObject(y).optString("sender","");
								String oldnotekind = jsonoldnoti.optJSONObject(y).optString("notekind","");
								String oldfeedval = jsonoldnoti.optJSONObject(y).optString("feedval","");
								if(newfeedval.equals(oldfeedval) && newsender.equals(oldsender) && newnotekind.equals(oldnotekind)){
									isNew = false;
									break;
								}
							}
							if(isNew){
								jsonoldnoti.put(retcontent.optJSONObject(x));
								notesize++;
							}
						}
						String noteString = jsonoldnoti.toString();
						int sizeold = Integer.parseInt(intOldNoti);
						sizeold = sizeold  + notesize;
//
	 					String noteSize = String.valueOf(sizeold);
//						DataUtils.savePreference(Const.NOTIFICATION_SIZE, noteSize);
						DataUtils.savePreference(Const.NOTIFICATION_CONTENT, noteString);
					}catch (JSONException e){

					}
				}
			}
		}
	};

	@Override
	    public void onCreate() {
	        super.onCreate();
			initSocket();
	        initScreenAdapter();
	        setContextToComponents();
	        initImageLoader(this);

	   }

	   private void initScreenAdapter()
	   {
		   ScreenAdapter.setDefaultSize(1080, 1920);
	       ScreenAdapter.setApplicationContext(this.getApplicationContext());
	   }

	   private void setContextToComponents()
	   {
		   DataUtils.setContext(this);
//		   NetworkUtils.setContext(this);
		   MessageUtils.setApplicationContext(this);
	   }

	   private void initImageLoader(Context context) {
		   DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
				   .cacheInMemory(true)
				   .cacheOnDisc(true)
				   .imageScaleType(ImageScaleType.EXACTLY)
				   .bitmapConfig(Bitmap.Config.RGB_565)
				   .considerExifParams(true).build();

		   ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				   .threadPriority(Thread.NORM_PRIORITY)
				   .denyCacheImageMultipleSizesInMemory()
				   .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				   .defaultDisplayImageOptions(defaultDisplayImageOptions)
				   .threadPoolSize(3)
				   .discCacheFileCount(100) // default
				   .memoryCacheSizePercentage(80)
				   .tasksProcessingOrder(QueueProcessingType.LIFO)
				   .build();

		   ImageLoader imageLoader = ImageLoader.getInstance();
		   imageLoader.init(config);
//		    imageLoader.clearDiskCache();
//		   ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
	   }

	   public static MyApplication getApplication(Context context) {
	       return (MyApplication) context.getApplicationContext();
	   }

		public static void initPreference(){
			DataUtils.savePreference(Const.REPORT, "");
		}


}
