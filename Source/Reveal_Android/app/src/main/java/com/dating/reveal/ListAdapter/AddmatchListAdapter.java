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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by JonIC on 2016-11-16.
 */
public class AddmatchListAdapter extends ArrayAdapter<ItemAddmatchActivity> {

    private final Context context;
    private final ArrayList<ItemAddmatchActivity> itemsArrayList;

    public AddmatchListAdapter(Context context, ArrayList<ItemAddmatchActivity> itemsArrayList) {

        super(context, R.layout.addfriend_listview_row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.addfriend_listview_row, parent, false);

        // 3. Get the two text view from the rowView
        final de.hdodenhof.circleimageview.CircleImageView labelView = (de.hdodenhof.circleimageview.CircleImageView) rowView.findViewById(R.id.img_profile_photo);
        final TextView valueView = (TextView) rowView.findViewById(R.id.txt_friend_name);
        final Button dots = (Button) rowView.findViewById(R.id.btn_dialog) ;

        // 4. Set the text for textView
        final  int pos = position;

        dots.setVisibility(View.VISIBLE);
        dots.setBackgroundResource(R.drawable.red_button);
        // request the user name for the facebookid;
        String facebookid = itemsArrayList.get(pos).getFacebookid();
//        String photopath = itemsArrayList.get(position).getProfilephoto();
        String username = itemsArrayList.get(position).getName();
        valueView.setText(username);

        DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        String photopath = "https://graph.facebook.com/" + facebookid + "/picture?type=large";

        ImageLoader.getInstance().displayImage(photopath, labelView, optoins);

                    // after get user name and display the user name, go on.
                    // if there is a add friend request.
        if(itemsArrayList.get(pos).getDescription().equals(Const.FRIENDREQUEST)){
            dots.setText("Accept");
            dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String match  = itemsArrayList.get(pos).getFacebookid();// matches facebookid
                    String username = itemsArrayList.get(pos).getName();
                    String destination = "acceptfriend";// request content ACCEPT
                    String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,""); // my facebookid
                    String myname = DataUtils.getPreference(Const.NAME,"");

                    ServerManager.sendAddMatchRequset(myfacebookid, match, destination, myname, username ,new ResultCallBack() {
                        @Override
                        public void doAction(LogicResult result) {
                            if(result.mResult == LogicResult.RESULT_OK){
//                                Toast.makeText(getContext(), "Add " + username +" to friend", Toast.LENGTH_SHORT).show();

                            } // if
                        }  // doAction
                    }); //  ServerManager. sendAddMatchRequest
                }
            }); //dots. setOnClickListener

        }else {// if there is no add friend request.
            dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String match = itemsArrayList.get(pos).getFacebookid();// match fbid
                    String username = itemsArrayList.get(pos).getName();
                    String destination = Const.FRIENDREQUEST;// requst content FRIENDREQUEST
                    String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                    String myname = DataUtils.getPreference(Const.NAME,"");
                    ServerManager.sendAddMatchRequset(myfacebookid, match, destination,myname, username ,new ResultCallBack() {
                        @Override
                        public void doAction(LogicResult result) {
                            Toast.makeText(getContext(), "Send Add Friend Request", Toast.LENGTH_SHORT).show();
                        } // doAction
                    }); // ServerManager. sendAddMatchRequest
                }
            }); // dots. setOnClickListener
        }// else

        return rowView;
    }
}