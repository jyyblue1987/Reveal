package com.dating.reveal.chat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.dating.reveal.data.DBManager;
import com.dating.reveal.main.Const;

import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.json.JSONException;
import org.json.JSONObject;

import com.dating.reveal.utility.CheckUtils;

public class SendFileTransferTask extends AsyncTask<Void, Void, Void> {
//	private static JSONObject state = new JSONObject();
	
	Context context = null;
	String path = null;
	int progress_id = -1;
	boolean ok = true;
	String username = "";
	String domain = "";
	int type = 0;
	public SendFileTransferTask(Context context, int id, String path, String username, String domain, int type) {
		this.context = context;
		progress_id = id;
		this.path = path + ""; 
		this.username = username + "";
		this.domain = domain + "";
		this.type = type;
//		setProgressState(progress_id, 0);
	}
	
//	public static synchronized void setProgressState(int id, int prog)
//	{
//		try {
//			state.put(id + "", prog);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	public static synchronized int getProgressState(int id)
//	{
//		return state.optInt(id + "", 0);	
//	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			if( CheckUtils.isEmpty(path) )
			{
				ok = false;
				return null;
			}
						
			FileTransfer transfer = ChatController.sendFile(context, username, domain, path, type + "");
			
			long filesize = transfer.getFileSize();
			if( filesize <= 0 )
			{
				ok = false;
				return null;
			}
			
			long st = System.currentTimeMillis();
			long prev_prog = 0;
			while (!transfer.isDone() || (transfer.getProgress() < 1)) {
//	        	setProgressState(progress_id, (int)(transfer.getProgress() * 100));
    			
 				Thread.sleep(1000);
 				
	            if (transfer.getStatus().equals(FileTransfer.Status.error)) {
	            	transfer.cancel();
	            	ok = false;
	            	Log.e(Const.SMACK, "File receive error");
	                break;
	            }	
	            
	            long current_prog = (long)(transfer.getProgress() * filesize);
	            if( System.currentTimeMillis() - st > 100000 &&  current_prog == prev_prog )
	            {
	            	transfer.cancel();
	            	ok = false;
	            	Log.e(Const.SMACK, "File receive not working");
	                break;
	            }
	            
	            if( current_prog > prev_prog )
	            	prev_prog = current_prog;
	        } 					
	        
	        if( transfer.isDone() == false )
	        	return null;
	        
//	        setProgressState(progress_id, 100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;		
	}	
	
	@Override
   	protected void onPostExecute(Void result)
   	{
		super.onPostExecute(result);
		
		int sent = 2;
		if( ok == false )
			sent = 0;
		
		JSONObject data = new JSONObject();
		try {
			data.put(Const.ID, progress_id);
			data.put(Const.SENT, sent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DBManager.setFlagSendChat(context, data, sent);
		
		Intent intent = new Intent(Const.UPDATE_MESSAGE_ACTION);
		intent.putExtra(Const.EXTRA_MESSAGE, data.toString());
        context.sendBroadcast(intent);
   	}
	
}
