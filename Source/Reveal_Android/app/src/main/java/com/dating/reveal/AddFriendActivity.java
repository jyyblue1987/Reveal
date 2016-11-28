package com.dating.reveal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dating.reveal.ListAdapter.AddfriendListAdapter;
import com.dating.reveal.ListAdapter.ItemAddfriendActivity;
import com.dating.reveal.commom.ActivityManager;
import com.dating.reveal.commom.OnSwipeTouchListener;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.DataUtils;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JonIC on 2016-11-08.
 */
// here we get matches list and show them
public class AddFriendActivity extends HeaderActivity implements AdapterView.OnItemClickListener{
    Button mBtnAddFriend;
    RelativeLayout relativeLayout;
    Button m_btn_search;
    EditText m_searchname;
    ProgressDialog progressDialog ;
    ProgressDialog progressDialog2;

    ListView mListView = null;
//    CommentListAdapter mFriendlistAdapter = null;
    AddfriendListAdapter mFriendlistAdapter = null;
    ArrayList<ItemAddfriendActivity> mListContent = null;
    ArrayList<ItemAddfriendActivity> mFriendListContent = null;
    ArrayList<ItemAddfriendActivity> mSearchResult = null;
    int isSearch = 0;

    private ProgressBar bar;
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
        setContentView(R.layout.activity_add_friend);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // get mathces form server.
        LoadParendActivity();
        getNotificationsize();
        getFriend();
        swip();
    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
    }
    protected void layoutControls(){
        super.layoutControls();
    }

    protected void initView(){
        super.initView();
        m_img_back_arrow.setVisibility(View.VISIBLE);
        m_img_three.setVisibility(View.INVISIBLE);
        String a = "\uD83D\uDD0D";
        a.toUpperCase();
        m_btn_search.setText(a);

        progressDialog= new ProgressDialog(AddFriendActivity.this);

        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");

    }
    protected void findView(){
        super.findView();
        bar = (ProgressBar) findViewById(R.id.progressBar);
        mListView = (ListView)findViewById(R.id.list);
        mBtnAddFriend = (Button)findViewById(R.id.btn_friend_add);
        relativeLayout =(RelativeLayout)findViewById(R.id.lay_container);
        m_btn_search = (Button) findViewById(R.id.btn_search);
        m_searchname = (EditText) findViewById(R.id.edit_search);
    }

    protected void initEvent(){
        super.initEvent();
        m_img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFriendPage();
            }
        });
        mBtnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddMatch();
            }
        });
        m_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearch();
            }
        });
    }

    protected void gotoSearch(){
        mSearchResult = new ArrayList<>();
        String searchCon = m_searchname.getText().toString().trim().toLowerCase();
        String myname = DataUtils.getPreference(Const.NAME,"");
        myname = myname.toLowerCase();
        if(searchCon.equals("") || searchCon.equals(myname)){
            isSearch = 0;
            mFriendlistAdapter = new AddfriendListAdapter(AddFriendActivity.this, mFriendListContent);
            // click event
            mListView.setAdapter(mFriendlistAdapter);
            mListView.setOnItemClickListener(AddFriendActivity.this);
            return;
        }
        progressDialog2 = new ProgressDialog(AddFriendActivity.this);
        progressDialog2.setMessage("Loading");
        progressDialog2.setIndeterminate(true);
        progressDialog2.setCancelable(false);
        progressDialog2.setTitle("");
        progressDialog2.setMessage("Please wait...");
        progressDialog2.show();

        ServerManager.findbyname(searchCon, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userinfo = result.getData();
                    int retcode = Integer.parseInt(userinfo.optString("retcode",""));
                    if(retcode!=200){
                        Toast.makeText(AddFriendActivity.this, "Server error",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONArray jsonArray = userinfo.optJSONArray("content");
                    for(int x=0; x< jsonArray.length(); x++){
                        String facebookid = jsonArray.optJSONObject(x).optString(Const.FACEBOOKID,"");
                        String name = jsonArray.optJSONObject(x).optString(Const.NAME,"");
                        String desctiption = "add";

                        ItemAddfriendActivity item = new ItemAddfriendActivity("",desctiption, facebookid,name, "profilephoto");//("", "",facebookid, username, profilephoto);
                        mSearchResult.add(item);
                    }
                    isSearch = 1;
                    mFriendlistAdapter = new AddfriendListAdapter(AddFriendActivity.this, mSearchResult);
                    // click event
                    mListView.setAdapter(mFriendlistAdapter);
                    mListView.setOnItemClickListener(AddFriendActivity.this);
                }
                progressDialog2.dismiss();
            }
        });


//        if(mFriendListContent.size() == 0){
//            Toast.makeText(AddFriendActivity.this, "There is no facebook friend.",Toast.LENGTH_SHORT).show();
//
//            return;
//        }
//
//        for(int x = 0; x < mFriendListContent.size(); x++){
//            String friendname = mFriendListContent.get(x).getName().toLowerCase();
//            if(friendname.equals(searchCon)){
//                String desctiption = mFriendListContent.get(x).getDescription();
//                String facebookid  = mFriendListContent.get(x).getFacebookid();
//                String name = mFriendListContent.get(x).getName();
//
//                ItemAddfriendActivity item = new ItemAddfriendActivity("",desctiption, facebookid,name, "profilephoto");//("", "",facebookid, username, profilephoto);
//                mSearchResult.add(item);
//            }
//        }
//        if(mSearchResult.size() == 0){
//            Toast.makeText(AddFriendActivity.this, "No found such friend.",Toast.LENGTH_SHORT).show();
//        }
    }

    protected void gotoFriendPage(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(AddFriendActivity.this, FriendActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        finish();
    }

    // 2016-11-27
    protected void  getFriend(){
        final String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        progressDialog.show();
        ServerManager.getFriend(facebookid, new ResultCallBack(){
            @Override
            public void doAction(LogicResult result) {
                progressDialog.dismiss();
                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userInfo = result.getData();
//                    JSONObject personProfile = userInfo.optJSONObject("content");
//                    String rawmatchdata = personProfile.toString().trim();
                    JSONArray friendlist = userInfo.optJSONArray("content");

                    mListContent = new ArrayList<>();

                    // here we use facebook friend list to show the data.
                    getFacebookFriend(friendlist);////////////////////////////////////////////////////////////////////////////// here get friend list sort type

                }else{
                    JSONArray empty = new JSONArray();
                    getFacebookFriend(empty);
                }
            }
        });

    }

    protected void getFacebookFriend(JSONArray appuser){
        ArrayList<String> app_frined = new ArrayList<>();
        for(int x = 0;x< appuser.length();x++ ){
            JSONObject friend = appuser.optJSONObject(x);
            String facebookid1 = friend.optString("facebookid1");
            String facebookid2 = friend.optString("facebookid2");
            String name1 = friend.optString("name1");
            String name2 = friend.optString("name2");
            String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
            String facebookid;
            String friendname;
            if(facebookid1.equals(myfacebookid)){
                facebookid = facebookid2;
                friendname  = name2;
            }else{
                facebookid = facebookid1;
                friendname  = name1;
            }
            app_frined.add(facebookid);
        }
        String   use_app_friend = DataUtils.getPreference(Const.USE_APP_FRIENDS,"");
        String   all_fb_friend= DataUtils.getPreference(Const.FACEBOOK_FRIENDS, "");
        mFriendListContent = new ArrayList<>();

        try {
            JSONArray json_use_app_frined = new JSONArray(use_app_friend);
            JSONArray json_all_fb = new JSONArray(all_fb_friend);

            // search fb frined which use this app.
            for(int y = 0; y < json_use_app_frined.length(); y++){
                boolean isAppFrined = false;
                for(int x=0; x < app_frined.size(); x++){
                    String friend_inapp = app_frined.get(x);
                    String frined_fb    = json_use_app_frined.optJSONObject(y).optString("id");
                    if(friend_inapp.equals(frined_fb)){
                        isAppFrined = true;
                        break;
                    }
                }
                if(isAppFrined == false){ // if the fb friend who use this app is not friend of you in this app "add".
                    String facebookid = json_use_app_frined.optJSONObject(y).optString("id").trim();
//                    String profilephoto = json_use_app_frined.optJSONObject(y).optString("icon").trim();
                    String desctiption = getDescription(facebookid);
                    String name = json_use_app_frined.optJSONObject(y).optString("name").trim();

                    ItemAddfriendActivity item = new ItemAddfriendActivity("",desctiption, facebookid,name, "profilephoto");//("", "",facebookid, username, profilephoto);
                    mFriendListContent.add(item);
                }
            }

            ////////////// here we get the facebook friend which does not use this app.
            for(int all = 0; all < json_all_fb.length(); all ++){
                boolean isUsing = false;
                for(int use = 0; use < json_use_app_frined.length(); use ++){
                    String inAll = json_all_fb.optJSONObject(all).optString("name");
                    String inUse = json_use_app_frined.optJSONObject(use).optString("name");
                    if(inAll.equals(inUse)){
                        isUsing = true;
                        break;
                    }
                }
                if(isUsing == false){ // if that user is not using this app.
                    String facebookid = json_all_fb.optJSONObject(all).optString("id").trim();
                    String profilephoto = json_all_fb.optJSONObject(all).optString("icon").trim();
                    JSONObject json = new JSONObject(profilephoto);
                    JSONObject jsonObject = json.optJSONObject("data");
                    String path = jsonObject.optString("url");



                    String desctiption = "invite";
                    String name = json_all_fb.optJSONObject(all).optString("name").trim();
                    ItemAddfriendActivity item = new ItemAddfriendActivity("",desctiption, facebookid,name, path);//("", "",facebookid, username, profilephoto);
                    mFriendListContent.add(item);
                }
            }


            progressDialog.dismiss();
            mFriendlistAdapter = new AddfriendListAdapter(AddFriendActivity.this, mFriendListContent);
            // click event
            mListView.setAdapter(mFriendlistAdapter);
            mListView.setOnItemClickListener(AddFriendActivity.this);

        }catch (JSONException e){

        }
    }

    // to see if this user request to be friend with you in this app.
    protected String  getDescription(String facebookid){
        String notification_content = DataUtils.getPreference(Const.NOTIFICATION_CONTENT,"");
        try{
            JSONArray jsonArray_notifi = new JSONArray(notification_content);
            for(int x = 0; x < jsonArray_notifi.length(); x++){
                JSONObject json  = jsonArray_notifi.optJSONObject(x);
                String sender = json.optString("sender","");
                String notekind = json.optString("notekind","");
                if(notekind.equals("friendadd") && sender.equals(facebookid)){
                    return "friendadd";
                }
            }
            return "add";
        }catch (JSONException e){
            return "add";
        }
    }

    protected void getProfilephoto(final int x, final String facebookid){
        ServerManager.getPersonInfo(facebookid, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                if (result.mResult == LogicResult.RESULT_OK) {
                    String str = String.valueOf(x);

                    JSONObject jsonResult = result.getData();
                    JSONArray userinfo = jsonResult.optJSONArray("content");

                    JSONArray empty = new JSONArray();
                    if (userinfo.equals(empty)) {
                        Toast.makeText(AddFriendActivity.this, "Sorry, could not find person information.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject jsonObject = userinfo.optJSONObject(0);
                    String username = jsonObject.optString("name", "");
                    String profilephoto = jsonObject.optString("profilephoto", "");
                    ItemAddfriendActivity item = new ItemAddfriendActivity("", "",facebookid, username, profilephoto);
                    mListContent.add(item);

                    String fbmax = DataUtils.getPreference("maxcount","");
                    if(str.equals(fbmax)){
                        // discuss progress bar.

                        // here we got the friend list.
                        mFriendlistAdapter = new AddfriendListAdapter(AddFriendActivity.this, mListContent);
                        // click event
                        mListView.setAdapter(mFriendlistAdapter);
                        mListView.setOnItemClickListener(AddFriendActivity.this);
                    }
                    // here we got the friend list.
                }
            }

        });


    }
    protected void showdialog(){

        String appLinkUrl, previewImageUrl;

        appLinkUrl = "https://www.mydomain.com/myapplink";
        previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }
    }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            ItemAddfriendActivity item;
//            if(isSearch == 0){
//                item = mFriendListContent.get(position);
//                String facebookid = item.getFacebookid();
//                String kine = item.getDescription();
//                if(kine.equals("invite")){
//                    showdialog();
//                }else{
//                    String match = mFriendListContent.get(position).getFacebookid();// match fbid
//                    String destination = "friendadd";// requst content FRIENDREQUEST
//                    String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
//                    ServerManager.sendAddMatchRequset(myfacebookid, match, destination, new ResultCallBack() {
//                        @Override
//                        public void doAction(LogicResult result) {
//                            Toast.makeText(AddFriendActivity.this, "Send Add Friend Request", Toast.LENGTH_SHORT).show();
//                        } // doAction
//                    }); // ServerManager. sendAddMatchRequest

//            Toast.makeText(AddFriendActivity.this, "inner click",Toast.LENGTH_SHORT).show();
//                    facebookid = facebookid + "^friend";
//                    Bundle bundle = new Bundle();
//                    bundle.putString("person",facebookid);
//                    Intent intent = new Intent(AddFriendActivity.this, FriendProfileActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
//                    finish();
//                };
//
//
//            }else{
//                item = mSearchResult.get(position);
//                String facebookid = item.getFacebookid();
//                String kine = item.getDescription();
////            Toast.makeText(AddFriendActivity.this, "inner click",Toast.LENGTH_SHORT).show();
//                    facebookid = facebookid + "^non";
//                    Bundle bundle = new Bundle();
//                    bundle.putString("person",facebookid);
//                    Intent intent = new Intent(AddFriendActivity.this, FriendProfileActivity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
//                    finish();
//                };

        }

    protected void swip(){

        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(AddFriendActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                Intent intent = new Intent(AddFriendActivity.this,FriendActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
                finish();
            }
            public void onSwipeLeft() {
//                Intent intent = new Intent(AddFriendActivity.this,MatchsMessageActivity.class);
//                startActivity(intent);
            }
            public void onSwipeBottom() {
            }

        });
    }

    protected void  gotoAddMatch(){
        // here should send add friend message.
        Bundle bundle = new Bundle();
        ActivityManager.changeActivity(AddFriendActivity.this,AddMatchesActivity.class, bundle,false,null);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        finish();
    };

//    private class FriendListAdapter extends ArrayAdapter<String> {
//
//        public FriendListAdapter(Context context, List item) {
//            super(context, R.layout.addfriend_listview_row, R.id.txt_friend_name, item);
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            View row = convertView;
//            final ViewHolder holder;
//            final int itemposition = position;
//
//            // Check if a view can be reused, otherwise inflate a layout and set up the view holder
//            if (row == null) {
//                // Inflate view from layout file
////                row = getLayoutInflater().inflate(R.layout.activity_friend, null);
//                row = getLayoutInflater().inflate(R.layout.addfriend_listview_row, null);
//            }
//                // Set up holder and assign it to the View
//                holder = new ViewHolder();
//                holder.textFriendName = (TextView) row.findViewById(R.id.txt_friend_name);
//                holder.imgFriendPhoto = (ImageView) row.findViewById(R.id.img_profile_photo);
//                holder.btnDialog = (Button) row.findViewById(R.id.btn_dialog);
//                // Set holder as tag for row for more efficient access.
//                row.setTag(holder);
//
//                String item = getItem(position);
//
//                // Set the text label for this item
////            holder.textFriendName.setText(item.optString(Const.FACEBOOKID));
//            holder.textFriendName.setText(item);
//                // set the Friends com.dating.reveal.photo
////            holder.imgDialog.setText(item.comment);
//
//                // set onclicklistener
//                holder.textFriendName.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // goto Profile
//                        String herfacebookid = mListContent.get(position);
//                        gotoProfile(herfacebookid);
//                    }
//                });
//
//                holder.imgFriendPhoto.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //goto profile
//                        String herfacebookid = mListContent.get(position);
//                        gotoProfile(herfacebookid);
//                    }
//                });
//
//                 holder.btnDialog .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // show dialog send add friend message.
//                    }
//                });
//            return row;
//        }
//    }
//
//    class ViewHolder {
//        TextView textFriendName ;
//        ImageView imgFriendPhoto;
//        Button btnDialog ;
//    }
    protected void  gotoProfile(String herfacebookid){
        ServerManager.getPersonInfo(herfacebookid, new ResultCallBack(){
            @Override
            public void doAction(LogicResult result){
                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userInfo = result.getData();
                    JSONObject personProfile = userInfo.optJSONObject("content");
                    String person = userInfo.optString("content");
                    String aa = person.substring(1,person.length()-1);
                    Bundle bundle = new Bundle();
                    bundle.putString("person", person);
                    Intent intent = new Intent(AddFriendActivity.this, FriendProfileActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


}
