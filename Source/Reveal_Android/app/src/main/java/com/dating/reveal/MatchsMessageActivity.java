package com.dating.reveal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dating.reveal.commom.Comment;
import com.dating.reveal.commom.CommentAdapter;
import com.dating.reveal.commom.Member;
import com.dating.reveal.commom.MemberAdapter;
import com.dating.reveal.commom.OnSwipeTouchListener;
import com.dating.reveal.commom.RecyclerItemClickListener;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.DataUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JonIC on 2016-11-07.
 */
/// here i did not set onclicklistener on the list items,. i will do it in the future.
    // listener should to to chat page with that message and go to profile page with that matches data.
public class MatchsMessageActivity extends HeaderActivity//AppCompatActivity
         {


    ArrayList<Member> members = new ArrayList<>();
    MemberAdapter adapter;


    ArrayList<Comment> androidVersions = new ArrayList<>();
    CommentAdapter commentAdapter;

    // this is my new message array.
    private JSONArray newMessageJsonArray = new JSONArray();

    // this is my match list
    private List<String> names = new ArrayList<>();
    private final String comments[] = {
    };

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerView mrecyclerView;
    private RecyclerView.LayoutManager mlayoutManager;
    private RelativeLayout relativeLayout;
    private ProgressBar bar;
    ProgressDialog progressDialog ;
    ProgressDialog progressDialog2 ;
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
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        setContentView(R.layout.activity_matches_message);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));


        ///////////////////////// my code /////////////////////////
        LoadParendActivity();
//        new ProgressTask().execute();
        getNotificationsize();
        initData();

        ///////////////////////// my code /////////////////////////


        RelativeLayout drawer = (RelativeLayout) findViewById(R.id.lay_container);

        drawer.setOnTouchListener(new OnSwipeTouchListener(MatchsMessageActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                Intent intentright = new Intent(MatchsMessageActivity.this,NewFeedActivity_candidate.class);
                startActivity(intentright);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
                finish();
            }
            public void onSwipeLeft() {
                Intent intent = new Intent(MatchsMessageActivity.this,FriendActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);//forward
                finish();
            }
            public void onSwipeBottom() {
            }

        });
    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
        bar = (ProgressBar) findViewById(R.id.progressBar);

    }
    protected void findView(){
        super.findView();
        bar = (ProgressBar) findViewById(R.id.progressBar);
    }

    protected void layoutControls(){
        super.layoutControls();
    }

    protected void initEvent(){
        super.initEvent();
    }

    protected void initView(){
        super.initView();
        recyclerView = (RecyclerView)findViewById(R.id.recy);
        mrecyclerView = (RecyclerView)findViewById(R.id.recy2);
        recyclerView.setHasFixedSize(true);
        mrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(MatchsMessageActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mlayoutManager = new LinearLayoutManager(MatchsMessageActivity.this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        mrecyclerView.setLayoutManager(mlayoutManager);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
        return true;
    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
////        // Handle navigation view item clicks here.
////        int id = item.getItemId();
////
////        if (id == R.id.nav_camera) {
////            // Handle the camera action
////        } else if (id == R.id.nav_gallery) {
////
////        } else if (id == R.id.nav_slideshow) {
////
////        } else if (id == R.id.nav_share) {
////
////        } else if (id == R.id.nav_send) {
////
////        }
////
////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    private ArrayList<Member> prepareData(){

        ArrayList<Member> android_version = new ArrayList<>();
        for(int i=0;i<names.size();i++){
            Member member = new Member();
            member.setName(names.get(i));
            android_version.add(member);
        }
        return android_version;
    }
//    private ArrayList<Comment> prepData(){
//
//        ArrayList<Comment> android_version = new ArrayList<>();
//        for(int i=0;i<names.length;i++){
//            Comment androidVersion = new Comment();
//            androidVersion.setName(names[i]);
//            androidVersion.setComment(comments[i]);
//            android_version.add(androidVersion);
//        }
//        return android_version;
//    }
private ArrayList<Comment> prepData(){

    progressDialog2= new ProgressDialog(MatchsMessageActivity.this);

    progressDialog2.setMessage("Loading");
    progressDialog2.setIndeterminate(true);
    progressDialog2.setCancelable(false);
    progressDialog2.setTitle("");
    progressDialog2.setMessage("Please wait...");

    ArrayList<Comment> android_version = new ArrayList<>();
    JSONArray emptyJsonArray = new JSONArray();
    if(!newMessageJsonArray.equals(emptyJsonArray)){
        String fblength = String.valueOf(newMessageJsonArray.length()-1);
        DataUtils.savePreference("maxcount_message",fblength);


        for(int i=0;i<newMessageJsonArray.length();i++){
            Comment androidVersion = new Comment();
            JSONObject json = newMessageJsonArray.optJSONObject(i);
            String facebookid = json.optString("sender","");
            String message = json.optString("feedval");
            getProfilephoto_Message(i, facebookid, message);

        }
    }
    return android_version;
}

    protected void getProfilephoto_Message(final int x, final String facebookid, final String message){
        ServerManager.getPersonInfo(facebookid, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {

                if (result.mResult == LogicResult.RESULT_OK) {
                    String str = String.valueOf(x);

                    JSONObject jsonResult = result.getData();
                    JSONArray userinfo = jsonResult.optJSONArray("content");

                    JSONArray empty = new JSONArray();
                    if (userinfo.equals(empty)) {
                        Toast.makeText(MatchsMessageActivity.this, "Sorry, could not find person information.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject jsonObject = userinfo.optJSONObject(0);
                    String username = jsonObject.optString("name", "");
                    String profilephoto = jsonObject.optString("profilephoto", "");
                    Comment item = new Comment();
                    //"", "",facebookid, username, profilephoto);

                    item.setName(username);
                    item.setFacebookid(facebookid);
                    item.setProfilephoto(profilephoto);
                    item.setComment(message);

                    androidVersions.add(item);

                    String fbmax = DataUtils.getPreference("maxcount_message","");
                    if(str.equals(fbmax)){
                        // discuss progress bar.
                        progressDialog2.dismiss();

                        // messages
//                        androidVersions = prepData();
                        commentAdapter = new CommentAdapter(getApplicationContext(),androidVersions);
                        mrecyclerView.addOnItemTouchListener(
                                new RecyclerItemClickListener(MatchsMessageActivity.this, mrecyclerView ,
                                        new RecyclerItemClickListener.OnItemClickListener() {
                                            @Override public void onItemClick(View view, int position) {
                                                JSONObject jsonObject = newMessageJsonArray.optJSONObject(position);
                                                String facebookid = jsonObject.optString("sender","");
                                                checkblock(facebookid);
                                            }

                                            @Override public void onLongItemClick(View view, int position) {
                                                // do whatever
                                            }
                                        })
                        );

                        mrecyclerView.setAdapter(commentAdapter);


//                        mFriendlistAdapter = new FriendListAdapter(FriendActivity.this, mListContent);
//                        mListView.setAdapter(mFriendlistAdapter);
                    }
                    // here we got the friend list.
                }
            }

        });
    }
             protected void checkblock(final String matchfaceboolid){

                 // show progressdialog

                 final ProgressDialog progressDialog ;
                 progressDialog = new ProgressDialog(MatchsMessageActivity.this);
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
                            // goto chat page;
                             String person  = "{\"facebookid\":\"" +matchfaceboolid+ "\"}";
                             Bundle bundle = new Bundle();
                             bundle.putString("person", person);
                             Intent intent = new Intent(MatchsMessageActivity.this, ChatActivity.class);
                             intent.putExtras(bundle);
                             startActivity(intent);

                         }else if(result.mResult == 301){
                             // if this user is not blocked or block you then you can chat with that match.
                             JSONObject userinfo = result.getData();
                             JSONObject data = userinfo.optJSONObject("content");
                             String blockmessage = data.optString("blocksort");
                             String blocksort  = "This user is blocked for \"" + blockmessage + "\"";
                             Toast.makeText(MatchsMessageActivity.this, blocksort, Toast.LENGTH_LONG).show();
                         }else{
                             Toast.makeText(MatchsMessageActivity.this,"Server Error", Toast.LENGTH_LONG).show();
                         }
                     }
                 });
             };


             // 2016-11-27

             protected void getMatches(){
                 progressDialog = new ProgressDialog(this);
                 progressDialog.setIndeterminate(true);
                 progressDialog.setCancelable(false);
                 progressDialog.setMessage("Please wait...");

                 progressDialog.show();

                 final String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                 ServerManager.getMatch(facebookid, new ResultCallBack(){
                     @Override
                     public void doAction(LogicResult result) {
                         progressDialog.dismiss();
                         if(result.mResult == LogicResult.RESULT_OK){
                             JSONObject userinfo = result.getData();
                             JSONArray matches = userinfo.optJSONArray("content");
                             makelistContent(matches);
                         }
                     }
                 });

             }

             protected void makelistContent(JSONArray matche){

                 for(int ma = 0; ma < matche.length(); ma++){
                     JSONObject person = matche.optJSONObject(ma);
                     String facebookid1 = person.optString("facebookid1");
                     String facebookid2 = person.optString("facebookid2");
                     String name1 = person.optString("name1");
                     String name2 = person.optString("name2");
                     String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                     String facebookid;
                     String matchname;
                     if(facebookid1.equals(myfacebookid)){
                         facebookid = facebookid2;
                         matchname  = name2;
                     }else{
                         facebookid = facebookid1;
                         matchname  = name1;
                     }

                     Member item = new Member();
                     //"", "",facebookid, username, profilephoto);

                     item.setFacebookid(facebookid);
                     item.setName(matchname);
                     item.setProfilephoto("");

                     members.add(item);


                 }// matches loop
                 adapter = new MemberAdapter(getApplicationContext(),members);
                 recyclerView.addOnItemTouchListener(
                         new RecyclerItemClickListener(MatchsMessageActivity.this, recyclerView ,
                                 new RecyclerItemClickListener.OnItemClickListener() {
                                     @Override public void onItemClick(View view, int position) {
                                         // do whatever
                                         String facebookid = members.get(position).getFacebookid();
                                         checkblock(facebookid);

                                     }

                                     @Override public void onLongItemClick(View view, int position) {
                                         // do whatever
                                     }
                                 })
                 );
                 recyclerView.setAdapter(adapter);
             }

             // my code
    protected void  initData(){
        progressDialog= new ProgressDialog(MatchsMessageActivity.this);

        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");



        // get message list.
//        String notification = DataUtils.getPreference(Const.NOTIFICATION_CONTENT,"");
        initNewFeedData();
        // we get the match list here.
        getMatches();
//        progressDialog.show();
//
//
//        String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
//        ServerManager.getMatch(facebookid, new ResultCallBack(){
//            @Override
//            public void doAction(LogicResult result) {
//
//                if(result.mResult == LogicResult.RESULT_OK){
//                    JSONObject userInfo = result.getData();
////                    JSONObject personProfile = userInfo.optJSONObject("content");
//                    String rawmatchdata = userInfo.optString("content");
//                    String[] strings = rawmatchdata.split("\\^");
//
//                    String fblength = String.valueOf(strings.length-1);
//                    DataUtils.savePreference("maxcount",fblength);
//
//                    for(int x = 1; x<strings.length; x++){
//                        if(strings[x].equals("")){
//                            int matchsize = Integer.parseInt(DataUtils.getPreference("maxcount",""));
//                            matchsize--;
//                            DataUtils.savePreference("maxcount", matchsize);
//                        }else{
//                            getProfilephoto(x, strings[x]);
//                        }
//                    }
//
////                    // messages
//                    androidVersions = prepData();
//
//                }else {
//                    JSONObject info = result.getData();
//                    progressDialog.dismiss();
////                    JSONObject error = info.optJSONObject("content");
//                    String errorstr = info.optString("error_msg");
//                    Toast.makeText(MatchsMessageActivity.this, errorstr, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//    protected void getProfilephoto(final int x, final String facebookid){
//        ServerManager.getPersonInfo(facebookid, new ResultCallBack() {
//            @Override
//            public void doAction(LogicResult result) {
//
//                if (result.mResult == LogicResult.RESULT_OK) {
//                    String str = String.valueOf(x);
//
//                    JSONObject jsonResult = result.getData();
//                    JSONArray userinfo = jsonResult.optJSONArray("content");
//
//                    JSONArray empty = new JSONArray();
//                    if (userinfo.equals(empty)) {
//                        Toast.makeText(MatchsMessageActivity.this, "Sorry, could not find person information.", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    JSONObject jsonObject = userinfo.optJSONObject(0);
//                    String username = jsonObject.optString("name", "");
//                    String profilephoto = jsonObject.optString("profilephoto", "");
//                    Member item = new Member();
//                    //"", "",facebookid, username, profilephoto);
//
//                    item.setFacebookid(facebookid);
//                    item.setName(username);
//                    item.setProfilephoto(profilephoto);
//
//                    members.add(item);
//
//                    String fbmax = DataUtils.getPreference("maxcount","");
//
//                    if(str.equals(fbmax)){
//                        // discuss progress bar.
//                        progressDialog.dismiss();
//                        adapter = new MemberAdapter(getApplicationContext(),members);
//                        recyclerView.addOnItemTouchListener(
//                                new RecyclerItemClickListener(MatchsMessageActivity.this, recyclerView ,
//                                                                            new RecyclerItemClickListener.OnItemClickListener() {
//                                    @Override public void onItemClick(View view, int position) {
//                                        // do whatever
//                                        String facebookid = members.get(position).getFacebookid();
//                                        checkblock(facebookid);
//
//                                    }
//
//                                    @Override public void onLongItemClick(View view, int position) {
//                                        // do whatever
//                                    }
//                                })
//                        );
//                        recyclerView.setAdapter(adapter);
//                    }
//                        // here we got the friend list.
//                }
//            }
//
//        });
    }

    protected void initNewFeedData(){
        String strNewFeed = DataUtils.getPreference(Const.NOTIFICATION_CONTENT, "");
        // if the string is null.
        if(strNewFeed.equals("") || strNewFeed.equals("[]")){
//            Toast.makeText(MatchsMessageActivity.this, "There is no New Message.", Toast.LENGTH_SHORT).show();
            return;
        }
        newMessageJsonArray= new JSONArray();
        JSONArray jsonArrayNewFeed = parseJson(strNewFeed);
        JSONArray emptyJsonArray = new JSONArray();
        // if the parsing failed
        if(jsonArrayNewFeed.equals(emptyJsonArray)){
//            Toast.makeText(MatchsMessageActivity.this, "There is no New Message.", Toast.LENGTH_SHORT).show();
            return;
        }
        for(int x = 0; x < jsonArrayNewFeed.length(); x++){
            JSONObject jsonO = jsonArrayNewFeed.optJSONObject(x);
            String notetype = jsonO.optString("notekind");
            if(notetype.equals("message")){
                newMessageJsonArray.put(jsonO);
            }
        }

        // here we got the message notification jsonobject. Next we should get the string to use recyclerview.
    }

    protected JSONArray parseJson(String receivephoto){
        try {
            JSONArray jsonarray = new JSONArray(receivephoto);
            return jsonarray;
        }catch(Throwable t){
            JSONArray jsonArray = new JSONArray();
            return jsonArray;
        }
    }
}
