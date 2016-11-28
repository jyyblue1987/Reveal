package com.dating.reveal;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.DataUtils;

import org.json.JSONObject;

/**
 * Created by JonIC on 2016-11-08.
 */
public class BlockChattingActivity extends HeaderActivity {
//    android:id="@+id/radio_group_photo"
//    android:id="@+id/radio_off_material"
//    android:id="@+id/radio_not_actual"
//    android:id="@+id/radio_wrong_gender"
//    android:id="@+id/radioSex"
//    android:id="@+id/btn_confirm"
    RadioGroup radioGroup;
    RadioButton rbtn_group_photo;
    RadioButton rbtn_off_material;
    RadioButton rbtn_not_actual;
    RadioButton rbtn_wrong_gender;
    Button mbtn_block;
    String blockfacebookid = "";

    RadioButton radioSexButton;
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
        setContentView(R.layout.activity_block_chat);

        Bundle intent = getIntent().getExtras();
        blockfacebookid = intent.getString("blockfacebookid");
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));
        LoadParendActivity();
        getNotificationsize();
    }
    protected void LoadParendActivity(){
        super.LoadParendActivity();
    }

    protected void findView(){
        super.findView();
        radioGroup  = (RadioGroup) findViewById(R.id.radioSex);
        rbtn_group_photo = (RadioButton) findViewById(R.id.radio_group_photo);
        rbtn_off_material= (RadioButton) findViewById(R.id.radio_off_material);
        rbtn_not_actual= (RadioButton) findViewById(R.id.radio_not_actual);
        rbtn_wrong_gender= (RadioButton) findViewById(R.id.radio_wrong_gender);
        mbtn_block = (Button) findViewById(R. id. btn_confirm);

    }

    protected void layoutControls(){
        super.layoutControls();
    }

    protected void initEvent(){
        super.initEvent();
        m_img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoChatpage();
            }
        });

        mbtn_block.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);

                String report  = (radioSexButton.getText()).toString();
                String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                // show dialog
                final ProgressDialog progressDialog ;
                progressDialog = new ProgressDialog(BlockChattingActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                ServerManager.blockmatch(myfacebookid, blockfacebookid, report, new ResultCallBack(){
                    @Override
                    public void doAction(LogicResult result) {
                        if(result.mResult == LogicResult.RESULT_OK){

                        }else {
                            JSONObject userinfo = result.getData();
                            String error = userinfo.optString("error_msg");
                            Toast.makeText(BlockChattingActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    };
                });
                // here we save the
                DataUtils.savePreference(Const.BLOCK, report);
                Toast.makeText(getApplicationContext(),
                        radioSexButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbtn_group_photo.isChecked()) {
                    rbtn_group_photo.setTextColor(Color.parseColor("#C80028"));
                    rbtn_off_material.setTextColor(Color.BLACK);
                    rbtn_not_actual.setTextColor(Color.BLACK);
                    rbtn_wrong_gender.setTextColor(Color.BLACK);
                }
                if (rbtn_off_material.isChecked()) {
                    rbtn_group_photo.setTextColor(Color.BLACK);
                    rbtn_off_material.setTextColor(Color.parseColor("#C80028"));
                    rbtn_not_actual.setTextColor(Color.BLACK);
                    rbtn_wrong_gender.setTextColor(Color.BLACK);
                }
                if (rbtn_not_actual.isChecked()) {
                    rbtn_group_photo.setTextColor(Color.BLACK);
                    rbtn_off_material.setTextColor(Color.BLACK);
                    rbtn_not_actual.setTextColor(Color.parseColor("#C80028"));
                    rbtn_wrong_gender.setTextColor(Color.BLACK);
                }
                if (rbtn_wrong_gender.isChecked()) {
                    rbtn_group_photo.setTextColor(Color.BLACK);
                    rbtn_off_material.setTextColor(Color.BLACK);
                    rbtn_not_actual.setTextColor(Color.BLACK);
                    rbtn_wrong_gender.setTextColor(Color.parseColor("#C80028"));
                }
            }
        });

    }

    protected void sendBlock(String blocksort){
        String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        String blockfacebookid;
    }


    protected void initView(){
        super.initView();
        m_img_three.setVisibility(View.INVISIBLE);
        m_img_back_arrow.setVisibility(View.VISIBLE);
    }

    protected void  gotoChatpage(){

        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);  // backward
        finish();
    }


}
