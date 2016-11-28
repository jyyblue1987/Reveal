package com.dating.reveal.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dating.reveal.Item_models.Notification;
import com.dating.reveal.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by JonIC on 2016-11-16.
 */
public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private final Context context;
    private final ArrayList<Notification> itemsArrayList;

    public NotificationListAdapter(Context context, ArrayList<Notification> itemsArrayList) {

        super(context, R.layout.notify_list_item, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.notify_list_item, parent, false);

        // 3. Get the two text view from the rowView
        final de.hdodenhof.circleimageview.CircleImageView labelView = (de.hdodenhof.circleimageview.CircleImageView) rowView.findViewById(R.id.img_profile_photo);
        final TextView valueView = (TextView) rowView.findViewById(R.id.txt_newfeed_description);
        // make the sentence to display in row from notekind.

        final String notekind   = itemsArrayList.get(position).notekind;
        final String feedval    = itemsArrayList.get(position).feedval;
        String facebookid       = itemsArrayList.get(position).sender;

        String name = itemsArrayList.get(position).username;
        String sentence = composeSentence(name, notekind);
//        String photopath = itemsArrayList.get(position).profilephoto;
        valueView.setText(sentence);


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        String photopath = "https://graph.facebook.com/" + facebookid + "/picture?type=large";

        ImageLoader.getInstance().displayImage(photopath, labelView, options);
//        ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + photopath, labelView, options);
        return rowView;
    }

    protected String composeSentence(String name, String notekind){
        String ret=name;
        if(notekind.equals("newfeed")){
            return ret+" posted a photo in your new feed.";

        }else if(notekind.equals("message")){
            return ret+ " sent you a message.";

        }else if(notekind.equals("like")){
            return ret + " liked you photo.";
        }
        else if(notekind.equals("comment")){
            return ret+ " commented on your photo.";
        }
        else if(notekind.equals("requestfriend")){
            return ret + " is eager to be friend with you.";
        }
        else if(notekind.equals("matchRequest")){
            return  ret +" is eager to be match with you.";

        }
        else if(notekind.equals("acceptfriend")){
            return ret + " is a new friend.";

        }
        else if(notekind.equals("newmatch")){ // find new match
            return ret + " is a new match.";

        }else if(notekind.equals("friendadd")){ // facebook friend want to be reveal friend
            return ret + " is your facebook friend.";
        }
        else if(notekind.equals("friendaccept")){ // facebook friend become reveal friend.
            return  ret + " become your reveal friend.";
        }
        return ret + " send unknown notification.";
    }

}