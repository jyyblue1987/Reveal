package com.dating.reveal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dating.reveal.ListAdapter.Item;
import com.dating.reveal.ListAdapter.LikeListAdapter;
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
public class LikeShowActivity extends HeaderActivity
{

    ListView mlistView;
    ArrayList<com.dating.reveal.ListAdapter.Item> mListContent;
    LikeListAdapter mListAdapter;

    ProgressDialog progressDialog;

    String likes;
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
        setContentView(R.layout.activity_like_show);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));


        Bundle intent = getIntent().getExtras();
        likes = intent.getString("likes");

        LoadParendActivity();
        getNotificationsize();
        initList();
    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
        findView();
        layoutControls();
        initEvent();
        initView();
    }
    protected void findView(){
        super.findView();
        mlistView = (ListView) findViewById(R.id.list);
    };

    protected void layoutControls(){
        super.layoutControls();
    }

    protected void initEvent(){
        super.initEvent();
    }

    protected void initView(){
        super.initView();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");
    }

    protected void initList(){
        // if the like count is 0.
        if(likes.equals("")){
            return;
        }
        mListContent = new ArrayList<>();
        String[] users = likes.split("\\^");
        int szstrings = users.length;
        String fblength =String.valueOf(szstrings);
        DataUtils.savePreference("maxcount",fblength);
        String sss = DataUtils.getPreference("maxcount","0");


        for(int x=0; x < users.length; x++){
            String fbid_name = users[x]; // facebookid & username

            String[] spiltfb_id_name =fbid_name.split("\\&");  // facebookid, username
            // the likes does not have the standard type.
            if(spiltfb_id_name.length <2){
                int length = Integer.parseInt(DataUtils.getPreference("maxcount","0"));
                length--;
                String len = String.valueOf(length);
                DataUtils.savePreference("maxcount", len);
                continue;
            }
            String facebookid = spiltfb_id_name[0];  // facebookid
            String username = spiltfb_id_name[1];    // username
            com.dating.reveal.ListAdapter.Item item = new Item(username, "", facebookid, "");  // username, , facebookid
            mListContent.add(item);

//            getProfilephoto(x, facebookid);

        }/// for
        mListAdapter = new LikeListAdapter(LikeShowActivity.this, mListContent);// create LikeListAdapter
        mlistView.setAdapter(mListAdapter);// attatch listview to adapter
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // On item click listener.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String facebookid = mListContent.get(position).getFacebookid();
                gotoFriendProfile(facebookid);
            }// onItemClick
        }); //OnItemClickListener

    }// initList
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
                        Toast.makeText(LikeShowActivity.this, "Sorry, could not find person information.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject jsonObject = userinfo.optJSONObject(0);
                    String username = jsonObject.optString("name", "");
                    String profilephoto = jsonObject.optString("profilephoto", "");
                    com.dating.reveal.ListAdapter.Item item = new Item(username, "", facebookid, profilephoto);  // username, , facebookid
                    mListContent.add(item);

                    String sss = DataUtils.getPreference("maxcount","0");

                    String fbmax = String.valueOf(DataUtils.getPreference("maxcount","0"));
                    if(str.equals(sss)){
                        // discuss progress bar.
                        progressDialog.dismiss();

                        mListAdapter = new LikeListAdapter(LikeShowActivity.this, mListContent);// create LikeListAdapter
                        mlistView.setAdapter(mListAdapter);// attatch listview to adapter
                        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // On item click listener.
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String facebookid = mListContent.get(position).getFacebookid();
                                gotoFriendProfile(facebookid);
                            }// onItemClick
                        }); //OnItemClickListener
                     }
                }
            }

        });
    }



    // goto friendprofile page.
    protected void gotoFriendProfile(String facebookid){
        String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");

        if(facebookid.equals(myfacebookid)){
            gotoMyProfile();
            return;
        }

        String person = facebookid + "^" + "friend";
        Bundle bundle = new Bundle();
        bundle.putString("person", person);
        Intent intent = new Intent(LikeShowActivity.this, FriendProfileActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
// Backward.

    }// gotoFriendProfile

    protected void gotoMyProfile(){
        Bundle bundle = new Bundle();
        String photopath = "";
        bundle.putString("photopath",photopath);
        Intent intent = new Intent(LikeShowActivity.this, MySelfPhotoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
// backward.
    }
}// end of Activity
