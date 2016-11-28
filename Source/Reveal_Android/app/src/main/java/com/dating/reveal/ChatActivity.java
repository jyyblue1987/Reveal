package com.dating.reveal;

/**
 * Created by JonIC on 2016-11-08.
 */

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.text.InputType;
import android.util.Base64;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.reveal.chat.MyChatListAdapter;
import com.dating.reveal.chat.SocChatBaseActivity;
import com.dating.reveal.commom.ActivityManager;
import com.dating.reveal.designs.LayoutUtils;
import com.dating.reveal.designs.ResourceUtils;
import com.dating.reveal.designs.ScreenAdapter;
import com.dating.reveal.main.Const;
import com.dating.reveal.main.MyApplication;
import com.dating.reveal.main.SocChatConst;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.network.ServerTask;
import com.dating.reveal.utility.DataUtils;
import com.dating.reveal.utility.MessageUtils;
import com.dating.reveal.utility.SoftKeyboardHandledLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by JonIC on 2016-10-07.
 */
public class ChatActivity extends SocChatBaseActivity {
    // block button
    Button m_btn_block;

    // header button
    Button mbtnLeft = null;
    Button mbtnRight = null;
    TextView mCenter = null;
    ImageView mCenterImage = null;
    //    ImageView       m_imgWhisper;
    Boolean			m_bWhisper = false;
    String			m_strWhisperUser="";

    Boolean			m_bExitActivity = false;

    ListView m_ChatListView = null;
    //        MyListAdapter    m_adapterMsgList = null;
    MyChatListAdapter m_adapterMsgList = null;

    List<JSONObject> m_arrChatMsgs = null;

    Button m_btnSend;
    //    EmojiconEditText m_EmojiconEditText = null;
    EditText m_EmojiconEditText = null;
    ImageView 		m_EmojiButton = null;

    ImageView m_imgMessageVoice = null;
    Button    m_btnVoice = null;

    String			m_strLivePath = "";

    int 			m_nIndex = 0;
    List<JSONObject> m_arrUsers = new ArrayList<JSONObject>();

    JSONArray m_arrAllUsers=null;

    TextView        m_textEndLive;
    Boolean			m_bPlaying = false;
    Boolean			m_bShowKeyboard = false;

    Button 			m_btnCameraSwitch;
    SurfaceView m_surfaceview;
    //listview play and stop image link
    ImageView play = null;
    ImageView stop = null;

    String not;
    String nots;

    //voice chat related variable
    Button b1;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FILE_EXT_AAC = ".aac";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP};
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_AAC, AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP};

    MediaPlayer mPlayer=null;
    String m_fileName = null;
    String m_strAudioUpload=null;
    Socket mSocket=null;
    PowerManager.WakeLock wl;
    // download related variable
    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private Uri urlToDownload;
    long latestId = 0;

    // chating user
    String mChatUser = "";
    String[] userList = null;
    // header part views
    ImageView m_img_three;
    ImageView m_img_back_arrow;
    TextView m_txt_app_title;
    ImageView m_img_bell;
    Button m_btn_notify;
    RelativeLayout m_layLeft = null;
    RelativeLayout m_layRight = null;
    @Override
    protected void onResume(){
        super.onResume();
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

    }
    protected void getNotificationsize(){
        String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        ServerManager.getNotificationSize(facebookid, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userinfo = result.getData();
                    JSONArray size = userinfo.optJSONArray("content");
                    JSONObject json = size.optJSONObject(0);
                    String notify_count = json.optString("count(*)", "");
                    if(notify_count.equals("0")){
                        m_btn_notify.setText("");
                        m_btn_notify.setVisibility(View.INVISIBLE);
                    }else{
                        m_btn_notify.setText(notify_count);
                        m_btn_notify.setVisibility(View.VISIBLE);
                    }

                }

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        Bundle intent = getIntent().getExtras();
        mChatUser = intent.getString("person");
        //
        parsemChatUser();
//        MessageUtils.showMessageDialog(this,mChatUser);
        not = DataUtils.getPreference(Const.NOTIFICATION_CONTENT, "");
        nots = DataUtils.getPreference(Const.NOTIFICATION_SIZE,"");

        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        setContentView(R.layout.soc_chat_activity_golive);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        initSocket();

        DataUtils.savePreference(Const.NOTIFICATION_CONTENT, not);
        DataUtils.savePreference(Const.NOTIFICATION_SIZE,nots);
        loadComponents();
        showLabels();
        getNotificationsize();
//        checkblock();
    }

    // check the match is blocked.
    protected void checkblock(){
        JSONObject json;
        String matchfaceboolid;
        try{
            json = new JSONObject(mChatUser);
//            JSONObject j = json.optJSONObject(2);
            matchfaceboolid = json.optString("facebookid");

        }catch (JSONException e){
            Toast.makeText(ChatActivity.this, "Sorry, could not identify this match", Toast.LENGTH_LONG).show();
            return;
        }

        // show progressdialog

        final ProgressDialog progressDialog ;
        progressDialog = new ProgressDialog(ChatActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        ServerManager.checkblock(myfacebookid, matchfaceboolid, new ResultCallBack(){
            @Override
            public void doAction(LogicResult result) {
                progressDialog.dismiss();
                if(result.mResult == LogicResult.RESULT_OK){

                }else if(result.mResult == 301){
                    // if this user is not blocked or block you then you can chat with that match.
                    JSONObject userinfo = result.getData();
                    JSONObject data = userinfo.optJSONObject("content");
                    String blockmessage = data.optString("blocksort");
                    String blocksort  = "This user is blocked for \"" + blockmessage + "\"";
                    Toast.makeText(ChatActivity.this, blocksort, Toast.LENGTH_LONG).show();
                    finish();

                    Toast.makeText(ChatActivity.this, "Sorry. Server is not responding", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ChatActivity.this,"Server Error", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    };

    public void setStatusBarColor(View statusBar,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int actionBarHeight = getActionBarHeight();
            int statusBarHeight = getStatusBarHeight();
            //action bar height
            statusBar.getLayoutParams().height = statusBarHeight;
            statusBar.setBackgroundColor(color);
        }
    }
    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    protected void findViews(){
//        mbtnLeft =(Button)((View)findViewById(R.id.fragment_chat)).findViewById(R.id.fragment_header).findViewById(R.id.btn_left_button);
//        mCenter = (TextView)((View)findViewById(R.id.fragment_chat)).findViewById(R.id.fragment_header).findViewById(R.id.txt_navigate_bar_title);
//        mbtnRight = (Button)((View)findViewById(R.id.fragment_chat)).findViewById(R.id.fragment_header).findViewById(R.id.btn_right_button);
//        mCenterImage = (ImageView) ((View)findViewById(R.id.fragment_chat)).findViewById(R.id.fragment_header).findViewById(R.id.img_header_center);
        m_layLeft = (RelativeLayout) findViewById(R.id. fragment_header).findViewById(R.id.lay_left);
        m_layRight = (RelativeLayout) findViewById( R.id. fragment_header). findViewById(R.id.lay_right);
        m_img_three = (ImageView) findViewById( R.id.fragment_header) .findViewById(R.id.img_left_three);
        m_img_back_arrow = (ImageView) findViewById( R.id.fragment_header).findViewById(R.id.img_back_arrow);
        m_txt_app_title = (TextView) findViewById(R.id.fragment_header). findViewById(R.id.txt_app_title);
        m_img_bell = (ImageView) findViewById(R.id.fragment_header). findViewById(R.id.img_right_bell);
        m_btn_notify = (Button) findViewById(R.id.fragment_header).findViewById(R.id.btn_notification);


        m_ChatListView = (ListView)this.findViewById(R.id.fragment_chat).findViewById(R.id.listChat);
        m_btnSend = (Button)this.findViewById(R.id.fragment_chat).findViewById(R.id.fragment_chatinput).findViewById(R.id.btnSend);
        m_EmojiconEditText = (EditText) this.findViewById(R.id.fragment_chat).findViewById(R.id.fragment_chatinput).findViewById(R.id.img_edit_text);
        m_EmojiconEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | ~(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT));
        m_EmojiButton = (ImageView) this.findViewById(R.id.fragment_chat).findViewById(R.id.fragment_chatinput).findViewById(R.id.img_message_text);

        m_imgMessageVoice=(ImageView)this.findViewById(R.id.fragment_chat).findViewById(R.id.fragment_chatinput).findViewById(R.id.img_message_voice);
        m_btnVoice = (Button)this.findViewById(R.id.fragment_chat).findViewById(R.id.fragment_chatinput).findViewById(R.id.btn_edit_voice);


//        m_imgWhisper = (ImageView)this.findViewById(R.id.fragment_chat).findViewById(R.id.fragment_chatinput).findViewById(R.id.btnWhisper);

        m_surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview);

        m_btnCameraSwitch = (Button) findViewById(R.id.btn_camera_switch);

        m_textEndLive = (TextView)this.findViewById(R.id.textEndLive);

        m_btn_block = (Button)findViewById(R.id.btn_block);
    }
    protected void layoutControls(){
//        LayoutUtils.setSize(mCenterImage, 80, 80, true);
        LayoutUtils.setSize(m_layLeft, 160, 140, true);
        LayoutUtils.setSize(m_layRight, 160, 140, true);

        m_btn_notify.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));
        LayoutUtils.setSize(m_img_bell, 100, 100, true);
        LayoutUtils.setSize(m_btn_notify, 60, 60, true);

//		LayoutUtils.setSize(m_txtNotify, 53, 53, true);
//		m_txtNotify.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(44));
//		LayoutUtils.setMargin(m_txtNotify, 90, 60, 0, 0, true);

        m_txt_app_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(60));


    }
    protected void initData(){
        String notify_count = DataUtils.getPreference(Const.NOTIFICATION_SIZE, "");
        if(notify_count.equals("0")){
            m_btn_notify.setText("");
            m_btn_notify.setVisibility(View.INVISIBLE);
        }else{
            m_btn_notify.setText(notify_count);
            m_btn_notify.setVisibility(View.VISIBLE);
        }
        m_img_back_arrow.setVisibility(View.VISIBLE);
        m_img_three.setVisibility(View.INVISIBLE);
    }


    protected void showLabels(){
//        mCenterImage.setVisibility(View.VISIBLE);
//        mCenterImage.setImageResource(R.drawable.chatting_icon_2);
//        mCenter.setVisibility(View.INVISIBLE);
//        mbtnRight.setVisibility(View.INVISIBLE);
    }

    private void initSocket(){
        MyApplication app = (MyApplication)this.getApplication();
        mSocket = app.getSocket();
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(SocChatConst.USER_LIST, onUserList);
        mSocket.on(SocChatConst.USER_JOIN, onUserJoin);
        mSocket.on(SocChatConst.USER_LEFT, onUserLeft);
        mSocket.on(SocChatConst.CACHED_MSG,onCachedMSG);
        mSocket.on(SocChatConst.WHISPER, onWhisper);
        mSocket.on(SocChatConst.WHISPER_REQ, onWhisperREQ);
        mSocket.on(SocChatConst.WHISPER_ACCEPT, onWhisperAccept);
        mSocket.on(SocChatConst.WHISPER_CANCEL,onWhisperCancel);

        mSocket.on(SocChatConst.CHAT, onChat);
        mSocket.on("chat_history",onCachedMSG);
        mSocket.connect();
        String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        mSocket.emit("joinRoom",facebookid);
        DataUtils.savePreference(Const.NOTIFICATION_CONTENT, not);
        DataUtils.savePreference(Const.NOTIFICATION_SIZE,nots);

        JSONObject data = new JSONObject();
        try {
            data.put("name", facebookid);
            data.put("receiver", userList[0]);
            mSocket.emit("chat_history",data);
        }catch (JSONException e){

        }
        DataUtils.savePreference(Const.NOTIFICATION_CONTENT, not);
        DataUtils.savePreference(Const.NOTIFICATION_SIZE,nots);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(SocChatConst.USER_LIST, onUserList);
        mSocket.off(SocChatConst.USER_JOIN, onUserJoin);
        mSocket.off(SocChatConst.USER_LEFT, onUserLeft);
        mSocket.off(SocChatConst.CACHED_MSG,onCachedMSG);
        mSocket.off(SocChatConst.WHISPER, onWhisper);
        mSocket.off(SocChatConst.WHISPER_REQ, onWhisperREQ);
        mSocket.off(SocChatConst.WHISPER_ACCEPT, onWhisperAccept);
        mSocket.off(SocChatConst.WHISPER_CANCEL,onWhisperCancel);
        mSocket.off(SocChatConst.CHAT, onChat);


    }
    private  Emitter.Listener onChat = new Emitter.Listener(){
        @Override
        public void call(Object... args){
            if( args[0] instanceof JSONObject )
            {
                final JSONObject msg = (JSONObject)args[0];

                runOnUiThread(new Runnable() {
                    public void run() {
                        addMessage(msg);
                    }
                });
            }
        }
    };
    private Emitter.Listener onWhisperCancel = new Emitter.Listener(){
        @Override
        public void call(Object... args){
            if( args[0] instanceof JSONObject )
            {
//                final JSONObject msg = (JSONObject)args[0];
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        cancelWhisper(msg);
//                    }
//                });
            }
        }
    };
    private Emitter.Listener onWhisperAccept = new Emitter.Listener(){
        @Override
        public void call(Object... args){
            if( args[0] instanceof JSONObject )
            {
//                final JSONObject msg = (JSONObject)args[0];
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        acceptWhisper(msg);
//                    }
//                });
            }
        }
    };
    private Emitter.Listener onWhisperREQ = new Emitter.Listener(){
        @Override
        public void call(Object... args){
            if( args[0] instanceof JSONObject )
            {
//                final JSONObject msg = (JSONObject)args[0];
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        requestWhisper(msg);
//                    }
//                });
            }

        }
    };
    private Emitter.Listener onWhisper = new Emitter.Listener(){
        @Override
        public void call(Object... args){
            if( args[0] instanceof JSONObject )
            {
                final JSONObject msg = (JSONObject)args[0];
                String strName = msg.optString("name", "");
                if ( strName.length() > 0 )
                {
                    strName = getResources().getString(R.string.whisperfrom) + strName;
                    try {
                        msg.put("name", strName);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        addMessage(msg);
                    }
                });
            }

        }
    };
    private Emitter.Listener onCachedMSG =new Emitter.Listener(){
        @Override
        public void call(Object... args){
            if( args[0] instanceof JSONObject )
            {
                final JSONObject arrMsg = (JSONObject)args[0];

                runOnUiThread(new Runnable() {
                    public void run() {
                        addMsgHistory(arrMsg);
                    }
                });
            }

        }
    };
    private Emitter.Listener onUserLeft = new Emitter.Listener(){
        @Override
        public void call(Object... args){
            if( args[0] instanceof String )
            {
                final String strUserName = (String) args[0];
                runOnUiThread(new Runnable() {
                    public void run() {
                        removeUser(strUserName);
                    }
                });
            }
        }
    };
    private Emitter.Listener onUserJoin = new Emitter.Listener(){
        @Override
        public void call(Object... args){
            if( args[0] instanceof String )
            {
                final String strUserName = (String) args[0];
                runOnUiThread(new Runnable() {
                    public void run() {
                        addUser(strUserName);
                    }
                });
            }

        }
    };

    private Emitter.Listener onUserList = new Emitter.Listener(){
        @Override
        public void call(Object... args){
            if( args[0] instanceof JSONArray )
            {
                final JSONArray arrUsers = (JSONArray)args[0];
                runOnUiThread(new Runnable() {
                    public void run() {
                        getUserList(arrUsers);
                    }
                });
            }

        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener(){
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                        isConnected = false;
                    Toast.makeText(getApplicationContext(),
                            R.string.disconnect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    private Emitter.Listener onConnect = new Emitter.Listener(){
        @Override
        public void call(Object... args) {

//            int intChannel = DataUtils.getPreference(Const.ID,0);//SocChatConst.USER_ID, "");

            try {
                JSONObject item = new JSONObject();
                item.put("channelName",1);//strChannel);

//                String strUserName = DataUtils.getPreference(SocChatConst.NICKNAME, "");
                String strUserName = DataUtils.getPreference(Const.USERNAME, "");
                if ( strUserName != null ){
                    String strUserName64 = strUserName;//EncodeTo64(strUserName);
                    item.put("userName",strUserName64);// "jerry");
                }

                mSocket.emit(SocChatConst.JOIN, item);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    protected void gotoMatchMessagePage(){
        DataUtils.savePreference(Const.NOTIFICATION_CONTENT, not);
        DataUtils.savePreference(Const.NOTIFICATION_SIZE,nots);


        Bundle bundle = new Bundle();
        Intent intent = new Intent(ChatActivity.this, MatchsMessageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        finish();
    }

    protected void  gotoNotification(){
        Bundle bundle = new Bundle();

        String noti = DataUtils.getPreference(Const.NOTIFICATION_CONTENT,"");
        String notisize = DataUtils.getPreference(Const.NOTIFICATION_SIZE,"");

        DataUtils.savePreference(Const.NOTIFICATION_CONTENT, not);
        DataUtils.savePreference(Const.NOTIFICATION_SIZE,nots);

        Intent intent = new Intent(ChatActivity.this, NotificationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        finish();

    }
    // declare event process module.
    protected void initEvents(){
        //
        // bell touch event
        ((ImageView)findViewById(R.id.img_right_bell)).setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    ((ImageView) v.findViewById(R.id.img_right_bell)).setImageResource(R.drawable.icon_bell_grey);

                if(event.getAction() == MotionEvent.ACTION_UP)
                    ((ImageView) v.findViewById(R.id.img_right_bell)).setImageResource(R.drawable.bell);

                return false;
            }
        });

        // bolck the user
        m_btn_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBlockActivity();
            }
        });

        m_img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMatchMessagePage();
            }
        });

        m_img_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNotification();
            }
        });
        resizeChatList();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                resizeChatList();
            }
        }, 100);


        SoftKeyboardHandledLayout mainView = (SoftKeyboardHandledLayout) findViewById(R.id.root_view);
        mainView.setOnSoftKeyboardVisibilityChangeListener(
                new SoftKeyboardHandledLayout.SoftKeyboardVisibilityChangeListener() {
                    @Override
                    public void onSoftKeyboardShow() {
                        // TODO: do something here
                        m_bShowKeyboard = true;
                        int height = ScreenAdapter.getDeviceHeight();
//                        LayoutUtils.setSize(findViewById(R.id.fragment_chat).findViewById(R.id.viewVideo), ViewGroup.LayoutParams.MATCH_PARENT, height*2/7, false);
                        reloadchatList();
                    }

                    @Override
                    public void onSoftKeyboardHide() {
                        // TODO: do something here
                        m_bShowKeyboard = false;
                        int height = ScreenAdapter.getDeviceHeight();
//                        LayoutUtils.setSize(findViewById(R.id.fragment_chat).findViewById(R.id.viewVideo), ViewGroup.LayoutParams.MATCH_PARENT, height*3/5, false);
                        reloadchatList();
                    }
                });

        m_EmojiButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                m_btnVoice.setVisibility(View.VISIBLE);
                m_imgMessageVoice.setVisibility(View.VISIBLE);
                m_EmojiButton.setVisibility(View.INVISIBLE);
                m_EmojiconEditText.setVisibility(View.INVISIBLE);
                m_btnSend.setEnabled(false);
            }
        });

        m_imgMessageVoice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                m_btnVoice.setVisibility(View.INVISIBLE);
                m_imgMessageVoice.setVisibility(View.INVISIBLE);
                m_EmojiButton.setVisibility(View.VISIBLE);
                m_EmojiconEditText.setVisibility(View.VISIBLE);
                m_btnSend.setEnabled(true);
            }
        });

//        m_btnVoice.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        String mStrStart = getFilename();
//                        mStrStart += "start recording";
////                        MessageUtils.showMessageDialog(ChatActiviy.this, mStrStart);
//                        m_btnVoice.setText("Recording...");
////                        AppLog.logString("Start Recording");
//                        startRecording();
//                        break;
//                    case MotionEvent.ACTION_UP:
////                        MessageUtils.showMessageDialog(ChatActiviy.this, "Stop Recording");
////                        AppLog.logString("stop Recording");
//                        m_btnVoice.setText("Start Recording");
//                        stopRecording();
//                        break;
//                }
//                return false;
//            }
//        });

//        mbtnLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gotoMainActivity();
//            }
//        });

//        m_imgWhisper.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                boolean bLogin = DataUtils.getPreference(Const.LOGIN_OK, false);
////                if ( bLogin == false )
////                {
////                    Bundle bundle = new Bundle();
////                    ActivityManager.changeActivity(SocChatActivity.this, LoginActivity.class, bundle, false, null );
////                    return;
////                }
////                else
////                {
////                    if ( socket == null )
////                        connectSocket();
////                }
////                connectSocket();
//
//                Boolean bWhisper = !m_bWhisper;
//                String strName = DataUtils.getPreference(SocChatConst.NICKNAME, "");
////				JSONObject profile = m_userInfo.optJSONObject("profile");
////				String target = profile.optString("nickname");
//                if ( bWhisper == true )
//                {
//                    selectWhisperUser();
//                }
//                else
//                {
//                    m_bWhisper = false;
//                    m_imgWhisper.setImageResource(R.drawable.whisper_d);
//                    JSONObject sendMsg = new JSONObject();
//                    try {
//                        sendMsg.put("target", m_strWhisperUser);
//                        sendMsg.put("sender", strName);
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    mSocket.emit(SocChatConst.WHISPER_CANCEL, sendMsg);
//                    MessageUtils.showMessageDialog(ChatActiviy.this, getResources().getString(R.string.canceledwhisper) +m_strWhisperUser );
//                    m_strWhisperUser = "";
//                }
//            }
//        });

        m_EmojiconEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                boolean bLogin = DataUtils.getPreference(Const.LOGIN_OK, false);
//                if ( bLogin == false )
//                {
//                    Bundle bundle = new Bundle();
//                    ActivityManager.changeActivity(SocChatActivity.this, LoginActivity.class, bundle, false, null );
//                    return;
//                }
//                else
//                {
                if ( mSocket == null )
//                    connectSocket();

                    if ( m_bShowKeyboard == false )
                    {
                        m_bShowKeyboard = true;
                        int height = ScreenAdapter.getDeviceHeight();
//                    LayoutUtils.setSize(findViewById(R.id.fragment_chat).findViewById(R.id.viewVideo), ViewGroup.LayoutParams.MATCH_PARENT, height*2/7, false);
                        reloadchatList();
                    }
            }
//            }
        });

        m_btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                JSONObject item = new JSONObject();
                try {
                    String strMsg = m_EmojiconEditText.getText().toString();
                    m_EmojiconEditText.getText().clear();

                    if ( strMsg.length() < 1 )
                        return;
//                    if ( m_bWhisper == false )
//                    {
//                    String strMsg64 = EncodeTo64(strMsg);
//                    item.put("message", strMsg64);
                    item.put("message", strMsg);

                    String strName = DataUtils.getPreference(Const.FACEBOOKID, "");
//                    String strName64 = EncodeTo64(strName);
                    item.put("name", strName);
                    item.put("receiver", userList[0]);
                    addMessage(item);
//                    mSocket.emit(SocChatConst.CHAT, strMsg64);
//                    mSocket.emit(SocChatConst.CHAT, strMsg);
                    mSocket.emit(SocChatConst.CHAT, item);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

//        initEmoticonKeyboradEvents();

        ResourceUtils.addClickEffect(m_btnCameraSwitch);
    }
    protected void gotoBlockActivity(){
        DataUtils.savePreference(Const.NOTIFICATION_CONTENT, not);
        DataUtils.savePreference(Const.NOTIFICATION_SIZE,nots);

        String blockfacebookid = userList[0];
        Bundle bundle = new Bundle();
        bundle.putString("blockfacebookid", blockfacebookid);
        Intent intent = new Intent(ChatActivity.this, BlockChattingActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);//forward
    }

    protected void parsemChatUser(){
        JSONObject json;
        String str = "";
        try{
            json = new JSONObject(mChatUser);
//            JSONObject j = json.optJSONObject(2);
            str = json.optString("facebookid");

        }catch (JSONException e){

        }
        userList = new String[3];
        userList[0]= str;

    }

    private void addMessage(JSONObject msg){
        if(mChatUser == null || mChatUser.length() == 0)return;

        if ( m_arrChatMsgs == null )
        {
            m_arrChatMsgs = new ArrayList<JSONObject>();
            showMsgListData(m_arrChatMsgs);
        }

        try {
            msg.put("count", 1);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ////////////////////////////////////////////////////////////////////////
        // filter msg with userList
        boolean isChatUser = false;
//        String myname = DataUtils.getPreference(Const.USERNAME, "");
        String myname = DataUtils.getPreference(Const.FACEBOOKID, "");
//        String username64 = msg.optString("name","");
        String username = msg.optString(Const.FACEBOOKID,"");

//        String username = DecodeFrom64(username64);
//        for(int x = 0; x < userList.length; x++){
//            if(username.equals(userList[x])){
//                isChatUser = true;
//                break;
//            }
//        }
//        if(username.equals(myname))isChatUser=true;
//        if(!isChatUser)return;
        ////////////////////////////////////////////////////////////////////////

        m_arrChatMsgs.add(msg);
        if ( m_adapterMsgList != null )
        {
            m_adapterMsgList.notifyDataSetChanged();
            m_ChatListView.setSelection(m_adapterMsgList.getCount()-1);
        }
    }

    private void requestWhisperUser(String target) {
        // Here need Encoding to 64 but it remains for forward.
        String strName = DataUtils.getPreference(SocChatConst.NICKNAME, "");

        if ( target.equals(strName) )
            return;

        JSONObject sendMsg = new JSONObject();
        try {
            sendMsg.put("target", target);
            sendMsg.put("sender", strName);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mSocket.emit(SocChatConst.WHISPER_REQ, sendMsg);
        MessageUtils.showMessageDialog(ChatActivity.this, getResources().getString(R.string.requestedwhisper) + target );
    }


    public void addMsgHistory(JSONObject arrMsgs)
    {
        JSONArray arrMsg = arrMsgs.optJSONArray("content");
        for (int i = 0; i < arrMsg.length(); i++)
        {
            String msg = (String)arrMsg.optString(i, "");
            JSONObject item;
            try {
                item = new JSONObject(msg);
                if ( item != null )
                    addMessage(item);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void removeUser(String userName)
    {
        String strName =(String)userName;
        int delIndex = -1;
        for (int i = 0; i < m_arrUsers.size(); i++)
        {
            String name = m_arrUsers.get(i).optString("name", "");
            if ( name.length() > 0 )
            {
                if ( name.equals(strName) == true )
                {
                    delIndex = i;
                    break;
                }
            }
        }
        if ( delIndex > -1 )
        {
            m_arrUsers.remove(delIndex);
        }
    }
    public void addUser(String userName)
    {
        // Here need to decode from 64 but remains for forward
        String strName =(String)userName;
        JSONObject item = new JSONObject();
        try {
            if(m_arrAllUsers != null) {
                for (int j = 0; j < m_arrAllUsers.length(); j++) {
                    JSONObject profile = m_arrAllUsers.getJSONObject(j).optJSONObject("profile");
                    String strUserName = profile.optString("nickname", "");
                    if (strUserName.equals(strName) == true) {
                        String strAvatar = profile.optString("avatar", "");
                        item.put("avatar", strAvatar);
                        break;
                    }
                }
                item.put("name", strName);
                item.put("index", m_nIndex);
            }else{
                item.put("name", strName);
                item.put("index",1);
            }
            m_arrUsers.add(item);

            m_nIndex++;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getUserList(JSONArray arrUsers)
    {
        try {
            for (int i = 0; i < arrUsers.length(); i++)
            {
                String strName;
                strName = (String)arrUsers.optString(i);
                final JSONObject item = new JSONObject();

//                for (int j = 0; j < m_arrAllUsers.length(); j++)
//                {
//                    JSONObject profile = m_arrAllUsers.getJSONObject(j).optJSONObject("profile");
//                    String userName = profile.optString("nickname");
//                    if (userName.equals(strName) == true )
//                    {
//                        String strAvatar = profile.optString("avatar", "");
//                        item.put("avatar", strAvatar);
//                        break;
//                    }
//                }

                item.put("name", strName);
                item.put("index", m_nIndex);
                m_arrUsers.add(item);

                m_nIndex++;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void resizeChatList()
    {
        int height = ScreenAdapter.getDeviceHeight();
        LayoutUtils.setSize(findViewById(R.id.videoWnd), ViewGroup.LayoutParams.MATCH_PARENT, height, false);
        LayoutUtils.setSize(findViewById(R.id.surfaceview), ViewGroup.LayoutParams.MATCH_PARENT, height, false);
//        LayoutUtils.setSize(findViewById(R.id.fragment_chat).findViewById(R.id.viewVideo), ViewGroup.LayoutParams.MATCH_PARENT, height*3/5, false);
    }

    void reloadchatList()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if ( m_adapterMsgList != null )
                {
                    m_adapterMsgList.notifyDataSetChanged();
                    int nCount = m_adapterMsgList.getCount();
                    if ( nCount > 0 )
                        m_ChatListView.setSelection(nCount-1);
                }
            }
        }, 100);
    }
    private void selectWhisperUser() {
        final String [] items = new String[m_arrUsers.size()];
        for(int i = 0; i < items.length; i++)
        {
            items[i] = m_arrUsers.get(i).optString("name", "");
        }

        int nSel=0;
        for(int i = 0; i < items.length; i++)
        {
            if ( m_strWhisperUser.equals(items[i]) )
            {
                break;
            }
            nSel++;
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setSingleChoiceItems(items, nSel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                requestWhisperUser(items[whichButton]);
                dialog.dismiss();
            }
        });

        dialog.create();
        AlertDialog alertDialog = dialog.show();

        alertDialog.setCanceledOnTouchOutside(true);
    }

    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        JSONObject item = new JSONObject();
//
//        // 액티비티가 정상적으로 종료되었을 경우
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 3000) {
//                String strmsg = data.getStringExtra("upload");
////                item.put("message", strmsg);
////
////                String strName = DataUtils.getPreference(Const.USERNAME, "");
////                item.put("name", strName);
////
////                addMessage(item);
////
////                mSocket.emit(SocChatConst.CHAT, strMsg);
//
//            }
//        }
//    }

    //function: play voice chating data with url.
    // return:  null

    protected void playVoiceMessage(String url){
        String urlAll = ServerTask.SERVER_ADRESS+url;
        urlToDownload = Uri.parse(urlAll);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(url);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mPlayer.setOnCompletionListener(completionListener);
        mPlayer.start();

//        String urlAll = ServerTask.SERVER_ADRESS+url;
//        urlToDownload = Uri.parse(urlAll);
//        List<String> pathSegments = urlToDownload.getPathSegments();
//        request = new DownloadManager.Request(urlToDownload);
//        request.setTitle("다운로드 예제");
//        request.setDescription("항목 설명");
//        request.setDestinationInExternalPublicDir("kkkkk", pathSegments.get(pathSegments.size()-1));
//        Environment.getExternalStoragePublicDirectory("kkkkk").mkdirs();
//        latestId = downloadManager.enqueue(request);

    }
    // 다운로드가 완료를 알려줄 브로드캐스트 리시버
//    private BroadcastReceiver completeReceiver = new BroadcastReceiver(){
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            List<String> pathSegments = urlToDownload.getPathSegments();
////            play.setVisibility(View.INVISIBLE);
////            stop.setVisibility(View.VISIBLE);
//            String path =""+pathSegments.get(pathSegments.size()-1);
//            playVoicemessage("kkkkk"+pathSegments.get(pathSegments.size()-1));
//            Toast.makeText(context, "다운로드가 완료되었습니다.",Toast.LENGTH_SHORT).show();
//        }
//    };
//    //다운로드 완료시 발송되는 브로드캐스트 메시지를 처리하는 브로드캐스트 리시버를 등록
//    @Override
//    public void onResume(){
//        super.onResume();
//        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        registerReceiver(completeReceiver, completeFilter);
//    }
//    //액티비티가 비활성화 될 때 다운로드 완료를 감지하는 브로드캐스트 리시버를 해제
//    @Override
//    public void onPause(){
//        super.onPause();
//        unregisterReceiver(completeReceiver);
//    }
//    // function: find recording file with the name.
//    // return: boolean
//    protected String findInMyFolder(String url){
//        MessageUtils.showMessageDialog(ChatActiviy.this,"find my folder");
//        return "";
//    }
    // function: play the recording voice.
    // return: boolean
//    protected boolean playVoicemessage(String downVoicePath){
//        Uri uri = null;
//        //Here you should convert downVoicPath to uri.
//
//
//        MediaPlayer mediaPlayer = MediaPlayer.create(ChatActiviy.this, uri);
//        mediaPlayer.setOnCompletionListener(completionListener);
//        mediaPlayer.start();
//        MessageUtils.showMessageDialog(ChatActiviy.this,"playing voice message");
//        return true;
//    };


    MediaPlayer.OnCompletionListener completionListener
            = new MediaPlayer.OnCompletionListener(){

        @Override
        public void onCompletion(MediaPlayer arg0) {
            // TODO Auto-generated method stub
            MessageUtils.showMessageDialog(ChatActivity.this,"finished play");
            play.setVisibility(View.VISIBLE);
            stop.setVisibility(View.INVISIBLE);
        }};
    // function: stop the playing record voice.
    // return : boolean
    protected boolean stopVoicemessage(){
        MessageUtils.showMessageDialog(ChatActivity.this,"stop voice message");
        return true;
    }

    public void showMsgListData(List<JSONObject> list){
//        m_adapterMsgList = new MsgListAdapter(this, list, R.layout.soc_chat_fragment_chatitem_voice, null);
        m_adapterMsgList = new MyChatListAdapter(getApplicationContext(), R.layout.soc_chat_fragment_chat_input_voice,list);
//        m_ChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
//                // TODO Auto-generated method stub
//                String url = "";
//                String shortmessage="";
//                String downVoicePath = "";
//                JSONObject message = m_arrChatMsgs.get(index);
//                String voiceMessage = message.optString("message","");
//
//                play = (ImageView)arg1.findViewById(R.id.img_play);
//                stop = (ImageView)arg1.findViewById(R.id.img_stop);
//
//                int playVisibility = play.getVisibility();
//                int stopVisibility = stop.getVisibility();
//                if(isJSONValid(voiceMessage)){
//                    JSONObject jsonData = null;
//                    try{
//                        jsonData = new JSONObject(voiceMessage);
//                        String url64 = jsonData.optString("url","");
//                        url = url64;//DecodeFrom64(url64);
//                        String shortmessage64 = jsonData.optString("message","");
//                        shortmessage = shortmessage64;//DecodeFrom64(shortmessage64);
//                    }
//                    catch(JSONException j){
//
//                    }
//                    if(playVisibility == View.VISIBLE){
//                        if(url == null){
//                            Toast.makeText(
//                                    getApplicationContext(),
//                                    "Couldn`t find file`s path.",
//                                    Toast.LENGTH_SHORT
//                            ).show();
//
//                        }else{
//                            play.setVisibility(View.INVISIBLE);
//                            stop.setVisibility(View.VISIBLE);
//                            playVoiceMessage(url);
//                        }
//                    }else{
//                        if(mPlayer!=null && mPlayer.isPlaying()){
//                            mPlayer.stop();
//                            play.setVisibility(View.VISIBLE);
//                            stop.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                }
//            }
//        });

        m_ChatListView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                if ( m_bShowKeyboard == true )
                {
                    int height = ScreenAdapter.getDeviceHeight();
//                    LayoutUtils.setSize(findViewById(R.id.fragment_chat).findViewById(R.id.viewVideo), ViewGroup.LayoutParams.MATCH_PARENT, height*3/5, false);
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(m_EmojiconEditText.getWindowToken(), 0);
                    m_bShowKeyboard = false;
                    reloadchatList();
                }
                return false;
            }
        });
        m_ChatListView.setAdapter(m_adapterMsgList);
    }

//    class MsgListAdapter extends MyListAdapter {
//        public MsgListAdapter(Context context, List<JSONObject> data,
//                              int resource, ItemCallBack callback) {
//            super(context, data, resource, callback);
//        }
//
//        @Override
//        protected void loadItemViews(View rowView, int position,ViewGroup parent) {
//            String url = null;
//            String strMsg = null;
//            String play = null;
//            JSONObject jsonMessage = null;
//            final JSONObject item = getItem(position);
//            if (item == null)
//                return;
//
//            String strUsername64 = item.optString("name", "");
//            String strUsername = strUsername64;//DecodeFrom64(strUsername64);
////            String strmy = DataUtils.getPreference(Const.USERNAME,"");
////            if (strUsername.equals(strmy)) {
////                rowView = mInflater.inflate(R.layout.soc_chat_fragment_chatitem_voice, parent, false);
////            }else{
////                rowView = mInflater.inflate(R.layout.soc_chat_fragment_chatitem_right, parent, false);
////            }
//
//            strUsername = strUsername.replace("%20", " ");
//            if (strUsername.length() > 0)
//                ((TextView) ViewHolder.get(rowView, R.id.textName)).setText(strUsername);
//            else
//                ((TextView) ViewHolder.get(rowView, R.id.textName)).setText("");
//
//            String body64 = item.optString("message", "");
//            String body = body64;//DecodeFrom64(body64);
//            if(isJSONValid(body)) {
//                try {
//                    jsonMessage = new JSONObject(body);
//                    String strMsg64 = jsonMessage.optString("message");
//                    strMsg = strMsg64;//DecodeFrom64(strMsg64);
//                    String play64 = jsonMessage.optString("play");
//                    play = play64;//DecodeFrom64(play64);
//                    String url64 = jsonMessage.optString("url");
//                    url = url64;//DecodeFrom64(url64);
//                    ((ImageView) ViewHolder.get(rowView, R.id.img_play)).setVisibility(View.VISIBLE);
//                    ((ImageView) ViewHolder.get(rowView, R.id.img_stop)).setVisibility(View.INVISIBLE);
//                } catch (JSONException e) {
//                }
//            }else {
//                strMsg = body;
//                ((ImageView) ViewHolder.get(rowView, R.id.img_play)).setVisibility(View.INVISIBLE);
//                ((ImageView) ViewHolder.get(rowView, R.id.img_stop)).setVisibility(View.INVISIBLE);
//            }
//
//
//            ((TextView) ViewHolder.get(rowView, R.id.textMsg)).setText(strMsg);
//        }
//    }

    //// Voice chat related functions
    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
    }

//    private void startRecording() {
//        m_fileName = getFilename();
//        recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(output_formats[currentFormat]);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        recorder.setOutputFile(m_fileName);
//        recorder.setOnErrorListener(errorListener);
//        recorder.setOnInfoListener(infoListener);
//
//        try {
//            recorder.prepare();
//            recorder.start();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
//        @Override
//        public void onError(MediaRecorder mr, int what, int extra) {
//            MessageUtils.showMessageDialog(ChatActivity.this, "Error");
////            AppLog.logString("Error: " + what + ", " + extra);
//        }
//    };
//
//    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
//        @Override
//        public void onInfo(MediaRecorder mr, int what, int extra) {
//            MessageUtils.showMessageDialog(ChatActivity.this, "Warning");
////            AppLog.logString("Warning: " + what + ", " + extra);
//        }
//    };
//
//    private void stopRecording() {
//        if (null != recorder) {
//            recorder.stop();
//            recorder.reset();
//            recorder.release();
//            uploadRecording();
//            recorder = null;
//        }
//    }

//    private void uploadRecording() {
//        if (m_fileName == null) {
//            return;
//        } else {
//            // upload recording file to server and send message to partner.
//            ServerManager.uploadFile(m_fileName, new ResultCallBack() {
//
//                @Override
//                public void doAction(LogicResult result) {
//                    if( result.mResult == LogicResult.RESULT_OK ){
//                        JSONObject userInfo = result.getData();
//                        m_strAudioUpload = userInfo.optString("content");
//                        JSONObject json = new JSONObject();
//                        JSONObject jsonobject = new JSONObject();
//                        try{
//                            json.put("url", ServerTask.SERVER_ADRESS+m_strAudioUpload);
//                            json.put("play", "0");
//                            json.put("message","Voice Message");
////                            json.put("message",m_fileName);
//
//
//                            String message = json.toString().trim();
//                            jsonobject.put("message",message);
//                            String strName = DataUtils.getPreference(Const.USERNAME, "");
//                            jsonobject.put("name", strName);
//
//                            mSocket.emit(SocChatConst.CHAT, json);
//
//                            addMessage(jsonobject);
//                        }
//                        catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//        }
//    }

    // check if String is JSON .
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
//            try {
//                new JSONArray(test);
//            } catch (JSONException ex1) {
            return false;
//            }
        }
        return true;
    }

    protected void gotoMainActivity(){
        Bundle bundle = new Bundle();
        ActivityManager.changeActivity(ChatActivity.this, MainActivity.class, bundle,false,null);
    }

    protected String _EncodeTo64(String str){
        try{
            byte[] source = str.getBytes("UTF-8");
            String base64_encoded = Base64.encodeToString(source, Base64.DEFAULT);
            return base64_encoded;
        }catch (UnsupportedEncodingException e){
            return null;
        }
    }

    protected String _DecodeFrom64(String base64){
        String text = "";
//        if(!isBase64(base64)){return  base64;}
        try{
            byte[] data = Base64.decode(base64, Base64.DEFAULT);
            text = new String(data, "UTF-8");
        }catch (Exception e) {
            text =  base64;
        }
        return text;
    }
    //java.util.regex.Pattern
    private boolean isBase64(String stringBase64){
        String regex =
                "^([A-Za-z0-9+/]{4})*"+
                        "([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)";
        boolean a= Pattern.matches(stringBase64, regex);

        Pattern patron = Pattern.compile(regex);
        if (!patron.matcher(stringBase64).matches()) {
            return false;
        } else {
            return true;
        }
    }
    /////////////////////////////////////
//    String encoded = params.toString();
//    // Sending side
//    byte[] source = encoded.getBytes("UTF-8");
//    String base64_encoded = Base64.encodeToString(source, Base64.DEFAULT);

//			// Receiving side
//			byte[] data = Base64.decode(base64, Base64.DEFAULT);
//			String text = new String(data, "UTF-8");
    ///////////////////////////////////
}
