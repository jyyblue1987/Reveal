package com.dating.reveal.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;

import org.json.JSONObject;

import java.util.List;

import com.dating.reveal.media.MediaUtils;
import com.dating.reveal.utility.CheckUtils;

/**
 * Created by JonIC on 9/23/2016.
 */

public class SoSoUtils {
    public static String getChatID(String username)
    {
        if( CheckUtils.isEmpty(username) )
            return "";

        return Const.PREFIX + username;
    }

    public static String getChatID(JSONObject profile)
    {
        if( profile == null )
            return "";

        return Const.PREFIX + profile.optString(Const.USERNAME, "");
    }

    public static void changeDateFormat(List<JSONObject> list)
    {
        if( list == null )
            return;

        for(int i = 0; i < list.size(); i++)
            getChatID(list.get(i));
    }

    public static String getUserName(String chatID)
    {
        if( CheckUtils.isEmpty(chatID) )
            return "";

        String[] list = chatID.split("_");
        if( list.length < 2 )
            return chatID;

        return chatID.substring(list[0].length() + 1);
    }

    public static String getDisplayName(JSONObject data)
    {
        if( data == null )
            return "";

        String nickname = data.optString("pname", "");
        if( CheckUtils.isEmpty(nickname) == false )
            return nickname;

        nickname = data.optString(Const.REALNAME, "");
        if( CheckUtils.isEmpty(nickname) == false )
            return nickname;


        nickname = data.optString("mobile", "");
        if( CheckUtils.isEmpty(nickname) )
            return "";

        return nickname;
    }

    public static String showCameraGalleryPage(final Context context, final int requestCode)
    {
        String camera_temp = Environment.getExternalStorageDirectory() + "/";
        camera_temp += "tijarti_temp.jpg";

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        final String output = camera_temp;

        String items[] = {"Camera", "Gallery"};

        dialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (whichButton == 0) {
                    MediaUtils.doTakePhotoFromCamera(context, requestCode, output);
                } else if (whichButton == 1) {
                    MediaUtils.doTakePhotoFromGallery(context, requestCode + 1 );
                }
                dialog.dismiss();
            }
        });

        dialog.create();
        AlertDialog alertDialog = dialog.show();

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);

        return output;
    }

    public static String startPhotoZoom(Activity activity, String picturePath, int iconSize, int requestCode ) {
        String zoom_temp = Environment.getExternalStorageDirectory() + "/";
        zoom_temp += "tijartizoom_temp.jpg";

        MediaUtils.startPhotoZoom(activity, picturePath, zoom_temp, iconSize, requestCode);
        return zoom_temp;
    }

}
