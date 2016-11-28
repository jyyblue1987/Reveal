package com.dating.reveal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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

/**
 * Created by JonIC on 2016-11-08.
 */
public class MySelfPhotoPostActivity extends HeaderActivity {
    RelativeLayout relativeLayout;
    String photodata = "";
    String facebookid;
    String photopath;
    String rate;
    String aboutphoto;
    // view items
    EditText medit;
    ImageView m_imgPhoto;
    Button btnConfirm;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioButton radiobutton_group_photo;
    private RadioButton radiobutton_off_material;
    private RadioButton radiobutton_not_actual;
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
        Bundle intent = getIntent().getExtras();
        photodata = intent.getString("photodata");
        setContentView(R.layout.activity_myself_photo_post);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        getPhotoPaht();
        LoadParentActivity();
        getNotificationsize();
        swip();
        addListenerOnButton();
    }

    protected void getPhotoPaht(){
        String[] strpath = photodata.split("\\^");
        photopath = strpath[0];
        rate = strpath[1];
        aboutphoto = strpath[2];
    }
    protected void LoadParentActivity(){
        super.LoadParendActivity();
    }
    protected void  findView(){
        super.findView();
        medit = (EditText) findViewById(R.id.edit_surberb);
        m_imgPhoto = (ImageView) findViewById(R.id. img_newfeed_photo);
        btnConfirm = (Button) findViewById(R.id. btn_post);
        radioGroup = (RadioGroup) findViewById(R. id. radioGroup);

        radiobutton_group_photo = (RadioButton)findViewById(R.id.radio_group_photo);
        radiobutton_off_material= (RadioButton)findViewById(R.id.radio_off_material);
        radiobutton_not_actual= (RadioButton)findViewById(R.id.radio_not_actual);

        /// hide the radio button
        radiobutton_group_photo.setButtonDrawable(android.R.color.transparent);
        radiobutton_group_photo.setPadding(31, 0, 0, 0);
        /// hide the radio button
        radiobutton_off_material.setButtonDrawable(android.R.color.transparent);
        radiobutton_off_material.setPadding(31, 0, 0, 0);
        /// hide the radio button
        radiobutton_not_actual.setButtonDrawable(android.R.color.transparent);
        radiobutton_not_actual.setPadding(31, 0, 0, 0);

        relativeLayout = (RelativeLayout)findViewById(R.id.lay_container);
    }
    protected void layoutControls(){
        super.layoutControls();
    }
    protected void initEvent(){
        super.initEvent();
        m_img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMyphoto();
            }
        });

        /// radio button check text color change
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radiobutton_group_photo.isChecked()) {
                    radiobutton_group_photo.setTextColor(Color.parseColor("#C80028"));
                    radiobutton_off_material.setTextColor(Color.BLACK);
                    radiobutton_not_actual.setTextColor(Color.BLACK);
                }
                if (radiobutton_off_material.isChecked()){
                    radiobutton_group_photo.setTextColor(Color.BLACK);
                    radiobutton_off_material.setTextColor(Color.parseColor("#C80028"));
                    radiobutton_not_actual.setTextColor(Color.BLACK);
                }
                if (radiobutton_not_actual.isChecked()){
                    radiobutton_group_photo.setTextColor(Color.BLACK);
                    radiobutton_off_material.setTextColor(Color.BLACK);
                    radiobutton_not_actual.setTextColor(Color.parseColor("#C80028"));
                }
            }
        });
    }

//    radiobutton_group_photo = (RadioButton)findViewById(R.id.radio_group_photo);
//    radiobutton_off_material= (RadioButton)findViewById(R.id.radio_off_material);
//    radiobutton_not_actual= (RadioButton)findViewById(R.id.radio_not_actual);

    protected void gotoMyphoto(){
        Bundle bundle = new Bundle();
        bundle.putString("photopath",photopath);
        ActivityManager.changeActivity(MySelfPhotoPostActivity.this, MySelfPhotoActivity.class,bundle,false,null);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward//backward
        finish();
    }

    protected void initView(){
        super.initView();
        m_img_three.setVisibility(View.INVISIBLE);
        m_img_back_arrow.setVisibility(View.VISIBLE);
        medit.setText(aboutphoto);
        final ProgressDialog progressDialog ;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("");
        progressDialog.show();


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();


        ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + photopath, m_imgPhoto, options);
//        m_imgPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
//        m_imgPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        progressDialog.dismiss();
    }


    protected void swip(){
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(MySelfPhotoPostActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
            }
            public void onSwipeLeft() {
                gotoMyphoto();
//                Intent intent = new Intent(MySelfPhotoPostActivity.this,MySelfPhotoActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
//                finish();
            }
            public void onSwipeBottom() {
            }

        });

    }

    public void addListenerOnButton() {


        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);
                radioButton.setTextColor(Color.RED);
//                selected = (RadioButton) findViewById(selectedId);
//                selected.setTextColor(Color.GREEN);

//                if(!radioButton.equals(radiobutton_group_photo)){
//                    radiobutton_group_photo.setTextColor(Color.WHITE);
//                }
//                if(!radioButton.equals(radiobutton_not_actual)){
//                    radiobutton_not_actual.setTextColor(Color.WHITE);
//                }
//                if(!radioButton.equals(radiobutton_off_material)){
//                    radiobutton_off_material.setTextColor(Color.WHITE);
//                }

                String report  = (radioButton.getText()).toString();
                DataUtils.savePreference(Const.REPORT, report);
                Toast.makeText(getApplicationContext(),
                        "Sending New Feed to: "+radioButton.getText(), Toast.LENGTH_SHORT).show();
                facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                aboutphoto = medit.getText().toString().trim();
                String group = "facebook";


                // progress bar
                final ProgressDialog progressDialog ;
                progressDialog = new ProgressDialog(MySelfPhotoPostActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                String myname = DataUtils.getPreference(Const.NAME,"");

                ServerManager.sendNewFeed(facebookid, photopath, group, aboutphoto, rate, myname, new ResultCallBack() {
                    @Override
                    public void doAction(LogicResult result) {
                        progressDialog.dismiss();

                        if(result.mResult == LogicResult.RESULT_OK){
                            Toast.makeText(MySelfPhotoPostActivity.this, "Successfull send New Feed Photo.", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(MySelfPhotoPostActivity.this, "Sorry Fail to send New Feed Photo.", Toast.LENGTH_SHORT).show();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("photopath",photopath);
                        Intent intent = new Intent(MySelfPhotoPostActivity.this,MySelfPhotoActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
                        finish();
                    }
                });

            }

        });

    }

}
