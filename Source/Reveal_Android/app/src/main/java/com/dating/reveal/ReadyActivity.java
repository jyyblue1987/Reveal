package com.dating.reveal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dating.reveal.commom.OnSwipeTouchListener;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.DataUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by JonIC on 2016-11-14.
 */
public class ReadyActivity extends HeaderActivity{
    de.hdodenhof.circleimageview.CircleImageView m_imgPink;
    de.hdodenhof.circleimageview.CircleImageView m_imgFace1;
    RelativeLayout relativeLayout;
    final String MAN = "man";
    final String WOMEN = "woman";

    // Save variable
    int intMaxAge = 40;
    int intMinAge = 18;
    int intDistance = 100;
    int intMinRate = 0;
    int intMaxRate= 10;
    String othergender = WOMEN;
    String gender = WOMEN;
    String email ="";
    String facebookid="";
    String firstname = "";
    float locationx = 0;
    float locationy = 0;
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
        setContentView(R.layout.activity_ready);

        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));
        LoadParendActivity();
        getNotificationsize();
        ImageAnimation(m_imgPink);
//        getNotificationSize();
        swip();
    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
    }
    protected void initView(){
        super.initView();
        String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        String photopath = "https://graph.facebook.com/" + facebookid + "/picture?type=large";

//        String photopath = itemsArrayList.get(position).getProfilephoto();

        DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        ImageLoader.getInstance().displayImage(photopath, m_imgFace1, optoins);
    }

    protected void initEvent(){
        super.initEvent();
    }
    protected void layoutControls(){
        super.layoutControls();
    }
     protected void swip(){
        relativeLayout=(RelativeLayout)findViewById(R.id.lay_container);
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(ReadyActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
//                Intent intent = new Intent(ReadyActivity.this,SettingActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
//                finish();

            }
            public void onSwipeLeft() {
//                login();
            }
            public void onSwipeBottom() {
            }

        });

    }
    protected void findView(){
        super.findView();
        relativeLayout = (RelativeLayout) findViewById(R.id.lay_container);
        m_imgPink = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.img_pink);
        m_imgFace1 = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);
    }
    protected void ImageAnimation(de.hdodenhof.circleimageview.CircleImageView v){
        ScaleAnimation starScaleAnimation =
                new ScaleAnimation(1f, 5f, 1f, 5f,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f);

        starScaleAnimation.setDuration(3000);
        ((de.hdodenhof.circleimageview.CircleImageView) v).setImageResource(R.drawable.expanding_circle);
        ScaleAnimation scaleAnim = starScaleAnimation;
        scaleAnim.setRepeatCount(Animation.INFINITE);
        starScaleAnimation.setFillAfter(false);
        v.startAnimation(scaleAnim);
    }

    public void login(){
        facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");//"1313695498661735";//
        email = DataUtils.getPreference(Const.EMAIL,"");
        String name = DataUtils.getPreference(Const.NAME,"");
        gender = DataUtils.getPreference(Const.GENDER,"");
        int age = 18;
        String Age = String.valueOf(age);
        float locationx = 0;
        String locationxx = String.valueOf(locationx);
        float locationy = 0;
        String locationyy = String.valueOf(locationy);

        ServerManager.login(facebookid, email, gender, name, Age, locationxx, locationyy, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                if( result.mResult == LogicResult.RESULT_OK){
                    JSONObject userInfo = result.getData();

                    // if receive the data then go to the next screen.
                    // before goto there save the setting
                    saveSetting();
                    JSONArray retcontent  = userInfo.optJSONArray("content");
                    String receivephoto = retcontent.toString().trim();
//                   String receivephoto = userInfo.optString("content");
//                   receivephoto = receivephoto.substring(1,receivephoto.length()-1);
                    // the photos that nonfriend .
                    DataUtils.savePreference(Const.RATEPHOTO, receivephoto);
                    Bundle bundle = new Bundle();
                    bundle.putString("receivephoto", receivephoto);
                    Intent intent = new Intent(ReadyActivity.this,RatingActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);//forward
                    finish();
                }else{
                    Toast.makeText(ReadyActivity.this, "Server is not responding.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    protected void saveSetting(){
        DataUtils.savePreference(Const.MAXAGE, intMaxAge);
        DataUtils.savePreference(Const.MINAGE,intMinAge);
        DataUtils.savePreference(Const.MAXRATE, intMaxRate);
        DataUtils.savePreference(Const.MINRATE, intMinRate);
        DataUtils.savePreference(Const.DISTANCE, intDistance);
    }


    protected void getNotificationsize(){
        super.getNotificationsize();
        login();
    }
    protected void getNotificationSize(){
        String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        ServerManager.getNotificationSize(facebookid, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                if( result.mResult == LogicResult.RESULT_OK){
                    JSONObject userinfo = result.getData();
                    JSONArray size = userinfo.optJSONArray("content");
                    JSONObject json = size.optJSONObject(0);
                    String size_1 = json.optString("count(*)", "");
                    DataUtils.savePreference(Const.NOTIFICATION_SIZE,size_1);

                    login();
                }
            };
        });
    };
}
