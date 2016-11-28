package com.dating.reveal;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dating.reveal.commom.ActivityManager;
import com.dating.reveal.designs.LayoutUtils;
import com.dating.reveal.designs.ScreenAdapter;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.DataUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by JonIC on 2016-11-15.
 */
public class HeaderActivity extends Activity {
    RelativeLayout m_layLeft = null;
    RelativeLayout m_layRight = null;

    ImageView m_img_three;
    ImageView m_img_back_arrow;
    TextView m_txt_app_title;
    ImageView m_img_bell;
    Button m_btn_notify;

    protected void LoadParendActivity(){
        findView();
        layoutControls();
        initEvent();
        initView();
    }
    protected void findView()
    {
        m_layLeft = (RelativeLayout) findViewById(R.id. fragment_header).findViewById(R.id.lay_left);
        m_layRight = (RelativeLayout) findViewById( R.id. fragment_header). findViewById(R.id.lay_right);
        m_img_three = (ImageView) findViewById( R.id.fragment_header) .findViewById(R.id.img_left_three);
        m_img_back_arrow = (ImageView) findViewById( R.id.fragment_header).findViewById(R.id.img_back_arrow);
        m_txt_app_title = (TextView) findViewById(R.id.fragment_header). findViewById(R.id.txt_app_title);
        m_img_bell = (ImageView) findViewById(R.id.fragment_header). findViewById(R.id.img_right_bell);
        m_btn_notify = (Button) findViewById(R.id.fragment_header).findViewById(R.id.btn_notification);
     }
    protected void layoutControls()
    {
        LayoutUtils.setSize(m_layLeft, 160, 140, true);
        LayoutUtils.setSize(m_layRight, 160, 140, true);

        LayoutUtils.setSize(m_img_bell, 100, 100, true);

        LayoutUtils.setSize(m_btn_notify, 60, 60, true);
        LayoutUtils.setMargin(m_btn_notify, 50, 20, 0, 0, true);
        m_btn_notify.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));

        LayoutUtils.setSize(m_img_three, 100, 100, true);
		LayoutUtils.setMargin(m_img_bell, 0, 20, 0, 0, true);

//		LayoutUtils.setSize(m_txtNotify, 53, 53, true);
//		m_txtNotify.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(44));
//		LayoutUtils.setMargin(m_txtNotify, 90, 60, 0, 0, true);

        m_txt_app_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(60));

    }

    protected void initView(){
        String notify_count = DataUtils.getPreference(Const.NOTIFICATION_SIZE, "0");
        if(notify_count.equals("0")){
            m_btn_notify.setText("");
            m_btn_notify.setVisibility(View.INVISIBLE);
        }else{
            m_btn_notify.setText(notify_count);
            m_btn_notify.setVisibility(View.VISIBLE);
        }
        m_img_back_arrow.setVisibility(View.INVISIBLE);
    }

    protected void initEvent(){
        m_img_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSetting();
            }
        });

        m_img_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNotification();
            }
        });

        ((ImageView)findViewById(R.id.img_right_bell)).setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    ((ImageView) v.findViewById(R.id.img_right_bell)).setImageResource(R.drawable.icon_bell_grey);

                if(event.getAction() == MotionEvent.ACTION_UP)
                    ((ImageView) v.findViewById(R.id.img_right_bell)).setImageResource(R.drawable.bell);

                return false;
            }
        });
    }

    protected void gotoNotification(){
        Bundle bundle = new Bundle();
        ActivityManager.changeActivity(HeaderActivity.this, NotificationActivity.class, bundle, false, null);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        finish();
    }

    protected void getNotificationsize(){
        String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        ServerManager.getNotificationSize(facebookid, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userinfo = result.getData();
                    JSONArray size = userinfo.optJSONArray("content");
                    JSONObject json = size.optJSONObject(0);
                    String notify_count = json.optString("count(*)", "");
                    if(notify_count.equals("0")){
                        m_btn_notify.setText("");
                        m_btn_notify.setVisibility(View.INVISIBLE);
                    }else{
                        m_btn_notify.setText(notify_count);
                        m_btn_notify.setVisibility(View.VISIBLE);
                    }

                }

            }
        });
    }

    protected void gotoSetting(){
        Bundle bundle = new Bundle();
        ActivityManager.changeActivity(HeaderActivity.this, SettingActivity.class, bundle, false, null);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        finish();
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

}
