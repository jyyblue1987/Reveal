package com.dating.reveal.ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dating.reveal.FriendProfileActivity;
import com.dating.reveal.Item_models.NewFeedItem;
import com.dating.reveal.Item_models.PhotoInformation;
import com.dating.reveal.LikeShowActivity;
import com.dating.reveal.R;
import com.dating.reveal.main.Const;
import com.dating.reveal.net.util.LogicResult;
import com.dating.reveal.net.util.ResultCallBack;
import com.dating.reveal.network.ServerManager;
import com.dating.reveal.network.ServerTask;
import com.dating.reveal.utility.DataUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JonIC on 2016-11-15.
 */
public class NewFeedListAdapter extends ArrayAdapter<PhotoInformation> {
    private final Context context;
    // input new feed list
//    private final ArrayList<Notification> itemsArrayList;
    // photo information list
    private ArrayList<PhotoInformation>  photoArray = new ArrayList<>();
    // comment list content
    CommentListAdapter mCommentListAdapter;
    ArrayList<Item> commentDataList;

    public NewFeedListAdapter(Context context, ArrayList<PhotoInformation> itemsArrayList) {

        super(context, R.layout.newfeed_list_item, itemsArrayList);

        this.context = context;
        this.photoArray = itemsArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.newfeed_list_item, parent, false);
        final NewFeedItem viewholder = new NewFeedItem();
        PhotoInformation item = photoArray.get(position);

        // 3. Get the views from the rowView
        viewholder.profileImage     =(ImageView) rowView.findViewById( R.id.img_newfeed_profile);
        viewholder.name             = (TextView) rowView.findViewById(R.id.txt_newfeed_name);
        viewholder.aboutphoto       = (TextView) rowView.findViewById(R.id.txt_newfeed_about_photo);
        viewholder.mainImage        = (ImageView) rowView.findViewById(R.id.img_newfeed_photo) ;
        viewholder.photorate        = (TextView) rowView.findViewById(R.id.txt_newfeed_photo);
        viewholder.like             = (ImageView)rowView.findViewById(R.id.img_newfeed_like);
        viewholder.likefill         = (ImageView) rowView. findViewById(R. id. img_newfeed_like_fill);
        viewholder.likenum         = (TextView) rowView.findViewById(R. id. txt_newfeed_like);
        viewholder.comment          = (ImageView) rowView.findViewById(R. id. img_newfeed_comment);
        viewholder.commentnum       = (TextView) rowView.findViewById(R.id.txt_newfeed_comment);
        viewholder.sendcomment      = (TextView) rowView.findViewById(R.id.txt_edit_comment); // send textview
        viewholder.editcomment      = (EditText) rowView. findViewById(R.id. edit_your_comment); // edit comment view
        viewholder.commentlist      = (ListView) rowView. findViewById(R.id. commentlist);  // comment list view

        viewholder.sendcomment.setVisibility(View.VISIBLE);
        viewholder.editcomment.setVisibility(View.VISIBLE);
        viewholder.commentlist.setVisibility(View.INVISIBLE);
        // 4. read list content at position.

        // 5. get photo information from server. display the data in screen.

//        String profilephoto = photoArray.get(position).profilephoto;
        String name = photoArray.get(position).name;
        String aboutphoto = photoArray.get(position).aboutphoto;
        String rate = photoArray.get(position).rate;
        String likenum = photoArray.get(position).likenum;
        String commentnum = photoArray.get(position).commentnum;
        String facebookid = photoArray.get(position).facebookid;
        final String photopath = photoArray.get(position).mainphoto;
        DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        String profilephoto = "https://graph.facebook.com/" + facebookid + "/picture?type=large";

        ImageLoader.getInstance().displayImage( profilephoto, viewholder.profileImage, optoins);
//        ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + profilephoto, viewholder.profileImage, optoins);

       // display the photo information to screen.
        viewholder.name.setText(name);
        viewholder.aboutphoto.setText(aboutphoto);
        viewholder.photorate.setText(rate);
        viewholder.likenum.setText(likenum);
        viewholder.commentnum.setText(commentnum);

        ImageLoader.getInstance().displayImage(ServerTask.DOWNLOAD_URL + photopath, viewholder.mainImage, optoins);

        viewholder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewholder.likefill.setVisibility(View.VISIBLE);
//                viewholder.like.setVisibility(View.INVISIBLE);

                String comment = "";
                String like = "like";
                String sendname = DataUtils.getPreference(Const.NAME,"");
                String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                String facebookid = photoArray.get(position).facebookid;
                String photopath  = photoArray.get(position).mainphoto;
                sendCommentLike(comment, facebookid, photopath,myfacebookid, like, sendname,viewholder,position);

//                // upgrade like number
//                String strszlike = viewholder.likenum.getText().toString().trim();
//                int szlike = Integer.parseInt(strszlike);
//                szlike++;
//                String newszLike = String.valueOf(szlike);
//                if(!photoArray.get(position).isLiked){
//                    viewholder.likenum.setText(newszLike);
//                }
//                photoArray.get(position).isLiked = true;

            }
        });

        viewholder.like.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    viewholder.like.setImageResource(R.drawable.icon_like_fill);

                if (event.getAction() == MotionEvent.ACTION_UP)
                    viewholder.like.setImageResource(R.drawable.icon_like);

                return false;
            }

        });
        viewholder.likefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewholder.likefill.setVisibility(View.INVISIBLE);
                viewholder.like.setVisibility(View.VISIBLE);
            }
        });

        viewholder.sendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // upload the comment about the photo
                String facebookid = photoArray.get(position).facebookid;
                String photopath = photoArray.get(position).mainphoto;
                String sendname = photoArray.get(position).name;
                String comment = (viewholder.editcomment.getText()).toString().trim();
                if(comment.equals("")){return;}
                String like = "";
                comment = comment + "&" + facebookid;
                String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                sendCommentLike(comment, facebookid, photopath,myfacebookid, like, sendname, viewholder, position);
                viewholder.editcomment.setText("");
                // change the comment size displayed in the screen
                photoArray.get(position).nowComment =  comment;
                String strszComment = viewholder.commentnum.getText().toString().trim();
                int szComment = Integer.parseInt(strszComment);
                szComment++;
                String newComment = String.valueOf(szComment);
                viewholder.commentnum.setText(newComment);

            }
        });

        viewholder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // init the comment data list
                // disable comment listview.  // it is for test
                viewholder.commentlist.setVisibility(View.GONE);
                if(photoArray.get(position).commentcon.equals("")){
                    return;
                }
                commentDataList = new ArrayList<Item>();

                if(!photoArray.get(position).nowComment.equals("")){
                    String myname = DataUtils.getPreference(Const.NAME,"");
                    String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                    photoArray.get(position).commentcon = photoArray.get(position).commentcon + "^"+ myname +"&" +
                            photoArray.get(position).nowComment +"&"+myfacebookid;
                    photoArray.get(position).nowComment = "";
                }

                String[] commentnodes  = photoArray.get(position).commentcon.split("\\^");
                for(int x = 0; x < commentnodes.length; x++){
                    String commentNode = commentnodes[x];
                    String[] commentContent  = commentNode.split("\\&");
                    if(commentContent.length < 3){
                        continue;
                    }
                    Item item = new Item(commentContent[0], commentContent[1], commentContent[2],"");
                    commentDataList.add(item);
                }
                //here we should show list.
                mCommentListAdapter = new CommentListAdapter(getContext(), commentDataList);
//                viewholder.commentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Item item= commentDataList.get(position);
//                            gotoFriendProfilePage(item);
//                    }
//                });
                viewholder.commentlist.setAdapter(mCommentListAdapter);

            }
        });

        viewholder.comment.setOnTouchListener(new View.OnTouchListener() {
              public boolean onTouch(View v, MotionEvent event) {
                  if (event.getAction() == MotionEvent.ACTION_DOWN)
                      viewholder.comment.setImageResource(R.drawable.comment_b);

                  if (event.getAction() == MotionEvent.ACTION_UP)
                      viewholder.comment.setImageResource(R.drawable.comment);

                  return false;
              }

          });       // goto see like page.

        viewholder.likenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display likes that is gotoLike page.
                // here you should get single photo's data.
                String liked = photoArray.get(position).likecon;
                if(photoArray.get(position).isLiked){
                    String myfacebookid = DataUtils.getPreference(Const.FACEBOOKID,"");
                    String myName = DataUtils.getPreference(Const.NAME,"");
                    liked = liked + "^" + myfacebookid + "&" + myName;
                }

                Bundle bundle = new Bundle();
                bundle.putString("likes", liked); /// eroorororororororo

                Intent intent = new Intent(getContext(), LikeShowActivity.class);
                intent.putExtras(bundle);
//                getContext().startActivity(intent);


                Activity activity = (Activity) context;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right); //backward
            }
        });
        // 5. retrn rowView
        return rowView;
    }
    // init comment list

    // photo informations save specially for future using.
    protected void SavePhotoInformaion(String name,String  aboutphoto,String  mainphoto,String  rate,
                                       String likenum,String  commentnum,String likecon,String commentcon){
        PhotoInformation item = new PhotoInformation();

        item.name = name;
        item.aboutphoto = aboutphoto;
        item.mainphoto = mainphoto;
        item.rate = rate;
        item.likenum = likenum;
        item.likecon = likecon;
        item.commentcon = commentcon;
        item.commentnum = commentnum;

        photoArray.add(item);
    }
    protected void displayResult(JSONArray photoinfo, NewFeedItem viewholder){
        // 1. get data items from JSONArray.
        JSONObject json = photoinfo.optJSONObject(0);
        String name = json.optString("name","");
        String aboutphoto = json.optString("mycomment","");
        String mainphoto  = json.optString("photopath","");
        String rate = json.optString("rate", "");
        String likenum = json.optString("likenum","");
        String commentnum = json.optString("commentnum","");
        String commentcon = json.optString("commentcon","");
        String likecon = json.optString("likefacebookid","");
    }

    protected void sendCommentLike(String comment, String facebookid, String photopath,String myfacebookid, String like, String sendname,
                                   final NewFeedItem viewholder,final int position ){
        // in this case the like is not selected.
        ServerManager.sendCommentLike(comment, facebookid, photopath, myfacebookid, like, sendname, new ResultCallBack() {
            @Override
            public void doAction(LogicResult result) {
                if(result.mResult == LogicResult.RESULT_OK){
//                    String info = ("Success");
//                    Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
                    // upgrade like number
                    String strszlike = viewholder.likenum.getText().toString().trim();
                    int szlike = Integer.parseInt(strszlike);
                    szlike++;
                    String newszLike = String.valueOf(szlike);
                    if(!photoArray.get(position).isLiked){
                        viewholder.likenum.setText(newszLike);
                    }
                    photoArray.get(position).isLiked = true;

                }else{
                    JSONObject userInfo = result.getData();
                    String info = userInfo.optString("error_msg");
                    Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
                }
            }
        });
    };
// goto friend profile page.
    protected void gotoFriendProfilePage(Item item){
        String friendfacebookid = item.getFacebookid();
        Bundle bundle = new Bundle();
        bundle.putString("person", friendfacebookid);

        Intent intent = new Intent(getContext(), FriendProfileActivity.class);
        intent.putExtras(bundle);
        getContext().startActivity(intent);

    }


}
