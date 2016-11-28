package com.dating.reveal;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.dating.reveal.commom.ActivityManager;

/**
 * Created by JonIC on 2016-11-14.
 */
public class SplashActivity extends Activity {
    ImageView m_imgSplash;
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
        setContentView(R.layout.activity_splash);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        findView();
        startAlphaAnimation();
    }

    ////////////////// change the color of status bar.///////////////////////////////////////////////
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
//////////////////////////////////////////////////////////////////////////////////////////////////////
    protected void findView(){
        m_imgSplash = (ImageView) findViewById( R.id.img_splash);
    }

    private void startAlphaAnimation()
    {

        AlphaAnimation face_in_out_anim = new AlphaAnimation(0.1f, 1.0f);
        face_in_out_anim.setDuration(1000);
        face_in_out_anim.setRepeatMode(Animation.ABSOLUTE);

        if (m_imgSplash != null){
            m_imgSplash.setAnimation(face_in_out_anim);
        }
        face_in_out_anim.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                onFinishAnimation();
            }
        }, 2500);
    }

    protected void onFinishAnimation(){
        gotoMainPage();
    }


        private void gotoMainPage()
        {
            Bundle bundle = new Bundle();
            ActivityManager.changeActivity(this, MainActivity.class, bundle, true, null);
//            ActivityManager.changeActivity(this, DateActivity.class, bundle, true, null);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        }

}
