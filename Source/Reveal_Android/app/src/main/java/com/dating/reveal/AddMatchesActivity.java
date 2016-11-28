package com.dating.reveal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dating.reveal.ListAdapter.AddmatchListAdapter;
import com.dating.reveal.ListAdapter.ItemAddmatchActivity;
import com.dating.reveal.commom.OnSwipeTouchListener;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.DataUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JonIC on 2016-11-08.
 */
public class AddMatchesActivity extends HeaderActivity
{
    RelativeLayout relativeLayout;

    ListView mListView = null;
    AddmatchListAdapter mFriendlistAdapter = null;
    ArrayList<ItemAddmatchActivity> mListContent = null;
    String request = "";
    ProgressDialog progressDialog ;

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
        setContentView(R.layout.activity_add_matches);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        LoadParendActivity();
        getNotificationsize();
        getmatch();
//        getFriend();
        swip();
    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
    }
    protected void layoutControls(){
        super.layoutControls();
    }

    protected void initEvent(){

        super.initEvent();
        m_img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddFriendPage();
            }
        });

    }
    protected void findView()
    {
        super.findView();
        bar = (ProgressBar) findViewById(R.id.progressBar);
        mListView = (ListView)findViewById(R.id.list);
        relativeLayout = (RelativeLayout)findViewById(R.id.lay_container);
    }


    protected void  gotoAddFriendPage(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(AddMatchesActivity.this, AddFriendActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        finish();
    }
    protected void initView(){
        super.initView();
        m_img_back_arrow.setVisibility(View.VISIBLE);
        m_img_three.setVisibility(View.INVISIBLE);
    }

    // 2016-11-27
    protected void getmatch(){
        progressDialog= new ProgressDialog(AddMatchesActivity.this);

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
// 2016-11-27
    protected void makelistContent(JSONArray matche){
        mListContent = new ArrayList<>();

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

            String notification_content = DataUtils.getPreference(Const.NOTIFICATION_CONTENT, "");

            String description = "add";
            try{
                JSONArray jsonArray = new JSONArray( notification_content);
                for(int x = 0; x < jsonArray.length(); x++){
                    JSONObject jsonObject1  = jsonArray.getJSONObject(x);
                    String sender = jsonObject1.optString("sender","");
                    String notekind = jsonObject1.optString("notekind", "");
                    if(sender.equals(facebookid) && notekind.equals(Const.FRIENDREQUEST)){
                        description = Const.FRIENDREQUEST;
                    }
                } // notification loop
            }catch (JSONException e){

            }

            ItemAddmatchActivity item = new ItemAddmatchActivity("", description,facebookid, matchname, "");
            mListContent.add(item);

        }// matches loop
        initList();
    }
//    protected void  getFriend(){// in fact we search match.
//
//        progressDialog.show();
//        final String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
//        ServerManager.getMatch(facebookid, new ResultCallBack(){
//            @Override
//            public void doAction(LogicResult result) {
//                if(result.mResult == LogicResult.RESULT_OK){
//                    JSONObject userInfo = result.getData();
//                    JSONArray personProfile = userInfo.optJSONArray("content");
////                    String rawmatchdata = personProfile.toString().trim();
//                    String rawmatchdata = userInfo.optString("content");  // facebookid sequence of matches
//                    if(rawmatchdata == null || rawmatchdata.equals("")){
//                        Toast.makeText(AddMatchesActivity.this, "Sorry could not find that match.",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    String[] facebookids = rawmatchdata.split("\\^"); // the facebookid sequence of the matches
//                    mListContent = new ArrayList<>();
//
//                    String fblength = String.valueOf(facebookids.length-1);
//                    DataUtils.savePreference("maxcount",fblength);
//
//                    for(int x = 1; x<facebookids.length; x++){
//                        final String facebookid = facebookids[x];
//
//                        //////////////////////////////////////////////////////////////////
//                        getProfilephoto(x, facebookid);
//                        //////////////////////////////////////////////////////////////////
//                    }
//                    // here we got the matches list.
//                }else{
//                    progressDialog.dismiss();
//                }
//            }
//        });
//    }
//    protected void getProfilephoto(final int x, final String facebookid){
//        ServerManager.getPersonInfo(facebookid, new ResultCallBack() {
//            @Override
//            public void doAction(LogicResult result) {
//                findAddfriendRequest(facebookid);
//
//                if (result.mResult == LogicResult.RESULT_OK) {
//                    String str = String.valueOf(x);
//
//                    JSONObject jsonResult = result.getData();
//                    JSONArray userinfo = jsonResult.optJSONArray("content");
//
//                    JSONArray empty = new JSONArray();
//                    if (userinfo.equals(empty)) {
//                        Toast.makeText(AddMatchesActivity.this, "Sorry, could not find person information.", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    JSONObject jsonObject = userinfo.optJSONObject(0);
//                    String username = jsonObject.optString("name", "");
//                    String profilephoto = jsonObject.optString("profilephoto", "");
//                    // here check the notification.
//                    String notification_content = DataUtils.getPreference(Const.NOTIFICATION_CONTENT, "");
//                    String description = "add";
//                    try{
//                        JSONArray jsonArray = new JSONArray( notification_content);
//                        for(int x = 0; x < jsonArray.length(); x++){
//                            JSONObject jsonObject1  = jsonArray.getJSONObject(x);
//                            String sender = jsonObject1.optString("sender","");
//                            String notekind = jsonObject1.optString("notekind", "");
//                            if(sender.equals(facebookid) && notekind.equals(Const.FRIENDREQUEST)){
//                                description = Const.FRIENDREQUEST;
//                            }
//                        }
//                    }catch (JSONException e){
//
//                    }
//
//                    ItemAddmatchActivity item = new ItemAddmatchActivity("", description,facebookid, username, profilephoto);
//                    mListContent.add(item);
//
//                    String fbmax = DataUtils.getPreference("maxcount","");
//                    if(str.equals(fbmax)){
//                        // discuss progress bar.
//                        progressDialog.dismiss();
//                        initList();
//                    }
//                    // here we got the friend list.
//                }
//            }
//
//        });
//    }



    protected void initList(){

        mFriendlistAdapter = new AddmatchListAdapter(AddMatchesActivity.this, mListContent);
        mListView.setAdapter(mFriendlistAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(AddMatchesActivity.this, "asdkkf", Toast.LENGTH_SHORT).show();
                String facebookid = mListContent.get(position).getFacebookid();
                facebookid = facebookid + "^match";
                Bundle bundle = new Bundle();
                bundle.putString("person",facebookid);
                Intent intent = new Intent(AddMatchesActivity.this, FriendProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
// see if there is a notificaton from match to add him to friend group.
    protected void findAddfriendRequest(String matchid){
        String noticon = DataUtils.getPreference(Const.NOTIFICATION_CONTENT,"");
        request = "";
        try{
            JSONArray jsonArray = new JSONArray(noticon);
            for(int x=0; x < jsonArray.length(); x++){
                JSONObject jsonObject = jsonArray.optJSONObject(x);
                String sender = jsonObject.optString("sender","");
                String notekind = jsonObject.optString("notekind","");
                if(notekind.equals("requestfriend") )
                    if(matchid.equals(sender)){
                        request =  "requestfriend";
                }
            }
        }catch (JSONException e){
        }

    }

    protected void swip(){
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(AddMatchesActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                Intent intent = new Intent(AddMatchesActivity.this,AddFriendActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
                finish();
            }
            public void onSwipeLeft() {
//                Intent intent = new Intent(AddMatchesActivity.this,MatchsMessageActivity.class);
//                startActivity(intent);
            }
            public void onSwipeBottom() {
            }

        });
    }
    protected void  gotoProfile(String herfacebookid){
        ServerManager.getPersonInfo(herfacebookid, new ResultCallBack(){
            @Override
            public void doAction(LogicResult result){
                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userInfo = result.getData();
                    JSONObject personProfile = userInfo.optJSONObject("content");
//                    String person = personProfile.toString().trim();
                    String person = userInfo.optString("content");
                    if(person == null || person.equals("")){
                        Toast.makeText(AddMatchesActivity.this, "Sorry could not find that match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("person", person);
                    Intent intent = new Intent(AddMatchesActivity.this, FriendProfileActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
