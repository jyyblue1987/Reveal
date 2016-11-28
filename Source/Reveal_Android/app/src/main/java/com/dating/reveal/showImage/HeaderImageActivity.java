package com.dating.reveal.showImage;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dating.reveal.MySelfProfileActivity;
import com.dating.reveal.R;
import com.dating.reveal.commom.ActivityManager;
import com.dating.reveal.designs.LayoutUtils;
import com.dating.reveal.designs.ScreenAdapter;

/**
 * Created by JonIC on 2016-11-19.
 */
public class HeaderImageActivity extends Activity {
    RelativeLayout m_layLeft = null;
    RelativeLayout m_layRight = null;

    ImageView m_back_arrow;
    TextView m_photo;
    TextView m_folder;
    TextView m_cancel;

    protected void LoadParendActivity(){
        findView();
        layoutControls();
        initEvent();
        initView();
    }
    protected void findView()
    {
        m_layLeft = (RelativeLayout) findViewById(R.id. fragment_header_image).findViewById(R.id.lay_left);
        m_layRight = (RelativeLayout) findViewById( R.id. fragment_header_image). findViewById(R.id.lay_right);

        m_back_arrow = (ImageView) findViewById( R.id.fragment_header_image) .findViewById(R.id.img_back_arrow);
        m_photo = (TextView) findViewById(R.id.fragment_header_image). findViewById(R.id.txt_photo);
        m_folder = (TextView) findViewById(R.id.fragment_header_image). findViewById(R.id.txt_folder);
        m_cancel = (TextView) findViewById(R.id.fragment_header_image).findViewById(R.id.txt_cancel);
    }
    protected void layoutControls()
    {
        LayoutUtils.setSize(m_layLeft, 300, 140, true);
        LayoutUtils.setSize(m_layRight, 240, 140, true);

        LayoutUtils.setSize(m_back_arrow, 50, 60, true);

        m_photo.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(60));
        m_folder.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(60));
        m_cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(60));
        LayoutUtils.setMargin(m_cancel,0,0,10,0,false);
    }

    protected void initView(){

    }

    protected void initEvent(){
        m_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMyselfprofile();
            }
        });

    }

    protected void gotoMyselfprofile(){
        Bundle bundle = new Bundle();
        ActivityManager.changeActivity(HeaderImageActivity.this, MySelfProfileActivity.class, bundle, false, null);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        finish();
    }
    protected void gotoSetting(){
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
