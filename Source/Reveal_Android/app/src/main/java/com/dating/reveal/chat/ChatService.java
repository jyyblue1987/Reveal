package com.dating.reveal.chat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PresenceListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.sasl.SASLErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.util.Collection;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.dating.reveal.utility.CheckUtils;
import com.dating.reveal.utility.DataUtils;

public class ChatService extends Service {
	private static final String LOGIN_OK = "of_login";
	private static final String USER_NAME = "of_user";
	private static final String PASSWORD = "of_pw";
	
//	private String m_ServerAddress = "192.168.1.68";
	private String m_ServerAddress = "192.168.1.245";
//	private String m_ServerAddress = "192.168.1.253";
//	private String m_ServerAddress = "vps.ping2w.com";
	
	public static final String CHAT_RESOURCE = "tijarti";

	Handler handler = null;

	
	private boolean loop = false;
	AbstractXMPPConnection mConnection = null;
	
    protected ChatServiceInterface mChatServiceInterface = new ChatServiceInterface();
    
	public class ChatServiceInterface extends Binder {
		public boolean login(String username, String password, ResultCallBack callback)
		{
			loginToChatServer(username, password, callback);
			return true;
		}
		public boolean logout()
		{
			logoutFromChatServer();
			return true;
		}	
    }
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(Const.SMACK, "chat service is created");
		ChatController.setConnectState(Const.NOT_CONNECTED);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// 
		return mChatServiceInterface;
	}
	
	BroadcastReceiver mNotificationMessageReceiver = new NotificationReceiver();
	@Override
	public int onStartCommand(Intent intent, int flags, int startId )
	{
		Log.e(Const.SMACK, "chat service is started");
		
		registerReceiver(mNotificationMessageReceiver, new IntentFilter(Const.NOTIFICATON_RECEIVE_ACTION));
		
		ChatController.onStartedService(this);
		
		loop = false;
		
		Runnable r = new Runnable() {
		    public void run() {
				connectChatServer(m_ServerAddress, true);
		    }
		};
	    Thread mythread = new Thread(r);
	    mythread.start();

		return START_STICKY;
	}
	
    @Override
    public void onDestroy() {
    	Log.e(Const.SMACK, "chat service is stoped");
    	
    	disconnectChatServer();
    	ChatController.onStopService();
    	
        super.onDestroy();
    }
    
    private SSLContext getSSLContext()
    {
    	SASLAuthentication.blacklistSASLMechanism("PLAIN");
     	
    	SSLContext sc = null;
    	KeyStore trustStore;
    	try {
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	            trustStore = KeyStore.getInstance("AndroidCAStore");
	        } else {
	            trustStore = KeyStore.getInstance("BKS");
	        }
	        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	        trustManagerFactory.init(trustStore);
	        
	        sc = SSLContext.getInstance("TLS");
	    	sc.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
	    	return sc;
    	} catch(KeyStoreException e ){
    		e.printStackTrace();
    	} catch (java.security.GeneralSecurityException e) {
    		e.printStackTrace();
    	}
         
    	return sc;
    
    }
    
    public static void removeConnectInfo()
    {
		DataUtils.savePreference(LOGIN_OK, 0);
		DataUtils.savePreference(USER_NAME, "");
		DataUtils.savePreference(PASSWORD, "");
    }
    private synchronized void connectChatServer(final String server, boolean login)
    {
    	if( CheckUtils.isEmpty(server) )
    		return;
    	
		int loginok = DataUtils.getPreference(LOGIN_OK, 0);
		String username = DataUtils.getPreference(USER_NAME, "");
		String password = DataUtils.getPreference(PASSWORD, "");

    	if( isConnectedServer() )	// already connected
    	{
    		if( mConnection.getHost().equals(server) ) // equal server address
    		{
    			if( loginok == 1 && CheckUtils.isNotEmpty(loginok) && CheckUtils.isNotEmpty(password) && login == true)
    			{
    				Log.e(Const.SMACK, "try to login chat server automatically");
    				doLoginChatServer(username, password);
    			}
    			
    			return;
    		}
    		
    		mConnection.disconnect();    		
    		Log.e(Const.SMACK, "disconnect chat server.");
    	}
    	
    	ChatController.setConnectState(Const.NOT_CONNECTED);
    	
    	SmackConfiguration.DEBUG = false;
    	XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				  .setServiceName(server)						  
				  .setHost(server)
				  .setPort(5222)				  
				  .setSecurityMode(SecurityMode.disabled)				
//				  .setCustomSSLContext(getSSLContext())
				  .build();
    	
		mConnection = new XMPPTCPConnection(config);
		
		ChatController.setConnection(mConnection);
		
		ReconnectionManager reconnectManager = ReconnectionManager.getInstanceFor(mConnection);
		reconnectManager.enableAutomaticReconnection();
		
		PingManager pingManager = PingManager.getInstanceFor(mConnection); 
		pingManager.setPingInterval(60);
		pingManager.registerPingFailedListener(pingFailListener);
		
		if( mConnection == null )
			return;
		
		loop = true;
		while(loop)
		{
			try {
				Log.e(Const.SMACK, "try to connect chat server");
				mConnection.setPacketReplyTimeout(3000);
	            mConnection.addConnectionListener(connectionListener);
				mConnection.connect();
				
				if( loginok == 1 && CheckUtils.isNotEmpty(loginok) && CheckUtils.isNotEmpty(password) && login == true )
				{
					Log.e(Const.SMACK, "try to login chat server automatically");
					doLoginChatServer(username, password);					
				}
				break;
			} catch (SmackException e) {
				e.printStackTrace();
				Log.e(Const.SMACK, e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(Const.SMACK, e.getMessage());
			} catch (XMPPException e) {
				e.printStackTrace();
				Log.e(Const.SMACK, e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(Const.SMACK, e.getMessage());
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
    			
    }
    
    private boolean isConnectedServer()
    {
    	if( mConnection == null )
    		return false;
    	
    	return mConnection.isConnected();
    }
    
    private boolean isChatAvailable()
    {
    	if( isConnectedServer() == false )
    		return false;
    	
    	if( mConnection.isAuthenticated() == false )
    		return false;
    	
    	return true;
    }
    
    private void disconnectChatServer()
    {
    	if( mConnection == null )
    		return;
    	
    	mConnection.disconnect();
    }
    
    Thread m_loginTask = null;
    public void loginToChatServer(final String username, final String password, final ResultCallBack callback )// to thread
    {
    	loop = false;
    	
    	if( m_loginTask != null )
    		m_loginTask.stop();
    	
		Runnable r = new Runnable() {
			boolean m_LoginOK = false;

			public void run() {
				m_LoginOK = doLoginChatServer(username, password);
				LogicResult result = new LogicResult();
				if( m_LoginOK == true )
					result.mResult = LogicResult.RESULT_OK;
				else
					result.mResult = LogicResult.RESULT_FAIL;
				
				if( callback != null )
					callback.doAction(result);
				m_loginTask = null;

		    }
		};
		m_loginTask = new Thread(r);
		m_loginTask.start();
    }
    
    public void logoutFromChatServer()
    {
    	loop = false;
    	if( m_loginTask != null )
    		m_loginTask.stop();
    		
    	if( isConnectedServer() == false )
    	{
    		removeConnectInfo();
    		return;
    	}
    	mConnection.disconnect();
    	removeConnectInfo();
    }
    
    private boolean doLoginChatServer(String username, String password)
    {
    	if( isConnectedServer() == false )
			connectChatServer(m_ServerAddress, false);
		
		ChatController.onStartedLogin();
		
		if( mConnection.isAuthenticated() )
		{
			Log.e(Const.SMACK, "Account is already logined");
			createChatListeners();
			loadMyInfo();
			return true;
		}
		
		loop = true;
		while(loop)
		{
			try {
				Log.e(Const.SMACK, "try to login chat server");
				mConnection.login(username, password, CHAT_RESOURCE);
				
				DataUtils.savePreference(LOGIN_OK, 1);
				DataUtils.savePreference(USER_NAME, username);
				DataUtils.savePreference(PASSWORD, password);
				
				return true;
			} catch (SmackException e) {
				e.printStackTrace();
				if( e.getMessage().contains("No non-anonymous SASL authentication mechanism available") )
				{
					mConnection.disconnect();					
					break;
				}
				Log.e(Const.SMACK, e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(Const.SMACK, e.getMessage());
			} catch (SASLErrorException e) {
				removeConnectInfo();
				e.printStackTrace();
				Log.e(Const.SMACK, e.getMessage());
			} catch (XMPPException e) {
				e.printStackTrace();
				Log.e(Const.SMACK, e.getMessage());
			}
			
			if( mConnection.isAuthenticated() )
				break;
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		
		return false;
    }
       
   private void loadMyInfo()
   {
	   ChatController.loadMyInfo();
   }
    
   private void createChatListeners()
   {
    	createRosterListener();
		createIncomingChatListener();
		createFileTranformListener();
		createMultiUserChatInviteListener();
   }
    
    private void createIncomingChatListener()
    {
    	if( isChatAvailable() == false )
    		return;
    	
    	ChatManager chatmanager = ChatManager.getInstanceFor(mConnection);
    	
    	chatmanager.addChatListener( new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createdLocally)
			{
				Log.e(Const.SMACK, "chat session is created: " + chat.toString() );
				if (!createdLocally)
					chat.addMessageListener(chatListener);
				
			}
		});
    	
    }
    
    private void createRosterListener()
    {
    	if( isChatAvailable() == false )
    		return;

       	Roster roster = Roster.getInstanceFor(mConnection);
    	
    	roster.addRosterListener(rosterListener);    
    }
    
    private void createFileTranformListener()
    {
    	if( isChatAvailable() == false )
    		return;
    	
    	// Create the file transfer manager
    	FileTransferManager manager = FileTransferManager.getInstanceFor(mConnection);
    	FileTransferNegotiator.IBB_ONLY = true;
    	
    	manager.addFileTransferListener(new FileTransferListener() {
			
			@Override
			public void fileTransferRequest(FileTransferRequest request) {
				Log.e(Const.SMACK, "receive file: " + request.toString() );
				ChatController.onRequestFileReciver(ChatService.this, request);		
			}
		});
    	
    }
    
    private void createMultiUserChatInviteListener()
    {
    	if( isChatAvailable() == false )
    		return;
    	
    	MultiUserChatManager.getInstanceFor(mConnection).addInvitationListener(new InvitationListener() {

			@Override
			public void invitationReceived(XMPPConnection conn, MultiUserChat muc, String room, String inviter, String reason, Message message) {
				Log.e(Const.SMACK, inviter + " invite you in room: " + room);
				ChatController.invitationReceived(conn, muc, room, inviter, reason, message);
			} 			
    	});
    }
    
    ConnectionListener connectionListener = new ConnectionListener() {
		
		@Override
		public void reconnectionSuccessful() {
			Log.e(Const.SMACK, "chat server is reconnect.");
			ChatController.setConnectState(Const.CONNECTED);
		}
		
		@Override
		public void reconnectionFailed(Exception arg0) {
			Log.e(Const.SMACK, "reconnect is failed.");
			ChatController.setConnectState(Const.NOT_CONNECTED);			
		}
		
		@Override
		public void reconnectingIn(int arg0) {
			Log.e(Const.SMACK, "Now is reconnecting.");
			ChatController.setConnectState(Const.CONNECTING);
		}
		
		@Override
		public void connectionClosedOnError(Exception arg0) {
			Log.e(Const.SMACK, "Connect is closed. reason: " + arg0.getMessage());
			ChatController.setConnectState(Const.NOT_CONNECTED);
		}
		
		@Override
		public void connectionClosed() {
			Log.e(Const.SMACK, "Connect is closed sucessfully");
			ChatController.setConnectState(Const.NOT_CONNECTED);
		}
		
		@Override
		public void connected(XMPPConnection arg0) {
			Log.e(Const.SMACK, "Server is connected successfully");
			ChatController.setConnectState(Const.CONNECTED);
		}
		
		@Override
		public void authenticated(XMPPConnection arg0, boolean arg1) {
			Log.e(Const.SMACK, "account is authenticated successfully");
			createChatListeners();
			loadMyInfo();

			ChatController.setConnectState(Const.AUTHENTICATED);
			
		}
	};
	
	RosterListener rosterListener = new RosterListener() {
		
		@Override
		public void presenceChanged(Presence arg0) {
			Log.e(Const.SMACK, "Presence is changed: " + arg0.toXML());
			ChatController.presenceChanged(ChatService.this, arg0);
		}
		
		@Override
		public void entriesUpdated(Collection<String> arg0) {
			Log.e(Const.SMACK, "Entries are updated: " + arg0.toString());
			ChatController.entriesUpdated(ChatService.this, arg0);			
		}
		
		@Override
		public void entriesDeleted(Collection<String> arg0) {
			Log.e(Const.SMACK, "Entries are deleted: " + arg0.toString());
			ChatController.entriesDeleted(ChatService.this, arg0);			
		}
		
		@Override
		public void entriesAdded(Collection<String> arg0) {
			Log.e(Const.SMACK, "Entries are added: " + arg0.toString());
			ChatController.entriesAdded(ChatService.this, arg0);			
		}
	};
	
	public ChatMessageListener getMessageListener()
	{
		return chatListener;
	}
	
	ChatMessageListener chatListener = new ChatMessageListener() {
		
		@Override
		public void processMessage(Chat arg0, Message arg1) {
			Log.e(Const.SMACK, "Message is received: " + arg1.toXML());
			ChatController.onIncomingMessage(ChatService.this, arg0, arg1);	
		}
	};
	
	public MessageListener getGroupMessageListener()
	{
		return groupListener;
	}
	
	MessageListener groupListener = new MessageListener() {
		
		@Override
		public void processMessage(Message arg0) {
			Log.e(Const.SMACK, "Group message is rececived: " + arg0.toXML());
			ChatController.onIncomingGroupMessage(arg0);
			
		}
	};
	
	
	public PresenceListener getPresenceListener()
	{
		return participantListener;
	}
	
	PresenceListener participantListener = new PresenceListener() {
		
		@Override
		public void processPresence(Presence arg0) {
			Log.e(Const.SMACK, "Group participant presence is changed: " + arg0.toXML());
			ChatController.processPresence(arg0);			
		}
	};
	
	public ParticipantStatusListener getParticipantStatusListener()
	{
		return participantStatusListener;
	}
	
	
	ParticipantStatusListener participantStatusListener = new ParticipantStatusListener() {
		
		@Override
		public void voiceRevoked(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void voiceGranted(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void ownershipRevoked(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void ownershipGranted(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void nicknameChanged(String arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void moderatorRevoked(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void moderatorGranted(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void membershipRevoked(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void membershipGranted(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void left(String arg0) {
			ChatController.left(arg0);			
		}
		
		@Override
		public void kicked(String arg0, String arg1, String arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void joined(String arg0) {
			ChatController.joined(arg0);			
		}
		
		@Override
		public void banned(String arg0, String arg1, String arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void adminRevoked(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void adminGranted(String arg0) {
			// TODO Auto-generated method stub
			
		}
	};

	PingFailedListener pingFailListener = new PingFailedListener() {
		
		@Override
		public void pingFailed() {
			Log.e(Const.SMACK, "Ping is fail");
		}
	};
	
	public class RecieveSMS extends BroadcastReceiver
    {
       @Override
       public void onReceive(Context context, Intent intent)
       {

           Bundle bundle = intent.getExtras();
           SmsMessage[] recievedMsgs = null;
           String str = "";
           if (bundle != null)
           {

               Object[] pdus = (Object[]) bundle.get("pdus");
               recievedMsgs = new SmsMessage[pdus.length];
               for (int i=0; i <  pdus.length; i++ )
               {
            	   recievedMsgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                   str += "SMS from " + recievedMsgs[i].getOriginatingAddress()+ " :" + recievedMsgs[i].getMessageBody().toString();
               }

               Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
           }
       }
   }
}

