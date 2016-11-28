package com.dating.reveal.showImage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.reveal.HeaderActivity;
import com.dating.reveal.MySelfProfileActivity;
import com.dating.reveal.R;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by JonIC on 2016-11-19.
 */
public class ViewImageActivity extends HeaderActivity {
    // Declare Variable
    TextView text;
    ImageView imageview;
    Button mbtn_delete;
    Button mbtn_accept;

    int position;
    String[] filepath;
    String[] filename;
    private ProgressBar bar;
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
        // Get the view from view_image.xml
        setContentView(R.layout.view_image);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        // Retrieve data from MainActivity on GridView item click
        Intent i = getIntent();

        // Get the position
        position = i.getExtras().getInt("position");

        // Get String arrays FilePathStrings
        filepath = i.getStringArrayExtra("filepath");

        // Get String arrays FileNameStrings
        filename = i.getStringArrayExtra("filename");

        // here I init this activity for view
        LoadParendActivity();
        getNotificationsize();

        // Locate the TextView in view_image.xml

        // Load the text into the TextView followed by the position

        // Locate the ImageView in view_image.xml
        imageview = (ImageView) findViewById(R.id.full_image_view);

        // Decode the filepath with BitmapFactory followed by the position
//        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);

        DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .build();
        String filepath__ = "file://" + filepath[position];
//        DisplayImageOptions options = ImageUtils.buildUILOption(R.drawable.pfimage).build();
        ImageLoader.getInstance().displayImage(filepath__, imageview, optoins);

        // Set the decoded bitmap into ImageView
//        imageview.setImageBitmap(bmp);

    }

    // progress bar
    private class ProgressTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //my stuff is here
            return null;
        };

        @Override
        protected void onPostExecute(Void result) {
            bar.setVisibility(View.GONE);
            gotoMyprofileActivity();

        }
    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
    }

    protected void findView(){
        super.findView();

        bar = (ProgressBar) findViewById(R.id.progressBar);
        mbtn_delete = (Button) findViewById(R.id.btn_delete);
        mbtn_accept = (Button) findViewById(R.id.btn_accept);
        text = (TextView) findViewById(R.id.imagetext);

    }

    protected void layoutControls(){
        super.layoutControls();
    }
    protected void initEvent(){
        super.initEvent();
        mbtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMyprofileActivity();
            }
        });
        // upload the photo to server as your photos.
        mbtn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                uploadPhoto();
                uploadPhoto();

            }
        });
    }

    protected void initView(){
        super.initView();
    }


    protected void uploadPhoto(){

        String selectedImagePath = filepath[position];
        // start progress
        final ProgressDialog progressDialog ;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Uploading Photo.");
        progressDialog.show();


        ServerManager.uploadFile(selectedImagePath, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                // end progress
                progressDialog.dismiss();

                if(result.mResult == LogicResult.RESULT_OK){

                    Toast.makeText(ViewImageActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                // goto myself profile activity.
                gotoMyprofileActivity();
            }
        });
    }
    protected void gotoMyprofileActivity(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(ViewImageActivity.this, MySelfProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
    }
}