package com.dating.reveal.showImage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.dating.reveal.R;
import com.dating.reveal.commom.ActivityManager;

import java.io.File;

/**
 * Created by JonIC on 2016-11-19.
 */
public class ShowImageInFolderActivity extends HeaderImageActivity {

    // Declare variables
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    GridView grid;
    LazyAdapter adapter;
    File file;

    String path;
    String folder;
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
        setContentView(R.layout.gridview_main);

        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        Bundle intent = getIntent().getExtras();
        path = intent.getString("path","");
        folder = intent.getString("folder","");
        LoadParendActivity();
        // Check for SD Card
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                    .show();
        } else {
            // Locate the image folder in your SD Card
//            file = new File(Environment.getExternalStorageDirectory()
//                    + File.separator + "SDImageTutorial");
            file = new File(path);
            // Create a new folder if no folder named SDImageTutorial exist
            file.mkdirs();
        }
// permission code

        FileNameStrings = new String[0];
        FilePathStrings = new String[0];
        if (file.isDirectory()) {
            listFile = file.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }
        }
        if(FileNameStrings.length != 0){
            // Locate the GridView in gridview_main.xml
            grid = (GridView) findViewById(R.id.gridview);
            // Pass String arrays to LazyAdapter Class
            adapter = new LazyAdapter(this, FilePathStrings, FileNameStrings);
            // Set the LazyAdapter to the GridView
            grid.setAdapter(adapter);

            // Capture gridview item click
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Intent i = new Intent(ShowImageInFolderActivity.this, ViewImageActivity.class);
                    // Pass String arrays FilePathStrings
                    i.putExtra("filepath", FilePathStrings);
                    // Pass String arrays FileNameStrings
                    i.putExtra("filename", FileNameStrings);
                    // Pass click position
                    i.putExtra("position", position);
                    startActivity(i);
                }

            });

        }
    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
    }

    protected void findView(){
        super.findView();
    }

    protected void layoutControls(){
        super.layoutControls();
    }

    protected void initEvent(){
        super.initEvent();
        m_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoImagefolderActivity();
            }
        });
    }

    protected void initView(){
        super.initView();
        m_folder.setText(folder);
//        m_folder.setTextColor(Color.RED);
    }

    protected void gotoImagefolderActivity(){
        Bundle bundle = new Bundle();
        ActivityManager.changeActivity(ShowImageInFolderActivity.this, ImagefolderActivity.class, bundle,false, null );
        overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
        finish();
    }


}