package com.dating.reveal;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.dating.reveal.commom.OnSwipeTouchListener;
import com.dating.reveal.main.Const;
import com.dating.reveal.main.MyApplication;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.DataUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by JonIC on 2016-11-07.
 */
public class SettingActivity extends HeaderActivity {
    final String MAN = "man";
    final String WOMEN = "woman";
    final String ALL = "all";
    final String YES = "yes";
    final String NO = "no";


    private RelativeLayout relativeLayout;
    private TextView mToMyProfile;
    private TextView tvMax;
    private TextView tvMin;
    private com.dating.reveal.commom.CrystalRangeSeekBar rangeSeekbarAge;

    private TextView mMinRating;
    private TextView mMaxRating;
    private com.dating.reveal.commom.CrystalRangeSeekBar rangeSeekbarRating;

    private TextView mTxtDistance;
    private com.dating.reveal.commom.CrystalSeekbar rangeSeekbarDistance;
    private de.hdodenhof.circleimageview.CircleImageView m_imgPhoto;
    private TextView mTextMyname;
    ///////////////////////////
    // Save variable
    int intMaxAge = 18;
    int intMinAge = 18;
    int intDistance = 100;
    double intMinRate = 0.0;
    double intMaxRate= 10.0;
    String othergender = WOMEN;
    String gender = WOMEN;
    String email ="";
    String facebookid="";
    String firstname = "";
    float locationx = 0;
    float locationy = 0;
    String showme = ALL;
    String showmatch = YES;
    String showmessage = YES;
    String shownewfriend = YES;
    String shownewcomment = YES;
    String showlike = YES;
    String showmyfullname = YES;
    String allowname = YES;
    String autofriend = YES;

    private ProgressBar bar;

    //
    de.hdodenhof.circleimageview.CircleImageView m_img_mark;

    Button m_btn_male;
    Button m_btn_female;
    boolean isPressedMale = true; // true -> man,  false -> female

    TextView m_male_sign;
    TextView m_female_sign;

    Button m_btn_km;
    Button m_btn_miles;
    TextView m_txt_distance_unit;
    boolean isKm = true;          // true -> km, false -> miles

    com.kyleduo.switchbutton.SwitchButton m_swtich_man;
    boolean showMan;              // true-> show man, false-> do not show man.
    // hide the status bar.
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

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        // initialize the preference to ensure.
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));
        MyApplication app = (MyApplication)this.getApplication();
        app.initPreference();

        relativeLayout=(RelativeLayout)findViewById(R.id.lay_container);
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(SettingActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {

            }
            public void onSwipeLeft() {
//                login();
                new ProgressTask().execute();
            }
            public void onSwipeBottom() {
            }

        });
        LoadParendActivity();
        getNotificationsize();
    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
    }
    protected void initView(){
        super.initView();
        String user_id = DataUtils.getPreference(Const.FACEBOOKID,"");
        String imgUrl = "https://graph.facebook.com/" + user_id + "/picture?type=large";
            DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .build();

            // photo path, imageview
//        DisplayImageOptions options = ImageUtils.buildUILOption(R.drawable.pfimage).build();
//        Surface.ROTATION_90;m_imgPhoto
            ImageLoader.getInstance().displayImage(imgUrl, m_imgPhoto, optoins);
        mTextMyname.setText(DataUtils.getPreference(Const.NAME,""));
        m_img_back_arrow.setVisibility(View.INVISIBLE);
        m_img_three.setVisibility(View.INVISIBLE);

        String gender = DataUtils.getPreference(Const.GENDER,"");
        if(gender.equals("male")){
            isPressedMale = true;
            m_btn_male.setBackgroundResource(R.drawable.male_r);
            m_btn_male.setTextColor(Color.WHITE);

            m_btn_female.setBackgroundResource(R.drawable.female_w);
            m_btn_female.setTextColor(Color.RED);

        }else{
            m_btn_male.setBackgroundResource(R.drawable.male_w);
            m_btn_male.setTextColor(Color.RED);

            m_btn_female.setBackgroundResource(R.drawable.female_r);
            m_btn_female.setTextColor(Color.WHITE);
        }

        m_btn_male. setText("  Male");

        m_btn_female.setText("  Female");

        m_male_sign.setText("");
        m_female_sign.setText("");


        String input_age = DataUtils.getPreference(Const.INPUT_AGE,"0");
        int inpu_age = Integer.parseInt(input_age);
        int compare_lower;
        int compare_upper;
        if(inpu_age < 20){
            compare_lower = inpu_age -2;
        }else{
            compare_lower = 18;
        }

        if(inpu_age < 18){
            compare_upper = inpu_age + 2;
        }else{
            compare_upper = 99;
        }

        rangeSeekbarAge.setMaxValue(compare_upper);
        rangeSeekbarAge.setMinValue(compare_lower);

    }
    protected void layoutControls(){
        super.layoutControls();
    }
    protected void findView(){
        super.findView();
        mTextMyname = (TextView)findViewById(R.id.txt_myname);
        m_imgPhoto = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R. id.img_profile_photo);
        bar = (ProgressBar) findViewById(R.id.progressBar);


        m_img_mark = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.img_profile_photo);
        rangeSeekbarAge = (com.dating.reveal.commom.CrystalRangeSeekBar) findViewById(R.id.rangeSeekbarAge);
        tvMin = (TextView) findViewById(R.id.txt_min_age);
        tvMax = (TextView) findViewById(R.id.txt_max_age);

        rangeSeekbarDistance = (com.dating.reveal.commom.CrystalSeekbar)findViewById(R.id.rangeSeekbarDistance);
        mTxtDistance = (TextView)findViewById(R.id.txt_distance_value);

        rangeSeekbarRating =(com.dating.reveal.commom.CrystalRangeSeekBar)findViewById(R.id.rangeSeekbarRate);
        mMaxRating  =(TextView)findViewById(R.id.txt_max_rate);
        mMinRating  =(TextView)findViewById(R.id.txt_min_rate);
        m_btn_male = (Button) findViewById(R.id.btn_male);
        m_btn_female = (Button) findViewById(R.id.btn_female);

        m_btn_km = (Button)findViewById(R.id.btn_distance_unit_Km);
        m_btn_miles= (Button) findViewById(R.id.btn_distance_unit_mile);
        m_txt_distance_unit = (TextView) findViewById(R.id.txt_distance_unit);

        m_swtich_man = (com.kyleduo.switchbutton.SwitchButton) findViewById(R.id.toggleButton_man);

        m_male_sign = (TextView) findViewById(R.id.img_man_sign);
        m_female_sign = (TextView) findViewById(R.id. img_women_sign);

    }

    protected void initEvent(){
        super.initEvent();
        rangeSeekbarAge.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });

        rangeSeekbarAge.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                intMinAge = (int)minValue.intValue();
                intMaxAge = (int)maxValue.intValue();
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });
        rangeSeekbarRating.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mMinRating.setText(String.valueOf(minValue));
                mMaxRating.setText(String.valueOf(maxValue));
            }
        });

        rangeSeekbarRating.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                intMinRate  =(double) minValue.doubleValue();
                intMaxRate = (double) maxValue.doubleValue();
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });


        rangeSeekbarDistance.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                mTxtDistance.setText(String.valueOf(minValue));
            }
        });
        rangeSeekbarDistance.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                intDistance = (int)value.intValue();
                Log.d("CRS=>", String.valueOf(value));
            }
        });
        m_img_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,MySelfProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);//forward
                finish();

            }
        });
        m_btn_male.setOnClickListener(buttonListener_male);
        m_btn_female.setOnClickListener(buttonListener_female);
        m_btn_km.setOnClickListener(buttonListener_km);
        m_btn_miles.setOnClickListener(buttonListener_miles);
        m_swtich_man.setOnCheckedChangeListener(switch_man);
    }

    // switch listener which select man.
    CompoundButton.OnCheckedChangeListener switch_man = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                Toast.makeText(SettingActivity.this,"Show man.",Toast.LENGTH_SHORT).show();
                showMan = true;
            }else {
                showMan = false;
                Toast.makeText(SettingActivity.this,"Do not show man.",Toast.LENGTH_SHORT).show();
            }
        }
    };
    // male button click listener.

    View.OnClickListener buttonListener_male = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isPressedMale){
                m_btn_male.setBackgroundResource(R.drawable.male_w);
                m_btn_male.setTextColor(Color.RED);

                m_btn_female.setBackgroundResource(R.drawable.female_r);
                m_btn_female.setTextColor(Color.WHITE);
            }
            else{
                m_btn_male.setBackgroundResource(R.drawable.male_r);
                m_btn_male.setTextColor(Color.WHITE);

                m_btn_female.setBackgroundResource(R.drawable.female_w);
                m_btn_female.setTextColor(Color.RED);
            }

            isPressedMale = !isPressedMale;
        }
    };
    // female button click listener.
    View.OnClickListener buttonListener_female = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isPressedMale){
                m_btn_male.setBackgroundResource(R.drawable.male_w);
                m_btn_male.setTextColor(Color.RED);

                m_btn_female.setBackgroundResource(R.drawable.female_r);
                m_btn_female.setTextColor(Color.WHITE);
            }
            else{
                m_btn_male.setBackgroundResource(R.drawable.male_r);
                m_btn_male.setTextColor(Color.WHITE);

                m_btn_female.setBackgroundResource(R.drawable.female_w);
                m_btn_female.setTextColor(Color.RED);
            }

            isPressedMale = !isPressedMale;
        }
    };

    // km button click listener.
    View.OnClickListener buttonListener_km = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isKm){
                m_btn_km.setBackgroundResource(R.drawable.male_white);
                m_btn_km.setTextColor(Color.RED);

                m_btn_miles.setBackgroundResource(R.drawable.male_red);
                m_btn_miles.setTextColor(Color.WHITE);

                m_txt_distance_unit.setText("Miles");
            }else{
                m_btn_km.setBackgroundResource(R.drawable.male_red);
                m_btn_km.setTextColor(Color.WHITE);

                m_btn_miles.setBackgroundResource(R.drawable.male_white);
                m_btn_miles.setTextColor(Color.RED);

                m_txt_distance_unit.setText("Km");
            }
            isKm = !isKm;
        }
    };
    // miles button click listener.
    View.OnClickListener buttonListener_miles = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isKm){
                m_btn_km.setBackgroundResource(R.drawable.male_white);
                m_btn_km.setTextColor(Color.RED);

                m_btn_miles.setBackgroundResource(R.drawable.male_red);
                m_btn_miles.setTextColor(Color.WHITE);

                m_txt_distance_unit.setText("Miles");
            }else{
                m_btn_km.setBackgroundResource(R.drawable.male_red);
                m_btn_km.setTextColor(Color.WHITE);

                m_btn_miles.setBackgroundResource(R.drawable.male_white);
                m_btn_miles.setTextColor(Color.RED);

                m_txt_distance_unit.setText("Km");
            }
            isKm = !isKm;
        }
    };
    //
//    int intMaxAge = 18;
//    int intMinAge = 18;
//    int intDistance = 100;
//    int intMinRate = 0;
//    int intMaxRate= 10;
//    String othergender = WOMEN;
//    String gender = WOMEN;
//    String email ="";
//    String facebookid="";
//    String firstname = "";
//    float locationx = 0;
//    float locationy = 0;
//var facebookid=req.body.facebookid;
//    var email = req.body.email;
//    var age = req.body.age;
//    var name =req.body.name;
//    var gender =req.body.gender;
//    var locationx=req.body.locationx;
//    var locationy=req.body.locationy;
//    var othergender= req.body.othergender;
    protected void login(){
        facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        email = DataUtils.getPreference(Const.EMAIL,"");
        String name = DataUtils.getPreference(Const.NAME,"");
        gender = DataUtils.getPreference(Const.GENDER,"");
        float locationx = 0;
        String locationxx = String.valueOf(locationx);
        float locationy = 0;
        String locationyy = String.valueOf(locationy);

        int llimit_age;
        int ulimit_age;
        String input_age = DataUtils.getPreference(Const.INPUT_AGE, "0");
        int    szage = Integer.parseInt(input_age);
        String Age = DataUtils.getPreference(Const.AGE,"0");
        int   intage = Integer.parseInt(Age);
        if(szage < 20){
            llimit_age = intage - 2;
        }


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
                   Intent intent = new Intent(SettingActivity.this,RatingActivity.class);
                   intent.putExtras(bundle);
                   startActivity(intent);
                   overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);//forward
                   finish();
               }
            }
        });
    }
    //    int intMaxAge = 18;
//    int intMinAge = 18;
//    int intDistance = 100;
//    int intMinRate = 0;
//    int intMaxRate= 10;
//    String othergender = WOMEN;
//    String gender = WOMEN;
//    String email ="";
//    String facebookid="";
//    String firstname = "";
//    float locationx = 0;
//    float locationy = 0;
    // save the setting values to preference
    protected void saveSetting(){
        DataUtils.savePreference(Const.MAXAGE, intMaxAge);
        DataUtils.savePreference(Const.MINAGE,intMinAge);
        String minrate = String.valueOf(intMinRate);
        String maxrate = String.valueOf(intMaxRate);
        DataUtils.savePreference(Const.MAXRATE, minrate);
        DataUtils.savePreference(Const.MINRATE, maxrate);
        DataUtils.savePreference(Const.DISTANCE, intDistance);
    }

//    protected void initPreference(){
//        DataUtils.savePreference(Const.REPORT, "");
//    }

    private class ProgressTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //my stuff is here
            login();
            return null;
        };

        @Override
        protected void onPostExecute(Void result) {
            bar.setVisibility(View.GONE);
        }
    }

}

