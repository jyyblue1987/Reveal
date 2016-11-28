package com.dating.reveal.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JonIC on 2016-10-17.
 */
// yet it is not used but will use future.
public class MatchesListAdapter extends ArrayAdapter<JSONObject> {
    public HashMap<String,String> checked = new HashMap<String,String>();

    private TextView chatText;
    private List<JSONObject> locationHistoryList = new ArrayList<JSONObject>();
    //    private List<String> locationHistoryList = new ArrayList<String>();
    private Context context;
    private int mResource;

    @Override
    public void add(JSONObject object) {
        locationHistoryList.add(object);
        super.add(object);
    }

    public MatchesListAdapter(Context context, int textViewResourceId, List<JSONObject>  data) {
        super(context, textViewResourceId);
        this.context = context;
        locationHistoryList = data;
        mResource = textViewResourceId;
    }

    public int getCount() {
        return this.locationHistoryList.size();
    }

    public JSONObject getItem(int index) {
        return this.locationHistoryList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject locationHistoryObj = getItem(position);
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(mResource, parent, false);

        String facebookid = locationHistoryObj.optString("facebookid");
//        String comment= locationHistoryObj.optString("comment");
//        json.put("facebookid", commentContent[0]);
//        json.put("comment",commentContent[1]);

//        ((TextView) ViewHolder.get(convertView, R.id.txt_photo_path)).setText(facebookid);
//        ((TextView) ViewHolder.get(convertView, R.id.txt_about_photo)).setText(comment);
//        ((ImageView) ViewHolder.get(convertView, R.id.img_profile)).setImageResource();
//        ((TextView) ViewHolder.get(convertView, R.id.txt_city)).setText(locationHistoryObj);

        return convertView;
    }
    // get checked items.
    public void setCheckedItem(int item) {
        JSONObject selectedFromList = (this.getItem(item));

//        if (checked.containsKey(selectedFromList)){
//            checked.remove(selectedFromList);
//        }
//
//        else {
//
//            checked.put(selectedFromList, selectedFromList);
//        }
//    }
        // return checked items.
//    public HashMap<String, String> getCheckedItems(){
//        return checked;
    }
}