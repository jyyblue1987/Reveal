package com.dating.reveal.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dating.reveal.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by JonIC on 2016-11-18.
 */
public class LikeListAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> itemsArrayList;

    public LikeListAdapter(Context context, ArrayList<Item> itemsArrayList) {

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

        dots.setVisibility(View.INVISIBLE);
        // request the user name for the facebookid;
        String facebookid = itemsArrayList.get(pos).getFacebookid();
//        ServerManager.getPersonInfo(facebookid, new ResultCallBack() {
//            @Override
//            public void doAction(LogicResult result) {
//                if(result.mResult == LogicResult.RESULT_OK){
//                    JSONObject jsonResult = result.getData();
//                    JSONArray userinfo  = jsonResult.optJSONArray("content");
//
//                    JSONArray empty = new JSONArray();
//                    if(userinfo.equals(empty)){
//                        Toast.makeText(getContext(), "Sorry, could not find person information.",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    JSONObject jsonObject = userinfo.optJSONObject(0);
//                    final String username = jsonObject.optString("name","");
        String username = itemsArrayList.get(position).getTitle();
//        String photopath = itemsArrayList.get(position).getProfilephoto();
        valueView.setText(username);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        String photopath = "https://graph.facebook.com/" + facebookid + "/picture?type=large";

        ImageLoader.getInstance().displayImage(photopath, labelView, options);

                    // after get user name and display the user name, go on.
                    // if there is a add friend request.
//                    if(itemsArrayList.get(pos).getDescription().equals(Const.FRIENDREQUEST)){
//                        dots.setText("Accept");
//                        dots.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String match  = itemsArrayList.get(pos).getFacebookid();// matches facebookid
//                                String destination = "acceptfriend";// request content ACCEPT
//                                String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,""); // my facebookid
//
//                                ServerManager.sendAddMatchRequset(myfacebookid, match, destination, new ResultCallBack() {
//                                    @Override
//                                    public void doAction(LogicResult result) {
//                                        if(result.mResult == LogicResult.RESULT_OK){
//                                            Toast.makeText(getContext(), "Add" + username +"to friend", Toast.LENGTH_SHORT).show();
//
//                                        } // if
//                                    }  // doAction
//                                }); //  ServerManager. sendAddMatchRequest
//                            }
//                        }); //dots. setOnClickListener
//
//                    }else {// if there is no add friend request.
//                        dots.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String match = itemsArrayList.get(pos).getFacebookid();// match fbid
//                                String destination = Const.FRIENDREQUEST;// requst content FRIENDREQUEST
//                                String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
//                                ServerManager.sendAddMatchRequset(myfacebookid, match, destination, new ResultCallBack() {
//                                    @Override
//                                    public void doAction(LogicResult result) {
//                                        Toast.makeText(getContext(), "Send Add Friend Request", Toast.LENGTH_SHORT).show();
//                                    } // doAction
//                                }); // ServerManager. sendAddMatchRequest
//                            }
//                        }); // dots. setOnClickListener
//                    }// else
//                }else{
//                    JSONObject jsonObject = result.getData();
//                    String error = jsonObject.optString("error_msg","");
//                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                }// else
//            }// doAction
//        });// ServerManager. getPersonInfo
//

//        valueView.setText(itemsArrayList.get(position).getTitle());


        // 5. retrn rowView
        return rowView;
    }
}