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

public class ReceiveFileTransferTask extends AsyncTask<Void, Void, Void> {
//	private static JSONObject state = new JSONObject();
	
	Context context = null;
	FileTransfer transfer = null;
	int progress_id = -1;
	boolean ok = true;
	public ReceiveFileTransferTask(Context context, int id, FileTransfer transfer) {
		this.context = context;
		progress_id = id;
		this.transfer = transfer; 
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
	
	public void waitFileTransferDone()
	{
		try {
			if( transfer == null )
			{
				ok = false;
				return;
			}
			
			long filesize = transfer.getFileSize();
			if( filesize <= 0 )
			{
				ok = false;
				return;
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
	        	return;
	        
//	        setProgressState(progress_id, 100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected Void doInBackground(Void... params) {
		waitFileTransferDone();
		
		return null;		
	}	
	
	public void notifyTransferResult()
	{
		int sent = 2;
		if( transfer == null )
			sent = 0;
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
	@Override
   	protected void onPostExecute(Void result)
   	{
		super.onPostExecute(result);
		
		notifyTransferResult();
   	}
	
}
