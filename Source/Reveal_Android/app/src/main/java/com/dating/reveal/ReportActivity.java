package com.dating.reveal;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dating.reveal.main.Const;
import com.dating.reveal.utility.DataUtils;

/**
 * Created by JonIC on 2016-11-07.
 */
public class ReportActivity extends HeaderActivity
{
    private RadioGroup radioGroup;
    private RadioButton radioSexButton;
    private Button btnConfirm;


    private RadioButton radiobutton_group_photo;
    private RadioButton radiobutton_off_material;
    private RadioButton radiobutton_not_actual;
    private RadioButton radiobutton_feel_spam;
    private RadioButton radiobutton_wrong_gender;
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
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));
        LoadParendActivity();
        getNotificationsize();
        addListenerOnButton();

    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
    }

    protected void initView(){
        super.initView();
        m_img_three.setVisibility(View.INVISIBLE);
        m_img_back_arrow.setVisibility(View.VISIBLE);
    }
    protected void initEvent(){
        super.initEvent();
        m_img_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRatingActivity();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radiobutton_group_photo.isChecked()) {
                    radiobutton_group_photo.setTextColor(Color.parseColor("#C80028"));
                    radiobutton_off_material.setTextColor(Color.BLACK);
                    radiobutton_not_actual.setTextColor(Color.BLACK);
                    radiobutton_feel_spam.setTextColor(Color.BLACK);
                    radiobutton_wrong_gender.setTextColor(Color.BLACK);
                }
                if (radiobutton_off_material.isChecked()) {
                    radiobutton_group_photo.setTextColor(Color.BLACK);
                    radiobutton_off_material.setTextColor(Color.parseColor("#C80028"));
                    radiobutton_not_actual.setTextColor(Color.BLACK);
                    radiobutton_feel_spam.setTextColor(Color.BLACK);
                    radiobutton_wrong_gender.setTextColor(Color.BLACK);
                }
                if (radiobutton_not_actual.isChecked()) {
                    radiobutton_group_photo.setTextColor(Color.BLACK);
                    radiobutton_off_material.setTextColor(Color.BLACK);
                    radiobutton_not_actual.setTextColor(Color.parseColor("#C80028"));
                    radiobutton_feel_spam.setTextColor(Color.BLACK);
                    radiobutton_wrong_gender.setTextColor(Color.BLACK);
                }
                if (radiobutton_feel_spam.isChecked()) {
                    radiobutton_group_photo.setTextColor(Color.BLACK);
                    radiobutton_off_material.setTextColor(Color.BLACK);
                    radiobutton_not_actual.setTextColor(Color.BLACK);
                    radiobutton_feel_spam.setTextColor(Color.parseColor("#C80028"));
                    radiobutton_wrong_gender.setTextColor(Color.BLACK);
                }
                if (radiobutton_wrong_gender.isChecked()) {
                    radiobutton_group_photo.setTextColor(Color.BLACK);
                    radiobutton_off_material.setTextColor(Color.BLACK);
                    radiobutton_not_actual.setTextColor(Color.BLACK);
                    radiobutton_feel_spam.setTextColor(Color.BLACK);
                    radiobutton_wrong_gender.setTextColor(Color.parseColor("#C80028"));
                }
            }
        });

    }

    protected void layoutControls(){
        super.layoutControls();
    }
    protected void findView(){
        super.findView();
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        radioGroup = (RadioGroup) findViewById(R.id.radioSex);

        radiobutton_group_photo = (RadioButton)findViewById(R.id.radio_group_photo);
        radiobutton_off_material= (RadioButton)findViewById(R.id.radio_off_material);
        radiobutton_not_actual= (RadioButton)findViewById(R.id.radio_not_actual);
        radiobutton_feel_spam= (RadioButton)findViewById(R.id.radio_feel_spam);
        radiobutton_wrong_gender= (RadioButton)findViewById(R.id.radio_wrong_gender);

    }
    public void addListenerOnButton() {


        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);
                String report  = (radioSexButton.getText()).toString();
                DataUtils.savePreference(Const.REPORT, report);
                Toast.makeText(getApplicationContext(),
                        radioSexButton.getText(), Toast.LENGTH_SHORT).show();
                gotoRatingActivity();

            }

        });

    }
    protected void gotoRatingActivity(){
//        Bundle bundle = new Bundle();
//        bundle.putString("receivephoto", "1");
//
//        Intent intent = new Intent(ReportActivity.this, RatingActivity.class);
//        intent.putExtras(bundle);
//
//        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        finish();
    }
}
