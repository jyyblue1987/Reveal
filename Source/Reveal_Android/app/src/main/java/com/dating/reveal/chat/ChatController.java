package com.dating.reveal.chat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.dating.reveal.R;
import com.dating.reveal.data.DBManager;
import com.dating.reveal.main.AppContext;
import com.dating.reveal.main.Const;
import com.dating.reveal.main.SoSoUtils;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MUCNotJoinedException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dating.reveal.utility.AlgorithmUtils;
import com.dating.reveal.utility.AndroidUtils;
import com.dating.reveal.utility.CheckUtils;
import com.dating.reveal.utility.DataUtils;
import com.dating.reveal.utility.MyTime;


public class ChatController {
	static ChatService g_ChatService = null;
	public static final String ROOM_SERVICE = "conference.";
	
	private static XMPPConnection g_Connection = null;
	private static int g_connectState = Const.NOT_CONNECTED;
	
	private static HashMap<String, Presence> g_RosterState = new HashMap<String, Presence>();
	
	public static long startServiceTime = 0;
	public static void onStartedService(ChatService service)
	{
		g_ChatService = service;	
		startServiceTime = System.currentTimeMillis();
	}
	
	public static void onStartedLogin()
	{
		startServiceTime = System.currentTimeMillis();
	}
	
	public static void onStopService()
	{
		
	}
	public static void setConnection(XMPPConnection connection)
	{
		g_Connection = connection;
	}
	
	// callback from service
	public static void setConnectState(int state)
	{
		g_connectState = state;
	}
	
	public static void loginChatServer(String username, String password)
	{
		if( g_ChatService == null )
			return;
		
		g_ChatService.loginToChatServer(username, password, null);
	}
	
	public static void presenceChanged(Context context, Presence presence)
	{
		String from = presence.getFrom();
		if( CheckUtils.isEmpty(from) )
			return;
		
		String jid = from;
		String splite [] = from.split("/");
		if( splite != null && splite.length > 0 )
			jid = splite[0];
		g_RosterState.put(jid, presence);
		
		Intent intent = new Intent(Const.ROSTER_CHANGE_ACTION);
        intent.putExtra(Const.EXTRA_MESSAGE, from);
        context.sendBroadcast(intent);
	}
	
	public static void entriesUpdated(Context context, Collection<String> arg0)
	{
		for(String username: arg0) {
//			g_RosterState.put(username, value)
		}
	}
	
	
	public static void entriesDeleted(Context context, Collection<String> arg0) {
		for(String username: arg0) {
			g_RosterState.remove(username);			
		}
		
	}
	
	public static void entriesAdded(Context context, Collection<String> arg0) {
		Roster roster = Roster.getInstanceFor(g_Connection);
		for(String username: arg0) {
			Presence presence = roster.getPresence(username);
	    	g_RosterState.put(username, presence);
		}
	}
	
	public static void onIncomingMessage(Context context, Chat chat, Message messsage)
	{
		if( context == null || chat == null || messsage == null )			
			return;
		
		
		anaylzeMessage(context, messsage);
	}
	
	public static void onIncomingGroupMessage(Message message)
	{
		if( g_ChatService == null || message == null )			
			return;
		
//		anaylzeGroupMessage(g_ChatService, message);
	}
	private static void anaylzeMessage(final Context context, final Message messsage)
	{
		String body = messsage.getBody();
		
		if( CheckUtils.isEmpty(body) )
			return;		
		
		String from = getUsername(messsage.getFrom());
		String to = getUsername();
		
		JSONObject data = new JSONObject();
		
		try {
			JSONObject content = new JSONObject(body);
			if( content.optInt(Const.TYPE, 0) != 0 ) // file type
			{
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						onReceiveFile(context, messsage);						
					}
				}).start();
				
				return;
			}
			
			data.put(Const.FROM, to);
			data.put(Const.TO, from);
			data.put(Const.BODY, content.optString(Const.BODY, ""));
			data.put(Const.TYPE, 0);	// text message
			data.put(Const.UNREAD, 1);  // unread flag
			data.put(Const.SENT, 2);    // delivered
			data.put(Const.DIRECTION, 1); // incoming
			data.put(Const.GROUP_TYPE, 0);	// 1:1 chatting
			data.put(Const.DATE, MyTime.getCurrentTime());
			
			
			if( isConnectedServer(g_Connection) && from.equals(g_Connection.getHost()) )	// server broadcast message
			{
				 data.put(Const.NICKNAME, "Service center");
				 NotificationReceiver.addNotification(g_ChatService, data);
				 return;
			}
			
			long id = DBManager.addChat(context, data); // you don't know to use Const.ID ???
			data.put(Const.ID, id);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		long gap = System.currentTimeMillis() - startServiceTime;
		if( gap < 10000 )
		{
			return;
		}
		
		Intent intent = new Intent(Const.RECEIVE_MESSAGE_ACTION);
        intent.putExtra(Const.EXTRA_MESSAGE, data.toString());
        context.sendBroadcast(intent);
        
        addNotification(context, data);
	}
	
	private static JSONObject convertGroupMessageToJSONObject(Message message)
	{
		String body = message.getBody();
		if( CheckUtils.isEmpty(message) )
			return null;		
		
		String from = getUsername(message.getFrom());
		String to = getUsername();
		
		String[] splite = message.getFrom().split("/");
		
		String nickname = from;
		if( splite.length > 1 )
			nickname = splite[splite.length - 1];
		
		String sender = nickname;
		try {
			JSONObject content = new JSONObject(body);
			body = content.optString(Const.BODY, "");
			sender = content.optString(Const.SENDER);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JSONObject data = new JSONObject();
		try {
			data.put(Const.FROM, to);
			data.put(Const.TO, from);
			data.put(Const.NICKNAME, nickname);
			data.put(Const.BODY, body);
			data.put(Const.TYPE, 0);	// text message
			data.put(Const.UNREAD, 1);  // unread flag
			data.put(Const.SENT, 2);    // delivered
			data.put(Const.DIRECTION, 0); // incoming
			data.put(Const.GROUP_TYPE, 1);	// Group
			data.put(Const.DATE, MyTime.getCurrentTime());
			data.put(Const.SENDER, sender);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	private static String g_CurrrentRoomName = "";
	private static String g_CurrrentNickName = "";
	public static void setGroupChatInfo(String group_name, String nickname)
	{
		g_CurrrentRoomName = group_name + "";
		g_CurrrentNickName = nickname + "";
	}
	
	private static void anaylzeGroupMessage(Context context, Message message)
	{
		JSONObject data = convertGroupMessageToJSONObject(message);
		if( data == null )
			return;
		
		String from = ChatController.getUsername(data.optString(Const.TO, ""));
		String chatUser = ChatController.getUsername(g_CurrrentRoomName);
		
//		String nickname = data.optString(Const.NICKNAME, "");
		String sender = data.optString(Const.SENDER, "");
		
		if( from.equals(chatUser)  && sender.equals(ChatController.getUsername())  )	// self send message 
			return;
		
		long id = DBManager.addChat(context, data);
		try {
			data.put(Const.ID, id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		long gap = System.currentTimeMillis() - startServiceTime;
		if( gap < 10000 )
			return;
		
		Intent intent = new Intent(Const.RECEIVE_MESSAGE_ACTION);
        intent.putExtra(Const.EXTRA_MESSAGE, data.toString());
        context.sendBroadcast(intent);
        
        addNotification(context, data);
	}
	
	public static List<JSONObject> getGroupChatMessageHistory(MultiUserChat muc, int count)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		if( muc == null )
			return list;
		
		for(int i = 0; i < count; i++ )
		{
			Message message;
			try {
				message = muc.nextMessage();
				JSONObject data = convertGroupMessageToJSONObject(message);
				if( data == null )
					continue;
				
				list.add(data);
			} catch (MUCNotJoinedException e) {
				e.printStackTrace();
				break;
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}			
			
		}
		
		return list;
	}
	
	private static String g_FileCachePath = "";
	public static void initChatFileDirectory(Context context)
	{
		if( context == null )
			return;
		
	   File cacheLocation;
       if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
           cacheLocation = new File(
                   Environment.getExternalStorageDirectory() + "/Contact2W_Cache");
       } else {
           cacheLocation = new File(context.getFilesDir() + "/Contact2W_Cache");
       }
       cacheLocation.mkdirs();
       
       g_FileCachePath = cacheLocation.getAbsolutePath();
	}
	
	public static String getFileCacehPath()
	{
		return g_FileCachePath;
	}

	private static void onReceiveFile(final Context context, Message message)
	{
//		String body = message.getBody();
//		if( CheckUtils.isEmpty(body) )
//			return;
//
//		String from = getUsername(message.getFrom());
//		String to = getUsername();
//
//		try {
//			JSONObject content = new JSONObject(body);
//			if( content.optInt(Const.TYPE, 0) == 0 ) // file type
//				return;
//
//			int sent = content.optInt(Const.SENT, 3);
//			final long fileid = content.optLong(Const.ID, 0);
//			String url = content.optString(Const.BODY, "");
//
//			JSONObject data = new JSONObject();
//
//			if( sent == 1 ) // uploading now
//			{
//				Log.e(Const.SMACK, "file upload start is received");
//
//				data.put(Const.FROM, to);
//				data.put(Const.TO, from);
//				data.put(Const.BODY, content.optString(Const.BODY, ""));
//				data.put(Const.TYPE, content.optInt(Const.TYPE, 1));	// Image
//				data.put(Const.UNREAD, 1);  // unread flag
//				data.put(Const.SENT, 3);    // waiting
//				data.put(Const.DIRECTION, 0); // incoming
//				data.put(Const.GROUP_TYPE, 0);	// 1:1 chatting
//				data.put(Const.DATE, MyTime.getCurrentTime());
//				data.put(Const.SENDER, fileid);
//
//				long id = DBManager.addChat(context, data);
//				data.put(Const.ID, id);
//
//				long gap = System.currentTimeMillis() - startServiceTime;
//				if( gap < 10000 )
//				{
//					return;
//				}
//
//				Intent intent = new Intent(Const.RECEIVE_MESSAGE_ACTION);
//		        intent.putExtra(Const.EXTRA_MESSAGE, data.toString());
//		        context.sendBroadcast(intent);
//
//		        addNotification(context, data);
//		    }
//			else if( sent == 0 ) // upload fail
//			{
//				Log.e(Const.SMACK, "file upload fail is received");
//				notifyFileStateChange(context, fileid, 0);
//			}
//			else if( sent == 2 ) // upload ok
//			{
//				Log.e(Const.SMACK, "file upload success is received");
//				String ext = ".jpg";
//				int dot = url.lastIndexOf(".");
//				if( dot >= 0 )
//					ext = url.substring(dot);
//
//				String savePath = g_FileCachePath + "/" + System.currentTimeMillis() + ext;
//
//				DBManager.setFilePath(context, fileid, savePath);
//
//				DownloadFileTask task = new DownloadFileTask(url, savePath, null, null);
//				boolean ret = task.downloadFile(url, new File(savePath));
//
//				Log.e(Const.SMACK, "file download success");
//				sent = 0;
//				if( ret == true )
//					sent = 2;
//				notifyFileStateChange(context, fileid, sent);
//			}
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//
	}
	
	private static void notifyFileStateChange(Context context, long fileid, int sent)
	{
		if( DBManager.setFlagFileState(context, fileid, sent) < 0 )
			return;
		
		JSONObject data = DBManager.getFileChatID(context, fileid);
		
		if( data == null )
			return;
		
		Intent intent = new Intent(Const.UPDATE_MESSAGE_ACTION);
		intent.putExtra(Const.EXTRA_MESSAGE, data.toString());
        context.sendBroadcast(intent);
	}
	public static void onRequestFileReciver(final Context context, final FileTransferRequest request)
	{
		try {
			// Accept it
			
			IncomingFileTransfer transfer = request.accept();
			
			String ext = ".jpg";
			int dot = request.getFileName().lastIndexOf(".");
			if( dot >= 0 )
				ext = request.getFileName().substring(dot);
		   
			String savePath = g_FileCachePath + "/" + System.currentTimeMillis() + ext;
			
			transfer.recieveFile( new File(savePath));
			
			long id = -1;
			boolean ok = true;
			boolean bReceiveFlag = false;
			
			long filesize = transfer.getFileSize();
			if( filesize <= 0 )
				return;
						
			long st = System.currentTimeMillis();
	        while (!transfer.isDone() || (transfer.getProgress() < 1)) {
//	        	setProgressState(progress_id, (int)(transfer.getProgress() * 100));
    			
	        	if( bReceiveFlag == false )
	        	{
	        		String desc = request.getDescription();
	                
	                int type = 1;
	                try {
	                	type = Integer.parseInt(desc);
	                } catch(Exception e ) {
	                	e.printStackTrace();
	                }
	                
	                String from = getUsername(request.getRequestor());
	                String to = getUsername(g_Connection.getUser());
	        		
	        		JSONObject data = new JSONObject();
	        		try {
	        			data.put(Const.FROM, to);
	        			data.put(Const.TO, from);
	        			data.put(Const.BODY, savePath);
	        			data.put(Const.TYPE, type);	// file message
	        			data.put(Const.UNREAD, 1);  // unread flag
	        			data.put(Const.SENT, 3);    // delivered
	        			data.put(Const.DIRECTION, 0); // incoming
	        			data.put(Const.GROUP_TYPE, 0);	// 1:1 chatting
	        			data.put(Const.DATE, MyTime.getCurrentTime());
	        			
	          			id = DBManager.addChat(context, data);
	        			data.put(Const.ID, id);
        			
	        			JSONObject profile = loadVCard(from);
	        			
	        			String domain = getDomain(request.getRequestor());
	        			addContacts(from, domain, profile.optString(Const.NICKNAME));
	        			    			
	        		} catch (JSONException e) {
	        			e.printStackTrace();
	        		}
	    			
	        		long gap = System.currentTimeMillis() - startServiceTime;
	        		if( gap < 10000 )
	        		{
	        			bReceiveFlag = true;
	        			continue;
	        		}
	        		
	        		Intent intent = new Intent(Const.RECEIVE_MESSAGE_ACTION);
	                intent.putExtra(Const.EXTRA_MESSAGE, data.toString());
	                context.sendBroadcast(intent);
	                
	                addNotification(context, data);
	                
	                bReceiveFlag = true;
	        	}
	        	else
	        	{
	        		try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
	        	}
 				
	        	long current_prog = (long)(transfer.getProgress() * filesize);
	        	Log.e(Const.SMACK, "File receive size filesize: " + current_prog);
	            if (transfer.getStatus().equals(FileTransfer.Status.error)) {
	            	transfer.cancel();
	            	ok = false;
	            	Log.e(Const.SMACK, "File receive error filesize: " + current_prog);
	                break;
	            }	
	            
	            long gap = System.currentTimeMillis() - st;
	            if( filesize * 10 < gap && gap > 20000 ) // < 0.1Kb/s
	            {
	            	transfer.cancel();
	            	ok = false;
	            	Log.e(Const.SMACK, "File receive not progress: timeout = " + (System.currentTimeMillis() - st) + " filesize: " + current_prog);
	                break;
	            }
            
	        } 					
	        
	        if( transfer.isDone() == false )
	        	return;
	        
			int sent = 2;
			if( ok == false )
				sent = 0;
			
			JSONObject data = new JSONObject();
			try {
				data.put(Const.ID, id);
				data.put(Const.SENT, sent);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			DBManager.setFlagSendChat(context, data, sent);
			
			Intent intent = new Intent(Const.UPDATE_MESSAGE_ACTION);
			intent.putExtra(Const.EXTRA_MESSAGE, data.toString());
	        context.sendBroadcast(intent);
			
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
    private static boolean isConnectedServer(XMPPConnection connection)
    {
    	if( connection == null )
    		return false;
    	
    	return connection.isConnected();
    }
    
    private static boolean isChatAvailable(XMPPConnection connection)
    {
    	if( isConnectedServer(connection) == false )
    		return false;
    	
    	if( connection.isAuthenticated() == false )
    		return false;
    	
    	return true;
    }
    
    public static Chat createChatSession(String username, String domain)
    {
    	if( g_ChatService == null )
    		return null;
    	
    	if( CheckUtils.isEmpty(username) )
    		return null;
    	
    	if( isChatAvailable(g_Connection) == false )
    		return null;
    	if( CheckUtils.isEmpty(domain) )
    		domain = g_Connection.getHost();
    	
    	ChatManager chatmanager = ChatManager.getInstanceFor(g_Connection);
		Chat newChat = chatmanager.createChat(username + "@" + g_Connection.getHost() + "/" + ChatService.CHAT_RESOURCE);
		
		if( newChat == null )
			return newChat;
		
		newChat.addMessageListener(g_ChatService.getMessageListener());
		
        return newChat;    	
    }
    
    
    public static MultiUserChat createMultiChatSession(String roomname, String nickname)
    {
    	if( g_ChatService == null )
    		return null;
    	
    	MultiUserChat muc = createOrJoinInstanceMultiUserChat(roomname, nickname);
    	
    	if( muc == null )
    		return null;
    	
    	muc.addMessageListener(g_ChatService.getGroupMessageListener());
    	muc.addParticipantListener(g_ChatService.getPresenceListener());
    	muc.addParticipantStatusListener(g_ChatService.getParticipantStatusListener());
    	
    	return muc;
    }
    
	public static FileTransfer sendFile(Context context, String to, String domain, String path, String desc)
	{
		if( isChatAvailable(g_Connection) == false )
			return null;
			
    	FileTransferManager manager = FileTransferManager.getInstanceFor(g_Connection);
    	// Create the outgoing file transfer
    	if( CheckUtils.isEmpty(domain) )
    		domain = g_Connection.getHost();
    	
    	String toJID = to + "@" + domain + "/" + ChatService.CHAT_RESOURCE;
    	// Send the file
    	try {
    		OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(toJID);
			transfer.sendFile(new File(path), desc);
			return transfer;
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();			
		}   
    	
    	return null;
	}
	
	public static List<JSONObject> getChatContacts(Context context, boolean refresh)
    {   
    	List<JSONObject> list = new ArrayList<JSONObject>();
    	if( context == null )
    		return list;
    	
       	if( isChatAvailable(g_Connection) == false || refresh == false)
        		return DBManager.getContactList(context, "", 0);    		

       	Roster roster = Roster.getInstanceFor(g_Connection);
    	Collection<RosterEntry> entries = roster.getEntries();
    	
    	for (RosterEntry entry : entries) {
    		JSONObject contact = new JSONObject();
    		
    		try {
    			String jid = entry.getUser();
    			if( CheckUtils.isEmpty(jid) )
    				continue;
    			
    			String[] splite = jid.split("@");
    			if( splite == null || splite.length < 1 )
    				continue;
    			
    			String username = splite[0];
    			String domain = "";
    			if( splite.length > 1 )
    				domain = splite[1];
    			else
    				domain = g_Connection.getHost();
    			
    			contact.put(Const.USERNAME, username);
				contact.put(Const.NICKNAME, entry.getName());
				contact.put(Const.DOMAIN, domain);
				
				JSONObject profile = loadVCard(username);
			      
			    AlgorithmUtils.bindJSONObject(contact, profile);
				
		    	Presence presence = roster.getPresence(jid);
		    	g_RosterState.put(jid, presence);
		    	
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    		
    		list.add(contact);
    	}
    	
    	DBManager.updateContactList(context, list);
    	
    	return list;    	
    }
    
    public static Presence.Type getUserState(String username)
    {
    	if( CheckUtils.isEmpty(username) )
    		return Presence.Type.unavailable;
    	
    	if( isChatAvailable(g_Connection) == false  )
    		return Presence.Type.unavailable;
    	
    	Presence presence = g_RosterState.get(username);
    	if( presence == null )
    		return Presence.Type.unavailable;
    	
    	return presence.getType();
    }
    
    public static void addContacts(String username, String domain, String nickname)
    {
       	if( isChatAvailable(g_Connection) == false )
    		return;
    	
       	if( CheckUtils.isEmpty(domain) )
    		domain = g_Connection.getHost();
       	
    	Roster roster = Roster.getInstanceFor(g_Connection);
    	
    	String group [] = {
    		"Friends"	
    	};
    	try {
    		String jid = username + "@" + domain;
    		roster.createEntry(jid, nickname, group);
		} catch (NotLoggedInException e) {
			e.printStackTrace();
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
    }   
    
    public static boolean isExistContact(String jid)
    {
    	if( CheckUtils.isEmpty(jid) )
			return false;
		
      	if( isChatAvailable(g_Connection) == false )
    		return false;

		Roster roster = Roster.getInstanceFor(g_Connection);

		RosterEntry entry = roster.getEntry(jid);
		if( entry == null )
		{    			
    		return false;        		
		}	
		
		return true;		
		
    }
    public static void deleteContact(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return;
		
      	if( isChatAvailable(g_Connection) == false )
    		return;

		String username = data.optString(Const.USERNAME, "");
		String domain = data.optString(Const.DOMAIN, "");
		int group_type = data.optInt(Const.GROUP_TYPE, 0);
		
		if( group_type != 0 )
			return;
		
		Roster roster = Roster.getInstanceFor(g_Connection);

    	try {
    		RosterEntry entry = roster.getEntry(username + "@" + domain);
    		if( entry == null )
    		{
    			entry = roster.getEntry(username);
        		if( entry == null )
        			return;        		
    		}	
    		
    		roster.removeEntry(entry);		
    		DBManager.deleteContact(context, data);
    		DBManager.removeContactFromGroup(context, username);		
		} catch (NotLoggedInException e) {
			e.printStackTrace();
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}
	
    
    private static final String USER_JID = "user_jid";
    public static String getUsername()
	{
    	String jid = "";
		if( isChatAvailable(g_Connection) == false )
		{
			return SoSoUtils.getChatID(AppContext.getProfile());
		}
		else
			jid = g_Connection.getUser();
		
		DataUtils.savePreference(USER_JID, jid);
		
		String[] splite = jid.split("@");
		if( splite.length < 1 )
			return jid.replace(",", ":");
		
		return splite[0].replace(",", ":");
	}

	public static String getUsername(String jid)
	{
		if( CheckUtils.isEmpty(jid) )
			return "";
		
		String[] splite = jid.split("@");
		if( splite.length < 1 )
			return jid.replace(",", ":");
		
		return splite[0].replace(",", ":");
	}
	
	public static String getDomain(String jid)
	{
		if( CheckUtils.isEmpty(jid) )
		{
			if( isConnectedServer(g_Connection) )
				return g_Connection.getHost();
			else
				return "";
		}
		
		String[]splite = jid.split("@");
		if( splite.length < 2 )
		{
			if( isConnectedServer(g_Connection) )
				return g_Connection.getHost();
			else
				return "";
		}
		
		String domainSplite [] = splite[1].split("/");
		if( domainSplite.length < 1 )
		{
			if( isConnectedServer(g_Connection) )
				return g_Connection.getHost();
			else
				return "";
		}
		return domainSplite[0];
	
	}
	public static String getJID(String username)
    {
		if( isChatAvailable(g_Connection) == false )
			return username;
		
    	if( CheckUtils.isEmpty(username) )
    		return "";
    	return username + "@" + g_Connection.getHost();
    }
	
	public static String getRoomName(String roomname)
	{
		if( isChatAvailable(g_Connection) == false )
			return roomname;
		
		if( CheckUtils.isEmpty(roomname) )
			return "";
		
		return roomname + "@" + ROOM_SERVICE + g_Connection.getHost();
	}
	public static MultiUserChat createOrJoinInstanceMultiUserChat(String roomname, String nickname)
	{
		if( isChatAvailable(g_Connection) == false )
			return null;
		
		// Get the MultiUserChatManager
		MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(g_Connection);

		// Get a MultiUserChat using MultiUserChatManager
		MultiUserChat muc = manager.getMultiUserChat(getRoomName(roomname).replace(":", ","));

		// Create the room
		try {
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxStanzas(0);
			
//			history.setMaxChars()
//			history.setMaxStanzas()
//			history.setSeconds()
//			if( muc.isJoined() )
//				return muc;
			
			muc.createOrJoin(nickname, null, history, SmackConfiguration.getDefaultPacketReplyTimeout());
			muc.sendConfigurationForm(new Form(DataForm.Type.submit));			
			
			return muc;
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return muc;
	}
	
	public static void leaveMultiUserChat(String roomname)
	{
		if( isChatAvailable(g_Connection) == false )
			return;
		
		// Get the MultiUserChatManager
		MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(g_Connection);

		// Get a MultiUserChat using MultiUserChatManager
		MultiUserChat muc = manager.getMultiUserChat(getRoomName(roomname).replace(":", ","));

		// Create the room
		try {
			muc.leave();
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static MultiUserChat createReservedMultiUserChat(String roomname, String nickname)
	{
		if( isChatAvailable(g_Connection) == false )
			return null;
		
		// Get the MultiUserChatManager
		MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(g_Connection);

		// Create a MultiUserChat using an XMPPConnection for a room
		MultiUserChat muc = manager.getMultiUserChat(getRoomName(roomname));

		// Create the room
		try {
			muc.create(nickname);
			
			// Get the the room's configuration form
			Form form = muc.getConfigurationForm();
			// Create a new form to submit based on the original form
			Form submitForm = form.createAnswerForm();
			
//			// Add default answers to the form to submit
//			for (Iterator fields = form.getFields(); fields.hasNext();) {
//				FormField field = (FormField) fields.next();
//				if (!FormField.Type.hidden.equals(field.getType()) && field.getVariable() != null) {
//					// Sets the default value as the answer
//					submitForm.setDefaultAnswer(field.getVariable());
//				}
//			}
//			// Sets the new owner of the room
//			List owners = new ArrayList();
//			owners.add("johndoe@jabber.org");
//			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			// Send the completed form (with default values) to the server to configure the room
			muc.sendConfigurationForm(submitForm);
			
			
			return muc;
			
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (SmackException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	


	public static MultiUserChat joinMultiUserChat(String roomname, String nickname)
	{
		if( isChatAvailable(g_Connection) == false )
			return null;

		// Get the MultiUserChatManager
		MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(g_Connection);
		
		// Create a MultiUserChat using an XMPPConnection for a room
		MultiUserChat muc = manager.getMultiUserChat(getRoomName(roomname));

		// User2 joins the new room
		// The room service will decide the amount of history to send
		try {
			
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxStanzas(0);
			
//			history.setMaxChars()
//			history.setMaxStanzas()
//			history.setSeconds()

			muc.join(nickname, null, history, SmackConfiguration.getDefaultPacketReplyTimeout() );			
			return muc;
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		
		return muc;
	}
	
	public static void inviteMultiUserChat(MultiUserChat muc, String inviter)
	{
		if( isChatAvailable(g_Connection) == false )
			return;
		
		if( muc == null )
			return;
		
		JSONObject contact = DBManager.getContact(g_ChatService, inviter, 0);
		String domain =  contact.optString(Const.DOMAIN, "");
		if( CheckUtils.isEmpty(domain) )
			domain = g_Connection.getHost();
		
		boolean exist = false;
		
		String toJID = inviter + "@" + domain;
		try {
			List<Occupant> users = muc.getParticipants();
			for(int i = 0; i < users.size(); i++ )
			{
				Occupant user = users.get(i);
				if( user == null )
					continue;
				
				String jid = user.getJid();
				String[] splite = jid.split("/");
				if( splite != null && splite.length > 0 )
					jid = splite[0];
				
				if( toJID.equals(jid) )
				{
					exist = true;
					break;
				}
			}
			
		} catch (NotConnectedException e) {
			e.printStackTrace();
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		}		

		if( exist == false )
		{
			try {
				muc.invite(getJID(inviter), "Meet me in this excellent room");
				Log.e(Const.SMACK, "invite to " + inviter);
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		muc.addInvitationRejectionListener(new InvitationRejectionListener() {
			public void invitationDeclined(String invitee, String reason) {
				// Do whatever you need here...
			}
		});
	}
	
	public static List<JSONObject> getJoinedRooms()
	{
		if( isChatAvailable(g_Connection) == false )
			return new ArrayList<JSONObject>();

		return getJoinedRooms(getUsername());
	}
	
	public static List<JSONObject> getJoinedRooms(String username)
	{
		List<JSONObject> roomList = new ArrayList<JSONObject>();
		if( isChatAvailable(g_Connection) == false )
			return roomList;

		// Get the MultiUserChatManager
		MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(g_Connection);

		// Get the rooms where user3@host.org has joined
		try {
			List<String> joinedRooms = manager.getJoinedRooms(getJID(username) + "/" + ChatService.CHAT_RESOURCE);
			for(int i = 0; i < joinedRooms.size(); i++ )
			{
				String roomname = joinedRooms.get(i);
				List<JSONObject> list = DBManager.getGroupList(g_ChatService, getUsername(roomname));
				if( list != null && list.size() > 0 )
					continue;
					
				roomList.add(getRoomInfo(roomname));
			}
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		
		return roomList;
	}
	
	public static JSONObject getRoomInfo(String fullRoomname)
	{
		JSONObject room = new JSONObject();
		
		if( isChatAvailable(g_Connection) == false )
			return room;

		// Get the MultiUserChatManager
		MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(g_Connection);

		// Get the rooms where user3@host.org has joined
		try {
			RoomInfo info = manager.getRoomInfo(fullRoomname);
			
			room.put(Const.USERNAME, getUsername(fullRoomname));
			
			JSONObject profile = loadMyVCard(false);
			String nickname = profile.optString(Const.NICKNAME, "");
			if( CheckUtils.isEmpty(nickname) )
				nickname = SoSoUtils.getUserName(getUsername());
			room.put(Const.NICKNAME, nickname);
			room.put(Const.GROUP_TYPE, 1);
			room.put(Const.EMAIL, "");
			room.put(Const.FAVORITE, 0);
			room.put(Const.ROLE, 1);
			room.put(Const.CREATOR, "");
			room.put(Const.DOMAIN, getDomain(fullRoomname));
			room.put(Const.CONTACT_COUNT, info.getOccupantsCount());			
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return room;
	}
	public static void invitationReceived(XMPPConnection conn, MultiUserChat muc, String room, String inviter, String reason, Message message) {
		String requestor = room;
		String roomname = message.getFrom();
		
		List<JSONObject> list = DBManager.getGroupList(g_ChatService, getUsername(roomname));
		String nickname = "";
		if( list.size() > 0 )
		{
			JSONObject roomInfo = list.get(0);
			nickname = roomInfo.optString(Const.NICKNAME, "");
		}
		
		if( CheckUtils.isEmpty(nickname) )
		{
			JSONObject profile = loadMyVCard(false);
			nickname = profile.optString(Const.REALNAME, "");
			if( CheckUtils.isEmpty(nickname) )
				nickname = SoSoUtils.getDisplayName(profile);
		}
		
		DiscussionHistory history = new DiscussionHistory();
		history.setMaxStanzas(0);
		
		try {
			if( muc.isJoined() == false )
				muc.leave();
			muc.join(nickname, null, history, SmackConfiguration.getDefaultPacketReplyTimeout());
			muc.addMessageListener(g_ChatService.getGroupMessageListener());
			Log.e(Const.SMACK, "Invite receive from " + nickname);
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Intent intent = new Intent(Const.ROSTER_CHANGE_ACTION);
        intent.putExtra(Const.EXTRA_MESSAGE, requestor);
        g_ChatService.sendBroadcast(intent);
		
	} 
	
	private static final String PROFILE = "profile";
	
	public static JSONObject loadVCard(String username)
    {
		JSONObject profile = new JSONObject();
		if( isChatAvailable(g_Connection) == false )
			return profile;
		
    	VCardManager vCardManager = VCardManager.getInstanceFor(g_Connection);
    	try {
	    	VCard vCard = vCardManager.loadVCard(getJID(username));
    		
    		String info = vCard.getMiddleName();
    		
    		JSONObject data = new JSONObject();
			try {
				if( CheckUtils.isEmpty(info) == false )
					data = new JSONObject(info);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			AlgorithmUtils.bindJSONObject(profile, data);
			
			try {
				profile.put(Const.USERNAME, username);		
				profile.put(Const.AVASTAR, vCard.getLastName());
			} catch(Exception e) {
				e.printStackTrace();
			}				
    	} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		} 
    	
    	return profile;
    }
    
	private static JSONObject g_MyProfile = null;
	
    public static JSONObject loadMyVCard(boolean refresh)
    {
		if( refresh == false )
		{
			if( g_MyProfile != null )
				return g_MyProfile;
			
			String data = DataUtils.getPreference(PROFILE, "");
			if( CheckUtils.isEmpty(data) == false )
			{
				try {
					JSONObject profile = new JSONObject(data);
					g_MyProfile = profile;
					return profile;
				} catch (JSONException e) {
					e.printStackTrace();
				}			
			}
		}
		
    	JSONObject profile = new JSONObject();
		if( isChatAvailable(g_Connection) == false )
		{
			if( g_MyProfile != null )
				return g_MyProfile;
			
			String data = DataUtils.getPreference(PROFILE, "");
			try {
				profile = new JSONObject(data);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return profile;			
		}
		
    	VCardManager vCardManager = VCardManager.getInstanceFor(g_Connection);
    	
		try {
			VCard vCard = vCardManager.loadVCard();
			
			String info = vCard.getMiddleName();
			
			JSONObject data = new JSONObject();
			try {
				if( CheckUtils.isEmpty(info) == false )
					data = new JSONObject(info);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			AlgorithmUtils.bindJSONObject(profile, data);
			
			try {				
				profile.put(Const.USERNAME, getUsername());		
				profile.put(Const.AVASTAR, vCard.getLastName());
				
				Iterator<String> iter = profile.keys();
				while (iter.hasNext()) {
				    String key = iter.next();
				    
				    try {
				    	String value = profile.optString(key, "");
				    	if( value.equals("false") )
				    		profile.put(key, "");
				    } catch (Exception e) {
				        // Something went wrong!
				    }
				}
				
				g_MyProfile = profile;			    	
			    DataUtils.savePreference(PROFILE, profile.toString());
			} catch(JSONException e) {
				e.printStackTrace();
			}
			
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		} 
    	
    	return profile;
    }
    
    public static void saveMyVCard(JSONObject profile)
    {
    	if( isChatAvailable(g_Connection) == false )
			return;
    	if( profile == null )
    		return;
    	
    	VCardManager vCardManager = VCardManager.getInstanceFor(g_Connection);
    	VCard vCard;
    	try {
			vCard = vCardManager.loadVCard();			
			vCard.setMiddleName(profile.toString());			
	    	vCardManager.saveVCard(vCard);
		} catch (NoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    	AccountManager accountManager = AccountManager.getInstance(g_Connection);
    	
    	String username = getUsername();
    	String password = AlgorithmUtils.invert(username);
    	
    	Collection<String> c;
		try {
			c = accountManager.getAccountAttributes();
			Map<String,String> map = new HashMap<String,String>();
	    	for (String key : c) {
	    	     map.put(key, accountManager.getAccountAttribute(key));
	    	}  
	    	  
	    	map.put("email", profile.optString(Const.EMAIL, ""));  
	    	map.put("name", profile.optString(Const.REALNAME, ""));  
	    	
	    	accountManager.createAccount(username, password, map);	    	
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}  
    	     	
    	DataUtils.savePreference(PROFILE, profile.toString());
    	
    	g_MyProfile = profile;
    }
    
    public static void saveMyAvatar(JSONObject profile)
    {
    	if( isChatAvailable(g_Connection) == false )
			return;
    	
    	VCardManager vCardManager = VCardManager.getInstanceFor(g_Connection);
    	VCard vCard;
    	try {
			vCard = vCardManager.loadVCard();
			
			vCard.setLastName(profile.optString(Const.AVASTAR, ""));
			
	    	vCardManager.saveVCard(vCard);
		} catch (NoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	    	     	
    	DataUtils.savePreference(PROFILE, profile.toString());
    	
    	g_MyProfile = profile;
    }
    
    public static List<JSONObject> searchUsers(String filter, int category)
    {
    	List<JSONObject> results = new ArrayList<JSONObject>();
    
    	if( isChatAvailable(g_Connection) == false )
    		return results;    	
    	
		try {
		    UserSearchManager usm = new UserSearchManager(g_Connection);
		    Form searchForm = usm.getSearchForm("search." + g_Connection.getServiceName());
		    Form answerForm = searchForm.createAnswerForm();
		    UserSearch userSearch = new UserSearch();
		    answerForm.setAnswer("Username", true);
		    answerForm.setAnswer("Name", true);
		    answerForm.setAnswer("Email", true);
		    answerForm.setAnswer("search", filter);
		    ReportedData data = userSearch.sendSearchForm(g_Connection, answerForm, "search." + g_Connection.getServiceName());
		
		    String selfName = getUsername();
		    
			for (ReportedData.Row row : data.getRows())
			{
			      JSONObject user = new JSONObject();
			      String username = row.getValues("Username").toString();
			      username = username.replace("[", "").replace("]", "");
			      
			      if( CheckUtils.isEmpty(username) )
			    	  continue;			    	  
			      if( username.equals(selfName) )	// self contact
			    	  continue;
			      
			      JSONObject profile = loadVCard(username);
			      if( category > 0 )
			      {
			    	  int role = profile.optInt(Const.ROLE, 0);
			    	  if( category == 1 && role != 1 ) // doctor
			    		  continue;
			    	  if( category == 2 && role != 0 ) // patient
			    		  continue;	  
			      }
			      
			      user.put(Const.USERNAME, username);
			      
			      if( DBManager.isExistContact(g_ChatService, user) == true )	// already added contact
			    	  continue;

			      
			      String email = row.getValues("Email").toString();
			      if( CheckUtils.isEmpty(email) || email.equals("[false]") )
			    	  email = "";
			      email = email.replace("[", "").replace("]", "");
			      
			      String name = row.getValues("Name").toString();
			      if( CheckUtils.isEmpty(name) || name.equals("[false]") )
			    	  name = "";
			      
			      name = name.replace("[", "").replace("]", "");
			      
			      String domain = "";
			      List<String> jids = row.getValues("jid");
			      if( jids != null )
			      {
			    	  for(int j = 0; j < jids.size(); j++)
			    	  {
			    		  String jid = jids.get(j);
				    	  domain = getDomain(jid);
				    	  if(CheckUtils.isEmpty(domain) == false )
				    		  break;
			    	  }
			      }
			      
			      user.put(Const.EMAIL, email);
			      user.put(Const.NICKNAME, name);
			      user.put(Const.DOMAIN, domain);			      
			      
			      
			      AlgorithmUtils.bindJSONObject(user, profile);
			      
			      results.add(user);		         
			}
		  
		} catch (Exception e) {
		  e.printStackTrace();
		}
		 return results;
    }
    
    public static int isExistUserOnServer(String usernameKey)
    {
    	if( isChatAvailable(g_Connection) == false )
    		return -1;    	

    	if( CheckUtils.isEmpty(usernameKey) )
    		return -2;

		try {
		    UserSearchManager usm = new UserSearchManager(g_Connection);
		    Form searchForm = usm.getSearchForm("search." + g_Connection.getServiceName());
		    Form answerForm = searchForm.createAnswerForm();
		    UserSearch userSearch = new UserSearch();
		    answerForm.setAnswer("Username", true);
		    answerForm.setAnswer("search", usernameKey);
		    ReportedData data = userSearch.sendSearchForm(g_Connection, answerForm, "search." + g_Connection.getServiceName());
		
		    String selfName = getUsername();
		    
			for (ReportedData.Row row : data.getRows())
			{
			      String username = row.getValues("Username").toString();
			      username = username.replace("[", "").replace("]", "");
			      
			      if( CheckUtils.isEmpty(username) )
			    	  continue;			    	  
			      if( username.equals(selfName) )	// self contact
			    	  continue;
			      
			      if( username.equals(usernameKey) )
			    	  return 0;
			}
		  
		} catch (Exception e) {
		  e.printStackTrace();
		}
		 return -3;
    }
    
    public static void getOfflineMessage()
    {
    	if( isChatAvailable(g_Connection) == false )
    		return;
    	
    	 OfflineMessageManager offlineMessageManager = new OfflineMessageManager(g_Connection);
    	 try {
			List<Message> offlinelist = offlineMessageManager.getMessages();
			for(int i = 0; i < offlinelist.size(); i++ )
			{
				Message offline = offlinelist.get(i);
				anaylzeMessage(g_ChatService, offline);
			}
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
    }
    
    public static void loadMyInfo()
    {
 	    loadMyVCard(true);
 	    getChatContacts(g_ChatService, true);
// 	    getOfflineMessage();
    }
    
    public static void processPresence(Presence arg0) {
		Intent intent = new Intent(Const.ROSTER_CHANGE_ACTION);
        intent.putExtra(Const.EXTRA_MESSAGE, arg0.getFrom());
        g_ChatService.sendBroadcast(intent);
	}
    
	public static void left(String arg0) {
		Intent intent = new Intent(Const.ROSTER_CHANGE_ACTION);
        intent.putExtra(Const.EXTRA_MESSAGE, arg0);
        g_ChatService.sendBroadcast(intent);
	}
	public static void joined(String arg0) {
		Intent intent = new Intent(Const.ROSTER_CHANGE_ACTION);
        intent.putExtra(Const.EXTRA_MESSAGE, arg0);
        g_ChatService.sendBroadcast(intent);
	}
	
    private static void addNotification(Context context, JSONObject data)
	{
//        if( Foreground.get().isForeground() )
//        	return;
        
//        if( DataUtils.getPreference(Const.child_group_name[0][0], true) == true )
        	AndroidUtils.playNotification(context);
        
//        if( DataUtils.getPreference(Const.child_group_name[0][1], true) == true )
        	AndroidUtils.vibrateDevice(context, 500);
        
//        if( DataUtils.getPreference(Const.child_group_name[0][2], true) == true )
//        {
//        	showNotification(context, data);
//        }
	}

    private static void showNotification(Context context, JSONObject data)
	{
    	if( context == null || data == null )
    		return;
    	
    	String message = data.optString(Const.BODY, "");
        int group_type = data.optInt(Const.GROUP_TYPE, 0);
        String nickname = data.optString(Const.NICKNAME, "");
        int type = data.optInt(Const.TYPE, 0);
        
        if( group_type == 0 )
        {	
        	String username = data.optString(Const.TO, "");
        	JSONObject contact = DBManager.getContact(context, username, 0);
        	nickname = SoSoUtils.getDisplayName(contact);
        }
        else
        {
        	nickname = ChatController.getUsername(data.optString(Const.NICKNAME, ""));
        }
        
        switch(type)
        {
        case 1:
        	message = "Image";
        	break;
        case 2:
        	message = "Audio";
        	break;
        case 3:
        	message = "PDF";
        	break;
        }
        
		// Using RemoteViews to bind custom layouts into Notification
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.fragment_notify);
		
		// Open NotificationView Class on Notification Click
		Intent intent = NotificationReceiver.getStartIntentFromPush(context, data );
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
		
		// Create Notification Manager
		NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
		notificationmanager.cancelAll();
		
		Notification notification = builder.build();
		notification.when = System.currentTimeMillis();
		
		try {
			notificationmanager.notify(0, notification);
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
    
//    app.contact2w.com/upload_file.php
//
//    action: UPLOADFILE
//    myfile:   
//
//
//    return: 
//       code: 200
//       filename: filename
//    down url: http://app.contact2w.com/uploads/chatfiles/filename

}
