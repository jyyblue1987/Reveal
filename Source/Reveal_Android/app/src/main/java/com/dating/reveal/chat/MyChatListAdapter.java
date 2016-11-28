package com.dating.reveal.chat;

/**
 * Created by JonIC on 2016-10-15.
 */

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dating.reveal.R;
import com.dating.reveal.adapter.ViewHolder;
import com.dating.reveal.main.Const;
import com.dating.reveal.utility.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class  MyChatListAdapter extends ArrayAdapter<JSONObject> {

    private TextView chatText;
    private List<JSONObject> chatMessageList = new ArrayList<JSONObject>();
    private Context context;

    @Override
    public void add(JSONObject object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public MyChatListAdapter(Context context, int textViewResourceId,List<JSONObject>  data) {
        super(context, textViewResourceId);
        this.context = context;
        chatMessageList = data;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public JSONObject getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        String strWho64=chatMessageObj.optString("name","");
        String strWho = strWho64;// DecodeFrom64(strWho64);
        String strMe = DataUtils.getPreference(Const.FACEBOOKID,"");
        if (strWho.equals(strMe)) {
            convertView = inflater.inflate(R.layout.soc_chat_fragment_chatitem_right, parent, false);

        }else{
            convertView = inflater.inflate(R.layout.soc_chat_fragment_chatitem_voice, parent, false);
        }
        String url = null;
        String strMsg = null;
        String play = null;
        JSONObject jsonMessage = null;
        final JSONObject item = getItem(position);
        if (item == null)
            return null;
//        String strUsername64 = item.optString("name", "");
//        String strUsername = strUsername64;// DecodeFrom64(strUsername64);
//        strUsername = strUsername.replace("%20", " ");
//        if (strUsername.length() > 0)
//            ((TextView) ViewHolder.get(convertView, R.id.textName)).setText(strUsername);
//        else
//            ((TextView) ViewHolder.get(convertView, R.id.textName)).setText("");

        String body64 = item.optString("message", "");
//        if(isJSONValid(body64)) {
//            try {
//                jsonMessage = new JSONObject(body64);
//                strMsg = jsonMessage.optString("message");
//                play = jsonMessage.optString("play");
//                url = jsonMessage.optString("url");
//                ( ViewHolder.get(convertView, R.id.img_play)).setVisibility(View.VISIBLE);
//                ( ViewHolder.get(convertView, R.id.img_stop)).setVisibility(View.INVISIBLE);
//            } catch (JSONException e) {
//            }
//        }else {
            String body =body64;// DecodeFrom64(body64);
            strMsg = body;
//            ( ViewHolder.get(convertView, R.id.img_play)).setVisibility(View.INVISIBLE);
//            ( ViewHolder.get(convertView, R.id.img_stop)).setVisibility(View.INVISIBLE);
//        }


        ((TextView) ViewHolder.get(convertView, R.id.textName)).setText(strMsg);

        return convertView;
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
//            try {
//                new JSONArray(test);
//            } catch (JSONException ex1) {
            return false;
//            }
        }
        return true;
    }

    protected String EncodeTo64(String str){
        try{
            byte[] source = str.getBytes("UTF-8");
            String base64_encoded = Base64.encodeToString(source, Base64.DEFAULT);
            return base64_encoded;
        }catch (UnsupportedEncodingException e){
            return null;
        }
    }

    protected String __DecodeFrom64(String base64){
        String text = "";
//        if(!isBase64(base64)){return  base64;}
        try{
            byte[] data = Base64.decode(base64, Base64.DEFAULT);
            text = new String(data, "UTF-8");
        }catch (Exception e) {
            text =  base64;
        }
        return text;
    }
    private boolean isBase64(String stringBase64){
        String regex =
                "([A-Za-z0-9+/]{4})*"+
                        "([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)";

        Pattern patron = Pattern.compile(regex);

        if (!patron.matcher(stringBase64).matches()) {
            return false;
        } else {
            return true;
        }
    }
}