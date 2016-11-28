package com.dating.reveal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dating.reveal.Item_models.Notification;
import com.dating.reveal.Item_models.PhotoInformation;
import com.dating.reveal.ListAdapter.NewFeedListAdapter;
import com.dating.reveal.commom.OnSwipeTouchListener;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.utility.DataUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JonIC on 2016-11-15.
 */
public class NewFeedActivity_candidate extends HeaderActivity{
    ArrayList<Notification> m_listContent;
    NewFeedListAdapter m_NewFeedAdapter;
    JSONArray jsonNewFeed;
    String strNewFeed= "";
    ListView m_NewFeedList;
    RelativeLayout relativeLayout;
    ArrayList<PhotoInformation> photoArray = new ArrayList<>();
    ProgressDialog progressDialog;
    int sznewfeed = 0;
    int count = 0;
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
    protected void onCreate(Bundle savedInstanvestate){

        super.onCreate(savedInstanvestate);
        setContentView(R.layout.activity_newfeed_candidate);
        setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(R.color.red));

        LoadParendActivity();
        getNotificationsize();

        initNewFeedData();
//        initList();
        swip();
    }
    protected void LoadParendActivity(){
        super.LoadParendActivity();
    }

    protected void layoutControls(){
        super.layoutControls();
    }
    protected void initEvent(){
        super.initEvent();
    }
    protected void initView(){
        super.initView();

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("");
        progressDialog.setMessage("Please wait...");

    }


    protected void swip(){
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(NewFeedActivity_candidate.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                gotoRatingPage();
//                Bundle bundle = new Bundle();
//                bundle.putString("receivephoto", "1");
//
//                Intent intent = new Intent(NewFeedActivity_candidate.this, RatingActivity.class);
//                intent.putExtras(bundle);
//
//                startActivity(intent);
//                // initialize the new feed history.
//                DataUtils.savePreference(Const.NOTIFICATION_CONTENT,"");
//                finish();
            }
            public void onSwipeLeft() {

                Intent intent = new Intent(NewFeedActivity_candidate.this, MatchsMessageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);//forward
                finish();
            }
            public void onSwipeBottom() {
            }

        });
    }
    public void gotoRatingPage(){
        String facebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
        String email = DataUtils.getPreference(Const.EMAIL,"");
        String name = DataUtils.getPreference(Const.NAME,"");
        String gender = DataUtils.getPreference(Const.GENDER,"");
        int age = 18;
        String Age = String.valueOf(age);
        float locationx = 0;
        String locationxx = String.valueOf(locationx);
        float locationy = 0;
        String locationyy = String.valueOf(locationy);
//        final ProgressDialog progressDialog ;
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setCancelable(false);
//        progressDialog.setTitle("");
//        progressDialog.setMessage("Please wait...");
////        progressDialog.show();


        ServerManager.login(facebookid, email, gender, name, Age, locationxx, locationyy, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
//                progressDialog.dismiss();

                if( result.mResult == LogicResult.RESULT_OK){
                    JSONObject userInfo = result.getData();

                    // if receive the data then go to the next screen.
                    // before goto there save the setting
                    JSONArray retcontent  = userInfo.optJSONArray("content");
                    String receivephoto = retcontent.toString().trim();
//                   String receivephoto = userInfo.optString("content");
//                   receivephoto = receivephoto.substring(1,receivephoto.length()-1);
                    // the photos that nonfriend .
                    DataUtils.savePreference(Const.RATEPHOTO, receivephoto);
                    Bundle bundle = new Bundle();

                    bundle.putString("receivephoto", receivephoto);
                    Intent intent = new Intent(NewFeedActivity_candidate.this,RatingActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
                    finish();
                }
            }
        });
    }

    protected void initList(){
        ArrayList<PhotoInformation> empty = new ArrayList<>();
        if(photoArray.equals(empty)){
            return;
        }
        m_NewFeedAdapter = new NewFeedListAdapter(NewFeedActivity_candidate.this, photoArray);
        setListViewHeightBasedOnChildren(m_NewFeedList);

        m_NewFeedList.setAdapter(m_NewFeedAdapter);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    protected void findView(){
        super.findView();
        m_NewFeedList = (ListView) findViewById(R.id.list_new_feed);
        relativeLayout = (RelativeLayout) findViewById(R.id.lay_container);
    }
    protected void initData(){

    }
//    protected  void ExtractDataFromJSON(JSONObject json){
//        m_listContent= new ArrayList<Notification>();
//
//        Notification item1 = new Notification();
//        item1.sender = json.optString("sender");
//        item1.feedval =  json.optString("feedval");
//////        item.ratesum  = Integer.parseInt(json.optString("ratesum"));
//////        item.ratenum  = Integer.parseInt(json.optString("ratenum"));
////        item1.comment =  json.optString("comment");
////        item1.commentSize =  json.optString("commentSize");
////        item1.likesize =  json.optString("likesize");
////        item1.likes =  json.optString("likes");
////        item1.aboutPhoto =  json.optString("aboutPhoto");
//        m_listContent.add(item1);
//
//    }

    protected void eXtractDataFromJArray(JSONArray jsonArray){
//        m_listContent= new ArrayList<Notification>();
        sznewfeed = jsonArray.length();
//        len = len -1;
//        String fblength = String.valueOf(len);
//        DataUtils.savePreference("maxcount",fblength);

        String aaa = DataUtils.getPreference("maxcount","0");
        progressDialog.show();
        for(int i = 0 ; i < jsonArray.length(); i++){
//            progressDialog.show();

            JSONObject json = jsonArray.optJSONObject(i);
            String sender = json.optString("sender");
            String feedval =  json.optString("feedval");
            getNewFeedInformation_New(i, sender, feedval);
//            getNewFeedInformation(i, sender, feedval);
//            m_listContent.add(item);

        }
    }


    protected void getNewFeedInformation_New(final int x,final String facebookid, final String photopath){
        ServerManager.singlePhoto(facebookid, photopath, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {

                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject userinfo = result.getData();
                    JSONArray photoinfo = userinfo.optJSONArray("content");
                    JSONArray emptyjsonarray = new JSONArray();
                    if(photoinfo.equals(emptyjsonarray)){
                        Toast.makeText(NewFeedActivity_candidate.this, "Sorry, there is not such photo", Toast.LENGTH_SHORT).show();
                    }else{
                        // 1. get data items from JSONArray.
                        // here i need to get users profile photo. so without any other solution ( that is very complicate- change the notification table
                        // by adding sender's profile photo path.) i solve this problem to call the user's profile photo request to server.
                        JSONObject json = photoinfo.optJSONObject(0);
                        String name = json.optString("name","");            // user name
                        String aboutphoto = json.optString("mycomment",""); // about photo
                        String mainphoto  = json.optString("photopath",""); // new feed photo path
                        String rate = json.optString("rate", "");           // rate
                        String likenum = json.optString("likenum","");      // likenum
                        String commentnum = json.optString("commentnum","");// commentnum
                        String commentcon = json.optString("commentcon","");// commentcon
                        String likecon = json.optString("likefacebookid","");// likefacebookid
                        // save all photo information
                        SavePhotoInformaion(name, aboutphoto, mainphoto, rate, likenum, commentnum,likecon,commentcon,"", facebookid);
//                        String str = String.valueOf(x);
//                        String length = DataUtils.getPreference("maxcount","0");
                    }// else

                }// if result.mResult

                count = count + 1;
                if(sznewfeed == count){
                    progressDialog.dismiss();
                    // photo path, imageview
                    initList();
                } // of str.equals(length)

                progressDialog.dismiss();
            }
        }); // single photo

    }
    // get profile photo and photoinformation .

    protected void getNewFeedInformation(final int x,final String facebookid, final String photopath){
        ServerManager.getPersonInfo(facebookid, new ResultCallBack() {

            @Override
            public void doAction(LogicResult result) {
                if(result.mResult == LogicResult.RESULT_OK){
                    JSONObject jsonObject = result.getData();
                    JSONArray jsonArray = jsonObject.optJSONArray("content");
                    JSONArray empty = new JSONArray();

                    if(jsonArray.equals(empty)){
                        Toast.makeText(NewFeedActivity_candidate.this, "Sorry, there is no user's profile photo.", Toast.LENGTH_SHORT).show();
                        return;
                    }// if jsonarray is null.

                    JSONObject jsonObject1 = jsonArray.optJSONObject(0);
                    final String profilephoto = jsonObject1.optString("profilephoto","");

                    ////////////////////////////////////////////////////////////////////////////////
                    ServerManager.singlePhoto(facebookid, photopath, new ResultCallBack() {
                        @Override
                        public void doAction(LogicResult result) {

                            if(result.mResult == LogicResult.RESULT_OK){
                                JSONObject userinfo = result.getData();
                                JSONArray photoinfo = userinfo.optJSONArray("content");
                                JSONArray emptyjsonarray = new JSONArray();
                                if(photoinfo.equals(emptyjsonarray)){
                                    Toast.makeText(NewFeedActivity_candidate.this, "Sorry, there is not such photo", Toast.LENGTH_SHORT).show();
                                }else{
                                    // 1. get data items from JSONArray.
                                    // here i need to get users profile photo. so without any other solution ( that is very complicate- change the notification table
                                    // by adding sender's profile photo path.) i solve this problem to call the user's profile photo request to server.
                                    JSONObject json = photoinfo.optJSONObject(0);
                                    String name = json.optString("name","");            // user name
                                    String aboutphoto = json.optString("mycomment",""); // about photo
                                    String mainphoto  = json.optString("photopath",""); // new feed photo path
                                    String rate = json.optString("rate", "");           // rate
                                    String likenum = json.optString("likenum","");      // likenum
                                    String commentnum = json.optString("commentnum","");// commentnum
                                    String commentcon = json.optString("commentcon","");// commentcon
                                    String likecon = json.optString("likefacebookid","");// likefacebookid
                                    // save all photo information
                                    SavePhotoInformaion(name, aboutphoto, mainphoto, rate, likenum, commentnum,likecon,commentcon,profilephoto, facebookid);
                                    String str = String.valueOf(x);
                                    String length = DataUtils.getPreference("maxcount","");
                                    if(str.equals(length)){
                                        // photo path, imageview
                                        initList();
                                    } // of str.equals(length)
                                }// else
                            }// if result.mResult
                            progressDialog.dismiss();
                        }
                    }); // single photo


                    ////////////////////////////////////////////////////////////////////////////////



                }else{ // if
                    Toast.makeText(NewFeedActivity_candidate.this, "Sorry, could not find the user profile photo.", Toast.LENGTH_SHORT).show();
                }// else1
            }
        });


    }

    protected void SavePhotoInformaion(String name,String  aboutphoto,String  mainphoto,String  rate,
                                       String likenum,String  commentnum,String likecon,String commentcon, String profilephoto, String facebookid){
        PhotoInformation item = new PhotoInformation();

        item.facebookid = facebookid;
        item.name = name;
        item.aboutphoto = aboutphoto;
        item.mainphoto = mainphoto;
        item.rate = rate;
        item.likenum = likenum;
        item.likecon = likecon;
        item.commentcon = commentcon;
        item.commentnum = commentnum;
        item.profilephoto = profilephoto;
        item.isLiked = false;
        item.nowComment="";
        photoArray.add(item);
    }


    // get newfeed jsonarray from notification string.
    protected void initNewFeedData(){
        m_listContent = new ArrayList<>();
        JSONArray refreshNotification = new JSONArray();
        strNewFeed = DataUtils.getPreference(Const.NOTIFICATION_CONTENT, "");
        // if the string is null.
        if(strNewFeed.equals("[]") || strNewFeed.equals("")){
            Toast.makeText(NewFeedActivity_candidate.this, "There is no New Feed.", Toast.LENGTH_SHORT).show();
            return;
        }
        jsonNewFeed= new JSONArray();

        JSONArray jsonArrayNewFeed = parseJson(strNewFeed);
        // see if the parsing result is empty or not.
        if(jsonArrayNewFeed.equals(jsonNewFeed)){
            Toast.makeText(NewFeedActivity_candidate.this, "There is no New Feed.", Toast.LENGTH_SHORT).show();
            return;
        }
        for(int x = 0; x < jsonArrayNewFeed.length(); x++){
            JSONObject jsonO = jsonArrayNewFeed.optJSONObject(x);
            String notetype = jsonO.optString("notekind");
            if(notetype.equals("newfeed")){
                jsonNewFeed.put(jsonO);
//            }else{
//                refreshNotification.put(jsonO);
            }
        }
        if(jsonNewFeed.length() == 0){
            return;
        }
        eXtractDataFromJArray(jsonNewFeed);

        // refresh notification JSONArray. remove the new feed notifications.
        String strRefresh = refreshNotification.toString().trim();
//        DataUtils.savePreference(Const.NOTIFICATION_CONTENT, strRefresh);
        // refresh notification number too.
//        String notenum = DataUtils.getPreference(Const.NOTIFICATION_SIZE,"");
//        int toInt_notenum = Integer.parseInt(notenum);
//        int sizeNewfeed = jsonNewFeed.length();
//        int refreshnum = toInt_notenum - sizeNewfeed;
//        String strRefreshnum = String.valueOf(refreshnum);
//        DataUtils.savePreference(Const.NOTIFICATION_SIZE,strRefreshnum);
    }

    protected JSONArray parseJson(String receivephoto){
        try {
            JSONArray jsonarray = new JSONArray(receivephoto);
            return jsonarray;
        }catch(Throwable t){
            JSONArray emptyJsonArray = new JSONArray();
            return emptyJsonArray;
        }
    }

}

