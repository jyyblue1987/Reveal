package com.dating.reveal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.reveal.ListAdapter.CommentListAdapter;
import com.dating.reveal.ListAdapter.Item;
import com.dating.reveal.commom.OnSwipeTouchListener;
import com.dating.reveal.image.ImageUtils;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.network.ServerTask;
import com.dating.reveal.utility.DataUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by JonIC on 2016-11-07.
 */
public class NewFeedActivity extends Activity{
    RelativeLayout relativeLayout;
    ImageView m_imgPhoto;
    TextView mAverageRate;
    TextView mAboutPhoto;
    TextView mPhotoPath;
    TextView mCommentSize;
    TextView mLikeSize;
    ImageView mimgLike;
    ImageView mimgComment;
    EditText mEditcomment;
    ListView commentList;
    TextView mSendComment;

    int position= 0;
    String photopath="";
    String facebookid="";
    String comment = "";
    String liked= "";

    JSONArray jsonNewFeed;

    ArrayList<Item> commentDataList;
    CommentListAdapter mCommentListAdapter;
    ListView mListView;

    String strNewFeed= "";

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
        setContentView(R.layout.activity_newfeed);
        initNewFeedData();
        findView();
//        initshow();
        initEvent();
        swip();

    }

    //
    protected void initEvent(){
        mimgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send like post about this photo.
                String comment = "";
                String like = "like";
                String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                String sendname = DataUtils.getPreference(Const.NAME,"");
                sendCommentLike(comment, facebookid, photopath,myfacebookid, like, sendname);
            }
        });
        mLikeSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display likes that is gotoLike page.
                // here you should get single photo's data.
                // you have already photo data so you only show the data.
                Bundle bundle = new Bundle();
                bundle.putString("likes", liked);

                Intent intent = new Intent(NewFeedActivity.this, LikeShowActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
//                ServerManager.singlePhoto(facebookid, photopath, new ResultCallBack() {
//                    @Override
//                    public void doAction(LogicResult result) {
//                        if(result.mResult == LogicResult.RESULT_OK){
//                            JSONObject userInfo = result.getData();
//
//                            // if receive the data then go to the next screen.
//                            // before goto there save the setting
//                            JSONArray retcontent  = userInfo.optJSONArray("content");
//                            JSONObject json = retcontent.optJSONObject(0);
//                            String feed = json.optString("likefacebookid");
//                            if(feed.equals("")){
//                                Toast.makeText(NewFeedActivity.this, "There is no like or server error.",Toast.LENGTH_SHORT).show();
//                            }else{
//                                Bundle bundle = new Bundle();
//                                bundle.putString("likes", feed);
//
//                                Intent intent = new Intent(NewFeedActivity.this, LikeShowActivity.class);
//                                intent.putExtras(bundle);
//
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
//                            }
//
//                        }
//                    }
//                });
            }
        });
        mimgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display commentlist
                if(!strNewFeed.equals("[]") && !strNewFeed.equals("")){
                    return;
                }
                // init the comment data list
                String[] commentnodes  = comment.split("^");
                for(int x = 0; x < commentnodes.length; x++){
                    String commentNode = commentnodes[x];
                    String[] commentContent  = commentNode.split("&");
//                    JSONObject json = new JSONObject();
//                    try{
//                        json.put("facebookid", commentContent[0]);
//                        json.put("comment",commentContent[1]);
//                    }catch (JSONException e){
//
//                    }
                    Item item = new Item(commentContent[0], commentContent[1],facebookid,"");
                    commentDataList.add(item);
                }
                //here we should show list.
                initList();
            }
        });
        mSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // upload the comment about the photo
                String comment = (mEditcomment.getText()).toString().trim();
                if(comment.equals("")){return;}
                String like = "";
                comment = comment + "&" + facebookid;
                String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                String sendname = DataUtils.getPreference(Const.NAME,"");
                sendCommentLike(comment, facebookid, photopath,myfacebookid, like, sendname);
            }
        });
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    protected void initList(){
        mCommentListAdapter = new CommentListAdapter(NewFeedActivity.this, commentDataList);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                JSONObject json= jsonNewFeed.optJSONObject(position);
//                String strPerson = json.toString();
                gotoFriendProfilePage(json);
//                CheckedTextView m = (CheckedTextView)view;
//                if(m.isChecked()){
//                    m.setChecked(false);
//                }else{
//                    m.setChecked(true);
//                }
//
//                CommentListAdapter bla = (CommentListAdapter) parent.getAdapter();
//                bla.setCheckedItem(position);
//                String item = (String) listView.getItemAtPosition(position);
//                Toast.makeText(ChatCategory.this, item, Toast.LENGTH_LONG).show();
            }
        });
//
//        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                ListView listView = (ListView) parent;
//                String item = (String) listView.getSelectedItem();
////                Toast.makeText(ChatCategory, item, Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        mListView.setAdapter(mChatlistAdapter);
    }

    protected void gotoFriendProfilePage(JSONObject person){
        if(person.equals(null) || person.equals("")){return;}
//        String friendfacebookid = person.optString(Const.FACEBOOKID);
        String friendfacebookid = person.optString("sender");
        String friendphotopath  = person.optString("feedval");
        JSONObject json  = new JSONObject();
        try{
        json.put(Const.FACEBOOKID, friendfacebookid);
        json.put(Const.PHOTO_PATH, friendphotopath);
        }catch (JSONException e){

        }
        String strperson = json.toString().trim();
        Bundle bundle = new Bundle();
        bundle.putString("person", strperson);

        Intent intent = new Intent(NewFeedActivity.this, FriendProfileActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);

    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    protected void sendCommentLike(String comment, String facebookid, String photopath,String myfacebookid, String like, String sendname){
        // in this case the like is not selected.
        ServerManager.sendCommentLike(comment, facebookid, photopath, myfacebookid, like, sendname, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                if(result.mResult == LogicResult.RESULT_OK){
                    String info = ("Success");
                    Toast.makeText(NewFeedActivity.this, info, Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject userInfo = result.getData();
                    String info = userInfo.optString("error_msg");
                    Toast.makeText(NewFeedActivity.this, info, Toast.LENGTH_SHORT).show();
                }
            }
        });
    };
    // show first newfeed and
    protected void initshow(){
        // initialize the views
        if(strNewFeed.equals("[]") || strNewFeed.equals("")){
            // hide the views.
            mCommentSize.setVisibility(View.INVISIBLE);
            mLikeSize.setVisibility(View.INVISIBLE);
            mSendComment.setVisibility(View.INVISIBLE);
            mEditcomment.setVisibility(View.INVISIBLE);
            mimgComment.setVisibility(View.INVISIBLE);
            mimgLike.setVisibility(View.INVISIBLE);
        }
        int sizeNewFeed = 0;
        if(!strNewFeed.equals("[]") && !strNewFeed.equals("")){
            sizeNewFeed = jsonNewFeed.length();
        }
        if(sizeNewFeed != 0){
            JSONObject json = jsonNewFeed.optJSONObject(0);
            facebookid = json.optString("sender");
            // please refer to the notification table. if notification kind is newfeed then feedval contains the photo path.
            photopath  = json.optString("feedval");
            getandshowPhoto(facebookid, photopath);
//            position  = (position + 1) % jsonNewFeed.length();
        }
    }

    protected void getandshowPhoto(String face, String path){
        ServerManager.singlePhoto(face, path, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                // if we receive the photo info, show that in new feed page.
                JSONObject userInfo = result.getData();

                // if receive the data then go to the next screen.
                // before goto there save the setting
                String aaa = userInfo.optString("content");
                JSONArray retcontent  = userInfo.optJSONArray("content");
                if(aaa != null && aaa.trim() != ""){
                    JSONObject json = retcontent.optJSONObject(0);
                    String photopath = json.optString(Const.PHOTO_PATH);
                    String ownername = json.optString(Const.NAME);
                    String aboutphoto= json.optString(Const.ABOUT_PHOTO);
                    String commentsize=json.optString(Const.SIZE_COMMENT);
                    String likesize  = json.optString(Const.SIZE_LIKE);
                    comment = json.optString(Const.COMMENT_CONTENT);
                    liked   = json.optString(Const.LIKE_CONTENT);
                    float  ratesum   = Integer.parseInt(json.optString(Const.RATE_SUM, ""));
                    float  ratenum   = Integer.parseInt(json.optString(Const.RATE_NUMBER,""));
                    float average =0;
                    if(ratenum != 0 ){ average = ratesum/ratenum;}
                    average = round(average, 1);
                    String strAverage = String.valueOf(average);

                    // photo path, imageview
                    DisplayImageOptions options = ImageUtils.buildUILOption(R.drawable.pfimage).build();
                    ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + photopath, m_imgPhoto, options);

                    mPhotoPath.setText(photopath);
                    mAboutPhoto.setText(aboutphoto);
                    mCommentSize.setText(commentsize);
                    mLikeSize.setText(likesize);
                    mAverageRate.setText(strAverage);


                }
            }
        });
    }
    // get newfeed jsonarray from notification string.
    protected void initNewFeedData(){
        JSONArray refreshNotification = new JSONArray();
         strNewFeed = DataUtils.getPreference(Const.NOTIFICATION_CONTENT, "");
        // if the string is null.
        if(strNewFeed.equals("[]") || strNewFeed.equals("")){
            Toast.makeText(NewFeedActivity.this, "There is no New Feed.", Toast.LENGTH_SHORT).show();
            return;
        }
        jsonNewFeed= new JSONArray();
        JSONArray jsonArrayNewFeed = parseJson(strNewFeed);
        // if the parsing failed
        if(strNewFeed.equals("[]") || strNewFeed.equals("")){
            Toast.makeText(NewFeedActivity.this, "There is no New Feed.", Toast.LENGTH_SHORT).show();
            return;
        }
        for(int x = 0; x < jsonArrayNewFeed.length(); x++){
            JSONObject jsonO = jsonArrayNewFeed.optJSONObject(x);
            String notetype = jsonO.optString("notekind");
            if(notetype.equals("newfeed")){
                jsonNewFeed.put(jsonO);
            }else{
                refreshNotification.put(jsonO);
            }
        }

        // refresh notification JSONArray. remove the new feed notifications.
        String strRefresh = refreshNotification.toString().trim();
        DataUtils.savePreference(Const.NOTIFICATION_CONTENT, strRefresh);
        // refresh notification number too.
        String notenum = DataUtils.getPreference(Const.NOTIFICATION_SIZE,"");
        int toInt_notenum = Integer.parseInt(notenum);
        int sizeNewfeed = jsonNewFeed.length();
        int refreshnum = toInt_notenum - sizeNewfeed;
        String strRefreshnum = String.valueOf(refreshnum);
        DataUtils.savePreference(Const.NOTIFICATION_SIZE,strRefreshnum);
    }

    protected JSONArray parseJson(String receivephoto){
        try {
            JSONArray jsonarray = new JSONArray(receivephoto);
            return jsonarray;
        }catch(Throwable t){
            return null;
        }
    }

    /**
     * Round to certain number of decimals
     *
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
    protected void findView(){
        relativeLayout = (RelativeLayout)findViewById(R.id.lay_container);
        m_imgPhoto = (ImageView) findViewById(R.id.img_newfeed_photo);
        mAboutPhoto = (TextView)findViewById(R.id.txt_newfeed_about_photo);
        mPhotoPath = (TextView)findViewById(R.id.txt_newfeed_name);
        mCommentSize = (TextView)findViewById(R.id.txt_newfeed_comment);
        mLikeSize = (TextView)findViewById(R.id.txt_newfeed_like);
        mAverageRate=(TextView)findViewById(R.id.txt_newfeed_photo);
        mimgComment = (ImageView)findViewById(R.id.img_newfeed_comment) ;
        mimgLike = (ImageView)findViewById(R.id.img_newfeed_like) ;
        mEditcomment = (EditText)findViewById(R.id.edit_your_comment);
        commentList = (ListView)findViewById(R.id.commentlist);
        mSendComment = (TextView)findViewById(R.id.txt_edit_comment);
        mListView = (ListView)findViewById(R.id.commentlist);

        // photo path, imageview
    }
    protected void swip(){
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(NewFeedActivity.this) {
            public void onSwipeTop() {
                showphoto();
            }
            public void onSwipeRight() {
                Bundle bundle = new Bundle();
                bundle.putString("receivephoto", "1");

                Intent intent = new Intent(NewFeedActivity.this, RatingActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
                // initialize the new feed history.
                DataUtils.savePreference(Const.NOTIFICATION_CONTENT,"");
                finish();
            }
            public void onSwipeLeft() {

                Intent intent = new Intent(NewFeedActivity.this, MatchsMessageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);//forward
                finish();
            }
            public void onSwipeBottom() {
            }

        });
    }
    protected void showphoto(){
        commentDataList = new ArrayList<>();
        if(strNewFeed.equals("[]") || strNewFeed.equals("")){
            Toast.makeText(getApplicationContext(), "There is no photo to show", Toast.LENGTH_SHORT);
            return;
        }
        if(position >= jsonNewFeed.length() || position <= 0){position = 0;}
        position = (position + 1) % jsonNewFeed.length();

        JSONObject photo = jsonNewFeed.optJSONObject(position);
        photopath = photo.optString(Const.PHOTO_PATH);
        facebookid = photo.optString(Const.FACEBOOKID);
        getandshowPhoto(facebookid, photopath);


    }

}
