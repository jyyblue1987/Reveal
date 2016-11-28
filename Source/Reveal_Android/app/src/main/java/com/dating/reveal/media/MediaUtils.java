package com.dating.reveal.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.dating.reveal.image.CropImage;
import com.dating.reveal.utility.CheckUtils;

import java.io.File;
import java.io.IOException;

public class MediaUtils {
	public static void playAudio(String path)
	{
		if( CheckUtils.isEmpty(path) )
			return;
		
	    MediaPlayer mp = new MediaPlayer();
	    try {
			mp.setDataSource(path);
			mp.prepare();
			mp.start();
			mp.setVolume(10, 10);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	
	public static void playAudioWithIntent(Context context, String path)
	{
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		File file = new File(path);
		intent.setDataAndType(Uri.fromFile(file), "audio/*");
		context.startActivity(intent);
	}
	
	static int picture_mode = 0;
	
	private static final String IMAGE_UNSPECIFIED = "image/*";
	private static final String VIDEO_UNSPECIFIED = "video/*";
	private static final String AUDIO_UNSPECIFIED = "audio/*";
	
	public static void doTakeVideoFromCamera(Context context, int requestCode, String output )
	{
		Activity activity = (Activity) context;
		
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		
		File photo = new File(output);
		
		Uri imageUri = Uri.fromFile(photo);
		
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);   // does file create?
		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
				activity.getWindowManager().getDefaultDisplay().getOrientation());
		intent.putExtra("return-data", true);		
		
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static void doTakePhotoFromCamera(Context context, int requestCode, String output )
	{
		Activity activity = (Activity) context;
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		File photo = new File(output);
		
		Uri imageUri = Uri.fromFile(photo);
		
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);   // does file create?
		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
				activity.getWindowManager().getDefaultDisplay().getOrientation());
		intent.putExtra("return-data", true);		
		
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static void doTakePhotoFromGallery(Context context, int requestCode )
	{
		Activity activity = (Activity) context;
		
		Intent intent = null;
		
		if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_PICK);
        } else {
    	    intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
		
		intent.setType(IMAGE_UNSPECIFIED);

		activity.startActivityForResult(intent, requestCode );// to connect onActivityResult in activity	
	}
	
	public static void doTakeVideoFromGallery(Context context, int requestCode )
	{
		Activity activity = (Activity) context;
		
		Intent intent = null;
		
		if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_PICK);
        } else {
    	    intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
		
		intent.setType(VIDEO_UNSPECIFIED);

		activity.startActivityForResult(intent, requestCode );// to connect onActivityResult in activity	
	}
	
	public static String getPathFromURI(Context context, Uri uri) {
        // just some safety built in 
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the com.dating.reveal.media store first
        // this will only work for images selected from gallery
        Cursor cursor = null;
        
        try {
	        String[] projection = { MediaStore.Images.Media.DATA };
	        cursor = context.getContentResolver().query(uri, projection, null, null, null);
	        if( cursor != null ){
	            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	            cursor.moveToFirst();
	            String path = cursor.getString(column_index);
	            if( path == null || path.length() < 1 )
	            	path = uri.getPath();
	            return path;
	        }
        }
	    finally {
	    	if (cursor != null) {
	    	      cursor.close();
	    	}
	    }
	        
        // this is our fallback here
        return uri.getPath();
	}
	
	
	public static void startPhotoZoom(Activity activity, String picturePath, String outputPath, int iconSize, int requestCode ) {
	    Intent intent = new Intent(activity, CropImage.class);
	    
	    // here you have to pass absolute path to your file		
	    intent.putExtra("image-path", picturePath);
	    intent.putExtra("save-path", outputPath);
	    intent.putExtra("scale", true);
		intent.putExtra("outputX", iconSize);
		intent.putExtra("outputY", iconSize);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        activity.startActivityForResult(intent, requestCode);
	}
	
	public static void startPhotoZoom(Activity activity, String picturePath, String outputPath, int iconSize, float ratio, int requestCode ) {
	    Intent intent = new Intent(activity, CropImage.class);
	    
	    // here you have to pass absolute path to your file		
	    intent.putExtra("image-path", picturePath);
	    intent.putExtra("save-path", outputPath);
	    intent.putExtra("scale", true);
		intent.putExtra("outputX", (int)(iconSize * ratio));
		intent.putExtra("outputY", iconSize);
        intent.putExtra("aspectX", (int)(10 * ratio));
        intent.putExtra("aspectY", 10);
        activity.startActivityForResult(intent, requestCode);
	}
	
	public static boolean isVideoFile(String filename)
	{
		if( CheckUtils.isEmpty(filename) )
			return false;
		
		int lastindex = filename.lastIndexOf(".");
		if( lastindex < 0 )
			return false;
		
		String extension = filename.substring(lastindex).toUpperCase();
		
		if( extension.equals(".JPG") || extension.equals(".PNG") || extension.equals(".BMP") )
			return false;
		else if( extension.equals(".MP4") || extension.equals(".AVI") || extension.equals(".FLV") || extension.equals(".MOV") )
			return true;
		
		return false;
	}
	
	public static String getThumnail(String filename)
	{
		if( CheckUtils.isEmpty(filename) )
			return "";
		
		int lastindex = filename.lastIndexOf(".");
		if( lastindex < 0 )
			return filename + ".jpg";

		String extension = filename.substring(lastindex).toUpperCase();
		
		if( extension.equals(".JPG") || extension.equals(".PNG") || extension.equals(".BMP") )
			return filename;
		else 
		{
			String thumbname = filename.substring(0, lastindex) + ".jpg";
			return thumbname;
		}
	}
}
