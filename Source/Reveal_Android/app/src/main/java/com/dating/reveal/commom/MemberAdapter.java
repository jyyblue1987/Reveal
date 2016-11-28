package com.dating.reveal.commom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dating.reveal.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private ArrayList<Member> comments;
    private Context context;

    public MemberAdapter(Context context, ArrayList<Member> comments) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.member, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( final  MemberAdapter.ViewHolder viewHolder, int i) {

        String username = comments.get(i).getName();
        String facebookid = comments.get(i).getFacebookid();
//        String photopath = comments.get(i).getProfilephoto();

        viewHolder.name.setText(username);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        String photopath = "https://graph.facebook.com/" + facebookid + "/picture?type=large";
        ImageLoader.getInstance().displayImage( photopath, viewHolder.image, options);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private de.hdodenhof.circleimageview.CircleImageView image;
        public ViewHolder(View view) {
            super(view);

            name = (TextView)view.findViewById(R.id.name);
            image = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.profile_image);

        }
    }

}