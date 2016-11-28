package com.dating.reveal;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dating.reveal.adapter.CustomPagerAdapter;
import com.dating.reveal.dialog.DataPickerDialog;
import com.dating.reveal.main.Const;
import com.dating.reveal.main.MyApplication;
import com.dating.reveal.utility.DataUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

import io.socket.client.Socket;

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener
,DialogInterface.OnDismissListener{
    private CallbackManager callbackManager;

    int[] mResources = {R.drawable.background_screen_three, R.drawable.background_screen_one, R.drawable.background_screen_two
    };

    ViewPager mViewPager;
    private CustomPagerAdapter mAdapter;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;

    int islogin = 0;
    int isfriend = 0;
    int istagglefriend = 0;
    int age;
    Button m_fb_login;
    Calendar calendar;
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
        // init Preference
        initPreference();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        findView();
        initEvent();
        mAdapter = new CustomPagerAdapter(this, mResources);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(this);

        setPageViewIndicator();
    }

    protected void initEvent(){
        m_fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataPickerDialog dialog = new DataPickerDialog( MainActivity.this,"");
                dialog.setOnDismissListener( MainActivity.this );
                // make the background of the dialog to become transparent.
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                // align the dialog botton of the screen
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.BOTTOM;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);
                dialog.show() ;

            }
        });
    }

    // check the inputed age is greater than 18.
    @Override
    public void onDismiss(DialogInterface $dialog) {
        // TODO Auto-generated method stub
        DataPickerDialog dialog = (DataPickerDialog) $dialog ;
        age = dialog.getAge() ;
        if(age == -1){
            return;
        }
        String inputage = String.valueOf(age);
        DataUtils.savePreference(Const.INPUT_AGE, inputage);
//        gotoReadyPage(); // test facebook skip
        onLoginFacebook();
    }

    protected void findView() {
        m_fb_login = (Button) findViewById(R.id.img_login_fb);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
    }


    public void setStatusBarColor(View statusBar,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            int actionBarHeight = getActionBarHeight();
            int statusBarHeight = getStatusBarHeight();
            //action bar height
            statusBar.getLayoutParams().height = statusBarHeight;
            statusBar.setBackgroundColor(color);
        }
    }
    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    private void setPageViewIndicator() {

        Log.d("###setPageViewIndicator", " : called");
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            final int presentPosition = i;
            dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mViewPager.setCurrentItem(presentPosition);
                    return true;
                }

            });


            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        Log.d("###onPageSelected, pos ", String.valueOf(position));
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        if (position + 1 == dotsCount) {

        } else {

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    protected void initPreference(){
    // init the report content.
        DataUtils.savePreference(Const.REPORT, "");
        DataUtils.savePreference(Const.TRY_MATCH_PERSON,"");
    }

    AccessToken token;
    private void onLoginFacebook() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "email", "user_friends","user_birthday"));//,"user_photos"
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult result) {
                token = result.getAccessToken();

                GraphRequest request;
                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    // save token
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        if (response.getError() != null) {
                            // get personal information
                        } else {
                            Log.i("TAG", "user: " + user.toString());
                            Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());
                            String tokey = result.getAccessToken().getToken();
                            DataUtils.savePreference("token", tokey);

                            String id = user.optString(Const.GET_FACEBOOKID,"");
                            DataUtils.savePreference(Const.FACEBOOKID, id);//user.optString(Const.FACEBOOKID,""));
                             String aa =  DataUtils.getPreference(Const.FACEBOOKID,"");
                            DataUtils.savePreference(Const.GENDER, user.optString(Const.GENDER,""));
                            DataUtils.savePreference(Const.NAME, user.optString(Const.NAME,""));
                            DataUtils.savePreference(Const.EMAIL, user.optString(Const.EMAIL,""));
                            DataUtils.savePreference(Const.TOKEN, user.optString(Const.TOKEN,""));


                            DataUtils.savePreference(Const.AGE, user.optString(Const.BIRTHDAY, ""));
                            calendar = Calendar.getInstance();
                            int nowdate = calendar.get(Calendar.YEAR);
                            String age = DataUtils.getPreference(Const.AGE,"");
                            String[] ddmmyy = age.split("\\/");
                            int nowage;
                            if(ddmmyy.length == 3){
                                String year = ddmmyy[2];
                                int birthyear = Integer.parseInt(year);
                                 nowage = nowdate - birthyear;
                            }else{
                                 nowage = 18;
                            }
                            String label = String.valueOf(nowage);
                            DataUtils.savePreference(Const.AGE, label);

                            setResult(RESULT_OK);
///////////////////////////////////////////////////////////////
                            // join io socket
                            initsocket();
                        }
                        islogin = 1;
                        if(isfriend == 1 && istagglefriend == 1 ){
                            gotoReadyPage();
                        }

                        getfriendlist();
                        getToggleFriend();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("test", "Error: " + error);
                Toast.makeText(MainActivity.this, "Fail to login",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                finish();
            }
        });
    }


    //

    // get facebookids photos.
    protected void getAllPhoto(String facebookid){
        new GraphRequest(
                token,
                "/"+ facebookid + "/photos?type=uploaded",//uploaded
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        if (response.getError() != null) {
                            // get personal information
                        }else {
                            ////////////////////////////////////////////////////////////
                            ////////////////// all facebook users///////////////////////

                            try {
                                JSONObject json = response.getJSONObject();

                                if (json.has("data")) {
                                    JSONArray userphotos = json.getJSONArray("data");

                                } else {

                                }
                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        istagglefriend = 1;
                        if(isfriend == 1 &&  islogin == 1){
                            gotoReadyPage();
                        }

                        ////////////////////////////////////////////////////////////
                    }
                }
        ).executeAsync();
    }
 // example get facebook friend list.
    protected void getfriendlist(){

        String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");

        new GraphRequest(
                token,
                //"/" + myfacebookid  + "/taggable_friends",  //taggable_friends   // friendlists   // invitable_friends //excluded_ids //friends
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */////////////////////////////////////////////////////////////////////
                        if (response.getError() != null) {
                            // get personal information
                        }else {
                            try {
                                JSONArray rawPhotosData = response.getJSONObject().getJSONArray("data");
                                JSONArray useappfriend = new JSONArray();
                                for (int j = 0; j < rawPhotosData.length(); j++) {
                                    //save whatever data you want from the result
                                    JSONObject photo = new JSONObject();
                                    photo.put("id", ((JSONObject) rawPhotosData.get(j)).get("id"));
                                    photo.put("name", ((JSONObject) rawPhotosData.get(j)).get("name"));
                                    useappfriend.put(photo);

                                }

                                String struseappfriend = useappfriend.toString().trim();
                                DataUtils.savePreference(Const.USE_APP_FRIENDS, struseappfriend);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        isfriend = 1;
                        if(istagglefriend == 1 && islogin == 1){
                            gotoReadyPage();
                        }

//           ////////////////////////////////////////////////////////////////////////////////////////////
//
//
//                        GraphResponse rep = response;
//                        new GraphRequest(
//                                token,
//                                //"/" + myfacebookid  + "/taggable_friends",  //taggable_friends   // friendlists   // invitable_friends //excluded_ids //friends
//                                "/me/friendlists",
//                                null,
//                                HttpMethod.GET,
//                                new GraphRequest.Callback() {
//                                    public void onCompleted(GraphResponse response) {
//            /* handle the result */
//                                        GraphResponse rep = response;
//
//                                        ////////////////////////////////////////////////////////////
//                                        ////////////////// all facebook users///////////////////////
//
//                                        try {
//                                            JSONObject json  = response.getJSONObject();
//
//                                            if(json.has("data")){
//                                                JSONArray rawPhotosData =json.getJSONArray("data");
//                                                JSONArray facebookfriend = new JSONArray();
//                                                for(int j=0; j<rawPhotosData.length();j++){
//                                                    //save whatever data you want from the result
//                                                    JSONObject photo = new JSONObject();
//                                                    photo.put("id", ((JSONObject)rawPhotosData.get(j)).get("id"));
//                                                    photo.put("name",((JSONObject)rawPhotosData.get(j)).get("name"));
//                                                    photo.put("icon", ((JSONObject)rawPhotosData.get(j)).get("picture"));
//                                                    facebookfriend.put(photo);
//                                                }
//                                                String strfacebookfriend = facebookfriend.toString().trim();
//                                                DataUtils.savePreference(Const.FACEBOOK_FRIENDS, strfacebookfriend);
//
//                                            }else{
//
//                                            }
//                                        } catch (JSONException e) {
//                                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//                                            e.printStackTrace();
//                                        }
//
//                                        // goto setting scrreen.
//                                        Intent intent = new Intent(MainActivity.this,ReadyActivity.class);
//                                        startActivity(intent);
//                                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
//                                        //
//                                        finish();
//                                        ////////////////////////////////////////////////////////////
//                                    }
//                                }
//                        ).executeAsync();
                    }
                }

        ).executeAsync();
    }


    protected void  getToggleFriend(){


        new GraphRequest(
                token,
                //"/" + myfacebookid  + "/taggable_friends",  //taggable_friends   // friendlists   // invitable_friends //excluded_ids //friends
                "/me/friendlists",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        if (response.getError() != null) {
                            // get personal information
                        }else {
                            ////////////////////////////////////////////////////////////
                            ////////////////// all facebook users///////////////////////

                            try {
                                JSONObject json = response.getJSONObject();

                                if (json.has("data")) {
                                    JSONArray rawPhotosData = json.getJSONArray("data");
                                    JSONArray facebookfriend = new JSONArray();
                                    for (int j = 0; j < rawPhotosData.length(); j++) {
                                        //save whatever data you want from the result
                                        JSONObject photo = new JSONObject();
                                        photo.put("id", ((JSONObject) rawPhotosData.get(j)).get("id"));
                                        photo.put("name", ((JSONObject) rawPhotosData.get(j)).get("name"));
//                                        photo.put("icon", ((JSONObject) rawPhotosData.get(j)).get("picture"));
                                        facebookfriend.put(photo);
                                    }
                                    String strfacebookfriend = facebookfriend.toString().trim();
                                    DataUtils.savePreference(Const.FACEBOOK_FRIENDS, strfacebookfriend);

                                } else {

                                }
                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        istagglefriend = 1;
                        if(isfriend == 1 &&  islogin == 1){
                            gotoReadyPage();
                        }

                        ////////////////////////////////////////////////////////////
                    }
                }
        ).executeAsync();
    }

    protected void gotoReadyPage(){
//        DataUtils.savePreference(Const.FACEBOOKID, "333379007041276");//user.optString(Const.FACEBOOKID,""));
//        DataUtils.savePreference(Const.NAME, "jimmy");
//        DataUtils.savePreference(Const.EMAIL,"jyyblue1987@outlook.com");
//        DataUtils.savePreference(Const.GENDER,"male");

//        DataUtils.savePreference(Const.FACEBOOKID, "337825819928904");//user.optString(Const.FACEBOOKID,""));
//        DataUtils.savePreference(Const.NAME, "simmon");
//        DataUtils.savePreference(Const.EMAIL,"future.syg414@yahoo.com");
//        DataUtils.savePreference(Const.GENDER,"female");

//        initsocket();
//         goto setting scrreen.

        Intent intent = new Intent(MainActivity.this,ReadyActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        //
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    protected void initsocket(){
        Socket msocket;
        MyApplication app = (MyApplication)this.getApplication();
        msocket = app.getSocket();
        String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        msocket.emit("joinRoom", facebookid);
    }

}
