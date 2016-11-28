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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<com.dating.reveal.commom.Comment> comments;
    private Context context;

//    private final View.OnClickListener mOnClickListener = new RecyclerItemClickListener();
    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.comments = comments;
        this.context = context;
    }
     @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comments_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.ViewHolder viewHolder,  int i) {

        final int position = i;
        String username = comments.get(i).getName();
//        String photopath = comments.get(i).getProfilephoto();

        viewHolder.name.setText(username);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        String facebookid = comments.get(i).getFacebookid();
        String photopath = "https://graph.facebook.com/" + facebookid + "/picture?type=large";

        ImageLoader.getInstance().displayImage( photopath, viewHolder.image, options);

        viewHolder.comment.setText(comments.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,comment;
        private de.hdodenhof.circleimageview.CircleImageView image;
        public ViewHolder(View view) {
            super(view);

            name = (TextView)view.findViewById(R.id.name);
            comment = (TextView) view.findViewById(R.id.comment);
            image = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.image);

        }
    }

}