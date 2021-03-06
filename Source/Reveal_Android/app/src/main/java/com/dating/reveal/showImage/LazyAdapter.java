package com.dating.reveal.showImage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dating.reveal.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by JonIC on 2016-11-19.
 */
public class LazyAdapter extends BaseAdapter {

    // Declare variables
    private Activity activity;
    private String[] filepath;
    private String[] filename;

    private static LayoutInflater inflater = null;

    public LazyAdapter(Activity a, String[] fpath, String[] fname) {
        activity = a;
        filepath = fpath;
        filename = fname;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        String[] sti = new String[0];
        if(filepath.equals(sti))
            return 0;
        return filepath.length;

    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.gridview_item, null);
        // Locate the TextView in gridview_item.xml
        TextView text = (TextView) vi.findViewById(R.id.text);
        // Locate the ImageView in gridview_item.xml
        ImageView image = (ImageView) vi.findViewById(R.id.image);

        // Set file name to the TextView followed by the position
//        text.setText(filename[position]);

        // Decode the filepath with BitmapFactory followed by the position
//        Bitmap bmp = BitmapFactory.decodeFile(filepath[position]);

        DisplayImageOptions optoins = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .build();
        String filepath__ = "file://" + filepath[position];
//        DisplayImageOptions options = ImageUtils.buildUILOption(R.drawable.pfimage).build();
        ImageLoader.getInstance().displayImage(filepath__, image, optoins);


        // Set the decoded bitmap into ImageView
//        image.setImageBitmap(bmp);
        return vi;
    }
}