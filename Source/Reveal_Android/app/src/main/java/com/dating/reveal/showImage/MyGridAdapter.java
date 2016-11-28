package com.dating.reveal.showImage;

/**
 * Created by JonIC on 2016-11-19.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dating.reveal.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by JonIC on 2016-11-19.
 */
public class MyGridAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<GridViewItem> items;


    public MyGridAdapter(Context context, List<GridViewItem> items) {
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, null);
        }

        TextView text = (TextView) convertView.findViewById(R.id.textView);
        String path = items.get(position).getPath();
        String[] spilt = path.split("\\/");
        String folder = spilt[spilt.length-1];
        text.setText(folder);

        de.hdodenhof.circleimageview.CircleImageView imageView =
                (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.imageView);
        Bitmap image = items.get(position).getImage();

        TextView filecount = (TextView) convertView.findViewById(R.id.txt_file_count);
        filecount.setText(items.get(position).getFilecount());

        if (image != null){
            DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(true)
                    .build();
            String filepath__ = "file://" + items.get(position).getPath();

            ImageLoader.getInstance().displayImage(filepath__, imageView, optoins);
        }
        else {
            // If no image is provided, display a folder icon.
            imageView.setImageResource(R.drawable.folder_1);
        }

        return convertView;
    }

}