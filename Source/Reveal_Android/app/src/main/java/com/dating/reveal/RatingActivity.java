package com.dating.reveal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Arrays;

/**
 * Created by JonIC on 2016-11-07.
 */
public class RatingActivity extends HeaderActivity{
    String receivephoto="";
    JSONArray jsonarray = null;
    int position = 0;
    // report content for this photo.
    String report;

    // the rate the user evaluate this photo.  the user could rate only one photo when he go to the next page(Match page)
    int rate = 0;
    boolean[] isSend;

    ImageView m_imgPhoto;
    TextView txtPhoto ;
    TextView txtRate;
    RelativeLayout relativeLayout;
    Button imgYes1;
    Button imgNo1;
    Button imgYes2;
    Button imgNo2;
    Button imgYes3;
    Button imgNo3;
    Button imgYes4;
    Button imgNo4;
    Button imgYes5;
    Button imgNo5;
    Button imgYes6;
    Button imgNo6;
    Button imgYes7;
    Button imgNo7;
    Button imgYes8;
    Button imgNo8;
    Button imgYes9;
    Button imgNo9;
    Button imgYes10;
    Button imgNo10;

    Button btnRepot;
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

        Bundle intent = getIntent().getExtras();
        receivephoto = intent.getString("receivephoto");

//        report = DataUtils.getPreference(Const.REPORT, "");
//        receivephoto = DataUtils.getPreference(Const.RATEPHOTO,"");

        // if called from notify page.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        LoadParendActivity();
        getNotificationsize();
        String fromNoti = "try_match_person";
        if(receivephoto.equals(fromNoti)){

            initMatchData( DataUtils.getPreference(Const.TRY_MATCH_PERSON,""));
            isSend = new boolean[1];
            isSend[0]= false;
        }else{// if called from setting page.
            parseJson(receivephoto);
            initViewEvent();
        }
        // convert the string to jsonarray
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
        m_img_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }
//                sendRatingResult(pos);

                gotoSetting();

            }
        });

        m_img_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }
//                sendRatingResult(pos);

                gotoNotification();
            }
        });
    }
    protected void initView(){
        super.initView();
    };


    // get newfeed jsonarray from notification string.
    protected void initMatchData(String try_match_facebookid){

        final ProgressDialog progressDialog ;

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        JSONArray refreshNotification = new JSONArray();
        String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        ServerManager.getNonProfile(try_match_facebookid, myfacebookid, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                progressDialog.dismiss();

                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userinfo = result.getData();
                    JSONArray profile  = new JSONArray();//= userinfo.optJSONArray("content");
                    JSONObject jsonObject = userinfo.optJSONObject("content");
                    JSONObject emptyJson = new JSONObject();
                    JSONArray emptyJSONArray = new JSONArray();
                    if(!jsonObject.equals(emptyJson)){
                        profile.put(jsonObject);
                    }else{
                        return;
                    }
                    // if fail to receive photo data.
                    if(profile.equals(emptyJSONArray)){
                        Toast.makeText(RatingActivity.this, "Sorry, couldn't find photo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // if receive the photo data. give the data to jsonarray.
                    jsonarray = profile;
                    initViewEvent();
                }
            }
        });
    }


    protected void initViewEvent(){
        ButtonClicked();
        showphoto();
    }

    protected void findView(){
        super.findView();
        m_imgPhoto = (ImageView) findViewById(R.id.img_rating_photo);
//        txtPhoto = (TextView)findViewById(R.id.txt_photo_path);
//        txtRate = (TextView)findViewById(R.id.txt_rate);

        btnRepot = (Button)findViewById(R.id.btn_report);

        imgYes1= (Button)findViewById(R.id.but_num1);
        imgNo1 =(Button)findViewById(R.id.but_num1_fill);

        imgYes2= (Button)findViewById(R.id.but_num2);
        imgNo2 =(Button)findViewById(R.id.but_num2_fill);

        imgYes3= (Button)findViewById(R.id.but_num3);
        imgNo3 =(Button)findViewById(R.id.but_num3_fill);

        imgYes4= (Button)findViewById(R.id.but_num4);
        imgNo4 =(Button)findViewById(R.id.but_num4_fill);

        imgYes5= (Button)findViewById(R.id.but_num5);
        imgNo5 =(Button)findViewById(R.id.but_num5_fill);

        imgYes6= (Button)findViewById(R.id.but_num6);
        imgNo6 =(Button)findViewById(R.id.but_num6_fill);

        imgYes7= (Button)findViewById(R.id.but_num7);
        imgNo7 =(Button)findViewById(R.id.but_num7_fill);

        imgYes8= (Button)findViewById(R.id.but_num8);
        imgNo8 =(Button)findViewById(R.id.but_num8_fill);

        imgYes9= (Button)findViewById(R.id.but_num9);
        imgNo9 =(Button)findViewById(R.id.but_num9_fill);

        imgYes10= (Button)findViewById(R.id.but_num10);
        imgNo10 =(Button)findViewById(R.id.but_num10_fill);


    }
    protected void swip(){
        relativeLayout=(RelativeLayout)findViewById(R.id.lay_container);
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(RatingActivity.this) {
            public void onSwipeTop() {
                showphoto();
            }
            public void onSwipeRight() {

                showphoto();
                int pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }
//                sendRatingResult(pos);
//                Toast.makeText(RatingActivity.this, "Send Rate",Toast.LENGTH_SHORT).show();
                initRate();

//                Intent intent = new Intent(RatingActivity.this,SettingActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
//                finish();
            }
            public void onSwipeLeft() {
                int pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                    position = (position + jsonarray.length() - 1) % jsonarray.length();
                    position = (position + jsonarray.length() - 1) % jsonarray.length();

                    showphoto();
                }
//                sendRatingResult(pos);
                // before go to new feed page send the rating about the photo ,Only in the new feed page get the new feed
                // if the rate is 0 show alert.

                // this function init the rate value to zero. and button state to initialize.
                initRate();
            }
            public void onSwipeBottom() {
            }

        });
    }
    protected void initRate(){
        rate = 0;

        imgYes1.setBackgroundResource(R.drawable.select_button);
        imgYes2.setBackgroundResource(R.drawable.select_button);
        imgYes3.setBackgroundResource(R.drawable.select_button);
        imgYes4.setBackgroundResource(R.drawable.select_button);
        imgYes5.setBackgroundResource(R.drawable.select_button);
        imgYes6.setBackgroundResource(R.drawable.select_button);
        imgYes7.setBackgroundResource(R.drawable.select_button);
        imgYes8.setBackgroundResource(R.drawable.select_button);
        imgYes9.setBackgroundResource(R.drawable.select_button);
        imgYes10.setBackgroundResource(R.drawable.select_button);
    };

    //send the rating result to the server.
    protected void sendRatingResult(final int pos){
        if(receivephoto.equals("[]") || receivephoto == null || receivephoto.equals("") || pos== -1){
            Toast.makeText(RatingActivity.this, "No photo was rated.",Toast.LENGTH_SHORT).show();
            gotoNewFeedActivity();
            finish();
        }
//        position = (position + jsonarray.length() - 1) % jsonarray.length();
        JSONObject photo = jsonarray.optJSONObject(pos);
        // the photos path.
        String photopath = photo.optString("photopath");
        // the facebookid of the photo's owner.
        String facebookid = photo.optString("facebookid");
         // the facebookid of mine.
        String sendfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        // other name
        String othername = photo.optString("name");
        // report about this photo
        report = DataUtils.getPreference(Const.REPORT, "");
        String re;
        if(report.equals("")){
            re = "";
        }else {
            re = report;
        }
        // represent responding or not and accept or refuse // if this photo is for try_match_person's photo,
        // Const.TRY_MATCH_PERSON is not "".
        String response;
        int min = DataUtils.getPreference(Const.MINRATE, 0);
        int max = DataUtils.getPreference(Const.MAXRATE, 0);
        response = DataUtils.getPreference(Const.TRY_MATCH_PERSON, "");
        if(rate == 0){
//            Toast.makeText(RatingActivity.this, "Please Rate This photo.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(rate >= min && rate <=max && response == ""){
            response = "";
        };
        if(rate >= min && rate <=max && response != ""){
            response = "accept";
        }
        if(rate < min || rate > max && response != ""){
            response = "refuse";
        }

        // if response is defined Const.TRY_MATCH_PERSON should be "".
        DataUtils.savePreference(Const.TRY_MATCH_PERSON,"");
        // evaluated rate value
        String rat =String.valueOf(rate);
        String myname = DataUtils.getPreference(Const.NAME,"");

        ServerManager.sendRatingResult(sendfacebookid, facebookid, photopath, rat, re, response, myname, othername, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                // goto next page( New Feed page)
                isSend[pos] = true;
            Toast.makeText(RatingActivity.this, "Send rate.", Toast.LENGTH_SHORT).show();
//                gotoNewFeedActivity();
//                finish();
            }
        });
    }

    protected void gotoNewFeedActivity(){

//        Intent intent = new Intent(RatingActivity.this,NewFeedActivity.class);


        Intent intent = new Intent(RatingActivity.this,NewFeedActivity_candidate.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);//forward
        finish();


    }
    protected void parseJson(String receivephoto){
         try {
             jsonarray = new JSONArray(receivephoto);
             isSend = new boolean[jsonarray.length()];
             Arrays.fill(isSend, false);
         }catch(Throwable t){
            JSONArray emptyJSONArray = new JSONArray();
             jsonarray = emptyJSONArray;
         }
     }

    protected void showphoto(){
        JSONArray emptyJSONArray = new JSONArray();
        if(jsonarray.equals(emptyJSONArray)){
            Toast.makeText(getApplicationContext(), "There is no photo to show", Toast.LENGTH_SHORT).show();
            return;
        }
        if(position >= jsonarray.length() || position <= 0){position = 0;}
        JSONObject photo = jsonarray.optJSONObject(position);
        String photopath = photo.optString("photopath");
        String facebookid = photo.optString("facebookid");
        String othername  = photo.optString("name");

        // photo path, imageview
        DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true) // rotate solve
                .build();
//        DisplayImageOptions options = ImageUtils.buildUILOption(R.mipmap.ic_launcher).build();
        ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + photopath, m_imgPhoto, optoins);

        //  here we simplify and show photopath.
        String show = photopath + " from " + facebookid;
//        txtPhoto.setText(show);
        position = (position + 1) % jsonarray.length();

    }

    // get the report about photo if he/she need to report.
    protected void gotoReportActivity(){
        Bundle bundle  = new Bundle();
        ActivityManager.changeActivity(RatingActivity.this, ReportActivity.class,bundle,false,null);
        // after come back from repot page, the rated photo should be remain.
//        finish();
    }
    // evaluate the photo by click the button.
    protected void ButtonClicked() {
        btnRepot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoReportActivity();
            }
        });
    }
    protected void showRate(int x){
        String str = Integer.toString(x);
//        txtRate.setText(str);
    }

    Button button;

    public void onClick(View v) {

        Drawable dr = getResources().getDrawable(R.drawable.select_button);
        dr.setColorFilter(Color.parseColor("#C80028"), PorterDuff.Mode.SRC_ATOP);

        switch (v.getId()) {
            case R.id.but_num1:

                if (button == null) {
                    button = (Button) findViewById(v.getId());
                } else {
                    button.setBackgroundResource(R.drawable.select_button);
                    button = (Button) findViewById(v.getId());
                }
                button.setBackgroundDrawable(dr);
//                Toast.makeText(RatingActivity.this, "1", Toast.LENGTH_SHORT).show();
                showphoto();
                int pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }else{
                    return;
                }
                if(isSend[pos]){
                    Toast.makeText(RatingActivity.this,"You already rate this photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                rate = 1;
                sendRatingResult(pos);

                break;

            case R.id.but_num2:
                if (button == null) {
                    button = (Button) findViewById(v.getId());
                } else {
                    button.setBackgroundResource(R.drawable.select_button);
                    button = (Button) findViewById(v.getId());
                }
                button.setBackgroundDrawable(dr);
//                Toast.makeText(RatingActivity.this, "2", Toast.LENGTH_SHORT).show();
                pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }else{
                    return;
                }
                if(isSend[pos]){
                    Toast.makeText(RatingActivity.this,"You already rate this photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                rate = 2;
                sendRatingResult(pos);

                break;
            case R.id.but_num3:
                if (button == null) {
                    button = (Button) findViewById(v.getId());
                } else {
                    button.setBackgroundResource(R.drawable.select_button);
                    button = (Button) findViewById(v.getId());
                }
                button.setBackgroundDrawable(dr);
//                Toast.makeText(RatingActivity.this, "3", Toast.LENGTH_SHORT).show();
                pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }else{
                    return;
                }
                if(isSend[pos]){
                    Toast.makeText(RatingActivity.this,"You already rate this photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                rate = 3;
                sendRatingResult(pos);
                break;
            case R.id.but_num4:
                if (button == null) {
                    button = (Button) findViewById(v.getId());
                } else {
                    button.setBackgroundResource(R.drawable.select_button);
                    button = (Button) findViewById(v.getId());
                }
                button.setBackgroundDrawable(dr);
//                Toast.makeText(RatingActivity.this, "4", Toast.LENGTH_SHORT).show();
                pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }else{
                    return;
                }
                if(isSend[pos]){
                    Toast.makeText(RatingActivity.this,"You already rate this photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                rate = 4;
                sendRatingResult(pos);
                break;
            case R.id.but_num5:
                if (button == null) {
                    button = (Button) findViewById(v.getId());
                } else {
                    button.setBackgroundResource(R.drawable.select_button);
                    button = (Button) findViewById(v.getId());
                }
                button.setBackgroundDrawable(dr);
//                Toast.makeText(RatingActivity.this, "5", Toast.LENGTH_SHORT).show();
                pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }else{
                    return;
                }
                if(isSend[pos]){
                    Toast.makeText(RatingActivity.this,"You already rate this photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                rate = 5;
                sendRatingResult(pos);
                break;
            case R.id.but_num6:
                if (button == null) {
                    button = (Button) findViewById(v.getId());
                } else {
                    button.setBackgroundResource(R.drawable.select_button);
                    button = (Button) findViewById(v.getId());
                }
                button.setBackgroundDrawable(dr);
//                Toast.makeText(RatingActivity.this, "6", Toast.LENGTH_SHORT).show();
                pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }else{
                    return;
                }
                if(isSend[pos]){
                    Toast.makeText(RatingActivity.this,"You already rate this photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                rate = 6;
                sendRatingResult(pos);
                break;
            case R.id.but_num7:
                if (button == null) {
                    button = (Button) findViewById(v.getId());
                } else {
                    button.setBackgroundResource(R.drawable.select_button);
                    button = (Button) findViewById(v.getId());
                }
                button.setBackgroundDrawable(dr);
//                Toast.makeText(RatingActivity.this, "7", Toast.LENGTH_SHORT).show();
                pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }else{
                    return;
                }
                if(isSend[pos]){
                    Toast.makeText(RatingActivity.this,"You already rate this photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                rate = 7;
                sendRatingResult(pos);
                break;
            case R.id.but_num8:
                if (button == null) {
                    button = (Button) findViewById(v.getId());
                } else {
                    button.setBackgroundResource(R.drawable.select_button);
                    button = (Button) findViewById(v.getId());
                }
                button.setBackgroundDrawable(dr);
//                Toast.makeText(RatingActivity.this, "8", Toast.LENGTH_SHORT).show();
                pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }else{
                    return;
                }
                if(isSend[pos]){
                    Toast.makeText(RatingActivity.this,"You already rate this photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                rate = 8;
                sendRatingResult(pos);
                break;
            case R.id.but_num9:
                if (button == null) {
                    button = (Button) findViewById(v.getId());
                } else {
                    button.setBackgroundResource(R.drawable.select_button);
                    button = (Button) findViewById(v.getId());
                }
                button.setBackgroundDrawable(dr);
//                Toast.makeText(RatingActivity.this, "9", Toast.LENGTH_SHORT).show();
                pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }else{
                    return;
                }
                if(isSend[pos]){
                    Toast.makeText(RatingActivity.this,"You already rate this photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                rate = 9;
                sendRatingResult(pos);
                break;
            case R.id.but_num10:
                if (button == null) {
                    button = (Button) findViewById(v.getId());
                } else {
                    button.setBackgroundResource(R.drawable.select_button);
                    button = (Button) findViewById(v.getId());
                }
                button.setBackgroundDrawable(dr);
                pos = -1;
                if(jsonarray.length() > 0) {
                    pos = (position + jsonarray.length() - 1) % jsonarray.length();
                }else{
                    return;
                }
                if(isSend[pos]){
                    Toast.makeText(RatingActivity.this,"You already rate this photo",Toast.LENGTH_SHORT).show();
                    return;
                }
                rate = 10;
                sendRatingResult(pos);
//                Toast.makeText(RatingActivity.this, "10", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

}
