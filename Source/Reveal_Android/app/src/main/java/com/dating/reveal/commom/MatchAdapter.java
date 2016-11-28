//package com.dating.reveal.commom;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.dating.reveal.R;
//
//import java.util.ArrayList;
//
//public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {
//    private ArrayList<Member> comments;
//    private Context context;
//
//    public MatchAdapter(Context context, ArrayList<Member> comments) {
//        this.comments = comments;
//        this.context = context;
//    }
//
//    @Override
//    public MatchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.match, viewGroup, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(MatchAdapter.ViewHolder viewHolder, int i) {
//
//        viewHolder.name.setText(comments.get(i).getName());
//    }
//
//    @Override
//    public int getItemCount() {
//        return comments.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        private TextView name,comment;
//        private ImageView image;
//        public ViewHolder(View view) {
//            super(view);
//
//            name = (TextView)view.findViewById(R.id.name);
//            image = (ImageView) view.findViewById(R.id.image);
//
//        }
//    }
//
//}