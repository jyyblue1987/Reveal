package com.dating.reveal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.reveal.ListAdapter.CommentListAdapter;
import com.dating.reveal.commom.OnSwipeTouchListener;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.network.ServerTask;
import com.dating.reveal.utility.DataUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JonIC on 2016-11-08.
 */
public class MySelfPhotoActivity extends HeaderActivity {
    int avaliableshow = 0;

    String strperson;
    // save the my photo data from server
    JSONArray jsonarray;
    JSONObject jsonPerson;

    // after get the profile  with photo data about the input person
    JSONArray personProfile;
    // the position of person's photo that now is shown.
    int position  = 0;
    List<Item1> photos;

    // save the photo's owner facebookid and the photopath
    String facebookid;
    String photopath;
    String comment="";
    String liked= "";
    // screen views
    RelativeLayout relativeLayout;
    ImageView profileImage;
    TextView mPhotoName;
    TextView mAboutPhoto;
    ImageView mImagLike;
    ImageView mImagLikefill;
    TextView mLikeNum;
    ImageView mImgComment;
    ImageView mImagComment_b;
    TextView mCommentNum;
    TextView mSendComment;
    EditText mEditComment;
    ListView mCommentList;
    TextView mAverageRate;
    TextView mAddFriend;
    ImageView m_imgPhoto;

    String photopath_receive  = "";

    String myprofilephoto; // my profile photopath

    TextView mTxtshare;
    //    List<JSONObject> commentDataList;
    ArrayList<com.dating.reveal.ListAdapter.Item> mListContent;

    CommentListAdapter mCommentListAdapter;
    //    FriendListAdapter mCommentListAdapter;
    // progress bar
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
        setContentView(R.layout.activity_myself_photo);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        Bundle intent = getIntent().getExtras();
        // if notificationActivity call this activity then photopath contains liked or commented photopath.
        // else if MyselfProfileActivity call this activity then photopath is nothing.
        photopath_receive = intent.getString("photopath");
        //photopath
        strperson = DataUtils.getPreference(Const.FACEBOOKID,"");
        // initialize the activity.
        LoadParentAtivity();
        getNotificationsize();
        initPhotoData();
        swip();
    }
    protected void LoadParentAtivity(){
        super.LoadParendActivity();
    }

    protected void initView(){
        super.initView();
        m_img_three.setVisibility(View.INVISIBLE);
        m_img_back_arrow.setVisibility(View.VISIBLE);
    };

    protected void swip(){
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(MySelfPhotoActivity.this) {
            public void onSwipeTop() {
//                showPhoto();
            }
            public void onSwipeRight() {
                position = (position  + photos.size() - 1) % photos.size();
                position = (position  + photos.size() - 1) % photos.size();
                 showPhoto();
            }
            public void onSwipeLeft() {
                showPhoto();
            }
            public void onSwipeBottom() {
            }

        });
    }
    protected void findView(){
        super.findView();
        relativeLayout = (RelativeLayout)findViewById(R.id.lay_container);
//        mAddFriend = (TextView)findViewById(R.id.txt_add_friend);
        bar = (ProgressBar) findViewById(R.id.progressBar);


        profileImage     =(ImageView)findViewById( R.id.img_newfeed_profile);
        mPhotoName = (TextView)findViewById(R.id.txt_newfeed_name);
        mAboutPhoto = (TextView)findViewById(R.id.txt_newfeed_about_photo);
        m_imgPhoto = (ImageView) findViewById(R.id.img_newfeed_photo);
        mAverageRate = (TextView)findViewById(R.id.txt_newfeed_photo);
        mImagLike  =(ImageView)findViewById(R.id.img_newfeed_like);
        mImagLikefill = (ImageView) findViewById(R. id. img_newfeed_like_fill);
        mLikeNum = (TextView)findViewById(R.id.txt_newfeed_like);
        mImgComment = (ImageView)findViewById(R.id.img_newfeed_comment);
        mCommentNum = (TextView)findViewById(R.id.txt_newfeed_comment);
        mSendComment = (TextView)findViewById(R.id.txt_edit_comment);
        mEditComment = (EditText)findViewById(R.id.edit_your_comment);
        mCommentList = (ListView)findViewById(R.id.commentlist);
        mTxtshare = (TextView)findViewById(R.id.txt_share);

    }

    private class ProgressTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //my stuff is here
            ImageDown();
            return null;
        };

        @Override
        protected void onPostExecute(Void result) {
            bar.setVisibility(View.GONE);
        }
    }

    protected void initEvent() {
        // if the person is frind show all.
        super.initEvent();


        m_img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySelfPhotoActivity.this, MySelfProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right); //backward
                finish();
            }
        });

        mTxtshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSharepage();
            }
        });

        mImgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // display commentlist

            mCommentList.setVisibility(View.VISIBLE);
            mListContent = new ArrayList<com.dating.reveal.ListAdapter.Item>();
            // init the comment data list
            if(!comment.equals("")) {
                String[] commentnodes = comment.split("\\^");
                for (int x = 0; x < commentnodes.length; x++) {
                    String commentNode = commentnodes[x];
                    String[] commentContent = commentNode.split("\\&");
                    com.dating.reveal.ListAdapter.Item item = new com.dating.reveal.ListAdapter.Item("","","","");
                    if(commentContent.length==3){ // comment structure : (sender name, comment, sender facebookid)
                        item = new com.dating.reveal.ListAdapter.Item(commentContent[0], commentContent[1], commentContent[2],"");
                    }
//                            commentDataList.add(json);
                    mListContent.add(item);
                }
                //here we should show list.
                initList();
            }
            }
        });

        mImgComment.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    mImgComment.setImageResource(R.drawable.comment_b);

                if(event.getAction() == MotionEvent.ACTION_UP)
                    mImgComment.setImageResource(R.drawable.comment);

                return false;
            }
        });

        mLikeNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display likes that is gotoLike page.
                // here you should get single photo's data.
                Bundle bundle = new Bundle();
                bundle.putString("likes", liked);

                Intent intent = new Intent(MySelfPhotoActivity.this, LikeShowActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right); //backward


            }
        });
    }

    protected void initPhotoData(){
        photos = new ArrayList<Item1>();
        if ( strperson.equals("")){
            Toast.makeText(MySelfPhotoActivity.this, "Your facebookid is not correct", Toast.LENGTH_SHORT).show();
        }else{
            // if the person's information is correct get the person's all photo and show that.
            avaliableshow = 1;
            mListContent = new ArrayList<com.dating.reveal.ListAdapter.Item>();
            getMyProfile();
        }
    }
    protected void getMyProfile(){
        final  String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        ////////////////////////
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        ServerManager.getPersonInfo(myfacebookid, new ResultCallBack() {
                    @Override
                    public void doAction(LogicResult result) {
             if (result.mResult == LogicResult.RESULT_OK) {
                    JSONObject ret = result.getData();
                    JSONArray userinfo = ret.optJSONArray("content");
                    JSONArray empty = new JSONArray();
                    if (userinfo.equals(empty)) {
//                        Toast.makeText(viewHolder.image.getContext(), "Sorry, there is not user information.",Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(viewHolder.image.getContext(), "Ok, there is user information.",Toast.LENGTH_SHORT).show();

                    JSONObject jsonObject = userinfo.optJSONObject(0);
                    String username = jsonObject.optString("name", "");
                    myprofilephoto = jsonObject.optString("profilephoto", "");
                    // show my profile photo.


                    ServerManager.getMyProfile(myfacebookid, new ResultCallBack(){
                        @Override
                        public void doAction(LogicResult result){
                            progressDialog.dismiss();

                            if(result.mResult==LogicResult.RESULT_OK){

                                JSONObject userInfo = result.getData();
                                jsonarray = userInfo.optJSONArray("content");
                                JSONArray emptyjsonarray = new JSONArray();
                                JSONObject json  = userInfo.optJSONObject("content");
                                if(!jsonarray.equals(emptyjsonarray)){
                                    // extract photos data from jsonarray and save in photos (List<>)
                                    eXtractDataFromJArray(jsonarray);
                                }else
                                {
                                    Toast.makeText(MySelfPhotoActivity.this, "There is no your photo. Plesse Upload your photo.",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                showPhoto();
                            }
                        }
                                    });
                 }
             }
                    });
    }
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    protected void showPhoto(){

        Item1 item1 = photos.get(position);
//        String photopath = item1.photopath;
        photopath = item1.photopath;
        String name = item1.name;
        comment = item1.comment;
        liked   = item1.likes;
        String commentsize=item1.commentSize;
        String likesize  = item1.likesize;
        String aboutphoto = item1.aboutPhoto;

        String rate = item1.rate;
        float average = Float.parseFloat(rate);
        average = round(average, 1);
        rate = String.valueOf(average);

        String myname = DataUtils.getPreference(Const.NAME,"");
        mPhotoName.setText(myname);
        mAboutPhoto.setText(aboutphoto);
        mCommentNum.setText(commentsize);
        mLikeNum.setText(likesize);
        mAverageRate.setText(rate);

//        new ProgressTask().execute();
        // photo path, imageview
        DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

//        DisplayImageOptions options = ImageUtils.buildUILOption(R.drawable.pfimage).build();

        ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + photopath, m_imgPhoto, optoins);

        String user_id = DataUtils.getPreference(Const.FACEBOOKID,"");
        String imgUrl = "https://graph.facebook.com/" + user_id + "/picture?type=large";

        ImageLoader.getInstance().displayImage(imgUrl, profileImage, optoins);

// download photo. here we simplify and show photopath.
        position = (position + 1) % photos.size();
    }

    protected void ImageDown(){
        DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .build();

//        DisplayImageOptions options = ImageUtils.buildUILOption(R.drawable.pfimage).build();
        ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + photopath, m_imgPhoto, optoins);

        String myprofilephoto = "https://graph.facebook.com/" + facebookid + "/picture?type=large";

        ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + myprofilephoto, profileImage, optoins);
    }
    protected void initList(){
        mCommentList.setVisibility(View.VISIBLE);
        setListViewHeightBasedOnChildren(mCommentList);

        mCommentListAdapter = new CommentListAdapter(MySelfPhotoActivity.this, mListContent);

        mCommentList.setAdapter(mCommentListAdapter);
    }

/// enable to see listview in scroll view
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    protected void eXtractDataFromJArray(JSONArray jsonArray){
        photos= new ArrayList<Item1>();

        for(int i = 0 ; i < jsonArray.length(); i++){
            JSONObject json = jsonArray.optJSONObject(i);
            Item1 Item1 = new Item1();
            Item1.facebookid = json.optString(Const.FACEBOOKID);
            Item1.name  = json.optString("name","");
            Item1.photopath =  json.optString(Const.PHOTO_PATH);
            // if notificationActivity call this Activity then first show the photo that has NotifictionActivity sending photopath.
            if(photopath_receive.equals(json.optString(Const.PHOTO_PATH)))
            {
                position = i;
            }
            Item1.rate =    json.optString("rate","");
            Item1.comment =  json.optString("commentcon");
            Item1.commentSize =  json.optString("commentnum");
            Item1.likesize =  json.optString("likenum");
            Item1.likes =  json.optString("likefacebookid");
            Item1.aboutPhoto =  json.optString("mycomment");
            Item1.ratenum = Float.parseFloat(json.optString("ratenumber"));
            Item1.ratesum = Float.parseFloat(json.optString("ratesum"));
            photos.add(Item1);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    protected void gotoSharepage(){
        Bundle bundle = new Bundle();
        if(photos.size() == 0 )
            return;
        position = (position + photos.size() -1) % photos.size();
        Item1 item = photos.get(position);
        String photopath = item.photopath;
        if(photopath.equals("")){
            return;
        }
        String rate = item.rate;
        String aboutphoto = item.aboutPhoto;
        if(aboutphoto.equals("")){
            aboutphoto = "Please write about your photo.";
        }
        String sendpost = photopath + "^" + rate + "^" + aboutphoto;
        bundle.putString("photodata", sendpost);
        Intent intent = new Intent(MySelfPhotoActivity.this, MySelfPhotoPostActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);//forward
    }
}

