package com.dating.reveal.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dating.reveal.data.DBManager;
import com.dating.reveal.main.AppContext;
import com.dating.reveal.main.Const;
import com.dating.reveal.main.SoSoUtils;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;

import org.apache.commons.io.FileUtils;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Presence;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.dating.reveal.utility.BackgroundTaskUtils;
import com.dating.reveal.utility.BackgroundTaskUtils.OnTaskProgress;
import com.dating.reveal.utility.CheckUtils;
import com.dating.reveal.utility.MyTime;

//import com.sin.contact2w.utils.Contact2WUtils;

public class InstanceChatPresenter extends ChatPresenter {
	Chat m_Chat = null;
	String m_ToDomain = "";
	public static String PRODUCT_ID = "";
	public static String TO_ID = null;
	
	JSONObject data_base64 = new JSONObject();

	public InstanceChatPresenter(ChatView view) {
		super(view);
		
	}
	
	public void createChatSession(JSONObject contact)
	{
		super.createChatSession(contact);
		
//		JSONObject profile = ChatController.loadVCard(m_ToUsername);
//	
//		// update profile info
//		try {
//			profile.put(Const.USERNAME, m_ToUsername);
//			profile.put(Const.NICKNAME, contact.optString(Const.NICKNAME, ""));
//			DBManager.addContact((Context)view, profile);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		m_NickName = contact.optString("pname", "");
		m_ToDomain = contact.optString(Const.DOMAIN, "");
		
		ChatController.addContacts(m_ToUsername, m_ToDomain, "tijarti");
		
		m_Chat = ChatController.createChatSession(m_ToUsername, m_ToDomain);	
		
		sendUnsendMessage();
		
		getChatHistory(0);
	}
		
		
	public void getChatHistory(int id)
	{
		String from = ChatController.getUsername();
		String to = ChatController.getUsername(m_ToUsername);
		
		List<JSONObject> list = DBManager.getIndividualChatHistory((Context)view, from, to, 0, id, m_nPageNum);
		view.showChatHistory(list);
		DBManager.setFlagReadChatToAll((Context)view, m_ToUsername, 0);
		int count = DBManager.getUnreadMessageCount((Context)view, from, false);
		view.displayUnreadMessageCount(count);
	}
	
	public void addChatHistory(int id)
	{
		final int lastID = id;
		new BackgroundTaskUtils(new OnTaskProgress() {
			List<JSONObject> list = null;
			@Override
			public void onProgress() {
				JSONObject profile = AppContext.getProfile();
				String from =  profile.optString("username", "0");
				from = SoSoUtils.getChatID(from);
//				JSONObject info =  CONTACT.optJSONObject("user");	
//				String to = info.optString("username", "0");
				String to = TO_ID;
				to = SoSoUtils.getChatID(to);
				
				list = DBManager.getIndividualChatHistory((Context)view, from, to, 0, lastID, m_nPageNum);
			}
			
			@Override
			public void onFinished() {
				view.addChatHistory(list);
			}
		}).execute();
	}
	
	private void sendUnsendMessage()
	{
		String from = ChatController.getUsername();
		String to = ChatController.getUsername(m_ToUsername);
		List<JSONObject> list = DBManager.getUnsendChatHistory((Context)view, from, to, 0);
		for(int i = 0; i < list.size(); i++ )
		{
			JSONObject data = list.get(i);
			if( data == null )
				continue;
			
			int sent = 0;
			
			int type = data.optInt(Const.TYPE, 0);
			switch(type)
			{
			case 0: // text message
				try {
					String message = data.optString(Const.BODY, "");
					
					JSONObject textMessage = new JSONObject();
					textMessage.put(Const.TYPE, 0);
					textMessage.put(Const.BODY, message);
					
					Message newMessage = new Message();
					newMessage.setBody(textMessage.toString());
					newMessage.setSubject("Text");
					newMessage.setType(Type.chat);
					
					if( m_Chat != null )
					{
						m_Chat.sendMessage(newMessage);    
						sent = 2;
					}
				}
				catch (NotConnectedException e) {
				      // TODO Auto-generated catch block
				      e.printStackTrace();
				} catch(Exception e) {
					e.printStackTrace();
				}
				break;
//			case 1: // image file
//			case 2: // audio file
//			case 3: // pdf file
//				String path = data.optString(Const.BODY, "");
//				int ret = ChatController.sendFile((Context)view, m_ToUsername, m_ToDomain, path, type + "");
//				if( ret == 0 )
//					sent = 2;
//				break;	
			}
			if( sent == 0 )
				continue;
			
			DBManager.setFlagSendChat((Context)view, data, sent);
		}
	}
	public void sendTextMessage(String message)
	{
		if( CheckUtils.isEmpty(message) )
			return;

		if( m_Chat == null )
			m_Chat = ChatController.createChatSession(m_ToUsername, m_ToDomain);	

		if( m_bUnSendMessage == true )
			sendUnsendMessage();
		
		int sent = 0;
		try {
			JSONObject textMessage = new JSONObject();
			textMessage.put(Const.TYPE, 0);
			textMessage.put(Const.BODY, message);
			
			Message newMessage = new Message();
			newMessage.setBody(textMessage.toString());
			newMessage.setSubject("Text");
			newMessage.setType(Type.chat);
			
			if( m_Chat != null )
			{
				m_Chat.sendMessage(newMessage);    
				sent = 2;
			}			
		}
		catch (NotConnectedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		JSONObject data = new JSONObject();
		try {
			data.put(Const.FROM, ChatController.getUsername());
			data.put(Const.TO, ChatController.getUsername(m_ToUsername));
			data.put(Const.NICKNAME, m_NickName);
			data.put(Const.BODY, message);
			data.put(Const.TYPE, 0);	// text message
			data.put(Const.UNREAD, 0);  // unread flag
			data.put(Const.SENT, sent);    // sended
			data.put(Const.DIRECTION, 2); // outgoing
			data.put(Const.GROUP_TYPE, 0);	// 1:1 chatting
			data.put(Const.DATE, MyTime.getCurrentTime());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		long id = DBManager.addChat((Context)view, data);
		addChat(data);
		
		try {
			data.put(Const.ID, id);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		
		if( sent == 0)
			m_bUnSendMessage = true;
		else
			m_bUnSendMessage = false;
		
		view.addChat(data);
	}
	
	private void addChat( JSONObject data)
	{
		String id = AppContext.getUserID();
		String from_id = data.optString("from_id", "0");
		String to_id = data.optString("to_id", "0");
		String product_id = PRODUCT_ID;
		String body = data.optString("body", "0");
		ServerManager.addChat(id, from_id, to_id, product_id, body, null);
	}

	
	public void sendImageMessage(String path, int source)
	{
		if( CheckUtils.isEmpty(path) )
			return;
		
		if( source == 1 ) // camera
		{
			String copy_path = ChatController.getFileCacehPath() + "/camera_" + System.currentTimeMillis() + ".jpg";
			try {
				FileUtils.copyFile(new File(path), new File(copy_path));
				path = copy_path;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		sendFileMessage(path, 1);
	}
	
	public void sendAudioMessage(String path)
	{
		sendFileMessage(path, 2);
	}
	
	public void sendPDFMessage(String path)
	{
		sendFileMessage(path, 3);
	}
	
	public void sendFileMessage(final String path, final int type)
	{
		if( CheckUtils.isEmpty(path) )
			return;
		
		if( m_bUnSendMessage == true )
			sendUnsendMessage();
		
		final long fileid = System.currentTimeMillis();
		int sent = 0;
		try {
			JSONObject fileMessage = new JSONObject();
			fileMessage.put(Const.TYPE, type);
			fileMessage.put(Const.ID, fileid);
			fileMessage.put(Const.BODY, "");
			fileMessage.put(Const.SENT, 1); // uploading
			
			Message newMessage = new Message();
			newMessage.setBody(fileMessage.toString());
			newMessage.setSubject("Text");
			newMessage.setType(Type.chat);
			
			Log.e(Const.SMACK, "file send singal");
			
			if( m_Chat != null )
			{
				m_Chat.sendMessage(newMessage);    
				sent = 3; // uploading
			}			
		}
		catch (NotConnectedException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			if( CheckUtils.isEmpty(path) )
				return;
			
			JSONObject data = new JSONObject();
			data.put(Const.FROM, ChatController.getUsername());
			data.put(Const.TO, ChatController.getUsername(m_ToUsername));
			data.put(Const.NICKNAME, m_NickName);
			data.put(Const.BODY, path);
			data.put(Const.TYPE, type);	// file message
			data.put(Const.UNREAD, 0);  // unread flag
			data.put(Const.SENT, sent);    // sending
			data.put(Const.DIRECTION, 1); // outgoing
			data.put(Const.GROUP_TYPE, 0);	// 1:1 chatting
			data.put(Const.DATE, MyTime.getCurrentTime());
			
			final long id = DBManager.addChat((Context)view, data);
			
			data.put(Const.ID, id);
	        
			view.addChat(data);
			
			if( sent == 0 )
				return;
			
			Log.e(Const.SMACK, "file upload start");
			ServerManager.uploadFile(path, new ResultCallBack() {
				
				@Override
				public void doAction(LogicResult result) {
					Log.e(Const.SMACK, "file upload finish");
					int sent = 2;
					if( result.mResult != LogicResult.RESULT_OK )
						sent = 0;
					
					if( result.getData() == null )
						sent = 0;
					
					String uploadpath = result.getData().optString(Const.UPLOAD_PATH, "");
					if( CheckUtils.isEmpty(uploadpath) )
						sent = 0;
					
					JSONObject data = new JSONObject();
					try {
						data.put(Const.ID, id);
						data.put(Const.SENT, sent);
						data.put(Const.BODY, path);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					DBManager.setFlagSendChat((Context)view, data, sent);
					
					Intent intent = new Intent(Const.UPDATE_MESSAGE_ACTION);
					intent.putExtra(Const.EXTRA_MESSAGE, data.toString());
					((Context)view).sendBroadcast(intent);
					
					// send uploaded file path
					try {
						JSONObject fileMessage = new JSONObject();
						fileMessage.put(Const.TYPE, type);
						fileMessage.put(Const.ID, fileid);
						fileMessage.put(Const.BODY, "http://app.contact2w.com/uploads/chatfiles/" + uploadpath);
						fileMessage.put(Const.SENT, sent);
						Message newMessage = new Message();
						newMessage.setBody(fileMessage.toString());
						newMessage.setSubject("Text");
						newMessage.setType(Type.chat);
						
						if( m_Chat != null )
							m_Chat.sendMessage(newMessage);   
									
					}
					catch (NotConnectedException e) {
					      // TODO Auto-generated catch block
					      e.printStackTrace();
					} catch(Exception e) {
						e.printStackTrace();
					}
					
				}
			});
			
			
//			new BackgroundTaskUtils(new OnTaskProgress() {
//				boolean ok = true;
//				@Override
//				public void onProgress() {
//					Log.e(Const.SMACK, "File send start: ");
//					FileTransfer transfer = ChatController.sendFile((Context)view, m_ToUsername, m_ToDomain, path, type + "");
//					long filesize = transfer.getFileSize();
//					if( filesize <= 0 )
//						return;
//					
//					long st = System.currentTimeMillis();
//					while (!transfer.isDone() || (transfer.getProgress() < 1)) {
////			        	setProgressState(progress_id, (int)(transfer.getProgress() * 100));
//		    			
//		 				try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//		 				
//		 				long current_prog = (long)(transfer.getProgress() * filesize);
//		 				Log.e(Const.SMACK, "File send size filesize: " + current_prog);
//			            if (transfer.getStatus().equals(org.jivesoftware.smackx.filetransfer.FileTransfer.Status.error)) {
//			            	transfer.cancel();
//			            	ok = false;
//			            	Log.e(Const.SMACK, "File send error filesize: " + current_prog);
//			                break;
//			            }	
//			            
//			            long gap = System.currentTimeMillis() - st;
//			            if( filesize * 10 < gap && gap > 20000 ) // < 0.1Kb/s
//			            {
//			            	transfer.cancel();
//			            	ok = false;
//			            	Log.e(Const.SMACK, "File send not progress: timeout = " + (System.currentTimeMillis() - st) + " filesize: " + current_prog);
//			                break;
//			            }
//			        } 					
//			        
//			        if( transfer.isDone() == false )
//			        	ok = false;
//				}
//				
//				@Override
//				public void onFinished() {
//					int sent = 2;
//					if( ok == false )
//						sent = 0;
//					
//					JSONObject data = new JSONObject();
//					try {
//						data.put(Const.ID, id);
//						data.put(Const.SENT, sent);
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//					DBManager.setFlagSendChat((Context)view, data, sent);
//					
//					Intent intent = new Intent(Const.UPDATE_MESSAGE_ACTION);
//					intent.putExtra(Const.EXTRA_MESSAGE, data.toString());
//					((Context)view).sendBroadcast(intent);					
//				}
//			}).execute();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
//		SendFileTransferTask task = new SendFileTransferTask(((Context)view).getApplicationContext(), (int)id, path, m_ToUsername, m_ToDomain, type);
//		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
//			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//		else
//			task.execute();
		
//		if( sent == 0)
//			m_bUnSendMessage = true;
//		else
//			m_bUnSendMessage = false;
	}
	
	public void onReceiveMessage(String newMessage)
	{
		try {
			JSONObject data = new JSONObject(newMessage);
			
			String from = ChatController.getUsername(data.optString(Const.TO, ""));
			String chatUser = ChatController.getUsername(m_ToUsername);
			
			int count = DBManager.getUnreadMessageCount((Context)view, ChatController.getUsername(), false);
			
			if( from.equals(chatUser) == false )	// other user 
			{
				view.displayUnreadMessageCount(count);
				return;
			}
			
			int group_type = data.optInt(Const.GROUP_TYPE, 0);
			if( group_type != 0 )
			{
				view.displayUnreadMessageCount(count);
				return;
			}
			
			DBManager.setFlagReadChat((Context)view, data);
			
			view.addChat(data);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getChatState()
	{
		Presence.Type state = ChatController.getUserState(m_ToUsername + "@" + m_ToDomain);
		JSONObject contact = DBManager.getContact((Context)view, m_ToUsername, 0);

		return contact.optString("mobile", "");
//		String role = "Doctor";
//		if( contact != null )
//		{
//			int roleType = contact.optInt(Const.ROLE, 0);
//			if( roleType != 1 )
//				role = "Patient";
//		}
//		
//		String onlineState = "";
//		if( state == Presence.Type.unavailable)
//			onlineState = "offline";
//		else if( state == Presence.Type.available )
//			onlineState = "online";
//		else
//			onlineState = "offline";
//		
//		return onlineState;
	}
	
	public String getDisplayName()
	{
//		String nickname = Contact2WUtils.getDisplayName(m_ChatUserInfo);
		String nickname = SoSoUtils.getDisplayName(m_ChatUserInfo);
		return nickname;
	}

}
