package com.dating.reveal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.showImage.ImagefolderActivity;
import com.dating.reveal.commom.ActivityManager;
import com.dating.reveal.commom.OnSwipeTouchListener;
import com.dating.reveal.main.Const;
import com.dating.reveal.utility.DataUtils;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by JonIC on 2016-11-08.
 */
public class MySelfProfileActivity extends HeaderActivity {
    RelativeLayout relativeLayout;
    Button mBtnUpload;
    TextView m_txt_myname;
    EditText m_edit;
    TextView m_age;
    Calendar calendar;
    TextView totalrate;

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
        setContentView(R.layout.activity_myself_profile);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        LoadParendActivity();
        getNotificationsize();
        swip();
        initEvent();
    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
        initData();
        initView();
    }

    protected void initView(){
        super.initView();
        showRate();
        String myname = DataUtils.getPreference(Const.NAME,"");
        m_txt_myname.setText(myname);
        String age = DataUtils.getPreference(Const.AGE,"");
        m_age.setText(age);

    }

    protected void showRate(){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        ServerManager.gettotalrate(myfacebookid, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                progressDialog.dismiss();
                if(result.mResult== LogicResult.RESULT_OK){
                    JSONObject userinfo = result.getData();

                    String rate = userinfo.optString("rate");
                    float average = Float.parseFloat(rate);
                    average = FriendProfileActivity.round(average, 1);
                    rate = String.valueOf(average);
                    totalrate.setVisibility(View.VISIBLE);
                    totalrate.setText(rate);
                }else{
                    totalrate.setVisibility(View.VISIBLE);
                    totalrate.setText("0");
                }
            }
        });
    }

    protected void initData(){

    }

    protected void layoutControls(){
        super.layoutControls();
    }
    protected void findView()
    {
        super.findView();
        m_age = (TextView) findViewById(R.id.txt_myage) ;
        mBtnUpload = (Button)findViewById(R.id.txt_upload_photo);
        m_txt_myname = (TextView) findViewById(R.id.txt_myname);
        m_edit = (EditText) findViewById(R.id.edit_surberb);
        totalrate = (TextView) findViewById(R.id.txt_total_rate);

    }

    protected void initEvent(){
        super.initEvent();

        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String suberb = m_edit.getText().toString();
                if(suberb.equals("")){
                    Toast.makeText(MySelfProfileActivity.this, "Please input location.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
//                ActivityManager.changeActivity(MySelfProfileActivity.this, MyselfUploadPhotoActivity.class, bundle, false, null);
                ActivityManager.changeActivity(MySelfProfileActivity.this, ImagefolderActivity.class, bundle, false, null);
            }
        });

        m_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_edit.setFocusable(true);
            }
        });

    }
    protected void swip(){
        relativeLayout=(RelativeLayout)findViewById(R.id.lay_container);
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(MySelfProfileActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                Intent intent = new Intent(MySelfProfileActivity.this,SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
                finish();
            }
            public void onSwipeLeft() {
                Bundle bundle = new Bundle();
                String photopath = "nothing";
                bundle.putString("photopath",photopath);
                Intent intent = new Intent(MySelfProfileActivity.this,MySelfPhotoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);//forward
                finish();
            }
            public void onSwipeBottom() {
            }

        });
    }
}
