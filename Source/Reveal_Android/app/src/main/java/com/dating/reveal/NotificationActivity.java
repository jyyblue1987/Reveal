package com.dating.reveal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dating.reveal.Item_models.Notification;
import com.dating.reveal.ListAdapter.NotificationListAdapter;
import com.dating.reveal.commom.ActivityManager;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.DataUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JonIC on 2016-11-08.
 */
public class NotificationActivity extends HeaderActivity
{
    String ntData;
    int ntSize = -1;
    int position  = 0;
    ArrayList<Notification> notList;
    NotificationListAdapter mListAdapter;
    ListView mListView;
    JSONArray jsonarray;
    ProgressDialog progressDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));


//        ntData = DataUtils.getPreference(Const.NOTIFICATION_CONTENT,"");
//        ntSize = DataUtils.getPreference(Const.NOTIFICATION_SIZE,"");
        LoadParentActivity();
        getnotification();


    }
    protected void getnotification(){
        progressDialog= new ProgressDialog(NotificationActivity.this);

        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");


        String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        ServerManager.getnotification(myfacebookid, new ResultCallBack(){
            @Override
            public void doAction(LogicResult result) {
                progressDialog.dismiss();

                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userinfo = result.getData();
                    JSONArray jsonArraynoti = userinfo.optJSONArray("content");
                    ntData = jsonArraynoti.toString().trim();
                    if(ntData.equals("{}") || ntData.equals("")){
                        return;
                    }
                    DataUtils.savePreference(Const.NOTIFICATION_CONTENT,ntData);
                    String aaa= DataUtils.getPreference(Const.NOTIFICATION_CONTENT,"");
                    parseNtData();
                }else{
                    JSONObject useri = result.getData();
                    String error = useri.optString("error_msg");
                    Toast.makeText(NotificationActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    protected void parseNtData(){
        notList = new ArrayList<>();
            try {
                jsonarray = new JSONArray(ntData);
                ntSize = jsonarray.length();
                for(int x = 0; x < jsonarray.length(); x++){
                    JSONObject jsonObjectN = jsonarray.optJSONObject(x);
                    Notification item = new Notification();
                    String sender = jsonObjectN.optString("sender","");
                    String destination = DataUtils.getPreference(Const.FACEBOOKID,"");
                    String notekind = jsonObjectN.optString("notekind","");
                    String feedval  = jsonObjectN.optString("feedval","");
                    String sendtime = jsonObjectN.optString("sendtime","");
                    String username = jsonObjectN.optString("sender_name","");

                    item = new Notification();

                    item.sender = sender;
                    item.destination = destination;
                    item.notekind = notekind;
                    item.feedval = feedval;
                    item.sendtime = sendtime;
                    item.username = username;
                    item.profilephoto = "";

                    notList.add(item);
//                    getProfilephoto( x, sender,destination,  notekind, feedval, sendtime);
                }

            }catch(Throwable t){
                Toast.makeText(NotificationActivity.this, "Sorry, could not read notification", Toast.LENGTH_SHORT).show();
                return;
            }

        initList();

    }

    protected void getProfilephoto(final int x, final String facebookid, final String myfacebookid, final String notekind,
                                   final String feedval, final String sendtime){


        ServerManager.getPersonInfo(facebookid, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                if (result.mResult == LogicResult.RESULT_OK) {
                    String str = String.valueOf(x);

                    JSONObject jsonResult = result.getData();
                    JSONArray userinfo = jsonResult.optJSONArray("content");

                    JSONArray empty = new JSONArray();
                    if (userinfo.equals(empty)) {
                        Toast.makeText(NotificationActivity.this, "Sorry, could not find person information.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject jsonObject = userinfo.optJSONObject(0);
                    String username = jsonObject.optString("name", "");
                    String profilephoto = jsonObject.optString("profilephoto", "");
                    Notification item = new Notification();

                    item.sender = facebookid;
                    item.destination = myfacebookid;
                    item.notekind = notekind;
                    item.feedval = feedval;
                    item.sendtime = sendtime;
                    item.username = username;
                    item.profilephoto = profilephoto;

                    notList.add(item);

                    String fbmax = DataUtils.getPreference("maxcount","");
                    if(str.equals(fbmax)){
                        // discuss progress bar.
                        progressDialog.dismiss();
                        if(notList.size() >0){
                            initList();
                        }else{
                            Toast.makeText(NotificationActivity.this, "There is no Notification", Toast.LENGTH_SHORT).show();

                        }
                    }
                    // here we got the friend list.
                }
            }

        });
    }


    protected void initList(){
        mListAdapter = new NotificationListAdapter(NotificationActivity.this, notList);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String notekind = notList.get(position).notekind;
                String facebookid = notList.get(position).sender;
                String photopath = notList.get(position).feedval;
                if(notekind.equals("newfeed")){
                    gotoNewFeed(facebookid);
                    // delete this notification and goto newfeed page.
                    readNotification(position);
                    // if delete this item then NewFeedActivity could not see the New feed.
//                    gotoNewFeed(facebookid);
                }else if(notekind.equals("message")){
                    readNotification(position);
                    String not = DataUtils.getPreference(Const.NOTIFICATION_CONTENT, "");
                    String nots = DataUtils.getPreference(Const.NOTIFICATION_SIZE,"");
                    gotoChat(facebookid);
                    // goto chat with facebookid- sender.

                }else if(notekind.equals("like")){
                    readNotification(position);
//                    Toast.makeText(NotificationActivity.this, "",Toast.LENGTH_SHORT).show();
                    // goto my profile with photo path.
                    gotoMyProfilePhoto(photopath);

                }
                else if(notekind.equals("comment")){
                    readNotification(position);
//                    Toast.makeText(NotificationActivity.this, "",Toast.LENGTH_SHORT).show();
                    // goto my profile with photo path.
                    gotoMyProfilePhoto(photopath);

                }
                else if(notekind.equals("requestfriend")){  // match
//                    Toast.makeText(NotificationActivity.this, "requestfriend",Toast.LENGTH_SHORT).show();
                    readNotification(position);
                    // something request to be friend with you.
                    // you should go to add match page.
                    gotoAddMatchPage();
                }
                else if(notekind.equals("acceptfriend")){  // match
//                    Toast.makeText(NotificationActivity.this, "acceptfriend",Toast.LENGTH_SHORT).show();
                    readNotification(position);
                    // something request to be friend with you.
                    // you should go to friend page.
                    gotoFriendPage();
                }
                else if(notekind.equals("newmatch")){
//                 Toast.makeText(NotificationActivity.this, "",Toast.LENGTH_SHORT).show();
                    readNotification(position);
                    gotoMatchMessageActivity();
                }
                else if(notekind.equals("matchRequest")){
//                    Toast.makeText(NotificationActivity.this, "",Toast.LENGTH_SHORT).show();
                    String notekind_ = notList.get(position).notekind;
                    String facebookid_ = notList.get(position).sender;
                    gotoRatingFeed(facebookid_);
                    readNotification(position);

                }
                else if(notekind.equals("friendadd")){
//                    Toast.makeText(NotificationActivity.this, "",Toast.LENGTH_SHORT).show();
                    readNotification(position);
                    gotoAddFriendPage();

                }else if(notekind.equals("friendaccept")){ // the facebook friend accept to be friend with you in this app.
                    readNotification(position);
                    gotoFriendPage();
                }
            }
        });
    }

    protected void gotoAddFriendPage(){
        Bundle bundle = new Bundle();
        ActivityManager.changeActivity(NotificationActivity.this, AddFriendActivity.class, bundle, false, null);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        // backward
        finish();
    }
    protected void gotoMatchMessageActivity(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(NotificationActivity.this, MatchsMessageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        // backward
        finish();
    }
    protected void gotoNewFeed(String facebookid){
        DataUtils.savePreference(Const.TRY_MATCH_PERSON,facebookid);
        String receivephoto = "try_match_person";

        Bundle bundle = new Bundle();
        Intent intent = new Intent(NotificationActivity.this, NewFeedActivity_candidate.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        finish();
    }
    protected void gotoRatingFeed(String facebookid){
        DataUtils.savePreference(Const.TRY_MATCH_PERSON, facebookid);
        String receivephoto = "try_match_person";

        Bundle bundle = new Bundle();
        bundle.putString("receivephoto", receivephoto);
        Intent intent = new Intent(NotificationActivity.this, RatingActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        finish();
    }

    protected void gotoChat(String facebookid){
        checkblock(facebookid);
    }

    protected void checkblock(final String matchfaceboolid){

        // show progressdialog

        final ProgressDialog progressDialog ;
        progressDialog = new ProgressDialog(NotificationActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Checking block...");
        progressDialog.show();

        String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        ServerManager.checkblock(myfacebookid, matchfaceboolid, new ResultCallBack(){
            @Override
            public void doAction(LogicResult result) {
                progressDialog.dismiss();
                if(result.mResult == LogicResult.RESULT_OK){

                    String person = "{\"facebookid\":\""+matchfaceboolid+"\"}";
                    Bundle bundle = new Bundle();
                    bundle.putString("person", person);
                    Intent intent = new Intent(NotificationActivity.this, ChatActivity.class);
                    intent.putExtras(bundle);

                    String not = DataUtils.getPreference(Const.NOTIFICATION_CONTENT, "");
                    String nots = DataUtils.getPreference(Const.NOTIFICATION_SIZE,"");

                    startActivity(intent);
                    finish();

                }else if(result.mResult == 301){
                    // if this user is not blocked or block you then you can chat with that match.
                    JSONObject userinfo = result.getData();
                    JSONObject data = userinfo.optJSONObject("content");
                    String blockmessage = data.optString("blocksort");
                    String blocksort  = "This user is blocked for \"" + blockmessage + "\"";
                    Toast.makeText(NotificationActivity.this, blocksort, Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(NotificationActivity.this,"Server Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    };


    // send read alarm to server the notification at position
    protected void readNotification(int position){
            if(ntSize == -1){
                return;
            }
            String sender = notList.get(position).sender;
            String destionation = notList.get(position).destination;
            final String notekind = notList.get(position).notekind;
            String feedval = notList.get(position).feedval;

            ServerManager.readNotification(sender, destionation, notekind, feedval, new ResultCallBack(){
                @Override
                public void doAction(LogicResult result) {
                    if(result.mResult==LogicResult.RESULT_OK){

                        JSONObject userinfo = result.getData();
//                        String ret = userinfo.optString("content","");
//                        String notsize = DataUtils.getPreference(Const.NOTIFICATION_SIZE,"0");
//                        int size = Integer.parseInt(notsize);
//                        size--;
//                        String notesize = String.valueOf(size);
//                        DataUtils.savePreference(Const.NOTIFICATION_SIZE, notesize);
                    }
                }
            });

    }

    protected void LoadParentActivity(){
        super.LoadParendActivity();
    }
    protected  void findView(){
        super.findView();
        mListView = (ListView) findViewById(R.id.list);
    };
    protected void layoutControls(){
        super.layoutControls();
    };
    protected void initEvent(){
        super.initEvent();
        m_img_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        m_img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                readNotification(position);
                gotoFriendPage();
            }
        });
    };
    protected void initView(){
        super.initView();
        m_img_bell.setVisibility(View.INVISIBLE);
        m_btn_notify.setVisibility(View.INVISIBLE);
        m_img_three.setVisibility(View.INVISIBLE);
        // show only back arrow.
        m_img_back_arrow.setVisibility(View.VISIBLE);
    };
    protected void gotoFriendPage(){
        Bundle bundle = new Bundle();
        ActivityManager.changeActivity(NotificationActivity.this, FriendActivity.class, bundle,true, null);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
    }

    protected void  gotoAddMatchPage(){
        Bundle bundle = new Bundle();
        ActivityManager.changeActivity(NotificationActivity.this, AddMatchesActivity.class,bundle,true,null);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
    }

    protected void gotoMyProfilePhoto(String photopath){
        Bundle bundle = new Bundle();
        bundle.putString("photopath", photopath);
        Intent intent = new Intent(NotificationActivity.this, MySelfPhotoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward// backward;
    }

}
