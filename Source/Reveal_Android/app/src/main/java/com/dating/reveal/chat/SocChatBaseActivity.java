package com.dating.reveal.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;

import com.dating.reveal.main.BaseView;

import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.ActionUtils;
import com.dating.reveal.commom.ActivityManager;
import com.dating.reveal.utility.DataUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class SocChatBaseActivity extends Activity implements ServiceConnection, BaseView {
	protected ProgressDialog progressDialog = null;

	boolean m_bActivityForground = false;

	protected Handler mMessageHandle = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			processMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityManager.getInstance().pushActivity(SocChatBaseActivity.this);
		setBackgroundColor(Color.WHITE);
		initProgress();

		hideKeyboardTouchOutSideEditBox();

//		getApplicationContext().bindService(new Intent(this, ChatService.class), this, BIND_AUTO_CREATE);
	}


	protected void loadComponents()
	{
		findViews();
		layoutControls();
		initData();
		initEvents();
	}

	protected void findViews()
	{

	}

	protected void layoutControls()
	{

	}

	protected void initData()
	{

	}

	protected void initEvents()
	{

	}
	protected void hideKeyboardTouchOutSideEditBox()
	{
		FrameLayout rootView = (FrameLayout) findViewById(android.R.id.content);
		ActionUtils.hideKeyboardOutSideEditBox(rootView, this);
	}

	protected void setBackgroundColor( int color )
	{
		View view = getWindow().getDecorView();
	    view.setBackgroundColor(color);

	}

	public void sendMessage(Message msg)
	{
		mMessageHandle.sendMessage(msg);
	}

	protected void sendMessageDelayed(Message msg, long delayMillis )
	{
		mMessageHandle.sendMessageDelayed(msg, delayMillis);
	}

	protected void onFinishActivity()
	{
		ActivityManager.getInstance().popActivity();
	}
	@Override
	public void onBackPressed( ) {
		onFinishActivity();
	}


	protected void processMessage(Message msg)
	{
		switch( msg.what )
		{

		}
	}

	@Override
	public void initProgress() {
       progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
	}

	@Override
	public void showProgress(String title, String message) {
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		if(progressDialog.isShowing()){
			return;
		}
		progressDialog.show();

	}

	@Override
	public void changeProgress(String title, String message) {
		if( progressDialog.isShowing() == false )
			return;

		progressDialog.setTitle(title);
		progressDialog.setMessage(message);

	}

	@Override
	public void hideProgress() {
		progressDialog.dismiss();
	}

	@Override
	public void finishView() {
		onFinishActivity();
	}


	@Override
	public void onServiceConnected(ComponentName arg0, IBinder arg1) {
//		if (ChatService.class.getName().equals(arg0.getClassName())) {
//            mChatServiceInterface = (ChatServiceInterface) arg1;
//            onServiceConnected();
//        }
	}


	@Override
	public void onServiceDisconnected(ComponentName arg0) {
//		if (ChatService.class.getName().equals(arg0.getClassName())) {
//            mChatServiceInterface = null;
//            onServiceDisconnected();
//        }
	}

    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }

    protected void showLoadingProgress()
    {
    	showProgress("", "Please wait...");
    }

	protected void onResume( ) {
		super.onResume();

		m_bActivityForground = true;
	}

	protected void onPause( ) {
		m_bActivityForground = false;
		super.onPause();
	}

    protected void quitProgram()
    {
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
		alert_confirm.setMessage("Do you want to quit this program?").setCancelable(false).setPositiveButton("OK",
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	ActivityManager.getInstance().popAllActivity();
		    }
		}).setNegativeButton("Cancel",
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        return;
		    }
		});
		AlertDialog alert = alert_confirm.create();
		alert.show();
    }
}
