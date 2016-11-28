package com.dating.reveal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.reveal.ListAdapter.CommentListAdapter;
import com.dating.reveal.ListAdapter.Item;
import com.dating.reveal.commom.ActivityManager;
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
public class FriendProfileActivity extends HeaderActivity {
    int avaliableshow = 0;
    // input person's information.
    String strperson;
    JSONObject jsonPerson;
    String username;

     // after get the profile  with photo data about the input person
    JSONArray personProfile;
    // the position of person's photo that now is shown.
    int position  = 0;
    List<Item1> photos;

    // show the kind of a person.
    final String FRIEND = "friend";
    final String  MATCH  = "match";
    final String  NON    = "non";
    String kind = NON;

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
    TextView mCommentNum;
    TextView mSendComment;
    EditText mEditComment;
    ListView mCommentList;
    TextView mAverageRate;
//    TextView mAddFriend;

    String friendprofilephoto;
    Button mAddFriend;
    ImageView m_imgPhoto;

//    List<JSONObject> commentDataList;
    ArrayList<Item> mListContent;

    CommentListAdapter mCommentListAdapter;
//    FriendListAdapter mCommentListAdapter;
    // String now comment
    String nowComment = "";
    boolean isLiked = false;
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
        setContentView(R.layout.activity_base_profile);
        getNotificationsize();
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        Bundle intent = getIntent().getExtras();
        strperson = intent.getString("person");
        String[] checkkind = strperson.split("\\^");
        strperson = checkkind[0];
        kind = checkkind[1];
        // initialize the activity.
        LoadParentAtivity();
        initPhotoData();
        swip();
    }
    protected void LoadParentAtivity(){
        super.LoadParendActivity();
    }

    protected void   layoutControls(){
        super.layoutControls();
    };

    protected void initView(){
        super.initView();
        m_img_three.setVisibility(View.INVISIBLE);
        m_img_back_arrow.setVisibility(View.VISIBLE);
    };
    protected void swip(){
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(FriendProfileActivity.this) {
            public void onSwipeTop() {
                showPhoto();
            }
            public void onSwipeRight() {
                position = (position  + photos.size() - 1) % photos.size();
                position = (position  + photos.size() - 1) % photos.size();
                showPhoto();

            }
            public void onSwipeLeft() {
                showPhoto();

//                Intent intent = new Intent(FriendProfileActivity.this, MatchsMessageActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);//forward
            }
            public void onSwipeBottom() {
            }

        });
    }


    protected void findView(){
        super.findView();
        relativeLayout = (RelativeLayout)findViewById(R.id.lay_container);
//        mAddFriend = (TextView)findViewById(R.id.txt_add_friend);

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
        mAddFriend = (Button) findViewById(R.id.btn_add_match_to_friend);
    }

    protected void initEvent(){
        // if the person is frind show all.
        super.initEvent();
        m_img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendProfileActivity.this, FriendActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
                finish();
            }
        });
        if(kind.equals(FRIEND) ){
            mImagLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send like post about this photo.
                    mImagLike.setVisibility(View.VISIBLE);
                    mImagLikefill.setVisibility(View.INVISIBLE);
                    String comment = "";
                    String like = "like";
                    String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                    String sendname = DataUtils.getPreference(Const.NAME, "");
                    Toast.makeText(FriendProfileActivity.this, "Sending Like.", Toast.LENGTH_SHORT).show();
                    sendCommentLike(comment, facebookid, photopath,myfacebookid, like, sendname);

                    // upgrade like number
                    String strszlike = mLikeNum.getText().toString().trim();
                    int szlike = Integer.parseInt(strszlike);
                    szlike++;
                    String newszLike = String.valueOf(szlike);
                    if(!isLiked){
                        mLikeNum.setText(newszLike);
                    }
                    isLiked = true;
                }
            });

            mImagLike.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        mImagLike.setImageResource(R.drawable.icon_like_fill);

                    if (event.getAction() == MotionEvent.ACTION_UP)
                        mImagLike.setImageResource(R.drawable.icon_like);

                    return false;
                }

            });


            mImagLikefill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send like post about this photo.
                    mImagLike.setVisibility(View.VISIBLE);
                    mImagLikefill.setVisibility(View.INVISIBLE);
                    String comment = "";
                    String like = "like";
                    String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                    String sendname = DataUtils.getPreference(Const.NAME, "");
//                    sendCommentLike(comment, facebookid, photopath,myfacebookid, like, sendname);
                }
            });
            mLikeNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // display likes that is gotoLike page.
                    // here you should get single photo's data.
                    int pos = (position + photos.size() - 1) % photos.size();
                    liked = photos.get(pos).likes;
                    if(isLiked){
                        String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                        String myName = DataUtils.getPreference(Const.NAME,"");
                        liked = liked + "^" + myfacebookid + "&" + myName;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("likes", liked);

                    Intent intent = new Intent(FriendProfileActivity.this, LikeShowActivity.class);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
                }
            });


            mImgComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        // display commentlist

                    mCommentList.setVisibility(View.VISIBLE);
                    mListContent = new ArrayList<Item>();
                    if(!nowComment.equals("")){
                        String myname = DataUtils.getPreference(Const.NAME,"");
                        String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                        comment = comment + "^"+ myname +"&" + nowComment +"&"+myfacebookid;
                        nowComment = "";
                    }
                    // init the comment data list
                    if(!comment.equals("")) {
                        String[] commentnodes = comment.split("\\^");
                        for (int x = 0; x < commentnodes.length; x++) {
                            String commentNode = commentnodes[x];
                            String[] commentContent = commentNode.split("\\&");
                            Item item = new Item("","","","");
                            if(commentContent.length==3){
                                item = new Item(commentContent[0], commentContent[1], commentContent[2],"");
                            }
//                            commentDataList.add(json);
                            mListContent.add(item);
                        }
                        //here we should show list.
                        initList();
                    }
                }
            });

            mSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // upload the comment about the photo
                    String comment = (mEditComment.getText()).toString().trim();
                    mEditComment.setText("");
                    if(comment.equals("")){return;}
                    String like = "";
//                    comment = comment + "&" + facebookid;
                    String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                    String sendname = DataUtils.getPreference(Const.NAME,"");
                    Toast.makeText(FriendProfileActivity.this, "Sending comment.", Toast.LENGTH_SHORT).show();
                    sendCommentLike(comment, facebookid, photopath,myfacebookid, like,sendname);

                    // change the comment size displayed in the screen
                    nowComment =  comment;
                    String strszComment = mCommentNum.getText().toString().trim();
                    int szComment = Integer.parseInt(strszComment);
                    szComment++;
                    String newComment = String.valueOf(szComment);
                    mCommentNum.setText(newComment);
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

        }
        // if the person is match show photo only.
        if(kind .equals(MATCH) ){

            mImagLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mImagLikefill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mLikeNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mImgComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 }
            });

            m_img_back_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    ActivityManager.changeActivity(FriendProfileActivity.this, AddMatchesActivity.class, bundle,false,null);
                    overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
                    finish();
                }
            });

            mAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send add friend request.
                    String facebookid = strperson;
                    String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                    String myname = DataUtils.getPreference(Const.NAME,"");
                    String destination = Const.FRIENDREQUEST;// requst content FRIENDREQUEST
                    ServerManager.sendAddMatchRequset(myfacebookid, facebookid, destination,myname,username, new ResultCallBack() {
                        @Override
                        public void doAction(LogicResult result) {
                            Toast.makeText(FriendProfileActivity.this, "Send Add Friend Request", Toast.LENGTH_SHORT).show();
                        } // doAction
                    }); //ServerManager.sendAddMatchRequset();
                } // onClick
            }); // addFriend

        } // if person is match
        // if the person is neither friend nor match show only one photo.
        if(kind .equals( NON)){
            mImagLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mLikeNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mImgComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
//            mAddFriend.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // send add firend request.
//                    // socket tong sin.
////                    sendAddFriendRequest();
//                }
//            });

        }
    }

    protected void initShow(){
        // if the person is frind show all.
        if(kind .equals(FRIEND) ){
//            mAddFriend.setVisibility(View.INVISIBLE);
            mSendComment.setVisibility(View.VISIBLE);
            mEditComment.setVisibility(View.VISIBLE);
            mCommentList.setVisibility(View.INVISIBLE);

        }
        // if the person is match show photo only.
        if(kind .equals(MATCH)){
//            mAddFriend.setVisibility(View.INVISIBLE);
            mSendComment.setVisibility(View.INVISIBLE);
            mEditComment.setVisibility(View.INVISIBLE);
            mCommentList.setVisibility(View.INVISIBLE);
            mLikeNum.setVisibility(View.INVISIBLE);
            mImagLike.setVisibility(View.INVISIBLE);
            mImagLikefill.setVisibility(View.INVISIBLE);

            mCommentNum.setVisibility(View.INVISIBLE);
            mImgComment.setVisibility(View.INVISIBLE);

            mAddFriend.setVisibility(View.VISIBLE);
        }
        // if the person is neither friend nor match show only one photo.
        if(kind .equals(NON) ){
//            mAddFriend.setVisibility(View.VISIBLE);
            mSendComment.setVisibility(View.INVISIBLE);
            mEditComment.setVisibility(View.INVISIBLE);
            mCommentList.setVisibility(View.INVISIBLE);
        }

    }
    protected void sendCommentLike(String comment, String facebookid, String photopath,String myfacebookid, String like, String sendname){
        // in this case the like is not selected.
        ServerManager.sendCommentLike(comment, facebookid, photopath, myfacebookid, like,sendname, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                if(result.mResult == LogicResult.RESULT_OK){
                    String info = ("Success");
                    Toast.makeText(FriendProfileActivity.this, info, Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject userInfo = result.getData();
                    String info = userInfo.optString("error_msg");
                    Toast.makeText(FriendProfileActivity.this, info, Toast.LENGTH_SHORT).show();
                }
            }
        });
    };

    protected void initPhotoData(){
        photos = new ArrayList<Item1>();
//        jsonPerson = parseJson(strperson);
//        JSONObject nullJson = new JSONObject();
//        if (jsonPerson.equals(nullJson) ){
        // strperson is friends facebookid. so first we get friends profile
        if ( strperson.equals("")){
            Toast.makeText(FriendProfileActivity.this, "The person data is not correct", Toast.LENGTH_SHORT).show();
        }else{
                // if the person's information is correct get the person's all photo and show that.
                avaliableshow = 1;
//                commentDataList = new ArrayList<JSONObject>();
            if(kind.equals(FRIEND)){
                getFriendPhotos();
            }else if(kind.equals(MATCH)){
                gotoGetMatchProfile();
            }else{
                gotoNonProfile();
            }// else
        } // else
    } // initPhotoData
    protected void getFriendPhotos(){
        mListContent = new ArrayList<Item>();

//        facebookid = jsonPerson.optString(Const.FACEBOOKID);
        facebookid = strperson;

        // get friend person profile photo
        ServerManager.getPersonInfo(facebookid, new ResultCallBack() {
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
                    username = jsonObject.optString("name", "");
                    friendprofilephoto = jsonObject.optString("profilephoto", "");

                    String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID, "");
                    ServerManager.getFriendProfile(facebookid, myfacebookid, new ResultCallBack() {
                        @Override
                        public void doAction(LogicResult result) {
                            if(result.mResult == LogicResult.RESULT_OK){
                                JSONObject userInfo = result.getData();

                                personProfile = userInfo.optJSONArray("content");
                                JSONObject json  = userInfo.optJSONObject("content");
                                JSONArray emptyJsonarray = new JSONArray();
                                JSONObject emptyJsonObject = new JSONObject();
                                if(personProfile.equals(emptyJsonarray)){
                                    Toast.makeText(FriendProfileActivity.this, "Your friend does not upload photo yet.",Toast.LENGTH_SHORT).show();
                                    return;
//                        ExtractDataFromJSON(json);
                                }else if(!personProfile.equals(emptyJsonarray)){
                                    eXtractDataFromJArray(personProfile);
                                }
                                showPhoto();
                                initShow();
                                initEvent();// should be call after the person' sort is determined.

                            }else {
                                JSONObject userInfo = result.getData();
                                String info = userInfo.optString("error_msg");
//                    if(info.equals("No such man")){
//                        gotoGetMatchProfile();
//                    }
                                Toast.makeText(FriendProfileActivity.this, info, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }
        });
                    ///////////////////////////////////////
    }

    protected  void gotoGetMatchProfile(){
        mListContent = new ArrayList<Item>();

//        facebookid = jsonPerson.optString("facebookid");
        facebookid = strperson;
//        photopath  = jsonPerson.optString("photopath");
// get person profile photo path
        ServerManager.getPersonInfo(facebookid, new ResultCallBack() {
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
                            friendprofilephoto = jsonObject.optString("profilephoto", "");
// get person profile
                            String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID, "");
                            ServerManager.getMatchProfile(facebookid, myfacebookid, new ResultCallBack() {
                                @Override
                                public void doAction(LogicResult result) {
                                    if(result.mResult == LogicResult.RESULT_OK){
                                        JSONObject userInfo = result.getData();

                                        personProfile = userInfo.optJSONArray("content");
                                        JSONObject json  = userInfo.optJSONObject("content");
                                        JSONArray emptyJsonarray = new JSONArray();
                                        JSONObject emptyJsonObject = new JSONObject();
                                        if(personProfile.equals(emptyJsonarray)){// && json.equals(emptyJsonObject)){
                                            Toast.makeText(FriendProfileActivity.this, "Sorry, there is no photos", Toast.LENGTH_SHORT).show();
                                            return;

//                        ExtractDataFromJSON(json);
                                        }else if(!personProfile.equals(emptyJsonarray)){
                                            eXtractDataFromJArray(personProfile);
                                        }

                                        //
                                        showPhoto();
                                        initShow();
                                        initEvent();// should be call after the person' sort is determined.

                                    }else {
                                        JSONObject userInfo = result.getData();
                                        String info = userInfo.optString("error_msg");
//                    if(info.equals("No such man")){
//                        gotoNonProfile();
//                    }
                                        Toast.makeText(FriendProfileActivity.this, info, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                    }
                });
        /////////////////////////

    }

    protected void gotoNonProfile(){
        mListContent = new ArrayList<Item>();
        facebookid = strperson;
//        facebookid = jsonPerson.optString("facebookid");
//        photopath  = jsonPerson.optString("photopath");

        // get person profile photopath
        ServerManager.getPersonInfo(facebookid, new ResultCallBack() {
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
                            friendprofilephoto = jsonObject.optString("profilephoto", "");

                            // get person profile
                            String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID, "");
                            ServerManager.getNonProfile(facebookid, myfacebookid, new ResultCallBack() {
                                @Override
                                public void doAction(LogicResult result) {
                                    if(result.mResult == LogicResult.RESULT_OK){
                                        JSONObject userInfo = result.getData();

                                        personProfile = userInfo.optJSONArray("content");
                                        JSONObject json  = userInfo.optJSONObject("content");
                                        JSONArray emptyJsonarray = new JSONArray();
                                        JSONObject emptyJsonObject = new JSONObject();
                                         ExtractDataFromJSON(json);
//                                        eXtractDataFromJArray(personProfile);
//                                        if(personProfile.equals(emptyJsonarray)){
//                                            ExtractDataFromJSON(json);
//                                        }else if(!personProfile.equals(emptyJsonarray)){
//                                            eXtractDataFromJArray(personProfile);
//                                        }

                                        showPhoto();
                                        initShow();
                                        initEvent();// should be call after the person' sort is determined.

                                    }else {
                                        JSONObject userInfo = result.getData();
                                        String info = userInfo.optString("error_msg");
                                        Toast.makeText(FriendProfileActivity.this, info, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });
                    ////////////////////////////////////////////////////////////////////

    }
    protected void showPhoto(){
        // initialize nowComment and isLiked
        nowComment = "";
        isLiked = false;

        Item1 item1 = photos.get(position);
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
        if(average > 10){
            average = 10;
        }
        if(average < 0){
            average = 0;
        }

        rate = String.valueOf(average);//item1.rate;

        mPhotoName.setText(name);
        mAboutPhoto.setText(aboutphoto);
        mCommentNum.setText(commentsize);
        mLikeNum.setText(likesize);
        mAverageRate.setText(rate);

        DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        // photo path, imageview
//        DisplayImageOptions options = ImageUtils.buildUILOption(R.drawable.pfimage).build();
//        Surface.ROTATION_90;m_imgPhoto
        String friendprofilephoto = "https://graph.facebook.com/" + facebookid + "/picture?type=large";

        ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + photopath, m_imgPhoto, optoins);
        ImageLoader.getInstance().displayImage(friendprofilephoto, profileImage, optoins);
//        ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + friendprofilephoto, profileImage, optoins);
//        scaleImage(m_imgPhoto);

// download photo. here we simplify and show photopath.
        position = (position + 1) % photos.size();
    }
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

    protected void initList(){
        mCommentList.setVisibility(View.VISIBLE);
        setListViewHeightBasedOnChildren(mCommentList);
        mCommentListAdapter = new CommentListAdapter(FriendProfileActivity.this, mListContent);
        mCommentList.setAdapter(mCommentListAdapter);
    }


    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

protected  void ExtractDataFromJSON(JSONObject json){
    photos= new ArrayList<Item1>();

    Item1 item1 = new Item1();
    item1.facebookid = json.optString(Const.FACEBOOKID);
    item1.name = json.optString("name","");
    item1.photopath =  json.optString(Const.PHOTO_PATH);
        item1.ratesum  = Float.parseFloat(json.optString("ratesum"));
        item1.ratenum  = Float.parseFloat(json.optString("ratenum"));
    item1.comment =  json.optString("commentcon");
    item1.commentSize =  json.optString("commentcon");
    item1.likesize =  json.optString("likenum");
    item1.likes =  json.optString("likefacebookid");
    item1.aboutPhoto =  json.optString("mycomment");
    item1.rate = json.optString("rate","");
    photos.add(item1);

}

    protected void eXtractDataFromJArray(JSONArray jsonArray){
        photos= new ArrayList<Item1>();

        for(int i = 0 ; i < jsonArray.length(); i++){
            JSONObject json = jsonArray.optJSONObject(i);
            Item1 Item1 = new Item1();
            Item1.facebookid = json.optString(Const.FACEBOOKID);
            Item1.name  = json.optString("name","");
            Item1.photopath =  json.optString(Const.PHOTO_PATH);
            Item1.rate =    json.optString("rate","");
            Item1.ratesum  = Float.parseFloat(json.optString("ratesum"));
            Item1.ratenum  = Float.parseFloat(json.optString("ratenumber"));
            Item1.comment =  json.optString("commentcon");
            Item1.commentSize =  json.optString("commentnum");
            Item1.likesize =  json.optString("likenum");
            Item1.likes =  json.optString("likefacebookid");
            Item1.aboutPhoto =  json.optString("mycomment");
            photos.add(Item1);

        }
    }
}
class Item1 {
    public String facebookid;
    public String name;
    public String photopath;
    public float  ratesum;
    public float  ratenum;
    public String comment;
    public String commentSize;
    public String likesize;
    public String likes;
    public String aboutPhoto;
    public String rate;
}