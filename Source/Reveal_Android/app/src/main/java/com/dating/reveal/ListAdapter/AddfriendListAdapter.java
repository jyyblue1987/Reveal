package com.dating.reveal.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.reveal.R;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.DataUtils;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by JonIC on 2016-11-16.
 */
public class AddfriendListAdapter extends ArrayAdapter<ItemAddfriendActivity> {

    private final Context context;
    private final ArrayList<ItemAddfriendActivity> itemsArrayList;

    public AddfriendListAdapter(Context context, ArrayList<ItemAddfriendActivity> itemsArrayList) {

            super(context, R.layout.addfriend_listview_row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.addfriend_listview_row, parent, false);

        // 3. Get the two text view from the rowView
        final de.hdodenhof.circleimageview.CircleImageView labelView = (de.hdodenhof.circleimageview.CircleImageView) rowView.findViewById(R.id.img_profile_photo);
        final TextView valueView = (TextView) rowView.findViewById(R.id.txt_friend_name);
        final Button dots = (Button) rowView.findViewById(R.id.btn_dialog) ;
        ///////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////

        String username = itemsArrayList.get(position).getName();
        valueView.setText(username);
        String facebookid = itemsArrayList.get(position).getFacebookid();
        String photopath = "https://graph.facebook.com/" + facebookid + "/picture?type=large";

//        String photopath = itemsArrayList.get(position).getProfilephoto();

        DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        ImageLoader.getInstance().displayImage(photopath, labelView, optoins);

                // after get user name and display the user name, go on.
                // if there is a add friend request.
                if(itemsArrayList.get(position).getDescription().equals("friendadd")){
                    dots.setText("Accept");
                    dots.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String match  = itemsArrayList.get(position).getFacebookid();// matches facebookid
                            String username = itemsArrayList.get(position).getName();

                            String destination = "acceptfriend";// request content ACCEPT
                            String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,""); // my facebookid
                            String myname = DataUtils.getPreference(Const.NAME,"");
                            ServerManager.sendAddMatchRequset(myfacebookid, match, destination,myname ,username,new ResultCallBack() {
                                @Override
                                public void doAction(LogicResult result) {
                                    if(result.mResult == LogicResult.RESULT_OK){
                                        Toast.makeText(getContext(), "Add" +""+"to friend", Toast.LENGTH_SHORT).show();

                                    } // if
                                }  // doAction
                            }); //  ServerManager. sendAddMatchRequest
                        }
                    }); //dots. setOnClickListener

                }else if(itemsArrayList.get(position).getDescription().equals("add")){// if there is no add friend request.
                    dots.setText("Add");
                    dots.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String match = itemsArrayList.get(position).getFacebookid();// match fbid
                            String username = itemsArrayList.get(position).getName();
                            String destination = "friendadd";// requst content FRIENDREQUEST
                            String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                            String myname = DataUtils.getPreference(Const.NAME,"");
                            ServerManager.sendAddMatchRequset(myfacebookid, match, destination,myname, username,  new ResultCallBack() {
                                @Override
                                public void doAction(LogicResult result) {
                                    Toast.makeText(getContext(), "Send Add Friend Request", Toast.LENGTH_SHORT).show();
                                } // doAction
                            }); // ServerManager. sendAddMatchRequest
                        }
                    }); // dots. setOnClickListener
                }else{
                    dots.setText("Invite");
                 }// else

        return rowView;
    }
    protected void showdialog(){

        String appLinkUrl, previewImageUrl;

        appLinkUrl = "https://www.mydomain.com/myapplink";
        previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
//            AppInviteDialog.show(, content);
        }
    }
}