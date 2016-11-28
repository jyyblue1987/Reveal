package com.dating.reveal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.dating.reveal.commom.ActivityManager;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.photo.GalleryAdapter;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class MyselfUploadPhotoActivity extends HeaderActivity {

    GridView gridGallery;
    Handler handler;
    GalleryAdapter adapter;

    ImageView imgSinglePick;
    Button btnLeft;
    Button btnRight;
    Button btnCenter;

    String action;
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;

    String mCameraPhotoPath = null;
    ImageView img_select = null;

    static String selectedImagePath=null;
    static Uri mCapturedImageURI=null;
    TextView mUploadPhoto;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){

        super.onCreateContextMenu(menu, v, menuInfo);
        LoadParendActivity();
        menu.setHeaderTitle("Choose Image");
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.camera_menu, menu);
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
    }
    protected void initView(){
        super.initView();
    }

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
        setContentView(R.layout.mul_main);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        LoadParendActivity();
        getNotificationsize();

        initImageLoader();
        init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private File createImageFile() throws IOException {
        mCameraPhotoPath = null;
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCameraPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void init() {

        handler = new Handler();
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        imgSinglePick = (ImageView) findViewById(R.id.imgSinglePick);

        img_select = (ImageView)findViewById(R.id.img_select);
        registerForContextMenu(img_select);
        img_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                String m_cameraTempPath = Environment.getExternalStorageDirectory() + "/";
                m_cameraTempPath += "camera_temp.jpg";//.jpg
                String output = m_cameraTempPath;
                File photo = new File(output);
                Uri imageUri = Uri.fromFile(photo);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);   // does file create?
                intent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION,
                        getWindowManager().getDefaultDisplay().getOrientation());
//                intent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION,
//                        Surface.ROTATION_90);

                intent.putExtra("return-data", true);
                startActivityForResult(intent, 300);

//                startActivityForResult(intent, 300);

//                Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
//                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                File out = Environment.getExternalStorageDirectory();
//                String imagename = "myphoto";
//                out = new File(out, imagename);
//                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
//                startActivityForResult(i, 300);

            }
        });

        mUploadPhoto = (TextView)findViewById(R.id.txt_upload_photo);
        mUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here should upload image to server.
                //
                Bundle bundle = new Bundle();
                ActivityManager.changeActivity(MyselfUploadPhotoActivity.this, MySelfProfileActivity.class, bundle, false, null);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            adapter.clear();
// here you can find the image.

            viewSwitcher.setDisplayedChild(1);
            String single_path = data.getStringExtra("single_path");
            selectedImagePath=single_path;
            imageLoader.displayImage("file://" + single_path, imgSinglePick);


        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
//            String[] all_path = data.getStringArrayExtra("all_path");
//
//            ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();
//
//            for (String string : all_path) {
//                CustomGallery item = new CustomGallery();
//                item.sdcardPath = string;
//
//                dataT.add(item);
//            }
//
//            viewSwitcher.setDisplayedChild(0);
//            adapter.addAll(dataT);

            //camera
        } else if (requestCode == 300 && resultCode == Activity.RESULT_OK) {
            adapter.clear();
            viewSwitcher.setDisplayedChild(1);
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imgSinglePick.setImageBitmap(photo);
//            Uri tempUri = getImageUri(getApplicationContext(), photo);
//            selectedImagePath = getRealPathFromURI(tempUri);
//            File finalFile = new File(selectedImagePath);

            String m_cameraTempPath = Environment.getExternalStorageDirectory() + "/";
            m_cameraTempPath += "camera_temp.jpg";//.jpg
            String output = m_cameraTempPath;
//            File photo = new File(output);
//
//            File photo = new File(output);
//            Bitmap myBitmap = BitmapFactory.decodeFile(output);
//            Bitmap mm = rotateImage(myBitmap, 90);
//            FileOutputStream out = null;
//            try {
//                out = new FileOutputStream(output);
//                mm.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//                // PNG is a lossless format, the compression factor (100) is ignored
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (out != null) {
//                        out.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

//            imgSinglePick.setImageBitmap(myBitmap);

            DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(true)
                    .build();

            imageLoader.displayImage("file://" + output, imgSinglePick, optoins);


//            Uri imageUri = Uri.fromFile(photo);

//            Uri tempUri = getImageUri(getApplicationContext(), myBitmap);
//            selectedImagePath = getRealPathFromURI(tempUri);
//            Uri imageUri = Uri.fromFile(photo);
//            File finalFile = new File(selectedImagePath);

            selectedImagePath = output;
            ServerManager.uploadFile(selectedImagePath, new ResultCallBack() {
                @Override
                public void doAction(LogicResult result) {
                    if(result.mResult == LogicResult.RESULT_OK){

                    }
                }
            });

//            System.out.println(mImageCaptureUri);
//  first selection
//            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
//            String strtemp=null;
//
//            File f = new File(strtemp);
//            Uri contentUri = Uri.fromFile(f);
//            mediaScanIntent.setData(contentUri);
//            this.sendBroadcast(mediaScanIntent);
//
//
//            String strCon = contentUri.toString();
//            selectedImagePath=(f.toString().trim());
//            Bitmap com.dating.reveal.photo = (Bitmap) data.getExtras().get("data");
////			imageLoader.displayImage(strCon,imgSinglePick);

//            imgSinglePick.setImageBitmap(com.dating.reveal.photo);
        }
    }

    public Bitmap rotateImage(Bitmap src, float degree) {

        // Matrix
        Matrix matrix = new Matrix();
        // rotate angle
        matrix.postRotate(degree);
        // create bitmap
        return Bitmap.createBitmap(src, 0, 0,  src.getWidth(),src.getHeight(),
                 matrix, true);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    protected void gotoProfile2(){
        Bundle bundle=new Bundle();
    }


//    public static Bitmap rotateImage(Bitmap source, float angle) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
//                true);
//    }
}

