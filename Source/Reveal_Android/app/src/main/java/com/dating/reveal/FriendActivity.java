package com.dating.reveal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.reveal.ListAdapter.ItemFriendActivity;
import com.dating.reveal.commom.ActivityManager;
import com.dating.reveal.commom.OnSwipeTouchListener;
import com.dating.reveal.dialog.frienddialog;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.DataUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JonIC on 2016-11-08.
 */
public class FriendActivity extends HeaderActivity implements DialogInterface.OnDismissListener {
    ListView mListView = null;
    FriendListAdapter mFriendlistAdapter;
    ArrayList<ItemFriendActivity> mListContent = new ArrayList<>();
    Button mBtnAdd;
    RelativeLayout drawer;
    ProgressDialog progressDialog ;

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
    public void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_friend);
        getNotificationsize();
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));
        // we should give data to mListContent// friends list
        LoadParendActivity();
        getFriend_new();
//        getFriend();
//        new ProgressTask().execute();
        initEvent();
        swip();
    }
//    public void setStatusBarColor(View statusBar,int color){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //status bar height
//            int actionBarHeight = getActionBarHeight();
//            int statusBarHeight = getStatusBarHeight();
//            //action bar height
//            statusBar.getLayoutParams().height = statusBarHeight;
//            statusBar.setBackgroundColor(color);
//        }
//    }
//    public int getActionBarHeight() {
//        int actionBarHeight = 0;
//        TypedValue tv = new TypedValue();
//        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
//        {
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
//        }
//        return actionBarHeight;
//    }
//
//    public int getStatusBarHeight() {
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
//    private class ProgressTask extends AsyncTask<Void,Void,Void> {
//        @Override
//        protected void onPreExecute(){
//            bar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            //my stuff is here
////            getFriend();
//            return null;
//        };
//
//        @Override
//        protected void onPostExecute(Void result) {
//            bar.setVisibility(View.GONE);
//        }
//    }

    protected void LoadParendActivity(){
        super.LoadParendActivity();
    }

    protected void initView(){
        super.initView();
    }

    protected void layoutControls(){
        super.layoutControls();
    }

    protected void getFriend_new(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        progressDialog.show();

        final String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        ServerManager.getFriend(facebookid, new ResultCallBack(){
            @Override
            public void doAction(LogicResult result) {
                progressDialog.dismiss();
                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userinfo = result.getData();
                    JSONArray friends = userinfo.optJSONArray("content");
                    makelistContent(friends);
                }
            }
        });

    }
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    // 2016-11-27
    protected void makelistContent(JSONArray matche){
        for(int ma = 0; ma < matche.length(); ma++){
            JSONObject person = matche.optJSONObject(ma);
            String facebookid1 = person.optString("facebookid1");
            String facebookid2 = person.optString("facebookid2");
            String name1 = person.optString("name1");
            String name2 = person.optString("name2");
            String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
            String facebookid;
            String friendname;
            if(facebookid1.equals(myfacebookid)){
                facebookid = facebookid2;
                friendname  = name2;
            }else{
                facebookid = facebookid1;
                friendname  = name1;
            }

            ItemFriendActivity item = new ItemFriendActivity("", "",facebookid, friendname, "");
            mListContent.add(item);

        }// matches loop
        mFriendlistAdapter = new FriendListAdapter(FriendActivity.this, mListContent);
        mListView.setAdapter(mFriendlistAdapter);
    }
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
//    protected void  getFriend(){
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading");
//        progressDialog.setIndeterminate(true);
//        progressDialog.setCancelable(false);
//        progressDialog.setTitle("");
//        progressDialog.setMessage("Please wait...");
//
//        progressDialog.show();
//
//        String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
//        ServerManager.getFriend(facebookid, new ResultCallBack(){
//           @Override
//           public void doAction(LogicResult result) {
//
//               if(result.mResult == LogicResult.RESULT_OK){
//                   JSONObject userInfo = result.getData();
//                   JSONArray friendlist = userInfo.optJSONArray("content");
////                   String rawmatchdata = friendlist.toString().trim();
//                   String rawmatchdata = userInfo.optString("content"); // user list
//                   if(rawmatchdata == null || rawmatchdata.equals("")){
//                       Toast.makeText(FriendActivity.this, "There is no friend. Please invite.",Toast.LENGTH_SHORT).show();
//                       return;
//                   }
//                   String[] strings = rawmatchdata.split("\\^");
//                    if(strings.length == 0){
//                        progressDialog.dismiss();
//                        return;
//                    }
//                   String fblength = String.valueOf(strings.length-1);
//                   DataUtils.savePreference("maxcount",fblength);
//
//                   mListContent = new ArrayList<>();
//                   for(int x = 0; x<strings.length; x++){
//                       if(!strings[x].equals(""))
//
//                           getProfilephoto(x, strings[x]);
//
////                       mListContent.add(strings[x]);
//                   }
//                   // here we got the friend list.
////                   mCommentListAdapter = new CommentListAdapter(FriendProfileActivity.this, R.layout.commet_list_item, commentDataList);
////                   mFriendlistAdapter = new FriendListAdapter(FriendActivity.this, mListContent);
////                   mListView.setAdapter(mFriendlistAdapter);
//               }else{
//                   progressDialog.dismiss();
//               }
//           }
//        });
//    }
//
//    protected void getProfilephoto(final int x, final String facebookid){
//        ServerManager.getPersonInfo(facebookid, new ResultCallBack() {
//            @Override
//            public void doAction(LogicResult result) {
//
//                if (result.mResult == LogicResult.RESULT_OK) {
//                    String str = String.valueOf(x);
//
//                    JSONObject jsonResult = result.getData();
//                    JSONArray userinfo = jsonResult.optJSONArray("content");
//
//                    JSONArray empty = new JSONArray();
//                    if (userinfo.equals(empty)) {
//                        Toast.makeText(FriendActivity.this, "Sorry, could not find person information.", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    JSONObject jsonObject = userinfo.optJSONObject(0);
//                    String username = jsonObject.optString("name", "");
//                    String profilephoto = jsonObject.optString("profilephoto", "");
//                    ItemFriendActivity item = new ItemFriendActivity("", "",facebookid, username, profilephoto);
//                    mListContent.add(item);
//
//                    String fbmax = DataUtils.getPreference("maxcount","");
//                    if(str.equals(fbmax)){
//                        // discuss progress bar.
//                        progressDialog.dismiss();
//                        mFriendlistAdapter = new FriendListAdapter(FriendActivity.this, mListContent);
//                        mListView.setAdapter(mFriendlistAdapter);
//                    }
//                    // here we got the friend list.
//                }
//            }
//
//        });
//    }
//



    protected void swip(){
        drawer.setOnTouchListener(new OnSwipeTouchListener(FriendActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                Intent intent = new Intent(FriendActivity.this,MatchsMessageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
                finish();
            }
            public void onSwipeLeft() {
//                Intent intentright = new Intent(FriendActivity.this,NewFeedActivity.class);
//                startActivity(intentright);
            }
            public void onSwipeBottom() {
            }

        });

    }

    protected void findView(){
        super.findView();
        mListView = (ListView)findViewById(R.id.list);
        mBtnAdd   = (Button)findViewById(R.id.btn_friend_add);
        drawer = (RelativeLayout)findViewById(R.id.lay_container);
    }

    protected void initEvent(){
        super.initEvent();
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddFriendsPage();
            }
        });
    }

    protected void gotoAddFriendsPage(){
        Bundle bundle = new Bundle();
        ActivityManager.changeActivity(FriendActivity.this, AddFriendActivity.class, bundle, false, null);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        finish();
    }

    class ViewHolder {
        TextView textFriendName ;
        de.hdodenhof.circleimageview.CircleImageView imgFriendPhoto;
        ImageView imgDialog ;
    }

        private class FriendListAdapter extends ArrayAdapter<ItemFriendActivity> {

        private final Context context;
        private final ArrayList<ItemFriendActivity> itemsArrayList;


        public FriendListAdapter(Context context, ArrayList<ItemFriendActivity> itemsArrayList) {
            super(context, R.layout.friend_listview_row, R.id.txt_friend_name, itemsArrayList);
            this.context = context;
            this.itemsArrayList = itemsArrayList;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;
            int itemposition = position;

            // Check if a view can be reused, otherwise inflate a layout and set up the view holder
            if (row == null) {
                // Inflate view from layout file
//                row = getLayoutInflater().inflate(R.layout.friend_listview_row,null);
                row = getLayoutInflater().inflate(R.layout.friend_listview_row, null);
            }
                // Set up holder and assign it to the View
                holder = new ViewHolder();
                holder.textFriendName = (TextView) row.findViewById(R.id.txt_friend_name);
                holder.imgFriendPhoto = (de.hdodenhof.circleimageview.CircleImageView) row.findViewById(R.id.img_profile_photo);
                holder.imgDialog = (ImageView) row.findViewById(R.id.img_dialog);
                // Set holder as tag for row for more efficient access.
                row.setTag(holder);

                final String  facebookid = itemsArrayList.get(position).getFacebookid();
//            String photopath = itemsArrayList.get(position).getProfilephoto();
            String username =  itemsArrayList.get(position).getName();
            holder.textFriendName.setText(username);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .build();
            String photopath = "https://graph.facebook.com/" + facebookid + "/picture?type=large";
            ImageLoader.getInstance().displayImage( photopath,  holder.imgFriendPhoto, options);
//            ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + photopath,  holder.imgFriendPhoto, options);

                // set onclicklistener
                holder.textFriendName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        // what about go to chat page
//                        String herfacebookid = mListContent.get(position).getFacebookid();
//                        gotochatpage(herfacebookid);
//                        gotoProfile(herfacebookid);
                    }
                });

                holder.imgFriendPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //goto profile
                        String herfacebookid = mListContent.get(position).getFacebookid();
                        gotoProfile(herfacebookid);
                    }
                });

                holder.imgDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(FriendActivity.this, "try to show dialgo", Toast.LENGTH_SHORT).show();

                        String __facebookid = itemsArrayList.get(position).getFacebookid();
                        frienddialog dialog = new frienddialog( FriendActivity.this , __facebookid) ;
                        dialog.setOnDismissListener( FriendActivity.this ) ;
                        // make the background of the dialog to become transparent.
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        // align the dialog botton of the screen
                        Window window = dialog.getWindow();
                        WindowManager.LayoutParams wlp = window.getAttributes();

                        wlp.gravity = Gravity.BOTTOM;
                        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                        window.setAttributes(wlp);
                        dialog.show() ;

                    }
                });
            return row;
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onDismiss(DialogInterface $dialog) {
        // TODO Auto-generated method stub
        frienddialog dialog = (frienddialog) $dialog ;
        String type = dialog.getName() ;
        String name = dialog.get__facebookid();
//        Toast.makeText(FriendActivity.this, "facebookid="+ name, Toast.LENGTH_SHORT).show();

        if(type.equals("delete")){// if you select delete then
            String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
            ServerManager.deletefriend(name, myfacebookid, new ResultCallBack() {
                @Override
                public void doAction(LogicResult result) {
                    if(result.mResult == LogicResult.RESULT_OK){
                        JSONObject ret = result.getData();
                        String str_ret = ret.optString("content");
                        if(str_ret.equals("ok")){// if deleting success
                            Toast.makeText(FriendActivity.this, "OK", Toast.LENGTH_SHORT).show();
                        }else if(str_ret.equals("fail")){ // if the delete is fail
                            Toast.makeText(FriendActivity.this, "NO", Toast.LENGTH_SHORT).show();
                        }// else if
                    } // result if
                }// doAction
            });// ServerManager.deletefriend
        } // if type.equal("delete")
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    protected void  gotoProfile(String herfacebookid){
        ServerManager.getPersonInfo(herfacebookid, new ResultCallBack(){
            @Override
            public void doAction(LogicResult result){
                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userInfo = result.getData();
                    JSONArray personProfile = userInfo.optJSONArray("content");
//                    String person = personProfile.toString().trim();
                    String person = userInfo.optString("content");
                    JSONObject emptyJson = new JSONObject();
                    if(personProfile.equals(emptyJson)){
                        Toast.makeText(FriendActivity.this, "Sorry could not find that friend", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject jsonObject = personProfile.optJSONObject(0);
                    String aaa = jsonObject.optString("facebookid","");
                    aaa = aaa+ "^friend";
                    Bundle bundle = new Bundle();
                    bundle.putString("person", aaa);
                    Intent intent = new Intent(FriendActivity.this, FriendProfileActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    protected void gotochatpage(String herfacebookid){
        ServerManager.getPersonInfo(herfacebookid, new ResultCallBack(){
            @Override
            public void doAction(LogicResult result){
                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userInfo = result.getData();
                    JSONArray personProfile = userInfo.optJSONArray("content");
//                    String person = personProfile.toString().trim();
                    String person = userInfo.optString("content");
                    JSONArray emptyJson = new JSONArray();
                    if(personProfile.equals(emptyJson)){
                        Toast.makeText(FriendActivity.this, "Sorry could not find that friend", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String aaa = person.substring(1, person.length()-1);
                    Bundle bundle = new Bundle();
                    bundle.putString("person", aaa);
                    Intent intent = new Intent(FriendActivity.this, ChatActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
