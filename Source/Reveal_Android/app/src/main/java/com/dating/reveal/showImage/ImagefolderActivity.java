package com.dating.reveal.showImage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dating.reveal.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JonIC on 2016-11-19.
 */
public class ImagefolderActivity extends HeaderImageActivity implements AdapterView.OnItemClickListener {

    List<GridViewItem> gridItems;

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
        setContentView(R.layout.image_folder);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        String m_cameraTempPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/";
        String m_cameraTempPath2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+ "/";
        LoadParendActivity();
        setGridAdapter(m_cameraTempPath);
    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
        findView();
        layoutControls();
        initEvent();
        initView();
    }
   protected void findView(){
       super.findView();
   }

    protected void  layoutControls(){
        super.layoutControls();
    }

    protected void initEvent(){
        super.initEvent();
    }

    protected void initView(){
        super.initView();
        m_photo.setVisibility(View.INVISIBLE);
        m_back_arrow.setVisibility(View.INVISIBLE);
//        m_folder.setTextColor(Color.RED);
        m_folder.setText("Photos");
    }
    /**
     * This will create our GridViewItems and set the adapter
     *
     * @param path
     *            The directory in which to search for images
     */
    private void setGridAdapter(String path) {
        // Create a new grid adapter
        gridItems = createGridItems(path);
        MyGridAdapter adapter = new MyGridAdapter(this, gridItems);

        // Set the grid adapter
        ListView gridView = (ListView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        // Set the onClickListener
        gridView.setOnItemClickListener(this);
    }


    /**
     * Go through the specified directory, and create items to display in our
     * GridView
     */
    private List<GridViewItem> createGridItems(String directoryPath) {
        List<GridViewItem> items = new ArrayList<GridViewItem>();
        String m_cameraTempPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/";
        String m_cameraTempPath2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+ "/Camera/";
        items.add(new GridViewItem(m_cameraTempPath, true, null));
        items.add(new GridViewItem(m_cameraTempPath2, true, null));

        return items;
    }


    /**
     * Checks the file to see if it has a compatible extension.
     */
    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png"))
        // Add other formats as desired
        {
            return true;
        }
        return false;
    }


    @Override
    public void
    onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (gridItems.get(position).isDirectory()) {
            // here we should show all images that is in this folder.
            String path = gridItems.get(position).getPath();
            String[] spilt = path.split("\\/");
            String folder = spilt[spilt.length-1]; // the folder name to show in the showimagefolderactivity's header ' center
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            bundle.putString("folder", folder);
            Intent intent = new Intent(ImagefolderActivity.this, ShowImageInFolderActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            // forward
//            setGridAdapter(gridItems.get(position).getPath());

        }
        else {
            // Display the image
        }

    }

    /**
     * This can be used to filter files.
     */
    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            else if (isImageFile(file.getAbsolutePath())) {
                return true;
            }
            return false;
        }
    }

}